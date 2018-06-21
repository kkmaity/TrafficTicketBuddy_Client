package com.trafficticketbuddy.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class AboutUsActivity extends BaseActivity {
    private TextView txtTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        txtTitle=toolbar.findViewById(R.id.txtTitle);
        if (getIntent().getStringExtra("key")!=null){
            txtTitle.setText(getIntent().getStringExtra("key"));
           // toolbar.setTitle(getIntent().getStringExtra("key"));
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }
}
