package com.trafficticketbuddy.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends BaseActivity{
    private String deviceToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        callNewScreen();
    }


    private void callNewScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preference.getUserId()!=null&&preference.getUserId().length()>0){
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else if(preference.getIsFromSocial()){
                    Intent i = new Intent(SplashActivity.this, EditProfileActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 5000);
    }


}
