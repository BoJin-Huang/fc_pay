<?php
	// required headers
	header("Access-Control-Allow-Origin: *");
	header("Content-Type: application/json; charset=UTF-8");

	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/HelpUtil.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/Database.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/OrderManager.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/ProductManager.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/UserManager.php';
	
	$headers = getallheaders();
	if(!UserManager::verifyUser($headers["user_id"], $headers["user_auth"])) {
		sendResponse(401, 'Request is not authorized');
		return;
	}

	// get post body
	$inputJSON = file_get_contents('php://input');
	$input= json_decode( $inputJSON );

	if(isset($input)) {
		$memberId = $input->memberId;
		$limit = $input->limit;
		$offset = $input->offset;
	}
	if(isset($memberId) && isset($limit) && isset($offset)) {
		$offset = intval($test);
		$limit = intval($limit);

		if($offset >= 0 && $limit >= 1) {
			$database = new Database();
			$db = $database->getConnection();
			if(!isset($db)) {
				sendResponse(500, 'Server database is down');
				return;
			}

			$result = OrderManager::getValidedOrdersByMemberId($db, $memberId, $offset, $limit);
			$response_json = array();
			foreach($result as $order) {
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
				array_push($response_json, $order_json);
			}
			//error_log(print_r($response_json,true));
			echo json_encode($response_json);
		}
		else {
			sendResponse(400, 'Parameters are not in the right format');
		}
	}
	else {
		sendResponse(400, 'Unable to get the input');
	}
?>
