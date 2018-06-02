package com.trafficticketbuddy.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity {

    private TextView tvHeading;
    private ImageView back;
    private LinearLayout linAboutUs,linCntactUs,linPrivacyPolicy,linTramsCondition,linChangePAss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
  */      setContentView(R.layout.activity_settings);
        tvHeading = (TextView)findViewById(R.id.tvHeading);
        linAboutUs = (LinearLayout)findViewById(R.id.linAboutUs);
        linCntactUs = (LinearLayout)findViewById(R.id.linCntactUs);
        linPrivacyPolicy = (LinearLayout)findViewById(R.id.linPrivacyPolicy);
        linTramsCondition = (LinearLayout)findViewById(R.id.linTramsCondition);
        linChangePAss = (LinearLayout)findViewById(R.id.linChangePAss);
        tvHeading.setText("Settings");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        linAboutUs.setOnClickListener(this);
        linTramsCondition.setOnClickListener(this);
        linCntactUs.setOnClickListener(this);
        linChangePAss.setOnClickListener(this);
        linPrivacyPolicy.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
                case R.id.linAboutUs:
                    Intent intent=new Intent(SettingsActivity.this,AboutUsActivity.class);
                    intent.putExtra("key","About Us");
                     startActivity(intent);
                break;
                case R.id.linChangePAss:
                startActivity(new Intent(SettingsActivity.this,ChangePAsswordActivity.class));
                break;
                case R.id.linTramsCondition:
                    Intent intent2=new Intent(SettingsActivity.this,AboutUsActivity.class);
                    intent2.putExtra("key","Trams and Condition");
                    startActivity(intent2);                break;
                case R.id.linPrivacyPolicy:
                    Intent intent3=new Intent(SettingsActivity.this,AboutUsActivity.class);
                    intent3.putExtra("key","Privacy Policy");
                    startActivity(intent3);
                    break;
                case R.id.linCntactUs:
                    Intent intent4=new Intent(SettingsActivity.this,AboutUsActivity.class);
                    intent4.putExtra("key","Contact Us");
                    startActivity(intent4);
                break;
        }
    }
}
