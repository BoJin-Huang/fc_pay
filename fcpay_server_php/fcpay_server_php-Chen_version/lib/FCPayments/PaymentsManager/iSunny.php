<?php

require_once $_SERVER['DOCUMENT_ROOT'] . '/lib/FCPayments/Foundation/PaymentsConfig.php';

class iSunny
{
    public static function generateiSunnyObject($orderId, $amount, $orderDate) {
        //將付款資料用json格式回傳
        $message = array(
            "orderId" => $orderId,
            "memberId" => PaymentsConfig::$id,
            "procCode" => "010300",
            "amount" => $amount,
            "initDateTime" => $orderDate,
            "MAC" => self::getMAC(PaymentsConfig::$id."010300".$amount.$orderDate),
            "replyURL" => PaymentsConfig::$confirmURL."?orderID=".$orderId,
            "memo" => "",
            "noticeURL" => PaymentsConfig::$noticeURL."?orderID=".$orderId
        );
        //打包成json格式
        $message = json_encode($message);
        return $message;
    }

    public static function getMAC($plainText) {
        //產生iSunny交易驗證碼

        //陳冠霖的
        //A碼：4836694959627347 744D6C6E71426537
        //B碼：7A7944326F455932 676D585134476336
        $KEY = "\x48\x36\x69\x49\x59\x62\x73\x47\x67\x6D\x58\x51\x34\x47\x63\x36\x74\x4D\x6C\x6E\x71\x42\x65\x37";
        $ICV = "\x7A\x79\x44\x32\x6F\x45\x59\x32";

        $encrypt = self::encryptText($plainText, $KEY, $ICV);
        return strtoupper(substr( $encrypt , -16 ));
    }

    private static function encryptText($plainText, $key, $icv) {
        // mcrypt is deprecated in PHP 7
        $padded = self::pkcs5_pad($plainText, 8);
        $encText = openssl_encrypt($padded, "DES-EDE3-CBC", $key, OPENSSL_RAW_DATA | OPENSSL_NO_PADDING, $icv);
        return self::strToHex($encText);
    }

    private static function pkcs5_pad ($text, $blocksize) {
        $pad = $blocksize - (strlen($text) % $blocksize);
        return $text . str_repeat(chr($pad), $pad);
    }

    private static function strToHex($string){
        $hex = '';
        for ($i=0; $i<strlen($string); $i++){
            $ord = ord($string[$i]);
            $hexCode = dechex($ord);
            $hex .= substr('0'.$hexCode, -2);
        }
        return strToUpper($hex);
    }
}