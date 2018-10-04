package tw.edu.fcu.lukeway.qrcodescanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,OAuthLoginViewController.class);
        startActivity(intent);
    }
}
