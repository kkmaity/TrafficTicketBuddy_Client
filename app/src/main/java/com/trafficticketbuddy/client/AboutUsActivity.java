package com.trafficticketbuddy.client;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.trafficticketbuddy.client.utils.Constant;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import im.delight.android.webview.AdvancedWebView;

public class AboutUsActivity extends BaseActivity {
    private TextView txtTitle;
    private ProgressDialog progressDialog;
    private JustifiedTextView tvAboutUs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = toolbar.findViewById(R.id.txtTitle);
        progressDialog = new ProgressDialog(this);

        if (getIntent().getStringExtra("key") != null) {
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

        tvAboutUs = (JustifiedTextView) findViewById(R.id.tvAboutUs);
        tvAboutUs.setText(Html.fromHtml(getResources().getString(R.string.about_us_html)));

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

}