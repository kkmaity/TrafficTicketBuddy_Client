package com.trafficticketbuddy.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trafficticketbuddy.client.model.cases.Response;
import com.trafficticketbuddy.client.utils.Constant;

import java.util.ArrayList;
import java.util.List;

public class CaseDetailsActivity extends BaseActivity {

    private TextView tvHeading;
    private Response mCaseResponse;
    private ImageView ivLicense,ivBackImage,ivFontImage;
    private TextView tvDesc,tvState,tvCity,tvCaseno;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casedetails);
        back = (ImageView)findViewById(R.id.back);
        tvHeading = (TextView)findViewById(R.id.tvHeading);
        tvHeading.setText("Case Details");
        ivLicense = (ImageView)findViewById(R.id.ivLicense);
        ivBackImage = (ImageView)findViewById(R.id.ivBackImage);
        ivFontImage = (ImageView)findViewById(R.id.ivFontImage);
        tvDesc = (TextView)findViewById(R.id.tvDesc);
        tvState = (TextView)findViewById(R.id.tvState);
        tvCity = (TextView)findViewById(R.id.tvCity);
        tvCaseno = (TextView)findViewById(R.id.tvCaseno);
        ivLicense.setOnClickListener(this);
        ivBackImage.setOnClickListener(this);
        ivFontImage.setOnClickListener(this);
        back.setOnClickListener(this);

        Intent mIntent = getIntent();
        if(mIntent!=null){
            mCaseResponse = (Response) mIntent.getSerializableExtra("data");
            loadImage(this,mCaseResponse.getDrivingLicense(),ivLicense);
            loadImage(this,mCaseResponse.getCaseFrontImg(),ivFontImage);
            loadImage(this,mCaseResponse.getCaseRearImg(),ivBackImage);
            tvState.setText(mCaseResponse.getState());
            tvCity.setText(mCaseResponse.getCity());
            tvDesc.setText(mCaseResponse.getCaseDetails());
            tvCaseno.setText("Case details for case no. "+mCaseResponse.getCaseNumber());
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent=new Intent(CaseDetailsActivity.this,FullScreenImageActivity.class);

        switch (view.getId()){
            case R.id.ivFontImage:
                intent.putExtra("url",Constant.BASE_URL+mCaseResponse.getCaseFrontImg());
                startActivity(intent);
                break;
            case R.id.ivLicense:
                intent.putExtra("url",Constant.BASE_URL+mCaseResponse.getDrivingLicense());
                startActivity(intent);
                break;
            case R.id.ivBackImage:
                intent.putExtra("url",Constant.BASE_URL+mCaseResponse.getCaseRearImg());
                startActivity(intent);
                break;
                case R.id.back:
               finish();
                break;
        }
    }
}
