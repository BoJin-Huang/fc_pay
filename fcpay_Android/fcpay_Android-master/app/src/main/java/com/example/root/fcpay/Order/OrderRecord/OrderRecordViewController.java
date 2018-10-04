package com.example.root.fcpay.Order.OrderRecord;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.root.fcpay.CoreData.OrderRecord;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.Profile.UserProfileViewController;
import com.example.root.fcpay.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class OrderRecordViewController extends AppCompatActivity {

    private BottomNavigationView bnv;   //下方菜單
    private Toolbar toolBar;    //上方菜單
    private ListView orderRecordListView;   //訂單列表
    private ArrayList<OrderRecord> orderRecords = OrderRecordViewModel.orderRecords;    //獲得OrderRecordViewModel中得到的訂單資料

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_record_view_controller);
        initComponent();
        setEventListener();
    }

    //設置元件
    private void initComponent() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        bnv = (BottomNavigationView) findViewById(R.id.bottomNavigationView2);
        orderRecordListView = (ListView)findViewById(R.id.orderRecordListView);
        MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
        orderRecordListView.setAdapter(myCustomAdapter);
    }

    //設定元件動作
    private void setEventListener(){
        //設定點選ListView動作(產生QRcode視窗)
        orderRecordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showQRCodeDialog(i);
                view.setSelected(true);
            }
        });
        //設定下方菜單動作
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(
                    @NonNull
                            MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        finish();   //完成，關閉頁面
                        startActivity(new Intent(OrderRecordViewController.this, OrderTableViewModel.class));
                        break;
                    case R.id.list:
                        break;
                    case R.id.profile:
                        finish();   //完成，關閉頁面
                        startActivity(new Intent(OrderRecordViewController.this, UserProfileViewController.class));
                        break;
                }
                return true;
            }
        });
        bnv.getMenu().getItem(1).setChecked(true);
    }

    //產生ListView內容
    private class MyCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return orderRecords.size();
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
            orderId.setText("共NT$ "+orderRecords.get(i).getTotalOrderPrice());
            date.setText(orderRecords.get(i).orderDate);
            count.setText("共有 "+orderRecords.get(i).getTotalOrderNumber()+"個便當");
            ImageView icon = (ImageView) view.findViewById(R.id.icon);

            if(orderRecords.get(i).isPickup()) {
                icon.setImageResource(R.drawable.check);
            }
            else{
                //icon.setImageResource(R.drawable.ic_waiting_pickup);
            }
            notifyDataSetChanged();
            return view;
        }
    }

    //產生QRCode
    public void showQRCodeDialog(int index){

        final Dialog dialog = new Dialog(OrderRecordViewController.this);
        dialog.setTitle("選擇數量");
        dialog.setContentView(R.layout.dialog_qrcode_dialog);
        ImageView ivCode = (ImageView)dialog.findViewById(R.id.ivCode);
        BarcodeEncoder encoder = new BarcodeEncoder();
        try {
            Bitmap bit = encoder.encodeBitmap(orderRecords.get(index).orderId, BarcodeFormat.QR_CODE,
                    500, 500);
            ivCode.setImageBitmap(bit);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        TextView orderId = (TextView)dialog.findViewById(R.id.dialogOrderId);
        TextView orderNumber = (TextView)dialog.findViewById(R.id.dialogTotalOrderNumber);
        TextView orderPrice = (TextView)dialog.findViewById(R.id.dialogTotalOrderPrice);

        //處理各項目的字串，合成一個String
        String detail = "";
        int voi = orderRecords.get(index).details.size();
        for(int i=0;i<orderRecords.get(index).details.size();i++){
            detail = detail + orderRecords.get(index).details.get(i).quantity + "個 ";
            detail = detail + orderRecords.get(index).details.get(i).manufacturer;
            detail = detail + " 的 " + orderRecords.get(index).details.get(i).product + "\n";
        }

        orderId.setText(orderRecords.get(index).orderId);
        orderNumber.setText(detail);  //"便當總數量: "+Integer.toString(orderRecords.get(index).getTotalOrderNumber())
        orderPrice.setText("共有 "+Integer.toString(orderRecords.get(index).getTotalOrderNumber())+" 個便當\n便當總價格: NT "+Integer.toString(orderRecords.get(index).getTotalOrderPrice())+"$");

        dialog.show();
    }

    //設定上方菜單
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_record, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                finish();       //重新整理，重新請求一次
                startActivity(new Intent(OrderRecordViewController.this, OrderRecordViewModel.class ));
                return true;
            case android.R.id.home:
                ExitDialog(OrderRecordViewController.this).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //設定返回建動作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitDialog(OrderRecordViewController.this).show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private Dialog ExitDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_logo);
        builder.setTitle("系統訊息");
        builder.setMessage("確定要回到登入頁面？");
        builder.setPositiveButton("確定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        return builder.create();
    }
}
