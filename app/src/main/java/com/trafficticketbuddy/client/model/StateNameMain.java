package com.trafficticketbuddy.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateNameMain {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("response")
    @Expose
    private List<StateNameResult> response = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<StateNameResult> getResponse() {
        return response;
    }

    public void setResponse(List<StateNameResult> response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
