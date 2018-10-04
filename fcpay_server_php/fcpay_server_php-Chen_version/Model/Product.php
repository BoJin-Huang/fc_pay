<?php
    class Product{ 
        // object properties
        public $productId;
        public $productName;
        public $productPrice;
        public $manufacturerId;
        public $manufacturerName;
        public $introduction;
     
        // constructor with $db as database connection
        public function __construct($productId, $productName, $productPrice, $manufacturerId, $manufacturerName, $introduction){
            $this->productId = $productId;
            $this->productName = $productName;
            $this->productPrice = $productPrice;
            $this->manufacturerId = $manufacturerId;
            $this->manufacturerName = $manufacturerName;
            $this->introduction = $introduction;
        }
    }
?>