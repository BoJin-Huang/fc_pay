package com.example.root.fcpay.Order.PlentyOrder;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.root.fcpay.CoreData.OrderDetail;
import com.example.root.fcpay.Model.OrderDetailModel;
import com.example.root.fcpay.Model.ProductModel;
import com.example.root.fcpay.Order.OrderDetail.OrderDetailUIViewModel;
import com.example.root.fcpay.Order.OrderRecord.OrderRecordViewModel;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewController;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.Profile.UserProfileViewController;
import com.example.root.fcpay.R;

public class OrderPlentyViewController extends AppCompatActivity {

    private BottomNavigationView bnv;   //下方菜單
    private Toolbar toolBar;    //上方菜單
    private ProductModel productModel = OrderTableViewModel.productModel;   //獲得OrderTableViewModel中得到的產品資料
    private OrderDetailModel orderDetailModel = OrderTableViewController.orderDetailModel;  //自訂類別用來儲存訂單資訊
    private int index;  //用來紀錄ListView中哪個產品被點選
    private ListView plentyListView;
    private MyCustomAdapter myCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_plenty_view_controller);
        initComponent();
        productModel.clearQuantity();
        setEventListener();
    }

    //設置元件
    private void initComponent(){
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        bnv = (BottomNavigationView) findViewById(R.id.bottomNavigationView2);
        plentyListView = (ListView)findViewById(R.id.plentyListView);
        myCustomAdapter = new MyCustomAdapter();
        plentyListView.setAdapter(myCustomAdapter);
    }

    //設定元件動作
    private void setEventListener(){
        //當ListView被按，紀錄位置並跳出數量選擇器提示框
        plentyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                showDialog();
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
                        startActivity(new Intent(OrderPlentyViewController.this, OrderRecordViewModel.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(OrderPlentyViewController.this, UserProfileViewController.class));
                        break;
                }
                return true;
            }
        });
        bnv.getMenu().getItem(0).setChecked(true);
    }

    //數量選擇器提視窗
    public void showDialog(){
        final Dialog dialog = new Dialog(OrderPlentyViewController.this);
        dialog.setTitle("選擇數量");
        dialog.setContentView(R.layout.dialog_number_picker);
        Button b1 = (Button) dialog.findViewById(R.id.set);
        Button b2 = (Button) dialog.findViewById(R.id.cancel);
        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker);
        np.setMaxValue(100); // max value 100
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                productModel.setQuantity(index, String.valueOf(np.getValue()));
                myCustomAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        b2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // dismiss the dialog
            }
        });
        dialog.show();
    }

    //產生ListView內容
    private class MyCustomAdapter extends BaseAdapter {
        private int mCurrentItem=0;
        private boolean isClick=false;

        @Override
        public int getCount() {
            return productModel.size();
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
            view = getLayoutInflater().inflate(R.layout.layout_order_plenty_body, null);
            TextView orderPlentyItemName = (TextView)view.findViewById(R.id.orderPlentyItemName);
            TextView orderPlentyItemValue = (TextView)view.findViewById(R.id.orderPlentyItemValue);
            orderPlentyItemName.setText(String.format("%s\n%6s NT$%s",
                    productModel.getManufacturerName(i),
                    productModel.getName(i),
                    productModel.getPrice(i)));
            orderPlentyItemValue.setText(productModel.getQuantity(i));
            return view;
        }
    }

    //設定上方菜單
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_plenty, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.toDetail:
                orderDetailModel.clear();
                boolean isNull = true;
                for(int i = 0;i<productModel.size();i++) {
                    if(Integer.valueOf(productModel.getQuantity(i)) != 0) {
                        orderDetailModel.addOrderDetail(new OrderDetail(
                                productModel.getID(i),
                                productModel.getName(i),
                                productModel.getPrice(i),
                                productModel.getManufacturerName(i),
                                productModel.getIntroduce(i),
                                productModel.getQuantity(i)));
                        isNull = false;
                    }
                }
                if(isNull == true){
                    showErrorDialog();
                }
                else {
                    finish(); //結束，關閉頁面
                    startActivity(new Intent(OrderPlentyViewController.this, OrderDetailUIViewModel.class));
                }
                return true;
            case android.R.id.home:
                finish();   //上一頁，關閉頁面
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showErrorDialog() {

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("訂餐錯誤")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage("你沒有選擇任何餐點，請點選想要的餐點設定訂購數").create();
        dialog.show();
    }

    //設定返回鍵動作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitDialog(OrderPlentyViewController.this).show();
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