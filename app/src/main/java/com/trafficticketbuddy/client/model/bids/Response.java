package com.trafficticketbuddy.client.model.bids;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("lawyer_id")
    @Expose
    private String lawyerId;
    @SerializedName("case_id")
    @Expose
    private String caseId;
    @SerializedName("bid_amount")
    @Expose
    private String bidAmount;
    @SerializedName("bid_text")
    @Expose
    private String bidText;
    @SerializedName("is_accepted")
    @Expose
    private String isAccepted;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("lawyer_first_name")
    @Expose
    private String lawyerFirstName;
    @SerializedName("lawyer_last_name")
    @Expose
    private String lawyerLastName;
    @SerializedName("lawyer_email")
    @Expose
    private String lawyerEmail;
    @SerializedName("lawyer_phone")
    @Expose
    private String lawyerPhone;
    @SerializedName("lawyer_profile_image")
    @Expose
    private String lawyerProfileImage;


    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("accepted_at")
    @Expose
    private String accepted_at;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(String lawyerId) {
        this.lawyerId = lawyerId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getBidText() {
        return bidText;
    }

    public void setBidText(String bidText) {
        this.bidText = bidText;
    }



    public String getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLawyerFirstName() {
        return lawyerFirstName;
    }

    public void setLawyerFirstName(String lawyerFirstName) {
        this.lawyerFirstName = lawyerFirstName;
    }

    public String getLawyerLastName() {
        return lawyerLastName;
    }

    public void setLawyerLastName(String lawyerLastName) {
        this.lawyerLastName = lawyerLastName;
    }

    public String getLawyerEmail() {
        return lawyerEmail;
    }

    public void setLawyerEmail(String lawyerEmail) {
        this.lawyerEmail = lawyerEmail;
    }

    public String getLawyerPhone() {
        return lawyerPhone;
    }

    public void setLawyerPhone(String lawyerPhone) {
        this.lawyerPhone = lawyerPhone;
    }

    public String getLawyerProfileImage() {
        return lawyerProfileImage;
    }

    public void setLawyerProfileImage(String lawyerProfileImage) {
        this.lawyerProfileImage = lawyerProfileImage;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAccepted_at() {
        return accepted_at;
    }

    public void setAccepted_at(String accepted_at) {
        this.accepted_at = accepted_at;
    }
}
