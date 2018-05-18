package com.trafficticketbuddy.client;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends BaseActivity {
    private CardView cardLogin,cvGoogleLogin,cvFbLogin;
    private TextView tvForgetPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
        setContentView(R.layout.activity_login);
        cardLogin=(CardView)findViewById(R.id.cardLogin);
        cvGoogleLogin=(CardView)findViewById(R.id.cvGoogleLogin);
        cvFbLogin=(CardView)findViewById(R.id.cvFbLogin);
        tvForgetPassword=(TextView)findViewById(R.id.tvForgetPassword);
        tvForgetPassword.setOnClickListener(this);
        cvGoogleLogin.setOnClickListener(this);
        cvFbLogin.setOnClickListener(this);
        cardLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
        findViewById(R.id.txtRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tvForgetPassword:
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
                break;
                case R.id.cvGoogleLogin:
                signIn();
                break;
                case R.id.cvFbLogin:
                fbSignInClick();
                break;

        }
    }
}
