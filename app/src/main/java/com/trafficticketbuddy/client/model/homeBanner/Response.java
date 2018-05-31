package com.trafficticketbuddy.client.model.homeBanner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("banner_image")
    @Expose
    private String bannerImage;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created")
    @Expose
    private String created;

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
