<?php
	
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Model/Product.php';
	require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/Config.php';

	class ProductManager {
		public static function getAvaliableProducts($conn) {
			$dt = new DateTime("now", new DateTimeZone('Asia/Shanghai'));
			$wd = date('w',strtotime($dt->format('m/d/Y')));

	        // select all query
	        $query = "SELECT m.*, p.*
	                  FROM " . Config::$db_product . " p 
	                  JOIN " . Config::$db_manufacturer . " m ON p.manufacturerId = m.manufacturerId " .
	                  "WHERE p.weekday = ? " .
	                  "ORDER BY p.productId ASC";
	        // prepare query statement
	        $stmt = $conn->prepare($query);
	       	$stmt->bindParam(1, $wd);

	        // execute query
	        $stmt->execute();
	     
	     	$products_arr=array();
	       	while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
	        	// extract row will make $row['name'] to just $name only
	        	extract($row);
	        	$product_item = new Product($productId, $productName, $productPrice, $manufacturerId, $manufacturerName, $introduction);
	        	array_push($products_arr, $product_item);
	        }
	        return $products_arr;
		}

		public static function getProductsByWeekday($conn, $wd) {
			$dt = new DateTime("now", new DateTimeZone('Asia/Shanghai'));

	        // select all query
	        $query = "SELECT m.*, p.*
	                  FROM " . Config::$db_product . " p 
	                  JOIN " . Config::$db_manufacturer . " m ON p.manufacturerId = m.manufacturerId " .
	                  "WHERE p.weekday = ? " .
	                  "ORDER BY p.productId ASC";
	        // prepare query statement
	        $stmt = $conn->prepare($query);
	       	$stmt->bindParam(1, $wd);

	        // execute query
	        $stmt->execute();
	     
	     	$products_arr=array();
	       	while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
	        	// extract row will make $row['name'] to just $name only
	        	extract($row);
	        	$product_item = new Product($productId, $productName, $productPrice, $manufacturerId, $manufacturerName, $introduction);
	        	array_push($products_arr, $product_item);
	        }
	        return $products_arr;
		}

		public static function getProductById($conn, $id) {
			$id = htmlspecialchars(strip_tags($id));
	        $query = "SELECT m.*, p.* 
	                  FROM " . Config::$db_product . " p  
	                  JOIN " . Config::$db_manufacturer . " m ON p.manufacturerId = m.manufacturerId
	                  WHERE p.productId = ?";
	        $stmt = $conn->prepare($query);
	        $stmt->bindParam(1, $id);
	        // execute query
	        $stmt->execute();
	        // get retrieved row
	        $row = $stmt->fetch(PDO::FETCH_ASSOC);

	        // set values to object properties
	        $product_item = new Product($row['productId'], $row['productName'], $row['productPrice'], $row['manufacturerId'], $row['manufacturerName'], $row['introduction']);
	        return $product_item;
	    }
	}
?>