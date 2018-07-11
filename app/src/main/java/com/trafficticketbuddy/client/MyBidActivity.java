package com.trafficticketbuddy.client;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.trafficticketbuddy.client.adapter.AllCasesRecyclerAdapter;
import com.trafficticketbuddy.client.adapter.MyBidRecyclerAdapter;
import com.trafficticketbuddy.client.apis.ApiAcceptBids;
import com.trafficticketbuddy.client.apis.ApiCheckCaseStatus;
import com.trafficticketbuddy.client.apis.ApiGetBids;
import com.trafficticketbuddy.client.evt.BackEvent;
import com.trafficticketbuddy.client.interfaces.ItemClickListner;
import com.trafficticketbuddy.client.model.bids.GetBidListMain;
import com.trafficticketbuddy.client.model.bids.Response;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBidActivity extends BaseActivity {
    private RecyclerView rvRecycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private TextView tvHeading;
    private ImageView back;
    private List<Response> dataList=new ArrayList<>();
    private List<Response> tempList=new ArrayList<>();
    private MyBidRecyclerAdapter myBidRecyclerAdapter;
    public static  String state;
    public static String city;
    public static String status;
    private TextView txtNoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybid);
        tvHeading = (TextView)findViewById(R.id.tvHeading);
        tvHeading.setText("BIDS");
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);
        initialize();
    }
    private void initialize() {
        rvRecycler = (RecyclerView)findViewById(R.id.rvRecycler);
        txtNoItem = (TextView) findViewById(R.id.txtNoItem);
        // swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        state=  getIntent().getStringExtra("state");
        city= getIntent().getStringExtra("city");
        status= getIntent().getStringExtra("status");
        // swipeRefreshLayout.setRefreshing(false);
        mLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRecycler.setLayoutManager(mLayoutManager);
        setAdapterRecyclerView();
        if (getIntent().getStringExtra("case_id")!=null)
            getBids(getIntent().getStringExtra("case_id"));

    }

    private void getBids(String id) {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiGetBids(getParam(id), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dataList.clear();
                    tempList.clear();
                    dismissProgressDialog();
                    GetBidListMain res=(GetBidListMain)t;
                    if (res.getStatus()){

                        dataList.addAll(res.getResponse());
                        if(status.equalsIgnoreCase("Accepted")){
                            for (int i=0;i<dataList.size();i++){
                                if (dataList.get(i).getIsAccepted().equalsIgnoreCase("1")){
                                    tempList.add(dataList.get(i));
                                    break;

                                }
                            }
                        }else{
                            tempList.addAll(res.getResponse());
                        }
                        /*for (int i=0;i<dataList.size();i++){
                            if (dataList.get(i).getIsAccepted().equalsIgnoreCase("1")){
                                tempList.addAll(res.getResponse());

                            }else
                                tempList.addAll(res.getResponse());
                        }*/

                        if(dataList.size()==0){
                            txtNoItem.setVisibility(View.VISIBLE);
                        }else{
                            txtNoItem.setVisibility(View.GONE);
                        }
                        myBidRecyclerAdapter.notifyDataSetChanged();

                    }else{
                        if(dataList.size()==0){
                            txtNoItem.setVisibility(View.VISIBLE);
                        }else{
                            txtNoItem.setVisibility(View.GONE);
                        }
                    }


                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();
                    if(dataList.size()==0){
                        txtNoItem.setVisibility(View.VISIBLE);
                    }else{
                        txtNoItem.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError() {
                    dismissProgressDialog();
                    if(dataList.size()==0){
                        txtNoItem.setVisibility(View.VISIBLE);
                    }else{
                        txtNoItem.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private Map<String, String> getParam(String id) {
        Map<String,String> map=new HashMap<>();
        map.put("case_id",id);
        return map;

    }

    private void setAdapterRecyclerView() {

        myBidRecyclerAdapter=new MyBidRecyclerAdapter(this, tempList, new ItemClickListner() {
            @Override
            public void onItemClick(Object viewID, int position) {

              /*  Intent intent=new Intent(MyBidActivity.this,PaymentActivity.class);
                intent.putExtra("bid_id",dataList.get(position).getId());
                intent.putExtra("case_id",dataList.get(position).getCaseId());
                intent.putExtra("amount",dataList.get(position).getBidAmount());
                startActivity(intent);
                finish();
*/
                // callAcceptBid(dataList.get(position).getId(),dataList.get(position).getCaseId());
                callCheckCaseStatus(dataList.get(position).getCaseId(),dataList.get(position).getId());
            }
        });
        rvRecycler.setAdapter(myBidRecyclerAdapter);
    }




    private void callCheckCaseStatus(final String caseId, final String bidID) {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiCheckCaseStatus(getCheckCaseStatus(caseId), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    // dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                            //Toast.makeText(getApplicationContext(), "ISBid Active",Toast.LENGTH_LONG).show();
                           // setCardDetails(etCardnumber.getText().toString(),Integer.parseInt(month[0]),Integer.parseInt(et_year.getText().toString()),et_cvv.getText().toString());
                            /*// showDialog(object.getString("message"));
                            //getBids(getIntent().getStringExtra("case_id"));
                            // finish();
                            callSuccess();*/

                            callAcceptBid(bidID,caseId);
                        }
                        else{
                            showDialog(object.getString("message"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();
                }

                @Override
                public void onError() {
                    dismissProgressDialog();
                }
            });
        }
    }

    private Map<String, String> getCheckCaseStatus(String caseId) {
        Map<String,String> map=new HashMap<>();
        map.put("case_id",caseId);
        return map;

    }
    private void callAcceptBid(String id, String caseId) {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiAcceptBids(getParamAccept(id,caseId), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                            // showDialog(object.getString("message"));
                            //getBids(getIntent().getStringExtra("case_id"));
                            // finish();
                           // callSuccess();
                            showDialog(object.getString("message"));
                            if (getIntent().getStringExtra("case_id")!=null)
                                getBids(getIntent().getStringExtra("case_id"));
                        }
                        else
                            showDialog(object.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();
                }

                @Override
                public void onError() {
                    dismissProgressDialog();
                }
            });
        }
    }

    private Map<String, String> getParamAccept(String id, String caseId) {

        Map<String,String> map=new HashMap<>();
        map.put("case_id",caseId);
        map.put("bid_id",id);
        return map;
    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.back:
                finish();
                //  onBackPressed();
               /* EventBus.getDefault().post(new BackEvent());
                finish();*/
                break;
        }
    }

    public void refreshBid(String case_id){
        getBids(case_id);
    }

   /* @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new BackEvent());
        super.onBackPressed();

        finish();
    }*/
}
