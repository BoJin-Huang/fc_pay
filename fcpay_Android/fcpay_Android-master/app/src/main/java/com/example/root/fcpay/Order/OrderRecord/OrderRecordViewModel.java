package com.example.root.fcpay.Order.OrderRecord;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.root.fcpay.AndroidKeyStore.KeyStoreHelper;
import com.example.root.fcpay.AndroidKeyStore.SharedPreferencesHelper;
import com.example.root.fcpay.CoreData.OrderRecord;
import com.example.root.fcpay.CoreData.OrderRecordDetail;
import com.example.root.fcpay.Foundation.MyVolley.MyJsonArrayRequest;
import com.example.root.fcpay.MyStaticData;
import com.example.root.fcpay.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderRecordViewModel extends AppCompatActivity {

    private RequestQueue mQueue;
    public static ArrayList<OrderRecord> orderRecords = new ArrayList<>();
    private SharedPreferences userProfileManager;
    private SharedPreferencesHelper preferencesHelper;
    private KeyStoreHelper keyStoreHelper;  //自訂類別 用來加密機密資料

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_record_view_model);
        userProfileManager = getSharedPreferences("userProfile",0);
        preferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        keyStoreHelper = new KeyStoreHelper(getApplicationContext(), preferencesHelper);
        mQueue = Volley.newRequestQueue(this);
        jsonParse();
    }

    private void jsonParse() {

        String url = MyStaticData.IP + "orderRecord.php";

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("memberId", keyStoreHelper.decrypt(userProfileManager.getString("NID","")));
            parameters.put("offset", "0");
            parameters.put("limit", "40");
        }catch (Exception e){
            e.printStackTrace();
        }
        final MyJsonArrayRequest request = new MyJsonArrayRequest (Request.Method.POST, url, parameters,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            orderRecords.clear();
                            for (int i = 0; i < response.length(); i++) {

                                ArrayList<OrderRecordDetail> orderRecordDetails = new ArrayList<>();

                                JSONObject jsonObject = response.getJSONObject(i);
                                JSONArray details = jsonObject.getJSONArray("details");

                                for (int j = 0; j < details.length(); j++) {
                                    JSONObject detail = details.getJSONObject(j);
                                    orderRecordDetails.add(new OrderRecordDetail(
                                            detail.getString("product"),
                                            detail.getString("price"),
                                            detail.getString("manufacturer"),
                                            detail.getString("introduction"),
                                            detail.getString("quantity")
                                    ));
                                }
                                orderRecords.add(new OrderRecord(
                                        jsonObject.getString("orderId"),
                                        jsonObject.getString("totalPrice"),
                                        jsonObject.getString("location"),
                                        jsonObject.getString("orderDate"),
                                        jsonObject.getString("pickup"),
                                        jsonObject.getString("paymentType"),
                                        jsonObject.getString("status"),
                                        jsonObject.getString("statusDescription"),
                                        orderRecordDetails
                                ));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finish();   //完成，將空白頁面刪除
                        startActivity(new Intent(OrderRecordViewModel.this, OrderRecordViewController.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //處理錯誤，並關閉頁面
                showErrorDialog(Integer.toString(error.networkResponse.statusCode),new String(error.networkResponse.data));
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
