package com.trafficticketbuddy.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.trafficticketbuddy.client.utils.Constant;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends BaseActivity {
    private CardView carSignUp;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etCity;
    private EditText etPassword;
    private CheckBox chbxAgree;
    private TextView tvTramsCondition;
    private TextView tvLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_registration);

        init();



    }

    private void init() {
        carSignUp =(CardView)findViewById(R.id.cardSignUp);
        etFirstName=(EditText) findViewById(R.id.etFirstName);
        etLastName=(EditText)findViewById(R.id.etLastName);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etCity=(EditText)findViewById(R.id.etCity);
        chbxAgree=(CheckBox)findViewById(R.id.chbxAgree);
        tvTramsCondition=(TextView)findViewById(R.id.tvTramsCondition);
        tvLogin=(TextView)findViewById(R.id.tvLogin);

        carSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.cardSignUp:
                if (validate()){
                    callApi();
                }
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));

                break;

        }
    }

    private void callApi() {







    }
   private Map<String,String> getParam(){
        Map<String,String> map=new HashMap<>();
        map.put("first_name",etFirstName.getText().toString());
        map.put("last_name",etLastName.getText().toString());
        map.put("email",etEmail.getText().toString());
        map.put("phone",etPhone.getText().toString());
        map.put("gender","M");
        map.put("password",etPassword.getText().toString());
        map.put("user_type", Constant.USER_TYPE);
        return map;
    }

    private boolean validate() {
        return false;
    }
}
