package com.example.root.fcpay.Login;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.root.fcpay.AndroidKeyStore.KeyStoreHelper;
import com.example.root.fcpay.AndroidKeyStore.SharedPreferencesHelper;
import com.example.root.fcpay.Order.OrderTable.OrderTableViewModel;
import com.example.root.fcpay.R;
import com.example.root.fcu_oauth.OAuthLogin;

public class HomeViewController extends AppCompatActivity {

    private Toolbar toolBar;
    private SharedPreferences userProfileManager;
    private SharedPreferencesHelper preferencesHelper;
    private KeyStoreHelper keyStoreHelper;  //自訂類別 用來加密機密資料
    private Intent intent;
    private static final int CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view_controller);
        initComponent();
        preferencesHelper = new SharedPreferencesHelper(getApplicationContext());
        keyStoreHelper = new KeyStoreHelper(getApplicationContext(), preferencesHelper);
        userProfileManager = getSharedPreferences("userProfile",0);
    }
    private void initComponent() {
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        intent=new Intent();
        intent.setClass(HomeViewController.this,OAuthLogin.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.login:
                //startActivity(new Intent(HomeViewController.this, OAuthLoginViewController.class ));
                startActivityForResult(intent, CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ExitDialog(HomeViewController.this).show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private Dialog ExitDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_logo);
        builder.setTitle("系統訊息");
        builder.setMessage("確定要退出程式？");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case CODE:
                //Toast.makeText(this, data.getExtras().getString("B"), Toast.LENGTH_LONG).show();
                if(data == null){   //取消登入 無法取得資料
                    break;
                }
                userProfileManager.edit().putString("NID", keyStoreHelper.encrypt(data.getExtras().getString("nid", "error"))).commit();
                userProfileManager.edit().putString("token", keyStoreHelper.encrypt(data.getExtras().getString("token", "error"))).commit();
                Toast toast = Toast.makeText(HomeViewController.this,
                        "歡迎使用 FC Order!! " + data.getExtras().getString("nid") + "!!", Toast.LENGTH_LONG);
                toast.show();
                startActivity(new Intent(HomeViewController.this, OrderTableViewModel.class));
                break;
        }
    }
}
