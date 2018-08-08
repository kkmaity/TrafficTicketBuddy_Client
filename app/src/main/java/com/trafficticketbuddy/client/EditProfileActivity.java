package com.trafficticketbuddy.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.trafficticketbuddy.client.adapter.CityBaseAdapter;
import com.trafficticketbuddy.client.adapter.CountryBaseAdapter;
import com.trafficticketbuddy.client.adapter.StateBaseAdapter;
import com.trafficticketbuddy.client.apis.ApiCity;
import com.trafficticketbuddy.client.apis.ApiCountry;
import com.trafficticketbuddy.client.apis.ApiEditProfile;
import com.trafficticketbuddy.client.apis.ApiLogin;
import com.trafficticketbuddy.client.apis.ApiResendOTP;
import com.trafficticketbuddy.client.apis.ApiState;
import com.trafficticketbuddy.client.model.StateNameMain;
import com.trafficticketbuddy.client.model.StateNameResult;
import com.trafficticketbuddy.client.model.city.CityMain;
import com.trafficticketbuddy.client.model.city.CityResponse;
import com.trafficticketbuddy.client.model.country.CountryMain;
import com.trafficticketbuddy.client.model.country.Response;
import com.trafficticketbuddy.client.model.login.LoginMain;
import com.trafficticketbuddy.client.permission.Permission;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.utils.Constant;
import com.trafficticketbuddy.client.utils.FileUtils;
import com.trafficticketbuddy.client.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends BaseActivity {

    private ImageView ivProfileImage,ivLicense;
    private EditText et_first_name;
    private EditText et_last_name;
    //private EditText et_email;
    private EditText et_phone;
    private EditText et_state;
    private EditText et_city;
    private EditText et_country;

    int cameraPhotoRotation = 0;
    private Uri imageFileUri;
    private String takePhotoFile;
    private PopupWindow pw;
    private   String gender="";
    private PopupWindow pwState;
    private PopupWindow pwCity;
    private String nameState="";
    private String nameCity="";

    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int PICK_PDFFILE_RESULT_CODE=0x3;
    private List<StateNameResult> response;
    private String countryID="1";
    private com.trafficticketbuddy.client.model.login.Response mLogin;
    private Bitmap image_profile,image_license;
    private int image_type = 1;
    private File Image_profile,Image_license;
    private CardView cardUpdate;
    public static EditProfileActivity editProfileActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        editProfileActivity = this;
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        ivLicense = (ImageView)findViewById(R.id.ivLicense);
        et_first_name = (EditText)findViewById(R.id.et_first_name);
        et_last_name = (EditText)findViewById(R.id.et_last_name);
        //et_email = (EditText)findViewById(R.id.et_email);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_state = (EditText) findViewById(R.id.et_state);
        et_city = (EditText) findViewById(R.id.et_city);
        et_country = (EditText) findViewById(R.id.et_country);
        cardUpdate = (CardView) findViewById(R.id.cardUpdate);

        ivProfileImage.setOnClickListener(this);
        et_state.setOnClickListener(this);
        et_city.setOnClickListener(this);
        et_country.setOnClickListener(this);
        ivLicense.setOnClickListener(this);
        cardUpdate.setOnClickListener(this);

        Gson gson = new Gson();
        String json = preference.getString("login_user", "");
        mLogin = gson.fromJson(json, com.trafficticketbuddy.client.model.login.Response.class);
        if(mLogin.getFirstName()!=null){
            et_first_name.setText(mLogin.getFirstName());
        }if(mLogin.getLastName()!=null){
            et_last_name.setText(mLogin.getLastName());
        }if(mLogin.getPhone()!=null){
            et_phone.setText(mLogin.getPhone());
        }if(mLogin.getCountry()!=null){
            et_country.setText(mLogin.getCountry());
        }if(mLogin.getState()!=null){
            et_state.setText(mLogin.getState());
        }if(mLogin.getCity()!=null){
            et_city.setText(mLogin.getCity());
        }if(mLogin.getProfileImage()!=null && !mLogin.getProfileImage().isEmpty()){
            if(mLogin.getProfileImage().startsWith("http")){
                Glide.with(this).load(mLogin.getProfileImage()).into(ivProfileImage);
            }else{
                String path = Constant.BASE_URL+mLogin.getProfileImage();
                Glide.with(this).load(path).into(ivProfileImage);
            }

        }if(mLogin.getLicenseImage()!=null && !mLogin.getLicenseImage().isEmpty()){
            String path = Constant.BASE_URL+mLogin.getLicenseImage();
            Glide.with(this).load(path).into(ivLicense);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivProfileImage:
                if (new Permission().check_WRITE_FolderPermission2(this)) {
                    if (new Permission().checkCameraPermission(this)) {
                        image_type=1;
                        setImage();
                    }
                }
                break;
            case R.id.ivLicense:
                if (new Permission().check_WRITE_FolderPermission2(this)) {
                    if (new Permission().checkCameraPermission(this)) {
                        image_type=2;
                        setImage();
                    }
                }
                break;
            case R.id.et_country:
                callCountryAPI();
                break;
            case R.id.et_state:
                if (countryID.length()==0)
                    showDialog("Please select country first");
                else
                    getAllState();
                break;
            case R.id.et_city:
                if (!nameState.isEmpty())
                    getCity();
                else
                    showDialog("Please select state first");

                break;
            case R.id.cardUpdate:
                isValidate();
                break;

        }
    }

    private Map<String, String> getCityParam() {
        Map<String,String> map=new HashMap<>();
        map.put("state",nameState);
        return map;
    }

    private void initiateCityPopupWindow(final List<CityResponse> response) {
        try {
            LayoutInflater inflater = (LayoutInflater) EditProfileActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_state_city,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwCity = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pwCity.setBackgroundDrawable(new BitmapDrawable());
            pwCity.showAtLocation(layout, Gravity.CENTER, 0, 0);
            final TextView textViewMale = (TextView) layout.findViewById(R.id.textView);
            textViewMale.setText("Select Your City");
            ListView stateList = (ListView) layout.findViewById(R.id.stateList);

            CityBaseAdapter adapter=new CityBaseAdapter(EditProfileActivity.this,response);
            stateList.setAdapter(adapter);
            stateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    nameCity=response.get(i).getCity();
                    et_city.setText(nameCity);
                    pwCity.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void isValidate(){
        if(et_first_name.getText().toString().isEmpty()){
            et_first_name.setError("Please enter first name");
        }else if(et_last_name.getText().toString().isEmpty()){
            et_last_name.setError("Please enter last name");
        }else if(et_phone.getText().toString().isEmpty()){
            et_phone.setError("Please enter phone no.");
        }else if(et_country.getText().toString().isEmpty()){
            et_country.setError("Please select country");
        }else if(et_state.getText().toString().isEmpty()){
            et_state.setError("Please select state");
        }else if(et_city.getText().toString().isEmpty()){
            et_city.setError("Please select city");
        }else if(Image_profile==null && mLogin.getProfileImage() ==null){
            showDialog("Please select profile image");
        }else if(Image_profile==null && mLogin.getProfileImage().isEmpty()){
            showDialog("Please select profile image");
        }/*else if(Image_license==null && mLogin.getLicenseImage().isEmpty()){
            showDialog("Please select licence image");
        }*/else {
            doEditProfileApi();
        }
    }

    private void doEditProfileApi() {
        showProgressDialog();
        new ApiEditProfile(getParamEditProfile(),getImageParamEditProfile(), new OnApiResponseListener() {
            @Override
            public <E> void onSuccess(E t) {
                {
                    dismissProgressDialog();
                    LoginMain mLoginMain = (LoginMain) t;
                    if(mLoginMain.getStatus()){
                        preference.setLoggedInUser(new Gson().toJson(mLoginMain.getResponse()));
                        mLogin=mLoginMain.getResponse();
                        if(mLoginMain.getResponse().getPhone().isEmpty() || mLoginMain.getResponse().getCountry().isEmpty()
                                || mLoginMain.getResponse().getState().isEmpty() || mLoginMain.getResponse().getCity().isEmpty()){

                        }
                        else if(mLoginMain.getResponse().getIsPhoneVerified().equalsIgnoreCase("0")){
                            recendOTP();
//                        }else if(mLoginMain.getResponse().getIsEmailVerified().equalsIgnoreCase("0")){
//                            startActivity(new Intent(EditProfileActivity.this,EmailOTPActivity.class));
//                            finish();
//                        }
                        }
                       else{
                            startActivity(new Intent(EditProfileActivity.this,MainActivity.class));
                            finish();
                        }
                    }else{
                        showDialog(mLoginMain.getMessage());
                    }

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

    private Map<String, RequestBody> getParamEditProfile(){
        Map<String,RequestBody> map=new HashMap<>();
        map.put("first_name",RequestBody.create(MediaType.parse("text/plain"), et_first_name.getText().toString()));
        map.put("id",RequestBody.create(MediaType.parse("text/plain"), mLogin.getId()));
        map.put("last_name",RequestBody.create(MediaType.parse("text/plain"), et_last_name.getText().toString()));
        if(!mLogin.getPhone().equalsIgnoreCase(et_phone.getText().toString())){
            map.put("phone", RequestBody.create(MediaType.parse("text/plain"), et_phone.getText().toString()));
        }
        map.put("country",RequestBody.create(MediaType.parse("text/plain"), et_country.getText().toString()));
        map.put("state",RequestBody.create(MediaType.parse("text/plain"), et_state.getText().toString()));
        map.put("city",RequestBody.create(MediaType.parse("text/plain"), et_city.getText().toString()));
        return map;
    }

    private Map<String, MultipartBody.Part> getImageParamEditProfile(){
        Map<String,MultipartBody.Part> map=new HashMap<>();
        if(Image_profile!=null) {
            MultipartBody.Part profile_image = prepareFilePart("profile_image", Uri.fromFile(Image_profile));
            map.put("profile_image",profile_image);
        }
        if(Image_license!=null) {
            MultipartBody.Part license_image = prepareFilePart("license_image", Uri.fromFile(Image_license));
            map.put("license_image",license_image);
        }
        return map;
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


    private void countryPopup(final List<Response> response) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) EditProfileActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup_state_city,
                    (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pw.setBackgroundDrawable(new BitmapDrawable());
            pw.setOutsideTouchable(true);


            pw.showAsDropDown(et_country);
            final TextView textView = (TextView) layout.findViewById(R.id.textView);
            textView.setText("Select Your Country");
            ListView listCountry = (ListView) layout.findViewById(R.id.stateList);

            CountryBaseAdapter adapter=new CountryBaseAdapter(EditProfileActivity.this,response);
            listCountry.setAdapter(adapter);
            listCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    nameState=response.get(i).getCountryName();
                    et_country.setText(nameState);
                    countryID=response.get(i).getId();
                    pw.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
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

    public void getAllState(){
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiState(getStatePAram(),new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    StateNameMain main=(StateNameMain)t;
                    if (main.getStatus()){
                        response = main.getResponse();
                        initiateStatePopupWindow(main.getResponse());
                        //initiateStatePopupWindow(main.getResponse());
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

    private Map<String, String> getStatePAram() {
        Map<String,String> map=new HashMap<>();
        map.put("country_id",countryID);
        return map;
    }

    private void initiateStatePopupWindow(final List<StateNameResult> response) {
        try {
            LayoutInflater inflater = (LayoutInflater) EditProfileActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_state_city,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwState = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pwState.setBackgroundDrawable(new BitmapDrawable());
            pwState.showAtLocation(layout, Gravity.CENTER, 0, 0);
            final TextView textViewMale = (TextView) layout.findViewById(R.id.textView);
            textViewMale.setText("Select Your State");
            ListView stateList = (ListView) layout.findViewById(R.id.stateList);

            StateBaseAdapter adapter=new StateBaseAdapter(EditProfileActivity.this,response);
            stateList.setAdapter(adapter);
            stateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    nameState=response.get(i).getName();
                    et_state.setText(nameState);
                    pwState.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select State");
        for(int i=0; i<response.size(); i++){
            menu.add(0, v.getId(), 0, response.get(i).getName());
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Call"){
            Toast.makeText(getApplicationContext(),"calling code",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="SMS"){
            Toast.makeText(getApplicationContext(),"sending sms code",Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }


    public void setImage() {
        new AlertDialog.Builder(this)
                .setTitle("Choose Image")
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        pickFileFromGallery();
                        // EventBus.getDefault().post(new EvtGallery());
                    }
                })
                .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        takePhoto();
                        //  EventBus.getDefault().post(new EvtTakePhoto());
                    }
                })
                .show();
    }

    public void pickFileFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    private void takePhoto() {

        String storageState = Environment.getExternalStorageState();

        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, UUID.randomUUID().toString() + ".jpg");

            imageFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
            startActivityForResult(i, REQUEST_CODE_TAKE_PICTURE);
        } else {
            new AlertDialog.Builder(this).setMessage("External Storage (SD Card) is required.\n\nCurrent state: " + storageState)
                    .setCancelable(true).create().show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_CANCELED) {

            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CODE_TAKE_PICTURE) {

                String[] projection = {MediaStore.Images.Media.DATA};
                CursorLoader loader = new CursorLoader(this, imageFileUri, projection, null, null, null);
                Cursor cursor = loader.loadInBackground();

                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();


                takePhotoFile = cursor.getString(column_index_data);
                if (takePhotoFile != null) {
                    cameraPhotoRotation = Utility.getCameraPhotoOrientation(takePhotoFile);
                }

                if (cameraPhotoRotation == 0) {
                    cameraPhotoRotation = Utility.getRotationFromMediaStore(this, imageFileUri);
                }

                int dh = Utility.dpToPx(this, 640);
                int dw = Utility.dpToPx(this, 500);

                Bitmap bitmap = Utility.scaledDownBitmap(takePhotoFile, dh, dw);

                if (cameraPhotoRotation != 0) {
                    Bitmap rotationCorrectedBitmap = Utility.rotateBitmap(cameraPhotoRotation, bitmap);
                    bitmap.recycle();
                    bitmap = rotationCorrectedBitmap;
                }

                final File compressedImageFile = Utility.saveImage(bitmap, Utility.getApplicationCacheDir(this));

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(image_type==1) {
                            image_profile=BitmapFactory.decodeFile(compressedImageFile.getAbsolutePath());
                            ivProfileImage.setImageBitmap(image_profile);
                            Image_profile = compressedImageFile;
                        }else{
                            image_license=BitmapFactory.decodeFile(compressedImageFile.getAbsolutePath());
                            ivLicense.setImageBitmap(image_license);
                            Image_license = compressedImageFile;
                        }
                        /*EventBus.getDefault().post(new EventProfilePicSelectedForUpload(compressedImageFile.getAbsolutePath()));
                        EventBus.getDefault().post(new EventProfilePicSelectedForUpload4(compressedImageFile.getAbsolutePath()));*/
                    }
                }, 1000);


            } else if (requestCode == REQUEST_CODE_GALLERY) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                final String picturePath = c.getString(columnIndex);
                c.close();
                cameraPhotoRotation = Utility.getCameraPhotoOrientation(picturePath);
                if (cameraPhotoRotation == 0)
                    cameraPhotoRotation = Utility.getRotationFromMediaStore(this, selectedImage);


                int dh = Utility.dpToPx(this, 640);
                int dw = Utility.dpToPx(this, 500);

                Bitmap bitmap = Utility.scaledDownBitmap(picturePath, dh, dw);

                if (cameraPhotoRotation != 0) {
                    Bitmap rotationCorrectedBitmap = Utility.rotateBitmap(cameraPhotoRotation, bitmap);
                    bitmap.recycle();
                    bitmap = rotationCorrectedBitmap;
                }

                final File compressedImageFile = Utility.saveImage(bitmap, Utility.getApplicationCacheDir(this));

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(image_type==1) {
                            image_profile=BitmapFactory.decodeFile(compressedImageFile.getAbsolutePath());
                            ivProfileImage.setImageBitmap(image_profile);
                            Image_profile = compressedImageFile;
                        }else{
                            image_license=BitmapFactory.decodeFile(compressedImageFile.getAbsolutePath());
                            ivLicense.setImageBitmap(image_license);
                            Image_license = compressedImageFile;
                        }
                       /* EventBus.getDefault().post(new EventProfilePicSelectedForUpload(compressedImageFile.getAbsolutePath()));
                        EventBus.getDefault().post(new EventProfilePicSelectedForUpload4(compressedImageFile.getAbsolutePath()));*/
                    }
                }, 1000);


            }
        }
    }
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    private void recendOTP() {
        if (isNetworkConnected()){
            showProgressDialog();
            new ApiResendOTP(getParamResendOTP(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    String res=(String)t;
                    try {
                        JSONObject object=new JSONObject(res);
                        if (object.getBoolean("status")){
                            startActivity(new Intent(EditProfileActivity.this,OTPActivity.class));
                            finish();
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

    private Map<String, String> getParamResendOTP() {
        Map<String,String> map=new HashMap<>();
        map.put("user_id",mLogin.getId());
        map.put("phone",mLogin.getPhone());
        return map;
    }
}
