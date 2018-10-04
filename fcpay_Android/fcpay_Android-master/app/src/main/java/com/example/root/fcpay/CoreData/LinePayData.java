package com.example.root.fcpay.CoreData;

public class LinePayData {
    private String amount = "";
    private String productName = "";
    private String orderId = "";
    private String currency = "";
    private String paymentUrlWeb = "";
    private String paymentUrlLine = "";
    private String transactionId = "";
    private String transactionReserveId = "";
    private String loginUrl = "";

    public LinePayData(String amount, String productName, String orderId, String currency) {
        this.amount = amount;
        this.productName = productName;
        this.orderId = orderId;
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public String getProductName() {
        return productName;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setPaymentUrlWeb(String paymentUrlWeb) {
        this.paymentUrlWeb = paymentUrlWeb;
    }

    public String getPaymentUrlWeb() {
        return paymentUrlWeb;
    }

    public String getPaymentUrlLine() {
        return paymentUrlLine;
    }

    public String getTransectionId() {
        return transactionId;
    }

    public void setPaymentUrlLine(String paymentUrlLine) {
        this.paymentUrlLine = paymentUrlLine;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionReserveId() {
        return transactionReserveId;
    }

    public void setTransactionReserveId(String transactionReserveId) {
        this.transactionReserveId = transactionReserveId;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
