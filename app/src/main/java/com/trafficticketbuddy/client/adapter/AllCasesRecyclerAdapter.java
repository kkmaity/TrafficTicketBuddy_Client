package com.trafficticketbuddy.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.trafficticketbuddy.client.CaseDetailsActivity;
import com.trafficticketbuddy.client.FullImageZoomActivity;
import com.trafficticketbuddy.client.MyBidActivity;
import com.trafficticketbuddy.client.MyCaseActivity;
import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;
import com.trafficticketbuddy.client.model.cases.Response;
import com.trafficticketbuddy.client.utils.Constant;

import java.util.ArrayList;
import java.util.List;


public class AllCasesRecyclerAdapter extends RecyclerView.Adapter<AllCasesRecyclerAdapter.MyViewHolder> {

    private BaseActivity mContext;
    private ItemClickListner _interface;
    private List<Response> dataList=new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linAllCase;
        ImageView ivLicense,ivBackImage,ivFontImage;
        TextView tvCaseno,tvDesc,tvStateCity,tvDate,tvTime,tvBidCount,tvStatus,tvViewBids,tvViewDetails;
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
            tvViewBids = (TextView)view.findViewById(R.id.tvViewBids);
            tvViewDetails = (TextView)view.findViewById(R.id.tvViewDetails);

        }
    }


    public AllCasesRecyclerAdapter(BaseActivity mContext, List<Response> caseListData, ItemClickListner clickHandler) {
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
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
        holder.tvBidCount.setText("Total Bids : "+dataList.get(position).getBid_count());

        String date = dataList.get(position).getCreated_at().split(" ")[0];
        String time = dataList.get(position).getCreated_at().split(" ")[1];
        time = time.substring(0,5);

        holder.tvDate.setText(date);
        holder.tvTime.setText(time);
        holder.tvBidCount.setText(""+dataList.get(position).getBid_count());
        holder.tvStatus.setText("Status : "+dataList.get(position).getStatus());
        if (dataList.get(position).getStatus().equalsIgnoreCase("Accepted"))
        holder.tvStatus.setTextColor(Color.parseColor("#FF349344"));

        //holder.linAllCase.setTag(position);
        holder.tvViewBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int Pos  = (int) v.getTag();
                _interface.onItemClick(v.getId(),Pos);*/
                Intent case_id=new Intent(mContext,MyBidActivity.class);
                case_id.putExtra("case_id",dataList.get(position).getId());
                case_id.putExtra("state",dataList.get(position).getState());
                case_id.putExtra("city",dataList.get(position).getCity());
                case_id.putExtra("status",dataList.get(position).getStatus());
                mContext. startActivity(case_id);
            }
        });

        holder.tvViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext,CaseDetailsActivity.class);
                mIntent.putExtra("data",dataList.get(position));
                mContext.startActivity(mIntent);
            }
        });
        holder.ivLicense.setTag(position);
        holder.ivLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*int pos= (int) view.getTag();
                _interface.onItemClick(view.getId(),pos);*/
                FullImageZoomActivity.start(mContext, dataList.get(position).getDrivingLicense(),  holder.ivLicense);

            }
        });
        holder.ivFontImage.setTag(position);
        holder.ivFontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*int pos= (int) view.getTag();
                _interface.onItemClick(view.getId(),pos);*/
                FullImageZoomActivity.start(mContext, dataList.get(position).getCaseFrontImg(),  holder.ivFontImage);
            }
        });
        holder.ivBackImage.setTag(position);
        holder.ivBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  int pos= (int) view.getTag();
                _interface.onItemClick(view.getId(),pos);*/
                FullImageZoomActivity.start(mContext, dataList.get(position).getCaseRearImg(),  holder.ivBackImage);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
