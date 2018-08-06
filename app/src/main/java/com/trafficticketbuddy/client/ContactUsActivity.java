package com.trafficticketbuddy.client;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.trafficticketbuddy.client.utils.Constant;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import im.delight.android.webview.AdvancedWebView;

public class ContactUsActivity extends BaseActivity implements AdvancedWebView.Listener {
    //private TextView txtTitle;
    private AdvancedWebView mWebView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us1);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
       // txtTitle=toolbar.findViewById(R.id.txtTitle);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mWebView = (AdvancedWebView) findViewById(R.id.web);
        mWebView.setListener(this, this);
        if (getIntent().getStringExtra("key")!=null){
            //txtTitle.setText(getIntent().getStringExtra("key"));
           // toolbar.setTitle(getIntent().getStringExtra("key"));
            if (getIntent().getStringExtra("key").equalsIgnoreCase("Trams and Condition")){
                mWebView.loadUrl(Constant.TERMS_URL);
            }else if (getIntent().getStringExtra("key").equalsIgnoreCase("Privacy Policy")){
                mWebView.loadUrl(Constant.PRIVACY_URL);
            }else
                mWebView.loadUrl(Constant.ABOUT_US_URL);
        }


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        progressDialog.show();
    }

    @Override
    public void onPageFinished(String url) {
        progressDialog.dismiss();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        progressDialog.dismiss();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

}
