package com.example.root.fcpay.Order.OrderDetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.root.fcpay.AndroidKeyStore.KeyStoreHelper;
import com.example.root.fcpay.AndroidKeyStore.SharedPreferencesHelper;
import com.example.root.fcpay.CoreData.OrderDetailCheckItem;
import com.example.root.fcpay.Payment.LinePay.LinePayPaymentVM;
import com.example.root.fcpay.Payment.PayPal.PayPalVM;
import com.example.root.fcpay.Payment.iSunny.SunnyBankPaymentVM;
import com.example.root.fcpay.R;

import java.util.ArrayList;

public class OrderDetailUIViewController extends AppCompatActivity {

    private Toolbar toolBar;
    private  ArrayList<OrderDetailCheckItem> orderDetailCheckItems = OrderDetailUIViewModel.orderDetailCheckItems;
    private ListView orderDetailListView;
    private SharedPreferences userProfileManager;
    private SharedPreferencesHelper preferencesHelper;
    private KeyStoreHelper keyStoreHelper;  //自訂類別 用來加密機密資料

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_uiview_controller);
        userProfileManager = getSharedPreferences("userProfile",0);
        preferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        keyStoreHelper = new KeyStoreHelper(getApplicationContext(), preferencesHelper);
        initComponent();
    }

    private void initComponent() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        orderDetailListView = (ListView)findViewById(R.id.orderDetailListView);
        MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
        orderDetailListView.setAdapter(myCustomAdapter);
    }

    private class MyCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return orderDetailCheckItems.size();
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

            if(orderDetailCheckItems.get(i).layoutType == 1){
                view = getLayoutInflater().inflate(R.layout.layout_order_detail_head, null);
                TextView sectionTitle = (TextView) view.findViewById(R.id.sectionTitle);

                sectionTitle.setText(orderDetailCheckItems.get(i).itemName);
            }
            else if(orderDetailCheckItems.get(i).layoutType == 2){
                view = getLayoutInflater().inflate(R.layout.layout_order_detail_body, null);
                TextView orderDetailItemName = (TextView)view.findViewById(R.id.orderDetailItemName);
                TextView orderDetailItemValue = (TextView)view.findViewById(R.id.orderDetailItemValue);

                orderDetailItemName.setText(orderDetailCheckItems.get(i).itemName);
                orderDetailItemValue.setText(orderDetailCheckItems.get(i).itemValue);
            }

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_detail_ui_view, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toPayment:
                showDialog();
                //startActivity(new Intent(OrderDetailUIViewController.this,PaymentTypeController.class));
                return true;
            case android.R.id.home:
                finish();   //關閉頁面
                //startActivity(new Intent(OrderDetailUIViewController.this,OrderTableViewModel.class));        不必開新的頁面
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("訂餐確認")
                .setNegativeButton("否", null).setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int paymentType = Integer.valueOf(keyStoreHelper.decrypt(userProfileManager.getString("paymentTypeId", "尚未設置")));
                        Intent intent = new Intent();
                        intent.putExtra("PAYMENTTYPE", paymentType);
                        if (paymentType == 1) {
                            intent.setClass(OrderDetailUIViewController.this, SunnyBankPaymentVM.class);
                        } else if (paymentType == 2) {
                            intent.setClass(OrderDetailUIViewController.this, PayPalVM.class);
                        } else if (paymentType == 3) {
                            intent.setClass(OrderDetailUIViewController.this, LinePayPaymentVM.class);
                        } else {

                        }
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    }

                }).setMessage("是否送出訂單？").create();
        dialog.show();
    }
}
