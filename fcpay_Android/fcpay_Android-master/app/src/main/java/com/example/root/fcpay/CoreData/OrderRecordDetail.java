package com.example.root.fcpay.CoreData;

public class OrderRecordDetail {

    public String product = "";
    public String price = "";
    public String manufacturer = "";
    public String introduction = "";
    public String quantity = "";

    public OrderRecordDetail(String product, String price, String manufacturer, String introduction, String quantity) {
        this.product = product;
        this.price = price;
        this.manufacturer = manufacturer;
        this.introduction = introduction;
        this.quantity = quantity;
    }
}
