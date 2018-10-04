<?php


interface UserPaymentsManager
{
    public static function getPaymentObject($orderId, $amount, $orderDate, $productName);

    public static function getPaymentConfirm($transactionId, $amount, $data);

    public static function setPaymentType($paymentType);

    public static function setAccount();

    public static function setConfirmURL($url);

    public static function setNoticeURL($url);

    public static function setCurrency($currency);

    public static function setCancelURL($url);
}