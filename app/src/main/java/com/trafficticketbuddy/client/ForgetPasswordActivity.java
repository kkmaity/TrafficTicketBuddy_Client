package com.trafficticketbuddy.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trafficticketbuddy.client.apis.ApiForgotPassword;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends BaseActivity {

    private EditText etEmail;
    private TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Forgot Password");
        //getSupportActionBar().setTitle("Change Password");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etEmail=findViewById(R.id.etEmail);
        tvSubmit=findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tvSubmit:
                if (etEmail.getText().toString().isEmpty()){
                    showDialog("Please enter your registered email.");
                }else if (!isValidEmail(etEmail.getText().toString())){
                    showDialog("Please enter a valid email.");
                }else{
                     callApi();
                }

                break;
        }
    }

    private void callApi() {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiForgotPassword(getParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                            Toast.makeText(ForgetPasswordActivity.this, ""+object.optString("message"), Toast.LENGTH_LONG).show();
                           // startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                           // finish();

                        }
                        else{
                            Toast.makeText(ForgetPasswordActivity.this, "Something wrong", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.print(res);
                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();

                }

                @Override
                public void onError() {
                    dismissProgressDialog();

                }
            });
        }
    }

    private Map<String, String> getParam() {
        Map<String,String> map=new HashMap<>();
        map.put("email",etEmail.getText().toString());
        map.put("user_type", Constant.USER_TYPE);
        return map;
    }
}
