package com.trafficticketbuddy.client;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.trafficticketbuddy.client.adapter.CityBaseAdapter;
import com.trafficticketbuddy.client.adapter.StateBaseAdapter;
import com.trafficticketbuddy.client.apis.ApiCity;
import com.trafficticketbuddy.client.apis.ApiState;
import com.trafficticketbuddy.client.model.StateNameMain;
import com.trafficticketbuddy.client.model.StateNameResult;
import com.trafficticketbuddy.client.model.city.CityMain;
import com.trafficticketbuddy.client.model.city.CityResponse;
import com.trafficticketbuddy.client.model.registration.RegistrationMain;
import com.trafficticketbuddy.client.permission.Permission;
import com.trafficticketbuddy.client.restservice.APIHelper;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.restservice.RestService;
import com.trafficticketbuddy.client.utils.Constant;
import com.trafficticketbuddy.client.utils.FileUtils;
import com.trafficticketbuddy.client.utils.Imageutils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileCaseActivity extends BaseActivity implements Imageutils.ImageAttachmentListener {
    private TextView tvHeading;
    private Imageutils imageutils;
    private Bitmap bitmap;
    private String file_name;
    private LinearLayout linFontImage;
    private ImageView ivFontImage;
    private LinearLayout linBackImage;
    private ImageView ivBackImage;
    private LinearLayout linDrivingLiImage;
    private ImageView ivDrivingLiImage;
    private EditText etState;
    private EditText etCity;
    private EditText etDescription;
    private int imgPosition = 0;
    private PopupWindow pwState;
    private PopupWindow pwCity;
    private String nameState = "";
    private String nameCity = "";
    private String countryID = "1";
    private Bitmap bitmapFontImage;
    private Uri uriFontImage;
    private Bitmap bitmapBackImage;
    private Uri uriBackImage;
    private Bitmap bitmapDrivingLicence;
    private Uri uriDrivingLicence;
    private CardView cardSubmit;
    private com.trafficticketbuddy.client.model.login.Response mLogin;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
     */
        setContentView(R.layout.activity_filecase);
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        tvHeading.setText("File Case");
        imageutils = new Imageutils(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        initView();
    }

    private void initView() {
        Gson gson = new Gson();
        String json = preference.getString("login_user", "");
        mLogin = gson.fromJson(json, com.trafficticketbuddy.client.model.login.Response.class);
        linFontImage = (LinearLayout) findViewById(R.id.linFontImage);
        ivFontImage = (ImageView) findViewById(R.id.ivFontImage);
        linBackImage = (LinearLayout) findViewById(R.id.linBackImage);
        ivBackImage = (ImageView) findViewById(R.id.ivBackImage);
        linDrivingLiImage = (LinearLayout) findViewById(R.id.linDrivingLiImage);
        ivDrivingLiImage = (ImageView) findViewById(R.id.ivDrivingLiImage);
        etState = (EditText) findViewById(R.id.etState);
        etCity = (EditText) findViewById(R.id.etCity);
        cardSubmit = (CardView) findViewById(R.id.cardSubmit);
        etDescription = (EditText) findViewById(R.id.etDescription);
        linFontImage.setOnClickListener(this);
        linBackImage.setOnClickListener(this);
        linDrivingLiImage.setOnClickListener(this);
        etState.setOnClickListener(this);
        etCity.setOnClickListener(this);
        cardSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.linFontImage:
                if (new Permission().check_WRITE_FolderPermission2(this)) {
                    if (new Permission().checkCameraPermission(this)) {
                        ivFontImage.setVisibility(View.VISIBLE);
                        imgPosition = 1;
                        imageutils.imagepicker(1);
                    }
                }
                break;
            case R.id.linBackImage:
                if (new Permission().check_WRITE_FolderPermission2(this)) {
                    if (new Permission().checkCameraPermission(this)) {
                        ivBackImage.setVisibility(View.VISIBLE);
                        imgPosition = 2;
                        imageutils.imagepicker(1);
                    }
                }
                break;
            case R.id.linDrivingLiImage:
                ivDrivingLiImage.setVisibility(View.VISIBLE);
                imgPosition = 3;
                imageutils.imagepicker(1);
                break;
            case R.id.etState:
                getAllState();
                break;
            case R.id.etCity:
                getCity();
                break;
            case R.id.cardSubmit:
                if (isValidate()) {
                    fileACase();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private boolean isValidate() {
        if (bitmapFontImage == null) {
            showDialog("Please take a font picture of the ticket");
            return false;
        } else if (bitmapBackImage == null) {
            showDialog("Please take a back picture of the ticket");
            return false;
        } else if (bitmapDrivingLicence == null) {
            showDialog("Please take a driving licence picture");
            return false;
        } else if (etState.getText().toString().isEmpty()) {
            showDialog("Please select a state");
            return false;
        } else if (etCity.getText().toString().isEmpty()) {
            showDialog("Please select a city");
            return false;
        } else if (etDescription.getText().toString().isEmpty()) {
            showDialog("Please enter description about the ticket");
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.bitmap = file;
        this.file_name = filename;


        String path = Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file, filename, path, false);
        switch (imgPosition) {
            case 1:
                bitmapFontImage = file;
                uriFontImage = uri;
                ivFontImage.setImageBitmap(file);
                break;
            case 2:
                bitmapBackImage = file;
                uriBackImage = uri;
                ivBackImage.setImageBitmap(file);
                break;
            case 3:
                bitmapDrivingLicence = file;
                uriDrivingLicence = uri;
                ivDrivingLiImage.setImageBitmap(file);
                break;
        }

    }




    private void fileACase() {
        if(isNetworkConnected()){
            showProgressDialog();
           // RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), getImageFile(bitmapFontImage));
            MultipartBody.Part body_case_front_img = prepareFilePart("case_front_img",uriFontImage);
          //  RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), getImageFile(bitmapBackImage));
            MultipartBody.Part body_case_back_img = prepareFilePart("case_rear_img", uriBackImage);
           // RequestBody requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), getImageFile(bitmapDrivingLicence));
            MultipartBody.Part body_driving_lic = prepareFilePart("driving_license", uriDrivingLicence);
            RequestBody use_id_body = RequestBody.create(MediaType.parse("multipart/form-data"),mLogin.getId());
            RequestBody state_body = RequestBody.create(MediaType.parse("multipart/form-data"), etState.getText().toString());
            RequestBody city_body = RequestBody.create(MediaType.parse("multipart/form-data"), etCity.getText().toString());
            RequestBody description_body = RequestBody.create(MediaType.parse("multipart/form-data"), etDescription.getText().toString());

            Call<ResponseBody> getDepartment = RestService.getInstance().restInterface.fileACase(use_id_body,
                    description_body,state_body,city_body,body_case_front_img,body_case_back_img,body_driving_lic);

            APIHelper.enqueueWithRetry(getDepartment,new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dismissProgressDialog();
                    String main= null;
                    try {
                        main = response.body().string();
                        try {
                            JSONObject object=new JSONObject(main);
                            if (object.getBoolean("status")){
                                showDialog(object.getString("message"));
                            }
                            else
                                showDialog(object.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismissProgressDialog();

                }
            });
        }


    }
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }


    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = FileUtils.getFile(this, fileUri);
        MediaType type = MediaType.parse(getMimeType(fileUri));
        RequestBody requestFile = RequestBody.create(type, file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }




    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
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
        map.put("country_id","1");
        return map;
    }

    private void initiateCityPopupWindow(final List<CityResponse> response) {
        try {
            LayoutInflater inflater = (LayoutInflater) FileCaseActivity.this
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

            CityBaseAdapter adapter=new CityBaseAdapter(FileCaseActivity.this,response);
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
            LayoutInflater inflater = (LayoutInflater) FileCaseActivity.this
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

            StateBaseAdapter adapter=new StateBaseAdapter(FileCaseActivity.this,response);
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

}
