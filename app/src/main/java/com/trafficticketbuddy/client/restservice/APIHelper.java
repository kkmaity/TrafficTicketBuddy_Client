package com.trafficticketbuddy.client.restservice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class APIHelper {

    public static final int DEFAULT_RETRIES = 5;

    public static <T> void enqueueWithRetry(Call<T> call, final int retryCount, final Callback<T> callback) {
       /* call.enqueue(new RetryableCallback<T>(call, retryCount) {

            @Override
            public void onFinalResponse(Call<T> call, Response<T> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFinalFailure(Call<T> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });*/

               call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                   callback.onResponse(call, response);
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    callback.onFailure(call, t);
                }
            });
    }

    public static <T> void enqueueWithRetry(Call<T> call, final Callback<T> callback) {
        enqueueWithRetry(call, DEFAULT_RETRIES, callback);
    }

    public static boolean isCallSuccess(Response response) {
        int code = response.code();
        return (code >= 200 && code < 400);
    }


}