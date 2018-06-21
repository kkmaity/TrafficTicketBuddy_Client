package com.trafficticketbuddy.client.apis;

import com.trafficticketbuddy.client.model.login.LoginMain;
import com.trafficticketbuddy.client.restservice.APIHelper;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.restservice.RestService;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiEmailVerification {
    private OnApiResponseListener listener;
    private Map<String, String> param;

    public ApiEmailVerification(Map<String, String> param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {

        Call<ResponseBody> data = RestService.getInstance().restInterface.emailverify(param);
        APIHelper.enqueueWithRetry(data, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200 && response !=null){
                    listener.onSuccess(response.body());
                }else{
                    listener.onError();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onError();
            }
        });

    }
}
