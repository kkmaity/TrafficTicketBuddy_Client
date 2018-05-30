package com.trafficticketbuddy.client.fragement;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trafficticketbuddy.client.CaseDetailsActivity;
import com.trafficticketbuddy.client.MyCaseActivity;
import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.adapter.OpenCasesRecyclerAdapter;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;
import com.trafficticketbuddy.client.interfaces.MyCaseAllCaseDataLoaded;
import com.trafficticketbuddy.client.interfaces.MyCaseOpenCaseDataLoaded;
import com.trafficticketbuddy.client.model.cases.Response;

import java.util.ArrayList;
import java.util.List;

public class OpenCaseFragment extends BaseFragment implements MyCaseOpenCaseDataLoaded {
    private RecyclerView rvRecycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private List<Response> caseListData = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_opencase, container, false);
        initialize(view);
        return view;
    }
    private void initialize(View view) {
        rvRecycler = (RecyclerView)view.findViewById(R.id.rvRecycler);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        mLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvRecycler.setLayoutManager(mLayoutManager);
        MyCaseActivity mActivity = (MyCaseActivity) getActivity();
        mActivity.setMyCaseOpenCaseListener(this);
        setAdapterRecyclerView();
    }
    private void setAdapterRecyclerView() {
        OpenCasesRecyclerAdapter mOpenCasesRecyclerAdapter=new OpenCasesRecyclerAdapter(baseActivity, caseListData, new ItemClickListner() {
            @Override
            public void onItemClick(Object viewID, int position) {
                switch (position){
                    case R.id.linOpenCase:
                        Intent mIntent = new Intent(getActivity(),CaseDetailsActivity.class);
                        mIntent.putExtra("data",caseListData.get(Integer.parseInt(String.valueOf(viewID))));
                        startActivity(mIntent);
                        break;
                }
            }
        });
        rvRecycler.setAdapter(mOpenCasesRecyclerAdapter);
    }

    @Override
    public void openCaseDataLoaded(List<Response> caseListData) {
        this.caseListData.clear();
        for (Response mResponse:caseListData) {
            if(!mResponse.getStatus().equalsIgnoreCase("COMPLETED")){
                this.caseListData.add(mResponse);
            }
        }
        setAdapterRecyclerView();
    }
}
