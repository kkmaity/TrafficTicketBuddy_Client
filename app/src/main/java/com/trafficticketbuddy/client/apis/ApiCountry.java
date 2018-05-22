package com.trafficticketbuddy.client.apis;

import com.trafficticketbuddy.client.model.StateNameMain;
import com.trafficticketbuddy.client.model.country.CountryMain;
import com.trafficticketbuddy.client.restservice.APIHelper;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.restservice.RestService;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCountry {
    private OnApiResponseListener listener;
    private Map<String, String> param;

    public ApiCountry(OnApiResponseListener listener) {
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {

        Call<CountryMain> getDepartment = RestService.getInstance().restInterface.getCountry();
        APIHelper.enqueueWithRetry(getDepartment, new Callback<CountryMain>() {
            @Override
            public void onResponse(Call<CountryMain> call, Response<CountryMain> response) {
                if(response.code() == 200 && response !=null){

                        listener.onSuccess( response.body());

                }else{
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<CountryMain> call, Throwable t) {
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
