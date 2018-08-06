package com.trafficticketbuddy.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.trafficticketbuddy.client.utils.Constant;


public class FullImageZoomActivity extends AppCompatActivity {

    private static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";
    private static final String TRANSITION_NAME_PHOTO = "TRANSITION_NAME_PHOTO";
    private PhotoView photo_view;

    public static void start(Activity activity, String uri, ImageView imageView) {
        Intent intent = new Intent(activity, FullImageZoomActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, uri);
        //noinspection unchecked
        Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                Pair.create((View) imageView, TRANSITION_NAME_PHOTO))
                .toBundle();
        ActivityCompat.startActivity(activity, intent, options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_zoom_image);
        String uri = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        photo_view = (PhotoView) findViewById(R.id.photo_view);
        if(uri.startsWith("http")){
            Glide.with(this).load(uri)
                    .thumbnail(0.5f)
                    .into(photo_view);
        }else{
            Glide.with(this).load(Constant.BASE_URL+uri)
                    .thumbnail(0.5f)
                    .into(photo_view);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementReturnTransition(new Fade(Fade.IN));
        }
        ActivityCompat.finishAfterTransition(FullImageZoomActivity.this);
        ViewCompat.setTransitionName(photo_view, TRANSITION_NAME_PHOTO);
    }
}