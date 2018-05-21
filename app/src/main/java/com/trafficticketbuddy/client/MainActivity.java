package com.trafficticketbuddy.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private LinearLayout linMyProfile,linSettings,linFileCase,linMyCase,linMyCase_drawer,linLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Home");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        linMyProfile=(LinearLayout)findViewById(R.id.linMyProfile);
        linSettings=(LinearLayout)findViewById(R.id.linSettings);
        linFileCase=(LinearLayout)findViewById(R.id.linFileCase);
        linMyCase=(LinearLayout)findViewById(R.id.linMyCase);
        linLogout=(LinearLayout)findViewById(R.id.linLogout);
        linMyCase_drawer=(LinearLayout)findViewById(R.id.linMyCase_drawer);
        linLogout=(LinearLayout)findViewById(R.id.linLogout);
        linMyProfile.setOnClickListener(this);
        linSettings.setOnClickListener(this);
        linFileCase.setOnClickListener(this);
        linMyCase.setOnClickListener(this);
        linLogout.setOnClickListener(this);
        linMyCase_drawer.setOnClickListener(this);
        linLogout.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.linMyProfile:
                startActivity(new Intent(MainActivity.this,MyProfileActivity.class));
                break;
                case R.id.linSettings:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                break;
                case R.id.linFileCase:
                startActivity(new Intent(MainActivity.this,FileCaseActivity.class));
                break;
                case R.id.linMyCase:
                startActivity(new Intent(MainActivity.this,MyCaseActivity.class));
                break;
                case R.id.linMyCase_drawer:
                startActivity(new Intent(MainActivity.this,MyCaseActivity.class));
                break;
                case R.id.linLogout:
                    preference.clearData();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/


}
