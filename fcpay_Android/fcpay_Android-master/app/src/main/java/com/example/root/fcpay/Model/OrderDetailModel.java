package com.example.root.fcpay.Model;

import com.example.root.fcpay.CoreData.OrderDetail;

import java.util.ArrayList;

public class OrderDetailModel {

    private ArrayList<OrderDetail> orderDetailList = new ArrayList<>();

    public void addOrderDetail(OrderDetail orderDetail){
        this.orderDetailList.add(new OrderDetail(orderDetail));
    }

    public void clear(){
        this.orderDetailList.clear();
    }

    public int getTotalOrderQuantity(){
        int result = 0;
        for(int i=0; i<orderDetailList.size(); i++){
            result += Integer.parseInt(orderDetailList.get(i).quantity);
        }
        return result;
    }

    public int getTotalOrderPrice(){
        int result = 0;
        for(int i=0; i<orderDetailList.size(); i++){
            result += Integer.parseInt(orderDetailList.get(i).price)*Integer.parseInt(orderDetailList.get(i).quantity);
        }
        return result;
    }

    public String getOrderDetailProductName(int index){
        return orderDetailList.get(index).name;
    }

    public String getOrderDetailmanufacturerName(int index){
        return orderDetailList.get(index).manufacturerName;
    }

    public String getOrderDetailProductQuantity(int index){
        return orderDetailList.get(index).quantity;
    }

    public String getOrderDetailProductPrice(int index){
        return orderDetailList.get(index).price;
    }

    public int getOrderDetailSize() {
        return orderDetailList.size();
    }

    public String getOrderDetailProductID(int index){
        return orderDetailList.get(index).ID;
    }
}
