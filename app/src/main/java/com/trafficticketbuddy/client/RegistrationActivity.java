package com.trafficticketbuddy.client;

import android.content.Context;
import android.content.Intent;
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

import com.trafficticketbuddy.client.adapter.CityBaseAdapter;
import com.trafficticketbuddy.client.adapter.StateBaseAdapter;
import com.trafficticketbuddy.client.apis.ApiCity;
import com.trafficticketbuddy.client.apis.ApiRegistration;
import com.trafficticketbuddy.client.apis.ApiState;
import com.trafficticketbuddy.client.model.StateNameMain;
import com.trafficticketbuddy.client.model.StateNameResult;
import com.trafficticketbuddy.client.model.city.CityMain;
import com.trafficticketbuddy.client.model.city.CityResponse;
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
    private TextView tvLogin,tvGender;
    private PopupWindow pw;
    private   String gender="";
    private PopupWindow pwState;
    private PopupWindow pwCity;
    private String nameState="";
    private String nameCity="";

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
        tvGender=(TextView)findViewById(R.id.tvGender);

        etCity.setOnClickListener(this);
        etState.setOnClickListener(this);
        carSignUp.setOnClickListener(this);
        tvGender.setOnClickListener(this);
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
                case R.id.tvGender:
                    initiatePopupWindow();

                break;
                case R.id.etState:
                    getAllState();

                break;
                case R.id.etCity:
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
            new ApiState(new OnApiResponseListener() {
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

    private void initiateCityPopupWindow(final List<CityResponse> response) {
        try {
            LayoutInflater inflater = (LayoutInflater) RegistrationActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_state_city,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwCity = new PopupWindow(layout, 560, 900, true);
            pwCity.showAtLocation(layout, Gravity.CENTER, 0, 0);
            final TextView textViewMale = (TextView) layout.findViewById(R.id.textViewMale);
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
            pwState = new PopupWindow(layout, 560, 900, true);
            pwState.showAtLocation(layout, Gravity.CENTER, 0, 0);
            final TextView textViewMale = (TextView) layout.findViewById(R.id.textViewMale);
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
                            startActivity(new Intent(RegistrationActivity.this,OTPActivity.class));
                        }
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
        map.put("gender",gender);
        map.put("password",etPassword.getText().toString());
        map.put("user_type", Constant.USER_TYPE);
        map.put("country", Constant.USER_COUNTRY);
        map.put("state", etState.getText().toString());
        map.put("city", etCity.getText().toString());
        return map;
    }

    private boolean validate() {
        if (etFirstName.getText().toString().isEmpty()){
            showDialog("Please enter first name.");
            return false;
        }else if (etLastName.getText().toString().isEmpty()){
            showDialog("Please enter first name.");
            return false;
        }else if (gender.toString().isEmpty()){
            showDialog("Please select gender.");
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
        }else if (etState.getText().toString().isEmpty()){
            showDialog("Please select state name.");
            return false;
        }else if (etCity.getText().toString().isEmpty()){
            showDialog("Please select your city.");
            return false;
        }
        return true;
    }
    private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) RegistrationActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup_gender,
                    (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 200, 200, true);
            // display the popup in the center
           // pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pw.showAsDropDown(tvGender);

            TextView textViewMale = (TextView) layout.findViewById(R.id.textViewMale);
            TextView textViewFemale = (TextView) layout.findViewById(R.id.textViewFemale);
            textViewMale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvGender.setText("Male");
                    gender="M";
                    pw.dismiss();
                }
            });
            textViewFemale.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    tvGender.setText("Female");
                    gender="F";
                    pw.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
