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
import android.widget.EditText;
import android.widget.TextView;

import com.trafficticketbuddy.client.apis.ApiLogin;
import com.trafficticketbuddy.client.model.login.LoginMain;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.utils.Constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    private CardView cardLogin,cvGoogleLogin,cvFbLogin;
    private TextView tvForgetPassword;
    private EditText etEmail,etPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cardLogin=(CardView)findViewById(R.id.cardSignUp);
        //setContentView(R.layout.activity_login);
       // cardLogin=(CardView)findViewById(R.id.cardLogin);
        cvGoogleLogin=(CardView)findViewById(R.id.cvGoogleLogin);
        cvFbLogin=(CardView)findViewById(R.id.cvFbLogin);
        tvForgetPassword=(TextView)findViewById(R.id.tvForgetPassword);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        tvForgetPassword.setOnClickListener(this);
        cvGoogleLogin.setOnClickListener(this);
        cvFbLogin.setOnClickListener(this);
        cvFbLogin.setOnClickListener(this);

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
                case R.id.cardLogin:
                isValidate();
                break;
        }
    }

    private void doLoginApi() {
        new ApiLogin(getParam(), new OnApiResponseListener() {
            @Override
            public <E> void onSuccess(E t) {
                {
                    LoginMain mLoginMain = (LoginMain)t;
                    if(mLoginMain.getStatus()){

                    }else{
                        if(mLoginMain.getMessage().equalsIgnoreCase("phone not verified")){
                            startActivity(new Intent(LoginActivity.this,OTPActivity.class));
                        }else{

                        }
                    }

                }
            }

            @Override
            public <E> void onError(E t) {

            }

            @Override
            public void onError() {

            }
        });
    }


    private void isValidate(){
        if(etEmail.getText().toString().isEmpty()){
           etEmail.setError("Please enter email id");
        }else if(etPassword.getText().toString().isEmpty()){
            etEmail.setError("Please enter password");
        }else {
            doLoginApi();
        }
    }


    private Map<String,String> getParam(){
        Map<String,String> map=new HashMap<>();
        map.put("email",etEmail.getText().toString());
        map.put("password",etPassword.getText().toString());
        map.put("user_type",Constant.USER_TYPE);
        return map;
    }
}
