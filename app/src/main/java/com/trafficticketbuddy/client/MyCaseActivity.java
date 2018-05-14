package com.trafficticketbuddy.client;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;


import com.trafficticketbuddy.client.BaseActivity;
import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.adapter.MyCaseAdapter;
import com.trafficticketbuddy.client.fragement.AllCasesFragment;
import com.trafficticketbuddy.client.fragement.OpenCaseFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/*
com.appsbee.sad.ui.dashboard
10/11/16
sad
*/
public class MyCaseActivity extends BaseActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyCaseAdapter mMyCaseAdapter;
    private final int ACTION_USAGE_ACCESS_SETTINGS = 101;
    private final int NOTIFICATION_ACCESS = 102;
    private TextView tvHeading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycases);
        viewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mMyCaseAdapter = new MyCaseAdapter(getSupportFragmentManager());

        OpenCaseFragment mOpenCaseFragment= new OpenCaseFragment();
        mMyCaseAdapter.addFragment(mOpenCaseFragment, "Open Cases");

        AllCasesFragment mAllCaseFragment = new AllCasesFragment();
        mMyCaseAdapter.addFragment(mAllCaseFragment, "All Cases");

        viewPager.setAdapter(mMyCaseAdapter);
        tabLayout = (TabLayout) findViewById(R.id.id_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        tvHeading = (TextView)findViewById(R.id.tvHeading);
        tvHeading.setText("MY CASES");

    }

}