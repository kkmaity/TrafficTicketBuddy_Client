package com.trafficticketbuddy.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trafficticketbuddy.client.adapter.CityBaseAdapter;
import com.trafficticketbuddy.client.adapter.CountryBaseAdapter;
import com.trafficticketbuddy.client.adapter.StateBaseAdapter;
import com.trafficticketbuddy.client.apis.ApiCity;
import com.trafficticketbuddy.client.apis.ApiCountry;
import com.trafficticketbuddy.client.apis.ApiRegistration;
import com.trafficticketbuddy.client.apis.ApiState;
import com.trafficticketbuddy.client.model.StateNameMain;
import com.trafficticketbuddy.client.model.StateNameResult;
import com.trafficticketbuddy.client.model.city.CityMain;
import com.trafficticketbuddy.client.model.city.CityResponse;
import com.trafficticketbuddy.client.model.country.CountryMain;
import com.trafficticketbuddy.client.model.country.Response;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends BaseActivity {
    private CardView carSignUp;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etCity,etState;
    private EditText etPassword;
    private CheckBox chbxAgree;
    private TextView tvTramsCondition;
    private TextView tvLogin,tvCountry;
    private PopupWindow pw;
    private   String gender="";
    private PopupWindow pwState;
    private PopupWindow pwCity;
    private String nameState="";
    private String nameCity="";
    private String countryID="1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_registration);

        init();



    }

    private void init() {
        carSignUp =(CardView)findViewById(R.id.cardSignUp);
        etFirstName=(EditText) findViewById(R.id.etFirstName);
        etLastName=(EditText)findViewById(R.id.etLastName);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etState=(EditText)findViewById(R.id.etState);
        etCity=(EditText)findViewById(R.id.etCity);
        chbxAgree=(CheckBox)findViewById(R.id.chbxAgree);
        tvTramsCondition=(TextView)findViewById(R.id.tvTramsCondition);
        tvLogin=(TextView)findViewById(R.id.tvLogin);
        tvCountry=(TextView)findViewById(R.id.tvCountry);

        tvCountry.setText("Canada");

        etCity.setOnClickListener(this);
        etState.setOnClickListener(this);
        carSignUp.setOnClickListener(this);
        tvCountry.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.cardSignUp:
                if (validate()){
                    callApi();
                }
                break;
            case R.id.tvCountry:
                hideSoftKeyboard();
               callCountryAPI();

                break;
            case R.id.etState:
                hideSoftKeyboard();
                if (!countryID.isEmpty())
                getAllState();
                else
                    showDialog("Please select country first");

                break;
            case R.id.etCity:
                hideSoftKeyboard();
                if (!nameState.isEmpty())
                    getCity();
                else
                    showDialog("Please select state first");

                break;

        }
    }

    private void getCity() {
        if (isNetworkConnected()) {
            showProgressDialog();
            new ApiCity(getCityParam(),new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    CityMain main=(CityMain) t;
                    if (main.getStatus()){
                        initiateCityPopupWindow(main.getResponse());
                    }

                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();
                }

                @Override
                public void onError() {
                    dismissProgressDialog();
                }
            });
        }
    }

    private Map<String, String> getCityParam() {
        Map<String,String> map=new HashMap<>();
        map.put("state",nameState);
        return map;
    }

    public void getAllState(){
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiState(getStateParam(),new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    StateNameMain main=(StateNameMain)t;
                    if (main.getStatus()){
                        initiateStatePopupWindow(main.getResponse());
                    }

                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();
                }

                @Override
                public void onError() {
                    dismissProgressDialog();
                }
            });
        }
    }

    private Map<String, String> getStateParam() {

        Map<String,String>map=new HashMap<>();
        map.put("country_id",countryID);
        return map;
    }

    private void initiateCityPopupWindow(final List<CityResponse> response) {
        try {
            LayoutInflater inflater = (LayoutInflater) RegistrationActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_state_city,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwCity = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pwCity.setBackgroundDrawable(new BitmapDrawable());
            pwCity.setOutsideTouchable(true);
            pwCity.showAtLocation(layout, Gravity.CENTER, 0, 0);
            final TextView textView = (TextView) layout.findViewById(R.id.textView);
            textView.setText("Select Your City");
            ListView stateList = (ListView) layout.findViewById(R.id.stateList);

            CityBaseAdapter adapter=new CityBaseAdapter(RegistrationActivity.this,response);
            stateList.setAdapter(adapter);
            stateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    nameCity=response.get(i).getCity();
                    etCity.setText(nameCity);
                    pwCity.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateStatePopupWindow(final List<StateNameResult> response) {
        try {
            LayoutInflater inflater = (LayoutInflater) RegistrationActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_state_city,
                    (ViewGroup) findViewById(R.id.popup_element));
           /* pwState = new PopupWindow(layout, 560, 900, true);
            pwState.setBackgroundDrawable(new ColorDrawable());*/

            pwState = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pwState.setBackgroundDrawable(new BitmapDrawable());
            pwState.setOutsideTouchable(true);
            pwState.showAtLocation(layout, Gravity.CENTER, 0, 0);
            final TextView textView = (TextView) layout.findViewById(R.id.textView);
            textView.setText("Select Your State");
            ListView stateList = (ListView) layout.findViewById(R.id.stateList);

            StateBaseAdapter adapter=new StateBaseAdapter(RegistrationActivity.this,response);
            stateList.setAdapter(adapter);
            stateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    nameState=response.get(i).getName();
                    etState.setText(nameState);
                    pwState.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callApi() {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiRegistration(getParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                            preference.setUserId(""+object.getJSONObject("response").optInt("user_id"));
                            preference.setPhone(etPhone.getText().toString());
                            Gson gson = new Gson();
                            com.trafficticketbuddy.client.model.login.Response mResponse = gson.fromJson(object.getJSONObject("response").toString(), com.trafficticketbuddy.client.model.login.Response.class);
                            preference.setLoggedInUser(new Gson().toJson(mResponse));
                            startActivity(new Intent(RegistrationActivity.this,OTPActivity.class));
                            finish();
                        }
                        else
                            showDialog(object.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.print(res);



                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();
                }

                @Override
                public void onError() {
                    dismissProgressDialog();
                }
            });
        }









    }
    private Map<String,String> getParam(){
        Map<String,String> map=new HashMap<>();
        map.put("first_name",etFirstName.getText().toString());
        map.put("last_name",etLastName.getText().toString());
        map.put("email",etEmail.getText().toString());
        map.put("phone",etPhone.getText().toString());
        map.put("password",etPassword.getText().toString());
        map.put("user_type", Constant.USER_TYPE);
        map.put("country", tvCountry.getText().toString());
        map.put("state", etState.getText().toString());
        map.put("city", etCity.getText().toString());
        map.put("degree", "NA");
        map.put("device_type", "ANDROID");
        map.put("token", preference.getDeviceToken());
        return map;
    }

    private boolean validate() {
        if (etFirstName.getText().toString().isEmpty()){
            showDialog("Please enter first name.");
            return false;
        }else if (etLastName.getText().toString().isEmpty()){
            showDialog("Please enter first name.");
            return false;
        }else if (etEmail.getText().toString().isEmpty()){
            showDialog("Please enter email ID.");
            return false;
        }else if (!isValidEmail(etEmail.getText().toString())){
            showDialog("Please enter a valid email ID.");
            return false;
        }else if (etPhone.getText().toString().isEmpty()){
            showDialog("Please enter phone No.");
            return false;
        }else if (etPassword.getText().toString().isEmpty()){
            showDialog("Please enter password.");
            return false;
        }else if (etPassword.getText().toString().isEmpty()){
            showDialog("Please enter password.");
            return false;
        }else if (countryID.length()==0){
            showDialog("Please select Country.");
            return false;
        }else if (etState.getText().toString().equalsIgnoreCase("state")){
            showDialog("Please select state");
            return false;
        }else if (etCity.getText().toString().equalsIgnoreCase("city")){
            showDialog("Please select your city.");
            return false;
        }else if (!chbxAgree.isChecked()){
            showDialog("Please agree to the terms and condition privacy policy.");
            return false;
        }
        return true;
    }

    private void callCountryAPI(){
        if (isNetworkConnected()) {
            showProgressDialog();
            new ApiCountry(new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    CountryMain main= (CountryMain)t;
                    if (main.getStatus()){
                        countryPopup(main.getResponse());
                    }
                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();

                }

                @Override
                public void onError() {
                    dismissProgressDialog();
                }
            });
        }

    }


    private void countryPopup(final List<Response> response) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) RegistrationActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup_state_city,
                    (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pw.setBackgroundDrawable(new BitmapDrawable());
            pw.setOutsideTouchable(true);
            pw.showAsDropDown(tvCountry);
            final TextView textView = (TextView) layout.findViewById(R.id.textView);
            textView.setText("Select Your Country");
            ListView listCountry = (ListView) layout.findViewById(R.id.stateList);

            CountryBaseAdapter adapter=new CountryBaseAdapter(RegistrationActivity.this,response);
            listCountry.setAdapter(adapter);
            listCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    tvCountry.setText(response.get(i).getCountryName());
                    countryID=response.get(i).getId();
                    pw.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
