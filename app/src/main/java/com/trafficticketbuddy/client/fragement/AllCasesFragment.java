package com.trafficticketbuddy.client.fragement;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trafficticketbuddy.client.MainActivity;
import com.trafficticketbuddy.client.MyBidActivity;
import com.trafficticketbuddy.client.MyProfileActivity;
import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.adapter.AllCasesRecyclerAdapter;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;

import java.util.ArrayList;

public class AllCasesFragment extends BaseFragment{
    private RecyclerView rvRecycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_allcases, container, false);
        initialize(view);
        return view;
    }
    private void initialize(View view) {
        rvRecycler = (RecyclerView)view.findViewById(R.id.rvRecycler);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(false);
        mLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvRecycler.setLayoutManager(mLayoutManager);
        setAdapterRecyclerView();
    }
    private void setAdapterRecyclerView() {
        ArrayList<String>mList = new ArrayList();
        mList.add("");
        mList.add("");
        mList.add("");
        AllCasesRecyclerAdapter mAllCasesRecyclerAdapter=new AllCasesRecyclerAdapter(baseActivity, mList, new ItemClickListner() {
            @Override
            public void onItemClick(Object viewID, int position) {
                switch (position){
                    case R.id.linAllCase:
                        startActivity(new Intent(getActivity(),MyBidActivity.class));
                        break;
                }
            }
        });
        rvRecycler.setAdapter(mAllCasesRecyclerAdapter);
    }
}
