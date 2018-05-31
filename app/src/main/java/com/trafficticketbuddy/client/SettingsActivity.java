package com.trafficticketbuddy.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity {

    private TextView tvHeading;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
  */      setContentView(R.layout.activity_settings);
        tvHeading = (TextView)findViewById(R.id.tvHeading);
        tvHeading.setText("SETTINGS");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
