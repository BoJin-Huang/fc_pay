<?php

require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/UserPaymentsManager.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/Foundation/PaymentsConfig.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/Foundation/PaymentsEnum.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/PaymentsManager/LinePay.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/PaymentsManager/iSunny.php';
require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/PaymentsManager/PayPal.php';

class PaymentsManager implements UserPaymentsManager
{
    //取得付款所需物件
    public static function getPaymentObject($orderId, $amount, $orderDate, $productName)
    {
        switch (PaymentsConfig::$paymentType) {
            case 1:
                return iSunny::generateiSunnyObject($orderId, $amount, $orderDate);
                break;
            case 2:
                return PayPal::getPayPalObject($orderId, $amount);
                break;
            case 3:
                LinePay::setLinePayHeader();
                return LinePay::getLinePayObject($orderId, $amount, $productName);
            default:
                return "Error PaymentType";
        }
    }

    //取得驗證結果
    public static function getPaymentConfirm($transactionId, $amount, $data)
    {
        switch (PaymentsConfig::$paymentType) {
            case 1:
                return null;
                break;
            case 2:
                return PayPal::verifyTransaction($data);
                break;
            case 3:
                LinePay::setLinePayHeader();
                return LinePay::ConfirmLinePay($transactionId, $amount);
            default:
                return "Error PaymentType";
        }
    }

    //設定PaymentType
    public static function setPaymentType($paymentType)
    {
        PaymentsConfig::$paymentType = $paymentType;
    }

    //設定帳戶，overload，檢查有幾個parameter
    public static function setAccount()
    {
        $args = func_get_args();

        if(func_num_args() == 1) {

            self::setPaymentId($args[0]);       //目前isunny用到

        } else if(func_num_args() == 2) {

            self::setPaymentIdAndSecret($args[0], $args[1]);       //目前LinePay用到

        }
    }

    private static function setPaymentId($id)
    {
        PaymentsConfig::$id = $id;
    }

    private static function setPaymentIdAndSecret($id, $secret)
    {
        PaymentsConfig::$id = $id;
        PaymentsConfig::$secret = $secret;
    }

    //設定ConfirmURL
    public static function setConfirmURL($url)
    {
        PaymentsConfig::$confirmURL = $url;
    }

    public static function setNoticeURL($url)
    {
        PaymentsConfig::$noticeURL = $url;
    }

    //設定付款所用的貨幣
    public static function setCurrency($currency)
    {
        PaymentsConfig::$currency = $currency;
    }

    public static function setCancelURL($url)
    {
        PaymentsConfig::$cancelUrl = $url;
    }
}