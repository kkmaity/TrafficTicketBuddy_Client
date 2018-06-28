package com.trafficticketbuddy.client.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.trafficticketbuddy.client.BaseActivity;
import com.trafficticketbuddy.client.OTPActivity;
import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.RegistrationActivity;
import com.trafficticketbuddy.client.apis.ApiRating;
import com.trafficticketbuddy.client.apis.ApiRegistration;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DlgRate extends Dialog{


    public interface OnRatingListiner{
        public void onRateDone();
    }
    private BaseActivity baseActivity;
    private LinearLayout llMain;
    private AppCompatButton submit_btn;
    private EditText ll_comment_write;
    private com.trafficticketbuddy.client.model.login.Response mLogin;
    private RatingBar ll_rating_bar;
    private String lawyer_id, case_id, bid_id;
    private OnRatingListiner listener;

    public DlgRate(@NonNull BaseActivity baseActivity,String lawyer_id,String case_id,String bid_id,OnRatingListiner listener) {
        super(baseActivity);
        this.baseActivity = baseActivity;
        this.lawyer_id = lawyer_id;
        this.case_id = case_id;
        this.bid_id = bid_id;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.dialog_rating);

        Gson gson = new Gson();
        String json = baseActivity.preference.getString("login_user", "");
        mLogin = gson.fromJson(json, com.trafficticketbuddy.client.model.login.Response.class);

        llMain = (LinearLayout) findViewById(R.id.llMain);
        ll_comment_write = (EditText) findViewById(R.id.ll_comment_write);
        ll_rating_bar = (RatingBar) findViewById(R.id.ll_rating_bar);
        submit_btn = (AppCompatButton) findViewById(R.id.submit_btn);
        llMain.getLayoutParams().height = (int) (baseActivity.height * 0.70);
        llMain.getLayoutParams().width = (int) (baseActivity.width * 0.85);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isvalid()){
                    callApi();
                }
              //  dismiss();
            }
        });
    }

    public boolean isvalid(){
        boolean f = true;
        if(ll_comment_write.getText().toString().trim().length() == 0){
            f = false;
            ll_comment_write.setError("Please give description");
        }
        return f;
    }



    private void callApi() {
        if (baseActivity.isNetworkConnected()){
            baseActivity.showProgressDialog();
            new ApiRating(getParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    baseActivity.dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                            dismiss();
                            listener.onRateDone();
                        }
                        else{
                            Toast.makeText(baseActivity, "Something wrong", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.print(res);



                }

                @Override
                public <E> void onError(E t) {
                    baseActivity.dismissProgressDialog();
                }

                @Override
                public void onError() {
                    baseActivity.dismissProgressDialog();
                }
            });
        }









    }



    private Map<String, String> getParam() {
        Map<String,String> map=new HashMap<>();
        map.put("lawyer_id",""+lawyer_id);
        map.put("case_id",""+case_id);
        map.put("bid_id",""+bid_id);
        map.put("rating",""+(int)ll_rating_bar.getRating());
        map.put("description",""+ll_comment_write.getText().toString().trim());
        map.put("user_id",""+mLogin.getId());
        return map;

    }
}
