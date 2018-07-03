package com.trafficticketbuddy.client;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.trafficticketbuddy.client.apis.ApiResetPassword;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePAsswordActivity extends BaseActivity {

    private EditText newPass,oldPass;
    private TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Change Password");
        //getSupportActionBar().setTitle("Change Password");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
    }

    private void init() {
        oldPass=findViewById(R.id.oldPass);
        newPass=findViewById(R.id.newPass);
        tvSubmit=findViewById(R.id.tvSubmit);
        tvSubmit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tvSubmit:
                if (oldPass.getText().toString().isEmpty()){
                    showDialog("Please Enter Your Old Password");
                }else if (newPass.getText().toString().isEmpty()){
                    showDialog("Please Enter Your New Password");
                }else
                callApi();
                break;
        }
    }

    private void callApi() {
        if (isNetworkConnected()){
            showProgressDialog();
            new  ApiResetPassword(getParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                            showDialog(object.getString("message"));
                           // onBackPressed();
                        }
                        else
                            showDialog(object.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
        map.put("user_id",preference.getUserId());
        map.put("old_password",oldPass.getText().toString());
        map.put("new_password",newPass.getText().toString());



        return map;
    }
}
