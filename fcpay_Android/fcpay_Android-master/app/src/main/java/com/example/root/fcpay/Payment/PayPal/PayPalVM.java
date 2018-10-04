package com.example.root.fcpay.Payment.PayPal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.fcpay.AndroidKeyStore.KeyStoreHelper;
import com.example.root.fcpay.AndroidKeyStore.SharedPreferencesHelper;
import com.example.root.fcpay.CoreData.PayPalData;
import com.example.root.fcpay.Model.OrderDetailModel;
import com.example.root.fcpay.MyStaticData;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewController;
import com.example.root.fcpay.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayPalVM extends AppCompatActivity {

    private RequestQueue mQueue;
    private SharedPreferences userProfileManager;
    private OrderDetailModel orderDetailModel = OrderTableViewController.orderDetailModel;
    private int paymentType;
    private SharedPreferencesHelper preferencesHelper;
    private KeyStoreHelper keyStoreHelper;  //自訂類別 用來加密機密資料
    public static PayPalData payPalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal_vm);
        mQueue = Volley.newRequestQueue(this);
        userProfileManager = getSharedPreferences("userProfile",0);
        preferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        keyStoreHelper = new KeyStoreHelper(getApplicationContext(), preferencesHelper);
        Intent intent = getIntent();
        paymentType = intent.getIntExtra("PAYMENTTYPE", -1);
        jsonParse();
    }

    private void jsonParse() {

        String url = MyStaticData.IP + "order.php";
        //打包product，傳送parameters用
        JSONArray products = new JSONArray();
        for(int i=0; i<orderDetailModel.getOrderDetailSize(); i++) {
            try {
                JSONObject product = new JSONObject();
                product.put("productID", orderDetailModel.getOrderDetailProductID(i));
                product.put("quantity", orderDetailModel.getOrderDetailProductQuantity(i));
                products.put(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //打包parameters
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("paymentType", String.valueOf(paymentType));
            parameters.put("location", keyStoreHelper.decrypt(userProfileManager.getString("locationId","")));
            parameters.put("memberId", keyStoreHelper.decrypt(userProfileManager.getString("NID","")));
            parameters.put("product", products);
            parameters.put("memo", "");
        }catch (Exception e){
            e.printStackTrace();
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            payPalData = new PayPalData(response.getString("payment_url").toString(),
                                    orderDetailModel.getTotalOrderPrice()+"");
                            finish();   //結束，關閉頁面
                            startActivity(new Intent(PayPalVM.this, PayPalVC.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //處理錯誤，並關閉頁面
                //showErrorDialog(Integer.toString(error.networkResponse.statusCode),new String(error.networkResponse.data));
                Log.v("Error", new String(error.networkResponse.data));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("user_id", keyStoreHelper.decrypt(userProfileManager.getString("NID","")).replace("\"",""));
                headers.put("user_auth", keyStoreHelper.decrypt(userProfileManager.getString("token","")).replace("\"",""));
                return headers;
            }
        };
        mQueue.add(request);
    }

    //處理錯誤
    private void showErrorDialog(String statusCode,String errorMessage) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("錯誤")
                .setMessage(statusCode + " , " + errorMessage.substring(12, errorMessage.length()-2) + ".")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                }).create();
        dialog.show();
    }
}
