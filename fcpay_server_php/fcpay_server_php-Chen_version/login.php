<?php
// 跳轉到OAuth登入頁面
    //$newURL = "https://testapi.kid7.club/fcuOAuth/Auth.aspx?client_id=fcorder&client_url=http://10.0.2.2:8888/login_OAuth.php";
	$newURL = "https://testapi.kid7.club/fcuOAuth/Auth.aspx?client_id=fcorder&client_url=http://fcorder.fcudata.science/login_OAuth.php";
	header('Location: '.$newURL);
?>
