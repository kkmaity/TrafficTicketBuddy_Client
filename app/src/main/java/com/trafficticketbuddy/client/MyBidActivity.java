package com.trafficticketbuddy.client;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trafficticketbuddy.client.adapter.AllCasesRecyclerAdapter;
import com.trafficticketbuddy.client.adapter.MyBidRecyclerAdapter;
import com.trafficticketbuddy.client.apis.ApiAcceptBids;
import com.trafficticketbuddy.client.apis.ApiGetBids;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;
import com.trafficticketbuddy.client.model.bids.GetBidListMain;
import com.trafficticketbuddy.client.model.bids.Response;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBidActivity extends BaseActivity {
    private RecyclerView rvRecycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private TextView tvHeading;
    private ImageView back;
    private List<Response> dataList=new ArrayList<>();
    private MyBidRecyclerAdapter myBidRecyclerAdapter;
    public static  String state;
    public static String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybid);
        tvHeading = (TextView)findViewById(R.id.tvHeading);
        tvHeading.setText("BIDS");
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);
        initialize();
    }
    private void initialize() {
        rvRecycler = (RecyclerView)findViewById(R.id.rvRecycler);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        swipeRefreshLayout.setRefreshing(false);
        mLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRecycler.setLayoutManager(mLayoutManager);
        setAdapterRecyclerView();
        if (getIntent().getStringExtra("case_id")!=null)
            getBids(getIntent().getStringExtra("case_id"));
        state=  getIntent().getStringExtra("state");
        city= getIntent().getStringExtra("city");
    }

    private void getBids(String id) {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiGetBids(getParam(id), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    GetBidListMain res=(GetBidListMain)t;
                    if (res.getStatus()){
                        dataList.addAll(res.getResponse());
                        myBidRecyclerAdapter.notifyDataSetChanged();

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

    private Map<String, String> getParam(String id) {
        Map<String,String> map=new HashMap<>();
        map.put("case_id",id);
        return map;

    }

    private void setAdapterRecyclerView() {

        myBidRecyclerAdapter=new MyBidRecyclerAdapter(this, dataList, new ItemClickListner() {
            @Override
            public void onItemClick(Object viewID, int position) {
                callAcceptBid(dataList.get(position).getId(),dataList.get(position).getCaseId());
            }
        });
        rvRecycler.setAdapter(myBidRecyclerAdapter);
    }

    private void callAcceptBid(String id, String caseId) {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiAcceptBids(getParamAccept(id,caseId), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                            showDialog(object.getString("message"));
                        }
                        else
                            showDialog(object.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    private Map<String, String> getParamAccept(String id, String caseId) {

        Map<String,String> map=new HashMap<>();
        map.put("case_id",caseId);
        map.put("bid_id",id);
        return map;
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
