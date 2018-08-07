package com.trafficticketbuddy.client.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
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
import com.trafficticketbuddy.client.MyBidActivity;
import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.dialog.DlgRate;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;
import com.trafficticketbuddy.client.model.bids.Response;
import com.trafficticketbuddy.client.utils.Constant;

import java.util.ArrayList;
import java.util.List;


public class MyBidRecyclerAdapter extends RecyclerView.Adapter<MyBidRecyclerAdapter.MyViewHolder> {

    private MyBidActivity mContext;
    private ItemClickListner _interface;
    private List<Response> dataList=new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cardAccept;
        ImageView lawyrImage;
        TextView tvLawyrName,tvPrice,tvStateCity,tvDetails,tvDateTime,tvStatus;
        public MyViewHolder(View view) {
            super(view);
            cardAccept = (TextView)view.findViewById(R.id.cardAccept);
            lawyrImage = (ImageView)view.findViewById(R.id.lawyrImage);
            tvLawyrName = (TextView)view.findViewById(R.id.tvLawyrName);
            tvStateCity = (TextView)view.findViewById(R.id.tvStateCity);
            tvPrice = (TextView)view.findViewById(R.id.tvPrice);
            tvDetails = (TextView)view.findViewById(R.id.tvDetails);
            tvDateTime = (TextView)view.findViewById(R.id.tvDateTime);
            tvStatus = (TextView)view.findViewById(R.id.tvStatus);
        }
    }
    public MyBidRecyclerAdapter(MyBidActivity mContext, List<Response>  projectListingData, ItemClickListner clickHandler) {
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
       // holder.tvLawyrName.setText(dataList.get(position).getLawyerFirstName()+" "+dataList.get(position).getLawyerLastName());
        holder.tvLawyrName.setText(dataList.get(position).getLawyerFirstName()+ " "+dataList.get(position).getLawyerLastName());
        holder.tvDetails.setText(dataList.get(position).getBidText());
        holder.tvStateCity.setText(MyBidActivity.city+", "+MyBidActivity.state);
        holder.tvPrice.setText("Bid Price $"+dataList.get(position).getBidAmount());
        holder.tvDateTime.setText(dataList.get(position).getCreated_at());
        if(dataList.get(position).getIsAccepted().equalsIgnoreCase("1")){
            holder.cardAccept.setText("Rate Lawyer");
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText("Completed");
        }

        if(dataList.get(position).getIs_rate().equalsIgnoreCase("1")){
            holder.cardAccept.setVisibility(View.GONE);
        }
       // Glide.with(mContext).load(Constant.BASE_URL+dataList.get(position).getLawyerProfileImage()).into( holder.lawyrImage);
       // ImageLoader.getInstance().displayImage(Constant.BASE_URL+dataList.get(position).getLawyerProfileImage(), holder.lawyrImage, BaseActivity.cacheOptions);

        /*if(dataList.size() == 1){
            if(dataList.get(position).getIsAccepted().equalsIgnoreCase("1")){
                holder.cardAccept.setText("Rate Lawyer");
            }else{

            }
        }*/

        holder.cardAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
               // int posi = (int) view.getTag();
                if(dataList.get(position).getIsAccepted().equalsIgnoreCase("1")){
                        new DlgRate(mContext, dataList.get(position).getLawyerId(), dataList.get(position).getCaseId(), dataList.get(position).getId(), new DlgRate.OnRatingListiner() {
                            @Override
                            public void onRateDone() {
                                mContext.refreshBid(dataList.get(position).getCaseId());
                            }
                        }).show();
                }else{
                    new AlertDialog.Builder(mContext)
                            .setTitle("Alert")
                            .setMessage("Are you sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    _interface.onItemClick(view.getTag(),position);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }


            }
        });
        /*if (dataList.get(position).getIsAccepted().equalsIgnoreCase("0")){
            holder.cardAccept.setTag(position);
            holder.cardAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int posi = (int) view.getTag();

                    _interface.onItemClick(view.getTag(),posi);

                }
            });
        }else {
            holder.cardAccept.setVisibility(View.INVISIBLE);
        }*/


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
