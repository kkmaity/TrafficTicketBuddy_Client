package com.trafficticketbuddy.client;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trafficticketbuddy.client.adapter.AllCasesRecyclerAdapter;
import com.trafficticketbuddy.client.adapter.MyBidRecyclerAdapter;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;

import java.util.ArrayList;

public class MyBidActivity extends BaseActivity {
    private RecyclerView rvRecycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private TextView tvHeading;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybid);
        tvHeading = (TextView)findViewById(R.id.tvHeading);
        tvHeading.setText("BIDS");
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
    }
    private void setAdapterRecyclerView() {
        ArrayList<String> mList = new ArrayList();
        mList.add("");
        mList.add("");
        mList.add("");
        MyBidRecyclerAdapter myBidRecyclerAdapter=new MyBidRecyclerAdapter(this, mList, new ItemClickListner() {
            @Override
            public void onItemClick(Object viewID, int position) {}
        });
        rvRecycler.setAdapter(myBidRecyclerAdapter);
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
