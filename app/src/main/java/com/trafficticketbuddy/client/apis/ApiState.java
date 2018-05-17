package com.trafficticketbuddy.client.apis;

import com.trafficticketbuddy.client.model.StateNameMain;
import com.trafficticketbuddy.client.restservice.APIHelper;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.restservice.RestService;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiState {
    private OnApiResponseListener listener;
    private Map<String, String> param;

    public ApiState(OnApiResponseListener listener) {
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {

        Call<StateNameMain> getDepartment = RestService.getInstance().restInterface.getStateName();
        APIHelper.enqueueWithRetry(getDepartment, new Callback<StateNameMain>() {
            @Override
            public void onResponse(Call<StateNameMain> call, Response<StateNameMain> response) {
                if(response.code() == 200 && response !=null){

                        listener.onSuccess( response.body());

                }else{
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<StateNameMain> call, Throwable t) {
                listener.onError();
            }
        });

      /*  getDepartment.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200 && response !=null){
                    try {
                        listener.onSuccess( response.body().string());
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
        });*/
    }
}
