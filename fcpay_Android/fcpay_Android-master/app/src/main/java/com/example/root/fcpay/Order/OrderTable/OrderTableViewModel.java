package com.example.root.fcpay.Order.OrderTable;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.root.fcpay.AndroidKeyStore.KeyStoreHelper;
import com.example.root.fcpay.AndroidKeyStore.SharedPreferencesHelper;
import com.example.root.fcpay.CoreData.Product;
import com.example.root.fcpay.Model.ProductModel;
import com.example.root.fcpay.MyStaticData;
import com.example.root.fcpay.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderTableViewModel extends AppCompatActivity {

    public static ProductModel productModel = new ProductModel();   //自訂類別 儲存所有的商品資訊
    private SharedPreferences userProfileManager;
    private SharedPreferencesHelper preferencesHelper;
    private KeyStoreHelper keyStoreHelper;  //自訂類別 用來加密機密資料
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_table_view_model);
        mQueue = Volley.newRequestQueue(this);
        userProfileManager = getSharedPreferences("userProfile",0);
        preferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        keyStoreHelper = new KeyStoreHelper(getApplicationContext(), preferencesHelper);
        productModel.clear();
        jsonParse();
    }
    private void jsonParse() {

        String url = MyStaticData.IP+"todayOrders.php";

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                productModel.addProduct(new Product(
                                        jsonObject.getString("productID"),
                                        jsonObject.getString("product"),
                                        jsonObject.getString("productPrice"),
                                        jsonObject.getString("manufacturerID"),
                                        jsonObject.getString("manufacturer"),
                                        jsonObject.getString("introduction")));
                            }
                            finish();      //完成，關閉頁面
                            startActivity(new Intent(OrderTableViewModel.this, OrderTableViewController.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                headers.put("user_id", keyStoreHelper.decrypt(userProfileManager.getString("NID","")));
                headers.put("user_auth", keyStoreHelper.decrypt(userProfileManager.getString("token","")));
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
