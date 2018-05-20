package com.trafficticketbuddy.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trafficticketbuddy.client.utils.Imageutils;

import java.io.File;

public class FileCaseActivity extends BaseActivity implements Imageutils.ImageAttachmentListener{
    private TextView tvHeading;
    private Imageutils imageutils;
    private Bitmap bitmap;
    private String file_name;
    private LinearLayout linFontImage;
    private ImageView ivFontImage;
    private LinearLayout linBackImage;
    private ImageView ivBackImage;
    private LinearLayout linDrivingLiImage;
    private ImageView ivDrivingLiImage;
    private EditText etState;
    private EditText etCity;
    private EditText etDescription;
    private int imgPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
     */   setContentView(R.layout.activity_filecase);
        tvHeading = (TextView)findViewById(R.id.tvHeading);
        tvHeading.setText("FILE A CASE");
        imageutils =new Imageutils(this);

        initView();
    }

    private void initView() {
        linFontImage=(LinearLayout)findViewById(R.id.linFontImage);
        ivFontImage=(ImageView)findViewById(R.id.ivFontImage);
        linBackImage=(LinearLayout)findViewById(R.id.linBackImage);
        ivBackImage=(ImageView)findViewById(R.id.ivBackImage);
        linDrivingLiImage=(LinearLayout)findViewById(R.id.linDrivingLiImage);
        ivDrivingLiImage=(ImageView)findViewById(R.id.ivDrivingLiImage);
        etState=(EditText)findViewById(R.id.etState);
        etCity=(EditText)findViewById(R.id.etCity);
        etDescription=(EditText)findViewById(R.id.etDescription);
        linFontImage.setOnClickListener(this);
        linBackImage.setOnClickListener(this);
        linDrivingLiImage.setOnClickListener(this);
        etState.setOnClickListener(this);
        etCity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.linFontImage:
                ivFontImage.setVisibility(View.VISIBLE);
                imgPosition=1;
                imageutils.imagepicker(1);
                break;
                case R.id.linBackImage:
                    ivBackImage.setVisibility(View.VISIBLE);
                    imgPosition=2;
                    imageutils.imagepicker(1);
                break;
                case R.id.linDrivingLiImage:
                    ivDrivingLiImage.setVisibility(View.VISIBLE);
                    imgPosition=3;
                    imageutils.imagepicker(1);
                break;
                case R.id.etState:
                break;
                case R.id.etCity:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap=file;
        this.file_name=filename;


        String path =  Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file,filename,path,false);
        switch (imgPosition){
            case 1:
                ivFontImage.setImageBitmap(file);
                break;
            case 2:
                ivBackImage.setImageBitmap(file);
                break;
            case 3:
                ivDrivingLiImage.setImageBitmap(file);
                break;
        }

    }
}
