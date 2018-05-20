package com.trafficticketbuddy.client.interfaces;

import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONObject;

/**
 * Created by User on 19-05-2018.
 */

public interface GoogleLoginCompleted {
    public void onGoogleCompleted(GoogleSignInAccount account);
}
