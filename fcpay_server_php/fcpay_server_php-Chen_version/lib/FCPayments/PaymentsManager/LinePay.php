<?php

require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/Foundation/HttpRequest.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/Foundation/PaymentsConfig.php';

class LinePay
{
    //設定header，包含id and secret，指定轉帳帳戶
    public static function setLinePayHeader(){
        //新增header
        $header = array();
        $header[] = 'X-LINE-ChannelId:' . PaymentsConfig::$id;
        $header[] = 'X-LINE-ChannelSecret:' . PaymentsConfig::$secret;
        $header[] = 'Content-Type:application/json;charset=UTF-8';
        PaymentsConfig::$Header = $header;
    }

    //request請求
    public static function getLinePayObject($orderId, $amount, $productName){
        //將付款資料用json格式發送
        $message = self::generateRequestMessage($orderId, $amount, $productName);

        //RequestUrl
        $requestUrl = PaymentsConfig::$linePayURL . '/request';

        //post連線
        $result = HttpRequest::curl_post(PaymentsConfig::$Header, $requestUrl, $message);

        //整理response
        $resultMessage = self::generateRequestResponse($result, $orderId, $amount, $productName);

        return $resultMessage;
    }

    //confirm付款
    public static function ConfirmLinePay($transactionId,$amount){
        //ConfrimURL
        $linePayConfirmUrl = PaymentsConfig::$linePayURL . $transactionId . '/confirm';

        //整理requestMessage
        $message = self::generateConfirmMessage($amount);

        //post連線
        $resultMessage = HttpRequest::curl_post(PaymentsConfig::$Header, $linePayConfirmUrl, $message);

        return $resultMessage;
    }

    private static function generateRequestMessage($orderId, $amount, $productName){
        $message = array(
            "orderId" => $orderId,
            "amount" => $amount,
            "productName" => $productName,
            "currency" => PaymentsConfig::$currency,
            "langCd" => 'zh-Hant',
            "confirmUrl" => PaymentsConfig::$confirmURL.'?orderId='.
                $orderId.'&amount='.
                $amount
        );
        return json_encode($message);
    }
    private static function generateRequestResponse($message, $orderId, $amount, $productName){
        $decode_result = json_decode($message,true);
        $productDetail = array(
            "orderId" => $orderId,
            "amount" => $amount,
            "productName" => $productName
        );
        $decode_result = array_merge($decode_result,$productDetail);
        return json_encode($decode_result);
    }

    private static function generateConfirmMessage($amount){
        $message = array(
            "amount" => $amount,
            "currency" => PaymentsConfig::$currency
        );
        return json_encode($message);
    }
}