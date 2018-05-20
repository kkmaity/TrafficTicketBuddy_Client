package com.trafficticketbuddy.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trafficticketbuddy.client.adapter.CityBaseAdapter;
import com.trafficticketbuddy.client.adapter.StateBaseAdapter;
import com.trafficticketbuddy.client.apis.ApiCity;
import com.trafficticketbuddy.client.apis.ApiState;
import com.trafficticketbuddy.client.model.StateNameMain;
import com.trafficticketbuddy.client.model.StateNameResult;
import com.trafficticketbuddy.client.model.city.CityMain;
import com.trafficticketbuddy.client.model.city.CityResponse;
import com.trafficticketbuddy.client.permission.Permission;
import com.trafficticketbuddy.client.restservice.OnApiResponseListener;
import com.trafficticketbuddy.client.utils.Utility;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EditProfileActivity extends BaseActivity {

    private ImageView ivProfileImage;
    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_gender;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        et_first_name = (EditText)findViewById(R.id.et_first_name);
        et_last_name = (EditText)findViewById(R.id.et_last_name);
        et_email = (EditText)findViewById(R.id.et_email);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_gender = (EditText) findViewById(R.id.et_gender);
        et_state = (EditText) findViewById(R.id.et_state);
        et_city = (EditText) findViewById(R.id.et_city);
        et_country = (EditText) findViewById(R.id.et_country);

        ivProfileImage.setOnClickListener(this);
        et_state.setOnClickListener(this);
        et_city.setOnClickListener(this);
        et_gender.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivProfileImage:

                if (new Permission().check_WRITE_FolderPermission2(this)) {

                    if (new Permission().checkCameraPermission(this)) {
                        setImage();

                    }

                }
                break;
            case R.id.et_state:
                getAllState();
                break;
            case R.id.et_city:
                if (!nameState.isEmpty())
                    getCity();
                else
                    showDialog("Please select state first");

                break;
            case R.id.tvGender:
                initiatePopupWindow();

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
            pwCity = new PopupWindow(layout, 560, 900, true);
            pwCity.showAtLocation(layout, Gravity.CENTER, 0, 0);
            final TextView textViewMale = (TextView) layout.findViewById(R.id.textViewMale);
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

    private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) EditProfileActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup_gender,
                    (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 200, 200, true);
            // display the popup in the center
            // pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pw.showAsDropDown(et_gender);

            TextView textViewMale = (TextView) layout.findViewById(R.id.textViewMale);
            TextView textViewFemale = (TextView) layout.findViewById(R.id.textViewFemale);
            textViewMale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_gender.setText("Male");
                    gender="M";
                    pw.dismiss();
                }
            });
            textViewFemale.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    et_gender.setText("Female");
                    gender="F";
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
            new ApiState(new OnApiResponseListener() {
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
    private void initiateStatePopupWindow(final List<StateNameResult> response) {
        try {
            LayoutInflater inflater = (LayoutInflater) EditProfileActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_state_city,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwState = new PopupWindow(layout, 560, 900, true);
            pwState.showAtLocation(layout, Gravity.CENTER, 0, 0);
            final TextView textViewMale = (TextView) layout.findViewById(R.id.textViewMale);
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
                        ivProfileImage.setImageBitmap(BitmapFactory.decodeFile(compressedImageFile.getAbsolutePath()));
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
                        ivProfileImage.setImageBitmap(BitmapFactory.decodeFile(compressedImageFile.getAbsolutePath()));
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
}
