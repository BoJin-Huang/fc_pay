package com.example.root.fcpay.Order.OrderTable;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.root.fcpay.CoreData.OrderDetail;
import com.example.root.fcpay.Model.OrderDetailModel;
import com.example.root.fcpay.Model.ProductModel;
import com.example.root.fcpay.Order.OrderDetail.OrderDetailUIViewModel;
import com.example.root.fcpay.Order.OrderRecord.OrderRecordViewModel;
import com.example.root.fcpay.Order.PlentyOrder.OrderPlentyViewController;
import com.example.root.fcpay.Profile.UserProfileViewController;
import com.example.root.fcpay.R;

public class OrderTableViewController extends AppCompatActivity {

    private BottomNavigationView bnv;   //下方菜單
    private ListView productListView;   //產品列表
    private Toolbar toolBar;    //上方菜單
    private ProductModel productModel = OrderTableViewModel.productModel;   //獲得OrderTableViewModel中得到的產品資料
    public static OrderDetailModel orderDetailModel = new OrderDetailModel();   //自訂類別用來儲存訂單資訊

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_table_view_controller);
        initComponent();
        setEventListener();
    }

    //設置元件
    private void initComponent(){
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        bnv = (BottomNavigationView) findViewById(R.id.bottomNavigationView2);
        productListView = (ListView)findViewById(R.id.productListView);
        MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
        productListView.setAdapter(myCustomAdapter);
    }

    //設定元件動作
    private void setEventListener(){
        //設定點選商品ListView的動作，會直接將選取的商品以1份加入訂單中，接著進入訂單確認畫面
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == productModel.size()){
                    Intent OrderDetailUIViewModel = new Intent(OrderTableViewController.this, OrderPlentyViewController.class);
                    startActivity(OrderDetailUIViewModel);
                }
                else{
                    orderDetailModel.clear();
                    orderDetailModel.addOrderDetail(new OrderDetail(
                            productModel.getID(i),
                            productModel.getName(i),
                            productModel.getPrice(i),
                            productModel.getManufacturerName(i),
                            productModel.getIntroduce(i),
                            "1"
                    ));
                    startActivity(new Intent(OrderTableViewController.this, OrderDetailUIViewModel.class));
                }
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
                        break;
                    case R.id.list:
                        finish();   //完成，關閉頁面
                        startActivity(new Intent(OrderTableViewController.this, OrderRecordViewModel.class));
                        break;
                    case R.id.profile:
                        finish();   //完成，關閉頁面
                        startActivity(new Intent(OrderTableViewController.this, UserProfileViewController.class));
                        break;
                }
                return true;
            }
        });
        bnv.getMenu().getItem(0).setChecked(true);
    }

    //產生ListView內容
    private class MyCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return productModel.size()+1;
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

            if(i != productModel.size()) {
                view = getLayoutInflater().inflate(R.layout.layout_order_table, null);
                TextView productName = (TextView) view.findViewById(R.id.productName);
                TextView productPrice = (TextView) view.findViewById(R.id.productPrice);
                TextView ManufacturerName = (TextView) view.findViewById(R.id.manufacturerName);

                productName.setText(productModel.getName(i));
                productPrice.setText(productModel.getPrice(i));
                ManufacturerName.setText(productModel.getManufacturerName(i));
            }
            else{
                view = getLayoutInflater().inflate(R.layout.layout_order_table_foot, null);
                TextView foot = (TextView)view.findViewById(R.id.foot);
                foot.setText("訂多個便當");
            }
            return view;
        }
    }

    //設定上方菜單
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_table, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                finish();      //完成，關閉頁面
                startActivity(new Intent(OrderTableViewController.this, OrderTableViewModel.class ));
                return true;
            case android.R.id.home:
                ExitDialog(OrderTableViewController.this).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //設定返回建動作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitDialog(OrderTableViewController.this).show();
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
