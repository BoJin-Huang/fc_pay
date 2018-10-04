package com.example.root.fcpay.CoreData;

public class PayPalData {
    private String payment_url = "";
    private String amount = "";

    public PayPalData(String payment_url, String amount) {
        this.payment_url = payment_url;
        this.amount = amount;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public String getAmount() {
        return amount;
    }
}
