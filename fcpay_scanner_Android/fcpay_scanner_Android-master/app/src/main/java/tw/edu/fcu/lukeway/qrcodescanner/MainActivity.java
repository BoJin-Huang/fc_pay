package tw.edu.fcu.lukeway.qrcodescanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button scan_btn;

    private Context mContext;
    private RequestQueue mRequestQueue;
    public String nid;
    public String token;

    RequestQueue mQueue;
    public static ArrayList<OrderRecord> orderRecords = new ArrayList<>();

    ListView orderRecordListView;
    public ArrayList<OrderRecord> orderRecord = orderRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        nid = intent.getStringExtra("KEY_NID");
        token = intent.getStringExtra("KEY_TOKEN");
        Log.v("NID", nid);
        Log.v("TOKEN", token);

        scan_btn = (Button)findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        mQueue = Volley.newRequestQueue(this);
        Log.v("mid","mid");

        orderRecordListView = (ListView)findViewById(R.id.productlist);
        jsonParse();

        MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
        //myCustomAdapter.clearOrderList();
        myCustomAdapter.setDeviceList(orderRecords);
        orderRecordListView.setAdapter(myCustomAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result!= null)
        {
            if (result.getContents()==null)
            {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.v("result",result.getContents());
                String url = "http://fcorder.fcudata.science/deliveryCheckOrder.php";


                mContext = this;
                mRequestQueue = Volley.newRequestQueue(mContext);

                Map<String, String> map = new HashMap<String, String>();
                map.put("orderId", result.getContents());

                JSONObject jsonObject = new JSONObject(map);

                JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.v("OK","請求結果:" + response);
                                Toast.makeText(MainActivity.this,"完成領取餐點",Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Error","請求錯誤:" + error.toString());

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Accept", "application/json");
                        headers.put("Content-Type", "application/json; charset=UTF-8");
                        headers.put("user_id", nid);     //登入的nid
                        headers.put("user_auth", token);       //登入的token

                        return headers;
                    }
                };

                mRequestQueue.add(jsonRequest);
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //歷史訂單
    private void jsonParse() {

        String url = "http://fcorder.fcudata.science/orderRecord.php";

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("memberId",nid);
            Log.v("memberID",nid);
            parameters.put("offset", "0");
            parameters.put("limit", "20");
        }catch (Exception e){
            e.printStackTrace();
        }
        final MyJsonArrayRequest request = new MyJsonArrayRequest (Request.Method.POST, url, parameters,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(orderRecord.size() == 0) {
                                Log.v("長度", String.valueOf(response.length()));
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
                                Log.v("checksize", String.valueOf(orderRecords.size()));
                                MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
                                //myCustomAdapter.clearOrderList();
                                myCustomAdapter.setDeviceList(orderRecords);
                                orderRecordListView.setAdapter(myCustomAdapter);
                                Log.v("final", "OK");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.v("error", "error");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("user_id", nid);     //登入的nid
                headers.put("user_auth", token);       //登入的token

                return headers;
            }
        };

        mQueue.add(request);
    }

    //自訂清單
    private class MyCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            Log.v("sizes", String.valueOf(orderRecord.size()));
            return orderRecord.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {


            view = getLayoutInflater().inflate(R.layout.layout_order_record_body, null);
            TextView orderId = (TextView) view.findViewById(R.id.orderId);
            TextView date = (TextView) view.findViewById(R.id.date);
            TextView count = (TextView) view.findViewById(R.id.count);
            orderId.setText("共NT$ "+orderRecord.get(i).totalPrice);
            date.setText(orderRecord.get(i).orderDate);
            count.setText("共有 "+orderRecord.get(i).getTotalOrderNumber()+"個便當");
            ImageView icon = (ImageView) view.findViewById(R.id.icon);

            if(orderRecord.get(i).isPickup()) {
                icon.setImageResource(R.drawable.check);
            }
            else{
                //icon.setImageResource(R.drawable.ic_waiting_pickup);
            }
            notifyDataSetChanged();

            return view;
        }

        public void clearOrderList() {
            if (orderRecord != null) {
                orderRecord.clear();
            }
            notifyDataSetChanged();
        }

        public void setDeviceList(ArrayList<OrderRecord> list) {
            if (list != null) {
                orderRecord = (ArrayList<OrderRecord>) list.clone();
                notifyDataSetChanged();
            }
        }
    }
}
