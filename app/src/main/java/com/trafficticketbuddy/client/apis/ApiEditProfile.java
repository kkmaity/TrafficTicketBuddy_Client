package com.trafficticketbuddy.client.apis;

import com.trafficticketbuddy.client.model.login.LoginMain;
import com.trafficticketbuddy.client.restservice.APIHelper;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.restservice.RestService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiEditProfile {
    private OnApiResponseListener listener;
    private Map<String, String> param;

    public ApiEditProfile(Map<String, String> param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {

        Call<LoginMain> data = RestService.getInstance().restInterface.editprofile(param);
        APIHelper.enqueueWithRetry(data, new Callback<LoginMain>() {
            @Override
            public void onResponse(Call<LoginMain> call, Response<LoginMain> response) {
                if(response.code() == 200 && response !=null){
                    listener.onSuccess(response.body());
                }else{
                    listener.onError();
                }
            }
            @Override
            public void onFailure(Call<LoginMain> call, Throwable t) {
                listener.onError();
            }
        });

    }
}
