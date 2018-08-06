package com.trafficticketbuddy.client.model.cases;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("case_number")
    @Expose
    private String caseNumber;
    @SerializedName("case_details")
    @Expose
    private String caseDetails;
    @SerializedName("case_front_img")
    @Expose
    private String caseFrontImg;
    @SerializedName("case_rear_img")
    @Expose
    private String caseRearImg;
    @SerializedName("driving_license")
    @Expose
    private String drivingLicense;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("bid_count")
    @Expose
    private int bid_count;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(String caseDetails) {
        this.caseDetails = caseDetails;
    }

    public String getCaseFrontImg() {
        return caseFrontImg;
    }

    public void setCaseFrontImg(String caseFrontImg) {
        this.caseFrontImg = caseFrontImg;
    }

    public String getCaseRearImg() {
        return caseRearImg;
    }

    public void setCaseRearImg(String caseRearImg) {
        this.caseRearImg = caseRearImg;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getBid_count() {
        return bid_count;
    }

    public void setBid_count(int bid_count) {
        this.bid_count = bid_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
