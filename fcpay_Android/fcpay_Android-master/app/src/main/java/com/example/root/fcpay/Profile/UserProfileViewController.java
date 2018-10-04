package com.example.root.fcpay.Profile;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.root.fcpay.AndroidKeyStore.KeyStoreHelper;
import com.example.root.fcpay.AndroidKeyStore.SharedPreferencesHelper;
import com.example.root.fcpay.CoreData.UserProfileData;
import com.example.root.fcpay.Order.OrderRecord.OrderRecordViewModel;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.R;

import java.util.ArrayList;

public class UserProfileViewController extends AppCompatActivity {

    private BottomNavigationView bnv;
    private Toolbar toolBar;
    private ListView userProfileListView;
    private ArrayList<UserProfileData> userProfileData = new ArrayList<>();
    private SharedPreferences userProfileManager;
    private SharedPreferencesHelper preferencesHelper;
    private KeyStoreHelper keyStoreHelper;  //自訂類別 用來加密機密資料
    private EditText edit_iSunnyAC;
    private EditText edit_iSunnyPW;
    private Spinner edit_location;
    private Spinner edit_time;
    private Spinner edit_paymentType;
    private ArrayAdapter<CharSequence> time;
    private ArrayAdapter<CharSequence> location;
    private ArrayAdapter<CharSequence> paymentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_view_controller);
        userProfileManager = getSharedPreferences("userProfile",0);
        preferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        keyStoreHelper = new KeyStoreHelper(getApplicationContext(), preferencesHelper);
        setData();
        initComponent();
        setEventListener();
    }

    private void initComponent() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        bnv = (BottomNavigationView)findViewById(R.id.bottomNavigationView2);
        userProfileListView = (ListView)findViewById(R.id.userProfileListView);
        MyCustomAdapter myCustomAdapter = new MyCustomAdapter();
        userProfileListView.setAdapter(myCustomAdapter);
    }

    private void setEventListener(){

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(
                    @NonNull
                            MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        finish();   //完成，關閉頁面
                        startActivity(new Intent(UserProfileViewController.this, OrderTableViewModel.class));
                        break;
                    case R.id.list:
                        finish();   //完成，關閉頁面
                        startActivity(new Intent(UserProfileViewController.this, OrderRecordViewModel.class));
                        break;
                    case R.id.profile:
                        break;
                }
                return true;
            }
        });
        bnv.getMenu().getItem(2).setChecked(true);
    }

    private void setData(){
        userProfileData.add(new UserProfileData("OAuth資訊", "","1"));
        userProfileData.add(new UserProfileData("NID:", keyStoreHelper.decrypt(userProfileManager.getString("NID","尚未設置")),"2"));
        userProfileData.add(new UserProfileData("帳戶資訊", "","1"));
        userProfileData.add(new UserProfileData("帳戶帳號:", keyStoreHelper.decrypt(userProfileManager.getString("iSunnyAC","尚未設置")),"2"));
        userProfileData.add(new UserProfileData("帳戶密碼:", keyStoreHelper.decrypt(userProfileManager.getString("iSunnyPW","尚未設置")).replaceAll("\\w","*"),"2"));
        userProfileData.add(new UserProfileData("領貨資訊", "","1"));
        userProfileData.add(new UserProfileData("地點:", keyStoreHelper.decrypt(userProfileManager.getString("location","尚未設置")),"2"));
        userProfileData.add(new UserProfileData("時間:", keyStoreHelper.decrypt(userProfileManager.getString("time","尚未設置")),"2"));
        userProfileData.add(new UserProfileData("付款資訊", "","1"));
        userProfileData.add(new UserProfileData("方法:", keyStoreHelper.decrypt(userProfileManager.getString("paymentType","尚未設置")),"2"));
    }

    private class MyCustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userProfileData.size();
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

            if(userProfileData.get(i).dataType == "1"){
                view = getLayoutInflater().inflate(R.layout.layout_userprofile_head, null);
                TextView sectionTitle = (TextView) view.findViewById(R.id.sectionTitle);
                sectionTitle.setText(userProfileData.get(i).dataName);
            }
            else if(userProfileData.get(i).dataType == "2"){
                view = getLayoutInflater().inflate(R.layout.layout_userprofile_body, null);
                TextView orderDetailItemName = (TextView)view.findViewById(R.id.orderDetailItemName);
                TextView orderDetailItemValue = (TextView)view.findViewById(R.id.orderDetailItemValue);

                orderDetailItemName.setText(userProfileData.get(i).dataName);
                orderDetailItemValue.setText(userProfileData.get(i).dataValue);
            }

            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_profile, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                showDialog();
                return true;
            case android.R.id.home:
                ExitDialog(UserProfileViewController.this).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDialog(){

        final Dialog dialog = new Dialog(UserProfileViewController.this);
        dialog.setTitle("個人資料編輯");
        dialog.setContentView(R.layout.dialog_edit_user_profile);
        edit_iSunnyAC = (EditText) dialog.findViewById(R.id.edit_iSunnyAC);
        edit_iSunnyPW = (EditText) dialog.findViewById(R.id.edit_iSunnyPW);

        edit_iSunnyAC.setText(keyStoreHelper.decrypt(userProfileManager.getString("iSunnyAC","尚未設置")));
        edit_iSunnyPW.setText(keyStoreHelper.decrypt(userProfileManager.getString("iSunnyPW","尚未設置")).replaceAll("\\w","*"));

        edit_location = (Spinner) dialog.findViewById(R.id.edit_location);
        location = ArrayAdapter.createFromResource(
                this, R.array.location_array, android.R.layout.simple_spinner_item );
        location.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        edit_location.setAdapter(location);

        edit_time = (Spinner) dialog.findViewById(R.id.edit_time);
        time = ArrayAdapter.createFromResource(
                this, R.array.time_array, android.R.layout.simple_spinner_item );
        time.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        edit_time.setAdapter(time);

        edit_paymentType = (Spinner) dialog.findViewById(R.id.edit_paymentType);
        paymentType = ArrayAdapter.createFromResource(
                this, R.array.paymentType_array, android.R.layout.simple_spinner_item );
        paymentType.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        edit_paymentType.setAdapter(paymentType);

        Button b1 = (Button) dialog.findViewById(R.id.set);
        Button b2 = (Button) dialog.findViewById(R.id.cancel);
        b1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileManager.edit().putString("iSunnyAC",keyStoreHelper.encrypt(edit_iSunnyAC.getText().toString())).commit();
                userProfileManager.edit().putString("iSunnyPW",keyStoreHelper.encrypt(edit_iSunnyPW.getText().toString())).commit();
                userProfileManager.edit().putString("location",keyStoreHelper.encrypt(location.getItem(edit_location.getLastVisiblePosition()).toString())).commit();
                userProfileManager.edit().putString("locationId",keyStoreHelper.encrypt(String.valueOf(edit_location.getLastVisiblePosition()))).commit();
                //Log.v("location", String.valueOf(edit_location.getLastVisiblePosition()+1));
                userProfileManager.edit().putString("time",keyStoreHelper.encrypt(time.getItem(edit_time.getLastVisiblePosition()).toString())).commit();
                userProfileManager.edit().putString("paymentType",keyStoreHelper.encrypt(paymentType.getItem(edit_paymentType.getLastVisiblePosition()).toString())).commit();
                userProfileManager.edit().putString("paymentTypeId",keyStoreHelper.encrypt(String.valueOf(edit_paymentType.getLastVisiblePosition()+1))).commit();
                //Log.v("paymentType", String.valueOf(edit_paymentType.getLastVisiblePosition()+1));
                dialog.dismiss();
                finish();   //完成，存擋並重新整理頁面
                startActivity(new Intent(UserProfileViewController.this,UserProfileViewController.class));
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

    //返回鍵動作
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitDialog(UserProfileViewController.this).show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private Dialog ExitDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_logo);
        builder.setTitle("系统信息");
        builder.setMessage("确定要回到主選單?");
        builder.setPositiveButton("确定",
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
