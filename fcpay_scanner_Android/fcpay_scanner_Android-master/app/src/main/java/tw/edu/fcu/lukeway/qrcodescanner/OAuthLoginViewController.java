package tw.edu.fcu.lukeway.qrcodescanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class OAuthLoginViewController extends AppCompatActivity {

    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_login_view_controller);

        findviews();
        webviewsetting();
        web.loadUrl("http://fcorder.fcudata.science/login.php");
    }

    private void findviews()
    {
        web = (WebView) findViewById (R.id.webview);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void webviewsetting()
    {
        WebSettings webSettings = web.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setJavaScriptEnabled(true);
        web.addJavascriptInterface(new MyJavaScriptInterface(this), "HTMLOUT");
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if(url.toString().startsWith("http://fcorder.fcudata.science/login_OAuth.php")){
                    web.loadUrl("javascript:window.HTMLOUT.showHTML" +
                            "('登入成功 NID: '+document.getElementsByTagName('body')[0].innerHTML);");
                }
            }
        });
    }

    class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {

            /*new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();*/
            Log.v("body",html);
            //擷取nid與token
            CoreData coreData = new CoreData();
            org.jsoup.nodes.Document document = Jsoup.parse(html);
            Element nid = document.getElementById("nid");
            coreData.setNid(nid.text());
            Element token = document.getElementById("token");
            coreData.setToken(token.text());

            Log.v("NID",nid.text());
            Log.v("Token",token.text());
            Intent GoMain = new Intent(OAuthLoginViewController.this, MainActivity.class);
            GoMain.putExtra("KEY_NID",nid.text());
            GoMain.putExtra("KEY_TOKEN",token.text());
            startActivity(GoMain);
        }

    }
}
