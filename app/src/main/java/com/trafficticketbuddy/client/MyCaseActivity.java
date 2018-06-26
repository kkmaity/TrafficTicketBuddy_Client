package com.trafficticketbuddy.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.trafficticketbuddy.client.adapter.AllCasesRecyclerAdapter;
import com.trafficticketbuddy.client.adapter.MyCaseAdapter;
import com.trafficticketbuddy.client.apis.ApiGetAllCases;
import com.trafficticketbuddy.client.evt.BackEvent;
import com.trafficticketbuddy.client.fragement.AllCasesFragment;
import com.trafficticketbuddy.client.fragement.OpenCaseFragment;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;
import com.trafficticketbuddy.client.interfaces.MyCaseAllCaseDataLoaded;
import com.trafficticketbuddy.client.interfaces.MyCaseOpenCaseDataLoaded;
import com.trafficticketbuddy.client.model.cases.GetAllCasesMain;
import com.trafficticketbuddy.client.model.cases.Response;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
com.appsbee.sad.ui.dashboard
10/11/16
sad
*/
public class MyCaseActivity extends BaseActivity {

    //private ViewPager viewPager;
    private TabLayout tabLayout;
    private MyCaseAdapter mMyCaseAdapter;
    private final int ACTION_USAGE_ACCESS_SETTINGS = 101;
    private final int NOTIFICATION_ACCESS = 102;
    private TextView tvHeading;
    private com.trafficticketbuddy.client.model.login.Response mLogin;
   // private MyCaseAllCaseDataLoaded allcaselistener;
    //private MyCaseOpenCaseDataLoaded opencaselistener;
    private ImageView back;



    private RecyclerView rvRecycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private List<Response> caseListData = new ArrayList<>();
    private AllCasesRecyclerAdapter mAllCasesRecyclerAdapter;
    private TextView txtNoItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycases);
        Gson gson = new Gson();
        String json = preference.getString("login_user", "");
        mLogin = gson.fromJson(json, com.trafficticketbuddy.client.model.login.Response.class);
       // viewPager = (ViewPager) findViewById(R.id.id_viewpager);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        mMyCaseAdapter = new MyCaseAdapter(getSupportFragmentManager());


        rvRecycler = (RecyclerView)findViewById(R.id.rvRecycler);
        txtNoItem = (TextView)findViewById(R.id.txtNoItem);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        mLayoutManager= new LinearLayoutManager(MyCaseActivity.this, LinearLayoutManager.VERTICAL, false);
        rvRecycler.setLayoutManager(mLayoutManager);
        tvHeading = (TextView)findViewById(R.id.tvHeading);
        tvHeading.setText("My Case");
        //mActivity.setMyCaseAllCaseListener(this);
       // setAdapterRecyclerView();

       /* OpenCaseFragment mOpenCaseFragment= new OpenCaseFragment();
        mMyCaseAdapter.addFragment(mOpenCaseFragment, "Open Cases");*/

        /*AllCasesFragment mAllCaseFragment = new AllCasesFragment();
        mMyCaseAdapter.addFragment(mAllCaseFragment, "All Cases");

        viewPager.setAdapter(mMyCaseAdapter);
        tabLayout = (TabLayout) findViewById(R.id.id_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        tvHeading = (TextView)findViewById(R.id.tvHeading);
        tvHeading.setText("My Case");
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        /*getAllCase();*/
    }

    private void setAdapterRecyclerView() {
        mAllCasesRecyclerAdapter=new AllCasesRecyclerAdapter(MyCaseActivity.this, caseListData, new ItemClickListner() {
            @Override
            public void onItemClick(Object viewID, int position) {
                int vID= (int) viewID;
                switch (vID){
                    case R.id.linAllCase:
                        Intent case_id=new Intent(MyCaseActivity.this,MyBidActivity.class);
                        case_id.putExtra("case_id",caseListData.get(position).getId());
                        case_id.putExtra("state",caseListData.get(position).getState());
                        case_id.putExtra("city",caseListData.get(position).getCity());
                        case_id.putExtra("status",caseListData.get(position).getStatus());
                        startActivity(case_id);

                        break;
                }
            }
        });
        rvRecycler.setAdapter(mAllCasesRecyclerAdapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getAllCase();
    }


    private void getAllCase() {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiGetAllCases(setParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    caseListData.clear();
                    dismissProgressDialog();
                    GetAllCasesMain main=(GetAllCasesMain)t;
                    if (main.getStatus()){
                        caseListData.addAll(main.getResponse());
                       // allcaselistener.allCaseDataLoaded(caseListData);
                      //  opencaselistener.openCaseDataLoaded(caseListData);

                        mAllCasesRecyclerAdapter=new AllCasesRecyclerAdapter(MyCaseActivity.this, caseListData, new ItemClickListner() {
                            @Override
                            public void onItemClick(Object viewID, int position) {
                                int vID= (int) viewID;
                                switch (vID){
                                    case R.id.linAllCase:
                                        Intent case_id=new Intent(MyCaseActivity.this,MyBidActivity.class);
                                        case_id.putExtra("case_id",caseListData.get(position).getId());
                                        case_id.putExtra("state",caseListData.get(position).getState());
                                        case_id.putExtra("city",caseListData.get(position).getCity());
                                        case_id.putExtra("status",caseListData.get(position).getStatus());
                                        startActivity(case_id);

                                        break;
                                }
                            }
                        });
                        rvRecycler.setAdapter(mAllCasesRecyclerAdapter);
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

    private Map<String, String> setParam() {
        Map<String,String>map=new HashMap<>();
        map.put("user_id",mLogin.getId());
        return map;
    }



    public void setMyCaseAllCaseListener(MyCaseAllCaseDataLoaded listener) {
       // this.allcaselistener = listener;
    }

    public void setMyCaseOpenCaseListener(MyCaseOpenCaseDataLoaded listener) {
       // this.opencaselistener = listener;
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