<?php
    class Order {
        // object properties
        public $orderId;
        public $memberId;
        public $totalPrice;
        public $location;
        public $orderDate;
        public $pickup;
        public $payment;
        public $paymentName;
        public $status;
        public $statusName;
        public $orderToken;
        public $details;

        public function __construct($orderId, $memberId, $totalPrice, $location, $orderDate, $pickup, $payment, $paymentName, $status, $statusName, $orderToken){
            $this->orderId = $orderId;
            $this->memberId = $memberId;
            $this->totalPrice = $totalPrice;
            $this->location = $location;
            $this->orderDate = $orderDate;
            $this->pickup = $pickup;
            $this->payment = $payment;
            $this->paymentName = $paymentName;
            $this->status = $status;
            $this->statusName = $statusName;
            $this->orderToken = $orderToken;
            $this->details = array();
        }
    }
?>