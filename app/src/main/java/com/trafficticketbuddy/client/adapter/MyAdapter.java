package com.trafficticketbuddy.client.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.trafficticketbuddy.client.R;
import com.trafficticketbuddy.client.model.homeBanner.Response;
import com.trafficticketbuddy.client.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends PagerAdapter {

    private List<Response> images;
    private LayoutInflater inflater;
    private Context context;

    public MyAdapter(Context context, List<Response> images1) {
        this.context = context;
        this.images=images1;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        Glide.with(context).load(Constant.BASE_URL+images.get(position).getBannerImage()).into(myImage);
       // myImage.setImageResource(images.get(position).getBannerImage());
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
