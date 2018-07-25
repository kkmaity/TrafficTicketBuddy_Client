package com.trafficticketbuddy.client;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.uncopt.android.widget.text.justify.JustifiedTextView;

public class AboutUsActivity extends BaseActivity {
    private TextView txtTitle;
    private JustifiedTextView tvAboutUs;

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

        tvAboutUs=findViewById(R.id.tvAboutUs);

        String str1 = "<!DOCTYPE html>\\n\" +\n" +
                "                \"<html>\\n\" +\n" +
                "                \"<head>\\n\" +\n" +
                "                \"<style>\\n\" +\n" +
                "                \"p {\\n\" +\n" +
                "                \"    display: block;\\n\" +\n" +
                "                \"    margin-top: 1em;\\n\" +\n" +
                "                \"    margin-bottom: 1em;\\n\" +\n" +
                "                \"    margin-left: 0;\\n\" +\n" +
                "                \"    margin-right: 0;\\n\" +\n" +
                "                \"}\\n\" +\n" +
                "                \"</style>\\n\" +\n" +
                "                \"</head>\\n\" +\n" +
                "                \"<body>\\n\" +\n" +
                "                \"<h1 style=\\\"font-size:16px;\\\">ABOUT US</h1>\\n\" +\n" +
                "                \"<p style=\\\"font-size:13px;\\\">Traffic Ticket Buddy is owned and operated by (Company name), which is a registered legal document assistant company, that specializes in helping motorists to fight their (country/state) traffic tickets through the Trial by Declaration process. We are registered to cover all of (state you cover), and we take pride in custom preparing each Trial by declaration defense package by hand. Our goal is to give our customers the best chance to get their tickets dismissed as fast as possible. No points, no school.\\n\" +\n" +
                "                \"Our experience and excellent track record in providing legal services to motorists charged with traffic offenses like speeding, suspended license and DUI has made us everyone's choice.\\n\" +\n" +
                "                \"We make all court appearances for you – so you do not have to go and miss work or spend long hours in line or in court. We help people with their traffic ticket problems in ALL courts and courthouses in (state you cover).</p>\\n\" +\n" +
                "                \"<h1 style=\\\"font-size:16px;\\\">OUR VISION</h1>\\n\" +\n" +
                "                \"<p style=\\\"font-size:13px;\\\">We never use computer software to generate our defense packages and this is what makes us stand out.</p>\\n\" +\n" +
                "                \"<p style=\\\"font-size:13px;\\\">If you have a traffic ticket or warrant out of in (state you cover) or GC Services has sent you a letter charging you a big fine or your license is suspended because of a traffic ticket and you need help clearing it up, feel free to call us for a free legal consultation (phone number) within our business hours. We will answer all of your questions and offer advice on how to fix your traffic ticket problem.</p>\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"<p style=\\\"font-size:15px;\\\">At Traffic Ticket Buddy, we work hard to protect your rights!</>\\n\" +\n" +
                "                \"</body>\\n\" +\n" +
                "                \"</html>\\n";


       // textView.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_COMPACT));

        tvAboutUs.setText(Html.fromHtml(getString(R.string.html)));
       /* if (Build.VERSION.SDK_INT >= 24) {
            tvAboutUs.setText(  Html.fromHtml(str1, Html.FROM_HTML_MODE_COMPACT));
           // for 24 api and more
        } else {
            tvAboutUs.setText(Html.fromHtml(str1));
          //  Html.fromHtml(String) // or for older api
        }*/
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }
}
