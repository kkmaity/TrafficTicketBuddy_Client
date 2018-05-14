package com.trafficticketbuddy.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;

import java.util.ArrayList;
import java.util.List;


public class MyBidRecyclerAdapter extends RecyclerView.Adapter<MyBidRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ItemClickListner _interface;
    private List<String> dataList=new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }
    public MyBidRecyclerAdapter(Context mContext, List<String> projectListingData, ItemClickListner clickHandler) {
        this.mContext=mContext;
        this.dataList=projectListingData;
        this._interface = clickHandler;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bids, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

//        holder.linAddToMylist.setTag(position);
//        holder.linAddToMylist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Object objects = v.getTag();
//                _interface.listItemBtnClickListener(objects,v.getId());
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
