
package com.trafficticketbuddy.client.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginMain {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("response")
    @Expose
    private Response response = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

}
