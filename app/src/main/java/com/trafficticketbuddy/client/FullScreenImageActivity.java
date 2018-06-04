package com.trafficticketbuddy.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.trafficticketbuddy.client.utils.Constant;

public class FullScreenImageActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        ImageView ivClose = (ImageView) findViewById(R.id.ivClose);
            if (getIntent().getStringExtra("url")!=null){
               // Glide.with(FullScreenImageActivity.this).load(getIntent().getStringExtra("url")).into(photoView);
               // imageView.setImage(ImageSource.uri(getIntent().getStringExtra("url")));
                ImageLoader.getInstance().displayImage(getIntent().getStringExtra("url"), photoView, BaseActivity.cacheOptions);


            }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
