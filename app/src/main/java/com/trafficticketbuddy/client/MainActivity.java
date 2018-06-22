package com.trafficticketbuddy.client;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trafficticketbuddy.client.apis.ApiHomeBanner;
import com.trafficticketbuddy.client.fragement.AutoScrollPagerFragment;
import com.trafficticketbuddy.client.fragement.TextFragment;
import com.trafficticketbuddy.client.model.homeBanner.HomeBannerMain;
import com.trafficticketbuddy.client.model.login.Response;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.utils.Constant;

import com.trafficticketbuddy.client.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private LinearLayout linHome,linMyProfile,linSettings,linFileCase,linMyCase,linMyCase_drawer,linLogout,linShare;
    private TextView tvName,tvEmail;
    private ImageView profile_image;
    private Response mLogin;
    private  ViewPager mPager;
    private  int currentPage = 0;
    //private static final Integer[] XMEN= {R.drawable.home_banner_1,R.drawable.home_banner_1,R.drawable.home_banner_1,R.drawable.home_banner_1,R.drawable.home_banner_1};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    public static List<com.trafficticketbuddy.client.model.homeBanner.Response> bannerList=new ArrayList<>();
    private MyAdapter bannerAdapter;
    private CircleIndicator indicator;
    private DrawerLayout drawer;
    private LinearLayout ll_side_panel_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Home");
         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mPager = (ViewPager) findViewById(R.id.pager);
         indicator = (CircleIndicator) findViewById(R.id.indicator);
        linHome=(LinearLayout)findViewById(R.id.linHome);
        linShare=(LinearLayout)findViewById(R.id.linShare);
        linMyProfile=(LinearLayout)findViewById(R.id.linMyProfile);
        linSettings=(LinearLayout)findViewById(R.id.linSettings);
        linFileCase=(LinearLayout)findViewById(R.id.linFileCase);
        linMyCase=(LinearLayout)findViewById(R.id.linMyCase);
        linLogout=(LinearLayout)findViewById(R.id.linLogout);
        ll_side_panel_profile=(LinearLayout)findViewById(R.id.ll_side_panel_profile);
        linMyCase_drawer=(LinearLayout)findViewById(R.id.linMyCase_drawer);
        linLogout=(LinearLayout)findViewById(R.id.linLogout);
        tvName=(TextView)findViewById(R.id.tvName);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        profile_image=(ImageView)findViewById(R.id.profile_image);
        linHome.setOnClickListener(this);
        linMyProfile.setOnClickListener(this);
        linSettings.setOnClickListener(this);
        linFileCase.setOnClickListener(this);
        linMyCase.setOnClickListener(this);
        linLogout.setOnClickListener(this);
        linShare.setOnClickListener(this);
        linMyCase_drawer.setOnClickListener(this);
        linLogout.setOnClickListener(this);
        ll_side_panel_profile.setOnClickListener(this);
        String deviceToken=    preference.getDeviceToken();
        System.out.println("!!!!!!!!!!!"+deviceToken);
        init();


    }



    private void init() {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);
      getallBanner();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        closeDrawer();
        switch (view.getId()){
            case R.id.linHome:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
               // startActivity(new Intent(MainActivity.this,MyProfileActivity.class));
                break;
            case R.id.linShare:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.ll_side_panel_profile:
            case R.id.linMyProfile:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                startActivity(new Intent(MainActivity.this,MyProfileActivity.class));
                break;
                case R.id.linSettings:
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                break;
                case R.id.linFileCase:
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                startActivity(new Intent(MainActivity.this,FileCaseActivity.class));
                break;
                case R.id.linMyCase:
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                startActivity(new Intent(MainActivity.this,MyCaseActivity.class));
                break;
                case R.id.linMyCase_drawer:
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                startActivity(new Intent(MainActivity.this,MyCaseActivity.class));
                break;
                case R.id.linLogout:
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    confirmLogoutDialog();

                break;

        }
    }

    public void setTitle(String title){
        getSupportActionBar().setTitle(title);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void closeDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Gson gson = new Gson();
        String json = preference.getString("login_user", "");
        mLogin = gson.fromJson(json, com.trafficticketbuddy.client.model.login.Response.class);
        if(mLogin!=null) {
            if (mLogin.getFirstName() != null && mLogin.getLastName() != null) {
                tvName.setText(mLogin.getFirstName() + " " + mLogin.getLastName());
            }
            if (mLogin.getCity() != null) {
                tvEmail.setText(mLogin.getEmail());
            }
            if (mLogin.getProfileImage() != null) {
                String path = Constant.BASE_URL + mLogin.getProfileImage();
                Glide.with(this).load(path).into(profile_image);
            }
        }

    }

    void confirmLogoutDialog(){
       final Dialog dialog=new Dialog(MainActivity.this);
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       dialog.setContentView(R.layout.dialog_logout);
       dialog.setCancelable(false);
       dialog.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dialog.dismiss();

           }
       });
        dialog.findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preference.clearData();
                dialog.dismiss();
                Intent in=new Intent(MainActivity.this,LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(in);
            }
        });
        dialog.show();
    }

    void getallBanner(){
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiHomeBanner(new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    bannerList.clear();
                    dismissProgressDialog();
                    HomeBannerMain main=(HomeBannerMain)t;
                    if (main.getStatus()){
                        bannerList.addAll(main.getResponse());
                        mPager.setOffscreenPageLimit(bannerList.size());
                        mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                            @Override
                            public Fragment getItem(int i) {
                                if (i == 0) {
                                    return new AutoScrollPagerFragment();
                                }
                                return TextFragment.newInstance("Fragment " + i);
                            }
                            @Override
                            public int getCount() {
                                return bannerList.size();
                            }
                        });
                    }

                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();
                }

                @Override
                public void onError() {
                    dismissProgressDialog();
                }
            });
        }
    }


}
