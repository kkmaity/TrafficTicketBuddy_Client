package com.trafficticketbuddy.client.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.trafficticketbuddy.client.BaseActivity;
import com.trafficticketbuddy.client.MyBidActivity;
import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;
import com.trafficticketbuddy.client.model.bids.Response;
import com.trafficticketbuddy.client.utils.Constant;

import java.util.ArrayList;
import java.util.List;


public class MyBidRecyclerAdapter extends RecyclerView.Adapter<MyBidRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ItemClickListner _interface;
    private List<Response> dataList=new ArrayList<>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardAccept;
        ImageView lawyrImage;
        TextView tvLawyrName,tvPrice,tvStateCity,tvDetails,tvDateTime;
        public MyViewHolder(View view) {
            super(view);
            cardAccept = (CardView)view.findViewById(R.id.cardAccept);
            lawyrImage = (ImageView)view.findViewById(R.id.lawyrImage);
            tvLawyrName = (TextView)view.findViewById(R.id.tvLawyrName);
            tvStateCity = (TextView)view.findViewById(R.id.tvStateCity);
            tvPrice = (TextView)view.findViewById(R.id.tvPrice);
            tvDetails = (TextView)view.findViewById(R.id.tvDetails);
            tvDateTime = (TextView)view.findViewById(R.id.tvDateTime);
        }
    }
    public MyBidRecyclerAdapter(Context mContext, List<Response>  projectListingData, ItemClickListner clickHandler) {
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
       // holder.tvLawyrName.setText(dataList.get(position).getLawyerFirstName()+" "+dataList.get(position).getLawyerLastName());
        holder.tvLawyrName.setText("Lawyer "+ (position+1));
        holder.tvDetails.setText(dataList.get(position).getBidText());
        holder.tvStateCity.setText(MyBidActivity.city+", "+MyBidActivity.state);
        holder.tvPrice.setText(dataList.get(position).getBidAmount());
        holder.tvDateTime.setText(dataList.get(position).getCreated_at());
        ImageLoader.getInstance().displayImage(Constant.BASE_URL+dataList.get(position).getLawyerProfileImage(), holder.lawyrImage, BaseActivity.cacheOptions);
        holder.cardAccept.setTag(position);
        holder.cardAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int posi = (int) view.getTag();
                _interface.onItemClick(view.getTag(),posi);
            }
        });

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
