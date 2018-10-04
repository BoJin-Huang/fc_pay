<?php

 	require_once $_SERVER['DOCUMENT_ROOT'] . '/Model/Order.php';
 	require_once $_SERVER['DOCUMENT_ROOT'] . '/Model/OrderDetail.php';
 	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/Config.php';

	//deal with the decode and encode function
	class OrderManager {
		public static function placeOrder($conn, $memberId, $totalPrice, $location, $memo, $paymentType, $orderDate) {
		    // sanitize
		    $memberId = htmlspecialchars(strip_tags($memberId));
		    $totalPrice = htmlspecialchars(strip_tags($totalPrice));
		    $location = htmlspecialchars(strip_tags($location));
		    $memo = htmlspecialchars(strip_tags($memo));
		    $paymentType = htmlspecialchars(strip_tags($paymentType));

			$query = "INSERT INTO " . Config::$db_order . " (`memberId`,`totalPrice`, `location`, `memo`, `orderDate`, `payment`, `status`, `orderToken`) 
					  VALUES (:memberId, :totalPrice, :location, :memo, :orderDate, :payment, 1, :orderToken)";

			$stmt = $conn->prepare($query);

		    // bind values
		    $stmt->bindParam(":memberId", $memberId);
		    $stmt->bindParam(":totalPrice", $totalPrice);
		    $stmt->bindParam(":location", $location);
		    $stmt->bindParam(":memo", $memo);
		    $stmt->bindParam(":orderDate", $orderDate);
		 	$stmt->bindParam(":payment", $paymentType);
            $md = md5($orderDate.$memberId.$totalPrice);
		 	$stmt->bindParam(":orderToken", $md);

			if($stmt->execute()) {
		    	return $conn->lastInsertId();
		    }
		    else {
		    	return false;
		    }
		}

		public static function insertOrderDetail($conn, $orderId, $productId, $quantity) {
			$quantity = htmlspecialchars(strip_tags($quantity));

			$query = "INSERT INTO " . Config::$db_order_detail . " (`orderId`, `productId`, `quantity`) VALUES (:orderId, :productId, :quantity)";
			$stmt = $conn->prepare($query);

		    // bind values
		    $stmt->bindParam(":orderId", $orderId);
		    $stmt->bindParam(":productId", $productId);
		    $stmt->bindParam(":quantity", $quantity);

			if($stmt->execute()) {
		    	return true;
		    }
		    else {
		    	return false;
		    }
		}

		public static function getValidedOrdersByMemberId($conn, $memberId, $offset, $limit) {
			$memberId = htmlspecialchars(strip_tags($memberId));
	        $query = "SELECT * FROM " . Config::$db_order . " o 
	                  JOIN " . Config::$db_order_status . " s ON o.status = s.statusId 
					  JOIN " . Config::$db_payment . " p ON o.payment = p.paymentId 
	                  WHERE `memberId` = :memberId AND `status` not in (1, 2) order by orderId DESC 
	                  limit :limit offset :offset";
	        $stmt = $conn->prepare($query);
		    // bind values
		    $stmt->bindParam(":memberId", $memberId);
		    $stmt->bindParam(":offset", $offset, PDO::PARAM_INT);
		    $stmt->bindParam(":limit", $limit, PDO::PARAM_INT);
	        // execute query
	        $stmt->execute();
	     	$orders_arr = array();
	       	while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
	        	// extract row will make $row['name'] to just $name only
	        	extract($row);
	        	$order_item = new Order($orderId, $memberId, $totalPrice, $location, $orderDate, $pickup, $payment, $paymentName, $status, $statusName, $orderToken);
	        	array_push($orders_arr, $order_item);
	        }
	        return self::fetchOrderDetails($conn, $orders_arr);
		}

		private static function fetchOrderDetails($conn, $orders) {
			$orders_dic = array();
	        $order_ids = array();
	        foreach($orders as $order_item) {
	        	$orders_dic[$order_item->orderId] = $order_item;
	        	array_push($order_ids, $order_item->orderId);
	        }
			$qMarks = str_repeat('?,', count($order_ids) - 1) . '?';
			$query = "SELECT o.orderId, d.quantity, p.*, m.manufacturerName 
					  FROM " . Config::$db_order . " o 
					  JOIN " . Config::$db_order_detail . " d ON o.orderId = d.orderId 
					  JOIN " . Config::$db_product . " p ON d.productId = p.productId 
					  JOIN " . Config::$db_manufacturer . " m ON m.manufacturerId = p.manufacturerId 
					  WHERE o.orderId IN ($qMarks) order by o.orderId";
			$stmt = $conn->prepare($query);
			$stmt->execute($order_ids);
	       	while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
	        	// extract row will make $row['name'] to just $name only
	        	extract($row);
	        	$detail = new OrderDetail($productId, $productName, $productPrice, $manufacturerId, $manufacturerName, $introduction, $quantity);
	        	array_push($orders_dic[$orderId]->details, $detail);
	        }
	        return $orders;
		}

		public static function getOrderByToken($conn, $token) {
			$token = htmlspecialchars(strip_tags($token));
	        $query = "SELECT * FROM " . Config::$db_order . " o 
	                  JOIN " . Config::$db_order_status . " s ON o.status = s.statusId 
					  JOIN " . Config::$db_payment . " p ON o.payment = p.paymentId 
	                  WHERE `orderToken` = :token AND `status` not in (1, 2)";
	        $stmt = $conn->prepare($query);
		    // bind values
		    $stmt->bindParam(":token", $token);
	        // execute query
	        $stmt->execute();
	     	$orders_arr = array();
	       	while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
	        	// extract row will make $row['name'] to just $name only
	        	extract($row);
	        	$order_item = new Order($orderId, $memberId, $totalPrice, $location, $orderDate, $pickup, $payment, $paymentName, $status, $statusName, $orderToken);
	        	array_push($orders_arr, $order_item);
	        }
	        $full_order = self::fetchOrderDetails($conn, $orders_arr);
	        if(count($full_order) > 0) {
	        	return $full_order[0];
	        }
	        else {
	        	return false;
	        }
		}

		public static function updateOrderToPickedUp($conn, $token) {
			$token = htmlspecialchars(strip_tags($token));
	        $query = "UPDATE " . Config::$db_order . " SET pickup = 1 WHERE orderToken = :token";
			$stmt = $conn->prepare($query);
		    // bind values
		    $stmt->bindParam(":token", $token);
	        // execute query
	        $stmt->execute();
		}
	}
?>
