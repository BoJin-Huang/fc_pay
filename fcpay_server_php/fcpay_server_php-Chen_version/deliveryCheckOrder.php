<?php
	// required headers
	header("Access-Control-Allow-Origin: *");
	header("Content-Type: application/json; charset=UTF-8");

	// include database and object files
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/Database.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/OrderManager.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/HelpUtil.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/UserManager.php';
	
	$headers = getallheaders();
	if(!UserManager::verifyUser($headers["user_id"], $headers["user_auth"])) {
		sendResponse(400, 'Request is not authorized');
		return;
	}

	// get post body
	$inputJSON = file_get_contents('php://input');
	$input= json_decode( $inputJSON );

	if(!isset($input)) {
		sendResponse(400, 'The input format is wrong');
	}
	$token = $input->orderId;

	//送餐員查詢Order ID的內容是否已付款取餐
	if(isset($token)) {
		$database = new Database();
		$db = $database->getConnection();
		if(!isset($db)) {
			sendResponse(500, 'Server database is down');
			return;
		}

		$order = OrderManager::getOrderByToken($db, $token);
		if(is_bool($order)) {
			sendResponse(400, 'Unable to get the order, please check the order id is correct');
		}
		else {
			$details = array();
			foreach($order->details as $detail) {
				array_push($details, array(
					"product" => $detail->productName,
					"price" => $detail->productPrice,
					"quantity" => $detail->quantity,
					"introduction" => $detail->introduction,
					"manufacturer" => $detail->manufacturerName
				));
			}
			$order_json = array(
				"orderId" => $order->orderToken,
				"totalPrice" => $order->totalPrice,
				"location" => $order->location,
				"orderDate" => $order->orderDate,
				"pickup" => $order->pickup,
				"paymentType" => $order->paymentName,
				"status" => $order->status,
				"statusDescription" => $order->statusName,
				"details" => $details
			);
			OrderManager::updateOrderToPickedUp($db, $token);
			echo json_encode($order_json);
		}
	} 
	else {
		sendResponse(400, 'Unable to get order Id');
	}
?>
