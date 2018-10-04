package com.example.root.fcpay.CoreData;

public class ISunnyData {

    public String orderId = "";
    public String memberId = "";
    public String procCode = "";
    public String amount = "";
    public String initDateTime = "";
    public String MAC = "";
    public String replyURL = "";
    public String memo = "";
    public String noticeURL = "";

    public ISunnyData(String orderId, String memberId, String procCode, String amount, String initDateTime, String MAC, String replyURL, String memo, String noticeURL) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.procCode = procCode;
        this.amount = amount;
        this.initDateTime = initDateTime;
        this.MAC = MAC;
        this.replyURL = replyURL;
        this.memo = memo;
        this.noticeURL = noticeURL;
    }
}
