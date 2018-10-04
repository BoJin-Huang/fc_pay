package com.example.root.fcpay.CoreData;

public class OrderDetailCheckItem{

    public String itemName = "";
    public String itemValue = "";
    public int layoutType; // 1: head   2: body

    public OrderDetailCheckItem(String name, String value, int type){
        itemName = name;
        itemValue = value;
        layoutType = type;
    }
}
