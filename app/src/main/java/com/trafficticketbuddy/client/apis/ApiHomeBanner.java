package com.trafficticketbuddy.client.apis;

import com.trafficticketbuddy.client.model.country.CountryMain;
import com.trafficticketbuddy.client.model.homeBanner.HomeBannerMain;
import com.trafficticketbuddy.client.restservice.APIHelper;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.restservice.RestService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHomeBanner {
    private OnApiResponseListener listener;
    private Map<String, String> param;

    public ApiHomeBanner(OnApiResponseListener listener) {
        this.listener = listener;
        doWebServiceCall();

    }

    public void doWebServiceCall() {

        Call<HomeBannerMain> getDepartment = RestService.getInstance().restInterface.banners();
        APIHelper.enqueueWithRetry(getDepartment, new Callback<HomeBannerMain>() {
            @Override
            public void onResponse(Call<HomeBannerMain> call, Response<HomeBannerMain> response) {
                if(response.code() == 200 && response !=null){

                        listener.onSuccess( response.body());

                }else{
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<HomeBannerMain> call, Throwable t) {
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
