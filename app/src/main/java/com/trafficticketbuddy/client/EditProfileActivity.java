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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.trafficticketbuddy.client.permission.Permission;
import com.trafficticketbuddy.client.utils.Utility;

import java.io.File;
import java.util.UUID;

public class EditProfileActivity extends BaseActivity {

    private ImageView ivProfileImage;
    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_email;
    private EditText et_phone;
    private Spinner spinner_gender;
    private Spinner spinner_state;
    private Spinner spinner_city;

    int cameraPhotoRotation = 0;
    private Uri imageFileUri;
    private String takePhotoFile;

    public static final int REQUEST_CODE_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final int PICK_PDFFILE_RESULT_CODE=0x3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        et_first_name = (EditText)findViewById(R.id.et_first_name);
        et_last_name = (EditText)findViewById(R.id.et_last_name);
        et_email = (EditText)findViewById(R.id.et_email);
        et_phone = (EditText)findViewById(R.id.et_phone);
        spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_city = (Spinner) findViewById(R.id.spinner_city);

        ivProfileImage.setOnClickListener(this);
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
        }
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
