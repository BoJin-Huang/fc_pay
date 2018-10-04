<?php
	//使用者通知FCOrder伺服器付款完成

	if( isset($_GET['orderID']) && isset($_POST["transID"]) ) {

		$orderID = $_GET['orderID'];
		$transID = $_POST['transID'];

		echo "付款完成"."<br>";
		echo "訂單編號：".$orderID."<br>";
		echo "交易序號：".$transID."<br>";
		echo "轉帳金額：".$_POST['amount']."<br>";
		echo "帳戶餘額：".$_POST['balance']."<br>";
		echo "交易手續費：".$_POST['fee']."<br>";

		$user = 'FCOrderServer';
		$password = 'fcorderpassword';
		$db = 'FCOrder';
		$host = 'localhost';
	
		$conn = mysqli_connect($host, $user, $password, $db);
		mysqli_query($conn,"SET NAMES 'UTF8'");

		$sql = "UPDATE `order_list` SET `clientPaid`='$transID', `status` = 3 WHERE `orderId`='$orderID' ";
		$result = mysqli_query($conn,$sql) or die ('MySQL query 1 error');
		mysqli_close($conn);
	}
	else {
		if(isset($_POST['rCodeMsg'])) {
            error_log(print_r("client error ： ".$_POST['rCodeMsg'],true));
			echo "<div id=\"error_msg\" hidden>" .	$_POST['rCodeMsg'] . "</div>";
		}
		else {
			echo "<div id=\"error_unknown\" hidden>" . "there is something wrong, so transaction failed" . "</div>";
		}
	}

?>
