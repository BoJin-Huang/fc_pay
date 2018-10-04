<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json; charset=UTF-8");
    
    class HttpRequest {
        
        
        //用curl傳post or get並取回傳值
        //一定要傳絕對路徑
        public static function curl_post($header, $url, $post){
            //設定
            $curl = curl_init();
            curl_setopt($curl, CURLOPT_URL, $url);                  //設定url
            curl_setopt($curl, CURLOPT_POST,true);
            curl_setopt($curl, CURLOPT_CUSTOMREQUEST,'POST');       //指定為post
            curl_setopt($curl, CURLOPT_HTTPHEADER, $header);        //設定header
            curl_setopt($curl, CURLOPT_POSTFIELDS, $post);          //設定post的參數
            curl_setopt($curl, CURLOPT_RETURNTRANSFER,true);
            //發送post
            $result = curl_exec($curl);
            //關閉
            curl_close ($curl);
            return $result;
        }
        
        public static function curl_get($header, $url){
            //設定
            $curl = curl_init();
            curl_setopt($curl, CURLOPT_URL, $url);                  //設定url
            curl_setopt($curl, CURLOPT_HTTPHEADER, $header);        //設定header
            curl_setopt($curl, CURLOPT_RETURNTRANSFER,true);
            //發送get
            $result = curl_exec($curl);
            //關閉
            curl_close ($curl);
            return $result;
        }
    }
?>
