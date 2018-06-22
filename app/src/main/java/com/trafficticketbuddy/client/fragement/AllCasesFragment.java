package com.trafficticketbuddy.client.fragement;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trafficticketbuddy.client.MyBidActivity;
import com.trafficticketbuddy.client.MyCaseActivity;
import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.adapter.AllCasesRecyclerAdapter;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;
import com.trafficticketbuddy.client.interfaces.MyCaseAllCaseDataLoaded;
import com.trafficticketbuddy.client.model.cases.Response;

import java.util.ArrayList;
import java.util.List;

public class AllCasesFragment extends BaseFragment implements MyCaseAllCaseDataLoaded {
    private RecyclerView rvRecycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private List<Response> caseListData = new ArrayList<>();
    private AllCasesRecyclerAdapter mAllCasesRecyclerAdapter;
    private TextView txtNoItem;

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
        txtNoItem = (TextView) view.findViewById(R.id.txtNoItem);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        mLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvRecycler.setLayoutManager(mLayoutManager);
        MyCaseActivity mActivity = (MyCaseActivity) getActivity();
        mActivity.setMyCaseAllCaseListener(this);
        setAdapterRecyclerView();

    }
    private void setAdapterRecyclerView() {
         mAllCasesRecyclerAdapter=new AllCasesRecyclerAdapter(baseActivity, caseListData, new ItemClickListner() {
            @Override
            public void onItemClick(Object viewID, int position) {
                int vID= (int) viewID;
                switch (vID){
                    case R.id.linAllCase:
                        Intent case_id=new Intent(baseActivity,MyBidActivity.class);
                        case_id.putExtra("case_id",caseListData.get(position).getId());
                        case_id.putExtra("state",caseListData.get(position).getState());
                        case_id.putExtra("city",caseListData.get(position).getCity());
                        startActivity(case_id);

                        break;
                }
            }
        });
        rvRecycler.setAdapter(mAllCasesRecyclerAdapter);
    }

    @Override
    public void allCaseDataLoaded(List<Response> caseListData) {
        this.caseListData.clear();
        this.caseListData=caseListData;
        if(this.caseListData.size()==0){
            txtNoItem.setVisibility(View.VISIBLE);
        }else{
            txtNoItem.setVisibility(View.GONE);
        }
        setAdapterRecyclerView();
    }
}
