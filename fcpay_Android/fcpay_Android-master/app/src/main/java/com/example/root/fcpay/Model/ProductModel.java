package com.example.root.fcpay.Model;

import com.example.root.fcpay.CoreData.Product;

import java.util.ArrayList;

public class ProductModel {
    private ArrayList<Product> productList = new ArrayList<>();

    public void addProduct(Product product){
        this.productList.add(new Product(product));
    }

    public String getID(int index){
        return productList.get(index).ID;
    }

    public String getName(int index){
        return productList.get(index).name;
    }

    public String getPrice(int index){
        return productList.get(index).price;
    }

    public String getManufacturerID(int index){
        return productList.get(index).manufacturerID;
    }

    public String getManufacturerName(int index){
        return productList.get(index).manufacturerName;
    }

    public String getIntroduce(int index){
        return productList.get(index).introduce;
    }

    public String getQuantity(int index){
        return productList.get(index).quantity;
    }

    public void setQuantity(int index, String quantity){
        productList.get(index).quantity = quantity;
    }

    public int size(){
        return productList.size();
    }

    public void clear(){
        productList.clear();
    }

    public void clearQuantity(){
        for(int i=0; i<productList.size(); i++){
            productList.get(i).quantity = "0";
        }
    }
}
