package com.example.root.fcpay.Payment.iSunny;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.root.fcpay.CoreData.ISunnyData;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewController;
import com.example.root.fcpay.R;

import org.apache.http.util.EncodingUtils;

public class SunnyBankPaymentVC extends AppCompatActivity {

    private WebView iSunnyWebView;
    private SharedPreferences userProfileManager;
    private SharedPreferencesHelper preferencesHelper;
    private KeyStoreHelper keyStoreHelper;  //自訂類別 用來加密機密資料
    private ISunnyData iSunnyData = SunnyBankPaymentVM.iSunnyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunny_bank_payment_vc);
        initComponent();
        userProfileManager = getSharedPreferences("userProfile",0);
        preferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        keyStoreHelper = new KeyStoreHelper(getApplicationContext(), preferencesHelper);
        String url = "https://isunnytrain.sunnybank.com.tw/SunnyPay/PSPay.do";
        String postDate = "memberID="+iSunnyData.memberId+
                "&procCode="+iSunnyData.procCode+
                "&amount="+iSunnyData.amount+
                "&initDateTime="+iSunnyData.initDateTime+
                "&MAC="+iSunnyData.MAC+
                "&replyURL="+iSunnyData.replyURL+
                "&memo="+iSunnyData.memo+
                "&noticeURL="+iSunnyData.noticeURL;
        iSunnyWebView.postUrl(url, EncodingUtils.getBytes(postDate, "utf-8"));
    }

    private void initComponent() {
        iSunnyWebView = (WebView)findViewById(R.id.iSunnyWebView);
        webviewsetting();
    }

    private void webviewsetting() {
        WebSettings webSettings = iSunnyWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setJavaScriptEnabled(true);

        iSunnyWebView.addJavascriptInterface(new MyJavaScriptInterface(this), "HTMLOUT");
        iSunnyWebView.setWebChromeClient(new WebChromeClient());
        iSunnyWebView.setWebViewClient(new WebViewClient() {

            @Override

            public void onPageFinished(WebView view, String url) {
                if(url.toString().startsWith("https://isunnytrain.sunnybank.com.tw/SunnyPay/PPayWithSVA.do")){
                    showFinishedDialog();
                }
                else if(url.toString().startsWith("https://isunnytrain.sunnybank.com.tw/SunnyPay/PSPay.do")){

                    //iSunnyWebView.loadUrl("javascript:window.HTMLOUT.showHTML" + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

                    iSunnyWebView.evaluateJavascript("javascript:document.getElementsByName('user_name')[0].value='"+keyStoreHelper.decrypt(userProfileManager.getString("iSunnyAC",""))+"';" +
                            "document.getElementsByName('user_passwd')[0].value='"+keyStoreHelper.decrypt(userProfileManager.getString("iSunnyPW",""))+"';", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });

                }
                else if(url.toString().startsWith("https://isunnytrain.sunnybank.com.tw/SunnyPay/PSPayL.do")) {

                    //iSunnyWebView.loadUrl("javascript:window.HTMLOUT.showHTML" + "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");

                    iSunnyWebView.evaluateJavascript("javascript:document.getElementsByName('payPwdS')[0].value='"+keyStoreHelper.decrypt(userProfileManager.getString("iSunnyPW",""))+"';", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });
                }
            }
        });
    }

    private void showFinishedDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("付款完成")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();   //結束，關閉頁面
                        startActivity(new Intent(SunnyBankPaymentVC.this, OrderTableViewController.class));
                    }
                }).setMessage("已附款成功! 交易金額: NT "+iSunnyData.amount+"$\n將跳回菜單頁").create();
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

