package com.example.root.fcpay.CoreData;

import java.util.ArrayList;

public class OrderRecord {

    public String orderId = "";
    public String totalPrice = "";
    public String location = "";
    public String orderDate = "";
    public String pickup = "";
    public String paymentType = "";
    public String status = "";
    public String statusDescription = "";
    public ArrayList<OrderRecordDetail> details;

    public OrderRecord(String orderId, String totalPrice, String location, String orderDate, String pickup, String paymentType, String status, String statusDescription, ArrayList<OrderRecordDetail> details) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.location = location;
        this.orderDate = orderDate;
        this.pickup = pickup;
        this.paymentType = paymentType;
        this.status = status;
        this.statusDescription = statusDescription;
        this.details = details;
    }

    public boolean isPickup(){
        if(pickup.equals("1")) {
            return true;
        }
        else{
            return false;
        }
    }

    public int getTotalOrderNumber(){
        int result = 0;
        for(int i=0; i<details.size(); i++){
            result += Integer.valueOf(details.get(i).quantity);
        }
        return result;
    }

    public int getTotalOrderPrice(){
        int result = 0;
        for(int i=0; i<details.size(); i++){

            result += Integer.valueOf(details.get(i).price)*Integer.valueOf(details.get(i).quantity);
        }
        return result;
    }
}
