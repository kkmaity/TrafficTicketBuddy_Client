package com.trafficticketbuddy.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;

import java.util.ArrayList;
import java.util.List;


public class OpenCasesRecyclerAdapter extends RecyclerView.Adapter<OpenCasesRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ItemClickListner _interface;
    private List<String> dataList=new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linOpenCase;
        public MyViewHolder(View view) {
            super(view);
            linOpenCase = (LinearLayout)view.findViewById(R.id.linOpenCase);
        }
    }


    public OpenCasesRecyclerAdapter(Context mContext, List<String> projectListingData, ItemClickListner clickHandler) {
        this.mContext=mContext;
        this.dataList=projectListingData;
        this._interface = clickHandler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_opencases, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.linOpenCase.setTag(position);
        holder.linOpenCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object objects = v.getTag();
                _interface.onItemClick(objects,v.getId());
            }
        });

    }





    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
