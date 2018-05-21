package com.trafficticketbuddy.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.trafficticketbuddy.client.apis.ApiResendOTP;
import com.trafficticketbuddy.client.apis.ApiValidateOTP;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends BaseActivity {

    private EditText etOTP;
    private TextView tvTimer;
    private ImageView ivReSend;
    private CardView cardSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validation);
        etOTP=(EditText)findViewById(R.id.etOTP);
        tvTimer=(TextView)findViewById(R.id.tvTimer);
        ivReSend=(ImageView)findViewById(R.id.ivReSend);
        cardSubmit=(CardView)findViewById(R.id.cardSubmit);
        cardSubmit.setOnClickListener(this);
        ivReSend.setOnClickListener(this);

        recendOTP();
        startTimer();
    }
public void startTimer(){
    ivReSend.setVisibility(View.GONE);
    tvTimer.setVisibility(View.VISIBLE);
    new CountDownTimer(50000, 1000) {

        public void onTick(long millisUntilFinished) {
            tvTimer.setText("" + millisUntilFinished / 1000);
        }

        public void onFinish() {
            ivReSend.setVisibility(View.VISIBLE);
            tvTimer.setVisibility(View.GONE);

            //mTextField.setText("done!");
        }
    }.start();
}
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.cardSubmit:
                if (!etOTP.getText().toString().isEmpty())
                    validateOTP();
                else
                    showDialog("Please Enter OTP");

                break;
            case R.id.ivReSend:
                recendOTP();
                startTimer();
                break;
        }
    }

    private void validateOTP() {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiValidateOTP(getParamValidate(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                            startActivity(new Intent(OTPActivity.this,MainActivity.class));
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

    private Map<String, String> getParamValidate() {
        Map<String,String> map=new HashMap<>();
        map.put("user_id",preference.getUserId());
        map.put("otp",etOTP.getText().toString());

        return map;
    }

    private void recendOTP() {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiResendOTP(getParamResendOTP(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                           // startActivity(new Intent(OTPActivity.this,MainActivity.class));

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
    private Map<String, String> getParamResendOTP() {
        Map<String,String> map=new HashMap<>();
        map.put("user_id",preference.getUserId());
        map.put("phone",preference.getPhone());

        return map;
    }
}
