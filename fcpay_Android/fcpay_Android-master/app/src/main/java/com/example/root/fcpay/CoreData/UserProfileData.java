package com.example.root.fcpay.CoreData;

public class UserProfileData {

    public String dataName = "";
    public String dataValue = "";
    public String dataType = "";    // 1: 標題 2: 內容

    public UserProfileData(String dataName, String dataValue, String dataType) {
        this.dataName = dataName;
        this.dataValue = dataValue;
        this.dataType = dataType;
    }
}
