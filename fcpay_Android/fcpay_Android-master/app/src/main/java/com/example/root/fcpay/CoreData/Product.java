package com.example.root.fcpay.CoreData;

public class Product {
    public String ID = "";
    public String name = "";
    public String price = "";
    public String manufacturerID = "";
    public String manufacturerName = "";
    public String introduce = "";
    public String quantity = "0";

    public Product(String ID, String name, String price, String manufacturerID, String manufacturerName, String introduce) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.manufacturerID = manufacturerID;
        this.manufacturerName = manufacturerName;
        this.introduce = introduce;
    }

    public Product(Product product){
        this.ID = product.ID;
        this.name = product.name;
        this.price = product.price;
        this.manufacturerID = product.manufacturerID;
        this.manufacturerName = product.manufacturerName;
        this.introduce = product.introduce;
    }
    /*
    public Product(String ID, String name, String manufacturerName, String quantity) {
        this.ID = ID;
        this.name = name;
        this.manufacturerName = manufacturerName;
        this.quantity = quantity;
    }*/
}
