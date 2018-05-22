package com.trafficticketbuddy.client.interfaces;

import com.facebook.GraphResponse;

import org.json.JSONObject;

/**
 * Created by User on 19-05-2018.
 */

public interface FbLoginCompleted {
    public void onFaceBookCompleted(JSONObject object, GraphResponse response);
}
