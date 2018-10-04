package com.example.root.fcpay.Payment.LinePay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.root.fcpay.AndroidKeyStore.KeyStoreHelper;
import com.example.root.fcpay.AndroidKeyStore.SharedPreferencesHelper;
import com.example.root.fcpay.CoreData.LinePayData;
import com.example.root.fcpay.MyStaticData;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.R;

public class LinePayPaymentVC extends AppCompatActivity {

    private WebView linePayWebView;
    private SharedPreferences userProfileManager;
    private SharedPreferencesHelper preferencesHelper;
    private KeyStoreHelper keyStoreHelper;  //自訂類別 用來加密機密資料
    private LinePayData linePayData = LinePayPaymentVM.getLinePayData();
    private Dialog dialog;
    private String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_pay_payment_vc);
        initComponent();
        userProfileManager = getSharedPreferences("userProfile",0);
        preferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        keyStoreHelper = new KeyStoreHelper(getApplicationContext(), preferencesHelper);
        webUrl = linePayData.getPaymentUrlWeb();
        linePayWebView.loadUrl(webUrl);
    }

    private void initComponent() {
        linePayWebView = (WebView)findViewById(R.id.LinePayWebView);
        webviewsetting();
    }

    private void webviewsetting() {
        WebSettings webSettings = linePayWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        linePayWebView.addJavascriptInterface(new MyJavaScriptInterface(this), "HTMLOUT");
        linePayWebView.setWebChromeClient(new WebChromeClient());
        linePayWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view,String url,Bitmap favicon){
                if (url.toString().startsWith(MyStaticData.IP + "Checkout/LinePayPaid.php?orderId="+linePayData.getOrderId()
                        +"&amount="+linePayData.getAmount()+"&transactionId=" + linePayData.getTransectionId())) {
                    dialog = ProgressDialog.show(LinePayPaymentVC.this, "付款確認中", "請稍候...",true);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.toString().startsWith(MyStaticData.IP + "Checkout/LinePayPaid.php?orderId="+linePayData.getOrderId()
                        +"&amount="+linePayData.getAmount()+"&transactionId=" + linePayData.getTransectionId())) {
                    dialog.dismiss();
                    showFinishedDialog();
                } else if (url.startsWith(linePayData.getLoginUrl())) {
                    linePayWebView.evaluateJavascript("javascript:document.getElementById('id').value='"+keyStoreHelper.decrypt(userProfileManager.getString("iSunnyAC",""))+"';"+
                            "document.getElementById('passwd').value='"+keyStoreHelper.decrypt(userProfileManager.getString("iSunnyPW",""))+"';", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });
                    //String Url = linePayWebView.getUrl();
                    //Log.v("url", Url);
                    //linePayWebView.loadUrl("javascript:window.HTMLOUT.showHTML" + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

                }
            }
        });
    }

    private void showFinishedDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("付款成功")
                .setPositiveButton("ＯＫ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();   //結束，關閉頁面
                        startActivity(new Intent(LinePayPaymentVC.this, OrderTableViewModel.class));
                    }
                }).setMessage("已確認付款成功! \n訂單編號："+linePayData.getOrderId()
                        +"\n交易序號："+linePayData.getTransectionId()
                        +"\n交易金額：$"+linePayData.getAmount()
                        +"\n付款貨幣："+linePayData.getCurrency()
                        +"\n\n跳回菜單頁").create();
        dialog.show();
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();
        }

    }
}
