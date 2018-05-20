package com.trafficticketbuddy.client.apis;

import com.trafficticketbuddy.client.model.city.CityMain;
import com.trafficticketbuddy.client.restservice.APIHelper;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.restservice.RestService;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCity {
    private OnApiResponseListener listener;
    private Map<String, String> param;

    public ApiCity(Map<String, String> param, OnApiResponseListener listener) {
        this.param = param;
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {

        Call<CityMain> getDepartment = RestService.getInstance().restInterface.getCityName(param);
        APIHelper.enqueueWithRetry(getDepartment, new Callback<CityMain>() {
            @Override
            public void onResponse(Call<CityMain> call, Response<CityMain> response) {
                if(response.code() == 200 && response !=null){

                        listener.onSuccess( response.body());

                }else{
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<CityMain> call, Throwable t) {
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
