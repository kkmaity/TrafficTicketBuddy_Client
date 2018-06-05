package com.trafficticketbuddy.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.trafficticketbuddy.client.BaseActivity;
import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;
import com.trafficticketbuddy.client.model.cases.Response;
import com.trafficticketbuddy.client.utils.Constant;

import java.util.ArrayList;
import java.util.List;


public class AllCasesRecyclerAdapter extends RecyclerView.Adapter<AllCasesRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ItemClickListner _interface;
    private List<Response> dataList=new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linAllCase;
        ImageView ivLicense,ivBackImage,ivFontImage;
        TextView tvCaseno,tvDesc,tvStateCity,tvDate,tvTime,tvBidCount,tvStatus;
        public MyViewHolder(View view) {
            super(view);
            linAllCase = (LinearLayout)view.findViewById(R.id.linAllCase);
            ivLicense = (ImageView)view.findViewById(R.id.ivLicense);
            ivBackImage = (ImageView)view.findViewById(R.id.ivBackImage);
            ivFontImage = (ImageView)view.findViewById(R.id.ivFontImage);
            tvCaseno = (TextView)view.findViewById(R.id.tvCaseno);
            tvDesc = (TextView)view.findViewById(R.id.tvDesc);
            tvStateCity = (TextView)view.findViewById(R.id.tvStateCity);
            tvDate = (TextView)view.findViewById(R.id.tvDate);
            tvTime = (TextView)view.findViewById(R.id.tvTime);
            tvBidCount = (TextView)view.findViewById(R.id.tvBidCount);
            tvStatus = (TextView)view.findViewById(R.id.tvStatus);

        }
    }


    public AllCasesRecyclerAdapter(Context mContext, List<Response> caseListData, ItemClickListner clickHandler) {
        this.mContext=mContext;
        this.dataList=caseListData;
        this._interface = clickHandler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_allcases, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        /*Glide.with(mContext).load(Constant.BASE_URL+dataList.get(position).getDrivingLicense())
                .thumbnail(0.5f)
                .into(holder.ivLicense);
        Glide.with(mContext).load(Constant.BASE_URL+dataList.get(position).getCaseFrontImg())
                .thumbnail(0.5f)
                .into(holder.ivFontImage);
        Glide.with(mContext).load(Constant.BASE_URL+dataList.get(position).getCaseRearImg())
                .thumbnail(0.5f)
                .into(holder.ivBackImage);*/

        ImageLoader.getInstance().displayImage(Constant.BASE_URL+dataList.get(position).getDrivingLicense(), holder.ivLicense, BaseActivity.cacheOptions);
        ImageLoader.getInstance().displayImage(Constant.BASE_URL+dataList.get(position).getCaseFrontImg(), holder.ivFontImage, BaseActivity.cacheOptions);
        ImageLoader.getInstance().displayImage(Constant.BASE_URL+dataList.get(position).getCaseRearImg(), holder.ivBackImage, BaseActivity.cacheOptions);



        holder.tvCaseno.setText("#"+dataList.get(position).getCaseNumber());
        holder.tvStateCity.setText(dataList.get(position).getState()+" "+dataList.get(position).getCity());
        holder.tvDesc.setText(dataList.get(position).getCaseDetails());
        // holder.tvDate.setText("");
        //holder.tvTime.setText("");
        //holder.tvBidCount.setText("");
        holder.tvStatus.setText(dataList.get(position).getStatus());
        holder.linAllCase.setTag(position);
        holder.linAllCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Pos  = (int) v.getTag();
                _interface.onItemClick(v.getId(),Pos);
            }
        });


    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
