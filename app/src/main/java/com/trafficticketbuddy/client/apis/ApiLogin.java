package com.trafficticketbuddy.client.apis;

import com.trafficticketbuddy.client.model.login.LoginMain;
import com.trafficticketbuddy.client.restservice.APIHelper;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.restservice.RestService;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiLogin {
    private OnApiResponseListener listener;
    private Map<String, String> param;

    public ApiLogin(Map<String, String> param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {

        Call<LoginMain> data = RestService.getInstance().restInterface.login(param);
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
