<?php

    //對資料庫連線
	$user = 'FCOrderServer';
	$password = 'fcorderpassword';
	$db = 'FCOrder';
	$host = 'localhost';

	$conn = mysqli_connect($host, $user, $password, $db);
	mysqli_query($conn,"SET NAMES 'UTF8'");
        
    $account = $_POST['account'];
    $password = $_POST['password'];

    $sql = " SELECT `account`,`password` FROM `account_list` WHERE `account` = '$account' ";
    $result = mysqli_query($conn,$sql) or die('MySQL query 1 error');
    $row = mysqli_fetch_assoc($result);

    $account2 = $row["account"];
    $password2 = $row["password"];
    

    if($password == $password2){
        $check = "1";
        ECHO $check;    
          
    }else{
        $check = "0";
        ECHO $check;
    }

?>