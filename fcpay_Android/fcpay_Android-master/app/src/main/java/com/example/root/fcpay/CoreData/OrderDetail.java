package com.example.root.fcpay.CoreData;

public class OrderDetail {
    public String ID = "";
    public String name = "";
    public String price = "";
    public String manufacturerName = "";
    public String introduce = "";
    public String quantity = "";

    public OrderDetail(String ID,String name, String price, String manufacturerName, String introduce, String quantity) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.manufacturerName = manufacturerName;
        this.introduce = introduce;
        this.quantity = quantity;
    }

    public OrderDetail(OrderDetail orderDetail) {
        this.ID = orderDetail.ID;
        this.name = orderDetail.name;
        this.price = orderDetail.price;
        this.manufacturerName = orderDetail.manufacturerName;
        this.introduce = orderDetail.introduce;
        this.quantity = orderDetail.quantity;
    }
}
