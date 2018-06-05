package com.trafficticketbuddy.client.apis;

import com.trafficticketbuddy.client.model.bids.GetBidListMain;
import com.trafficticketbuddy.client.model.login.LoginMain;
import com.trafficticketbuddy.client.restservice.APIHelper;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.restservice.RestService;

import java.io.IOException;
import java.net.ResponseCache;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiGetBids {
    private OnApiResponseListener listener;
    private Map<String, String> param;

    public ApiGetBids(Map<String, String> param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {

        Call<GetBidListMain> data = RestService.getInstance().restInterface.getBids(param);
        APIHelper.enqueueWithRetry(data, new Callback<GetBidListMain>() {
            @Override
            public void onResponse(Call<GetBidListMain> call, Response<GetBidListMain> response) {
                if(response.code() == 200 && response !=null){

                        listener.onSuccess(response.body());

                }else{
                    listener.onError();
                }
            }
            @Override
            public void onFailure(Call<GetBidListMain> call, Throwable t) {
                listener.onError();
            }
        });

    }
}
