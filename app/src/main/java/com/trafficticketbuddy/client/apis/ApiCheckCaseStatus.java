package com.trafficticketbuddy.client.apis;

import com.trafficticketbuddy.client.restservice.APIHelper;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.restservice.RestService;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCheckCaseStatus {
    private OnApiResponseListener listener;
    private Map<String, String> param;

    public ApiCheckCaseStatus(Map<String, String> param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {

        Call<ResponseBody> data = RestService.getInstance().restInterface.checkcasestatus(param);
        APIHelper.enqueueWithRetry(data, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200 && response !=null){

                    try {
                        listener.onSuccess(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();

                    }

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
