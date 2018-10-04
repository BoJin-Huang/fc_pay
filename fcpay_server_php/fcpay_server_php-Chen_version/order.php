<?php
	// required headers
	header("Access-Control-Allow-Origin: *");
	header("Content-Type: application/json; charset=UTF-8");

	// include database and object files
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/Database.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/ProductManager.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/OrderManager.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/HelpUtil.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/UserManager.php';
    require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/PaymentsManager/PaymentsManager.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/Foundation/PaymentsEnum.php';
    
	
	$headers = getallheaders();
	if(!UserManager::verifyUser($headers["user_id"], $headers["user_auth"])) {
		sendResponse(401, 'Request is not authorized');
		return;
	}

	// get post body
	$inputJSON = file_get_contents('php://input');
	$input= json_decode( $inputJSON );

	if(!isset($input)) {
		sendResponse(400, 'The input format is wrong');
	}
	$memberId = $input->memberId;
	$orderDetail = $input->product;
	$location = $input->location;
	$memo = $input->memo;
	$paymentType = $input->paymentType;

	if(isset($memberId) && isset($orderDetail) && isset($location) && isset($memo) && isset($paymentType)) {
		if(!validatesAsInt($paymentType)) {
			sendResponse(400, 'Payment type is not valid');
		}
		$paymentType = (int)$paymentType;
		if($paymentType < 1 || $paymentType > 3) {
			sendResponse(400, 'Payment type is not valid');
		}

		$database = new Database();
		$db = $database->getConnection();
		if(!isset($db)) {
			sendResponse(500, 'Server database is down');
			return;
		}

		$totalPrice = 0;
		date_default_timezone_set('Asia/Taipei');  
		$orderDate = date("YmdHis");

		// create an order
		foreach ($orderDetail as $data) {
			$fetchProductId = $data->productID;
			$orderQuantity = intval($data->quantity);
			$productItem = ProductManager::getProductById($db, $fetchProductId);
			if(isset($productItem->productPrice) && $orderQuantity > 0) {
				$totalPrice += ($productItem->productPrice * $orderQuantity);
			}
			else {
				sendResponse(400, 'The input parameters are invalid');
				return;
			}
		}
		$amount = strval($totalPrice);
		$orderId = OrderManager::placeOrder($db, $memberId, $totalPrice, $location, $memo, $paymentType, $orderDate);
		if(!is_bool($orderId)) {
			// create order details
			foreach ($orderDetail as $data) {
				$orderProductId = $data->productID;
				$orderQuantity = $data->quantity;
				OrderManager::insertOrderDetail($db, $orderId, $orderProductId, $orderQuantity);
			}

            PaymentsManager::setPaymentType($paymentType);

			switch ($paymentType) {
			    case 1:
                    PaymentsManager::setAccount("zxc1234");
                    PaymentsManager::setConfirmURL("http://fcorder.fcudata.science/Checkout/iSunnyClientPaid.php");
                    PaymentsManager::setNoticeURL("http://fcorder.fcudata.science/Checkout/iSunnyServerPaid.php");
			        break;
			    case 2:
                    PaymentsManager::setAccount("fgh351279@gmail.com");
                    PaymentsManager::setNoticeURL("http://fcorder.fcudata.science/payment-successful.html");
                    PaymentsManager::setCancelURL("http://fcorder.fcudata.science/payment-cancelled.html");
                    PaymentsManager::setConfirmURL("http://fcorder.fcudata.science/Checkout/PayPalPaid.php");
                    PaymentsManager::setCurrency('TWD');
			        break;
			    case 3:
                    PaymentsManager::setAccount('1591021350','49d286199c615f1d311b56a2910357f5');
                    PaymentsManager::setConfirmURL('http://fcorder.fcudata.science/Checkout/LinePayPaid.php');
                    PaymentsManager::setCurrency('TWD');
			        break;
			    default:
			    	break;
			}

            echo PaymentsManager::getPaymentObject($orderId, $amount, $orderDate, '便當');

		}
		else {
			sendResponse(500, 'Unable to place the order');
		}
	} 
	else {
		sendResponse(400, 'Failed to get parameters');
	}
	// error_log(print_r($message,true));
?>
