<?php

    require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/Database.php';
    require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/HelpUtil.php';
    require_once $_SERVER['DOCUMENT_ROOT'] . '/ModelManager/UserManager.php';

    $status = $_REQUEST["status"];
    $message = $_REQUEST["message"];
    $user_code = $_REQUEST["user_code"];

    if(!empty($user_code)) {
        $curl = curl_init();
        
        // OPTIONS:
        curl_setopt($curl, CURLOPT_URL, 'https://testapi.kid7.club/fcuapi/api/GetLoginUser?client_id=fcorder&user_code='. $user_code);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
        curl_setopt($curl, CURLOPT_HTTPHEADER, array(
          //'APIKEY: 111111111111111111111',
            'Content-Type: application/json'
        ));
        // curl_setopt($curl, CURLOPT_HTTPAUTH, CURLAUTH_BASIC);

        // EXECUTE:
        $result = curl_exec($curl);
        if(!$result) { 
            die("Connection Failure");
        }
        $json_result = json_decode($result, true);
        curl_close($curl);

        $nid = $json_result['UserInfo'][0]['stu_id'];

        if(isset($nid)) {
            $database = new Database();
            $db = $database->getConnection();
            if(!isset($db)) {
                sendResponse(500, 'Server database is down');
                return;
            }

            if(UserManager::loginOAuth($db, $nid, $user_code)) {
                echo "<div id=\"nid\" hidden>". $nid ."</div>";
                echo "<div id=\"token\" hidden>". $user_code ."</div>";
            }
            else {
                sendResponse(500, 'Unable to update account info');
            }
        }
        else {
            sendResponse(500, 'Unable to get nid');
        }
    }
    else {
        sendResponse(400, 'Failed to get parameters');
    }
?>


