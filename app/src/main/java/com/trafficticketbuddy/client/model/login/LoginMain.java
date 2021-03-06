package com.trafficticketbuddy.client.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kamal on 01/10/2018.
 */

public class LoginMain {
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ResponseData")
    @Expose
    private ResponseLoginData responseData;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseLoginData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseLoginData responseData) {
        this.responseData = responseData;
    }

}
