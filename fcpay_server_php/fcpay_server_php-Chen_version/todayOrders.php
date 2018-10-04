<?php
	// required headers
	header("Access-Control-Allow-Origin: *");
	header("Content-Type: application/json; charset=UTF-8");
 
	// include database and object files
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/HelpUtil.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/Database.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/ProductManager.php';	
	require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/UserManager.php';
	
	$database = new Database();
	$db = $database->getConnection();
	if(!isset($db)) {
		sendResponse(500, 'Server database is down');
		return;
	}

	if(!isset($_GET['weekday'])) {
		$products_arr = ProductManager::getAvaliableProducts($db);
	}
	else {
		$wd = (int)$_GET['weekday'];
		if($wd > 7) {
			$wd = 7;
		}
		else if($wd < 1) {
			$wd = 1;
		}
		$products_arr = ProductManager::getProductsByWeekday($db, $wd);		
	}
	$json_arr = array();
	if(count($products_arr) > 0) {
		foreach ($products_arr as $product) {
	        $product_item = array(
				"productID" => $product->productId,
				"product" => $product->productName,
				"productPrice" => $product->productPrice,
				"manufacturerID" => $product->manufacturerId,
				"manufacturer" => $product->manufacturerName,
				"introduction" => html_entity_decode($product->introduction)
	        );
	        array_push($json_arr, $product_item);
	    }
	    echo json_encode($json_arr);
	    // error_log(print_r($json_arr,true));
	}
	else {
		sendResponse(500, 'Unable to get any product');
	}
?>
