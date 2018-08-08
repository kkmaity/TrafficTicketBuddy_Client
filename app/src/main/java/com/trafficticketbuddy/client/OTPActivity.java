package com.trafficticketbuddy.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.trafficticketbuddy.client.apis.ApiResendOTP;
import com.trafficticketbuddy.client.apis.ApiValidateOTP;
import com.trafficticketbuddy.client.model.login.Response;
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
    private TextView tv_otp_txt;
    private Response mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_validation);
        etOTP=(EditText)findViewById(R.id.etOTP);
        tvTimer=(TextView)findViewById(R.id.tvTimer);
        tv_otp_txt=(TextView)findViewById(R.id.tv_otp_txt);
        ivReSend=(ImageView)findViewById(R.id.ivReSend);
        cardSubmit=(CardView)findViewById(R.id.cardSubmit);
        cardSubmit.setOnClickListener(this);
        ivReSend.setOnClickListener(this);

        Gson gson = new Gson();
        String json = preference.getString("login_user", "");
        mLogin = gson.fromJson(json, Response.class);


       // tv_otp_txt.setOnClickListener(this);
        tv_otp_txt.setText("A 4-digit OTP has been sent to "+mLogin.getPhone()+". Plesae enter the OTP below to verify your phone mumber.");


       // recendOTP();
        startTimer();
    }
public void startTimer(){
    ivReSend.setVisibility(View.GONE);
    tvTimer.setVisibility(View.VISIBLE);
    new CountDownTimer(90000, 1000) {

        public void onTick(long millisUntilFinished) {
            tvTimer.setText("Timer "+String.format("%02d", millisUntilFinished / 1000)+" s");
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
                            Gson gson = new Gson();
                            String json = preference.getString("login_user", "");
                            Response mLoginMain = gson.fromJson(json, Response.class);
                            if(mLoginMain.getRegister_from().equals("NORMAL")){
                                startActivity(new Intent(OTPActivity.this, LoginActivity.class));
                                finish();
                            }else{

                                mLoginMain.setIsPhoneVerified("1");
                                preference.setLoggedInUser(new Gson().toJson(mLoginMain));
                                if(EditProfileActivity.editProfileActivity!=null){
                                    EditProfileActivity.editProfileActivity.finish();
                                }

                                if(MyProfileActivity.myProfileActivity!=null){
                                    MyProfileActivity.myProfileActivity.finish();
                                }


                               // startActivity(new Intent(OTPActivity.this, MainActivity.class));
                                finish();
                            }

                           /* mLogin.setIsPhoneVerified("1");
                            preference.setLoggedInUser(new Gson().toJson(mLogin));
                            if(mLogin.getIsEmailVerified().equalsIgnoreCase("0")) {
                                startActivity(new Intent(OTPActivity.this, EmailOTPActivity.class));
                            }else{

                            }*/
                        }else{
                            Toast.makeText(OTPActivity.this, "OTP does not match", Toast.LENGTH_SHORT).show();
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
        map.put("user_id",mLogin.getId());
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
                        if (object.getBoolean("status")){ }
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
        map.put("user_id",mLogin.getId());
        map.put("phone",mLogin.getPhone());

        return map;
    }
}
