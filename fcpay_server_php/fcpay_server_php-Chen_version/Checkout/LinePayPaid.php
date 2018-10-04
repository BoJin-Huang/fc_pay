 <?php
     //付款後回傳網址
     
     // include database and object files
     require_once $_SERVER['DOCUMENT_ROOT'] . '/Foundation/HelpUtil.php';
     require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/PaymentsManager/PaymentsManager.php';
     require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/Foundation/PaymentsEnum.php';
     
     if( isset($_GET['orderId']) && isset($_GET['amount']) && isset($_GET['transactionId'])) {
         
         $orderId = $_REQUEST['orderId'];
         $amount = $_REQUEST['amount'];
         $transactionId = $_REQUEST['transactionId'];
         
         //回傳網址成功為買家付款確認前，更改訂單狀態為確認中
         //連線資料庫並更改訂單狀態
         $user = 'FCOrderServer';
         $password = 'fcorderpassword';
         $db = 'FCOrder';
         $host = 'localhost';
         
         $conn = mysqli_connect($host, $user, $password, $db);
         mysqli_query($conn,"SET NAMES 'UTF8'");
         
         $sql = "UPDATE `order_list` SET `clientPaid`='$transactionId', `status` = 3 WHERE `orderId`='$orderId' ";
         $result = mysqli_query($conn,$sql) or die ('MySQL query 1 error');
         
         //----------------------------------------------------

         //confirm付款
         PaymentsManager::setPaymentType(3);
         PaymentsManager::setAccount('1591021350','49d286199c615f1d311b56a2910357f5');
         PaymentsManager::setCurrency('TWD');
         $resultMessage = PaymentsManager::getPaymentConfirm($transactionId, $amount, null);
         
         $decodeMessage = json_decode($resultMessage);
         //error_log(print_r($decodeMessage,true));
         $returnCode = $decodeMessage->returnCode;
         //error_log(print_r($resultMessage,true));
         
         //回傳成功，更改訂單狀態為已付款
         if( isset($resultMessage) && strcmp($returnCode,"0000") == 0) {
             
             //連線資料庫並更改訂單狀態
             $sql = "UPDATE `order_list` SET `serverPaid`='$transactionId', `status` = 4 WHERE `orderId`='$orderId' ";
             $result = mysqli_query($conn,$sql) or die ('MySQL query 2 error');
             mysqli_close($conn);
             
             echo "付款完成"."<br>";
             echo "訂單編號：".$orderId."<br>";
             echo "交易序號：".$transactionId."<br>";
             echo "轉帳金額：".$amount."<br>";
             echo "付款貨幣：TWD<br>";
         }
         else {
             sendResponse(400,'Failed to confirm order');
         }
     }
     else {
         echo "<div id=\"error_unknown\" hidden>" . "there is something wrong, so transaction failed" . "</div>";
         sendResponse(400,'Failed to get parameters');
     }
 ?>
