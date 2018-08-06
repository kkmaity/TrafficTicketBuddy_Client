package com.trafficticketbuddy.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.trafficticketbuddy.client.custom_views.HeaderView;
import com.trafficticketbuddy.client.utils.Constant;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyProfileActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    @Bind(R.id.toolbar_header_view)
    protected HeaderView toolbarHeaderView;

    @Bind(R.id.float_header_view)
    protected HeaderView floatHeaderView;

    @Bind(R.id.appbar)
    protected AppBarLayout appBarLayout;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.fab)
    protected ImageView fab;

    private ImageView image,ivLicense;
    private EditText tv_email, tv_phone, tv_country, tv_state, tv_city;
    private com.trafficticketbuddy.client.model.login.Response mLogin;

    private boolean isHideToolbarView = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_profile);

        image = (ImageView)findViewById(R.id.image);
        ivLicense = (ImageView)findViewById(R.id.ivLicense);
        tv_email = (EditText) findViewById(R.id.tv_email);
        tv_phone = (EditText) findViewById(R.id.tv_phone);
        tv_country = (EditText) findViewById(R.id.tv_country);
        tv_state = (EditText) findViewById(R.id.tv_state);
        tv_city = (EditText) findViewById(R.id.tv_city);

        Gson gson = new Gson();
        String json = preference.getString("login_user", "");
        mLogin = gson.fromJson(json, com.trafficticketbuddy.client.model.login.Response.class);
         ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initUi();

       if(mLogin.getPhone()!=null){
            tv_phone.setText(mLogin.getPhone());
        }if(mLogin.getEmail()!=null){
            tv_email.setText(mLogin.getEmail());
        }if(mLogin.getCountry()!=null){
            tv_country.setText(mLogin.getCountry());
        }if(mLogin.getState()!=null){
            tv_state.setText(mLogin.getState());
        }if(mLogin.getCity()!=null){
            tv_city.setText(mLogin.getCity());
        }if(mLogin.getProfileImage()!=null){
            if(mLogin.getProfileImage().startsWith("http")){
                Glide.with(this).load(mLogin.getProfileImage()).into(image);
            }else{
                String path = Constant.BASE_URL+mLogin.getProfileImage();
                Glide.with(this).load(path).into(image);
            }

        }if(mLogin.getLicenseImage()!=null){
           if(mLogin.getLicenseImage().length()>0){
               String path = Constant.BASE_URL+mLogin.getLicenseImage();
               Glide.with(this).load(path).into(ivLicense);
           }


        }
        ivLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullImageZoomActivity.start(MyProfileActivity.this, mLogin.getLicenseImage(), ivLicense);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullImageZoomActivity.start(MyProfileActivity.this, mLogin.getProfileImage(), ivLicense);
            }
        });

    }

    private void initUi() {
        appBarLayout.addOnOffsetChangedListener(this);
        fab.setOnClickListener(this);
        toolbarHeaderView.bindTo(mLogin.getFirstName()+" "+mLogin.getLastName(), mLogin.getCountry());
        floatHeaderView.bindTo(mLogin.getFirstName()+" "+mLogin.getLastName(), mLogin.getCountry());
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
           case R.id.fab:
               startActivity(new Intent(MyProfileActivity.this,EditProfileActivity.class));
               break;
        }
    }
}
