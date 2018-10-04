<?php

require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/PaymentsManager/PaymentsManager.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/Foundation/PaymentsEnum.php';

$dbConfig = [
	'host' => 'localhost',
	'username' => 'FCOrderServer',
	'password' => 'fcorderpassword',
	'name' => 'FCOrder'
];

// Check if paypal request or response
if (isset($_POST["txn_id"]) && isset($_POST["txn_type"])) {
	// Handle the PayPal response.

	// Create a connection to the database.
	$db = new mysqli($dbConfig['host'], $dbConfig['username'], $dbConfig['password'], $dbConfig['name']);

	// Check connection
	if ($db->connect_error) {
		die("Connection failed: " . $db->connect_error);
	}
	
	// Assign posted variables to local data array.
	$data = [
		'item_name' => $_POST['item_name'],
		'item_number' => $_POST['item_number'],
		'payment_status' => $_POST['payment_status'],
		'payment_amount' => $_POST['mc_gross'],
		'payment_currency' => $_POST['mc_currency'],
		'txn_id' => $_POST['txn_id'],
		'receiver_email' => $_POST['receiver_email'],
		'payer_email' => $_POST['payer_email'],
		'custom' => $_POST['custom'],
	];

	//驗證client端
    $status = 3;

    if (is_array($data)) {
        $stmt = $db->prepare('UPDATE `order_list` SET `clientPaid`= ?, `status` = ? WHERE `orderId`= ?');
        $stmt->bind_param(
            'sdd',
            $data['txn_id'],
            $status,
            $data['item_number']
        );
        $stmt->execute();
        $stmt->close();

        $resultData = $data['item_number'];

        // Payment status successfully update.
    }
	
	// We need to verify the transaction comes from PayPal and check we've not
	// already processed the transaction before adding the payment to our
	// database.
    PaymentsManager::setPaymentType(2);
	if (PaymentsManager::getPaymentConfirm($data['txn_id'], $data['payment_amount'], $_POST)) {
	    $status = 4;
        if (is_array($data)) {
            $stmt = $db->prepare('UPDATE `order_list` SET `serverPaid`= ?, `status` = ? WHERE `orderId`= ?');
            $stmt->bind_param(
                'sdd',
                $data['txn_id'],
                $status,
                $data['item_number']
            );
            $stmt->execute();
            $stmt->close();

            $resultData = $data['item_number'];
        }
	}
	$db->close();
}
