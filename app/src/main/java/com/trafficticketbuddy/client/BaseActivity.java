package com.trafficticketbuddy.client;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.trafficticketbuddy.client.interfaces.FbLoginCompleted;
import com.trafficticketbuddy.client.interfaces.GoogleLoginCompleted;
import com.trafficticketbuddy.client.preferences.Preference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    public ProgressDialog prsDlg;
    public Preference preference=null;
    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=1001;
    private LoginManager fbLoginManager;
    private CallbackManager callbackManager;
    private FbLoginCompleted mFbLoginCompleted;
    private GoogleLoginCompleted mGoogleLoginCompleted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
   */     prsDlg = new ProgressDialog(this);
        preference =new Preference(BaseActivity.this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        facebookRegisterCallBack();

    }
    public String getCurrentDate(){
        String timeStmp="";
        Calendar calendar = Calendar.getInstance();
        java.sql.Date currentTimestamp = new java.sql.Date(calendar.getTime().getTime());
        timeStmp=""+currentTimestamp;
        return timeStmp;
    }
    public String getTimeStamp(){
        long time= System.currentTimeMillis();
        return ""+time;
    }
    public void showSnackMessage(View view){
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onClick(View view) {

    }


    public void showProgressDialog(){
        prsDlg.setMessage("Please wait...");
        prsDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prsDlg.setIndeterminate(true);
        prsDlg.setCancelable(false);
        prsDlg.show();
    }

    public void dismissProgressDialog() {
        if(prsDlg!=null){
            if (prsDlg.isShowing()) {
                prsDlg.dismiss();
            }
        }

    }


    public void hideKeyBoard(EditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public void showKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

public void showDialog(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
  /*  Dialog dialog=new Dialog(BaseActivity.this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(true);
    dialog.setTitle(msg);
    dialog.show();*/
}
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(BaseActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append(",");
                result.append(address.getSubAdminArea()).append(",");
                //result.append(address.getAdminArea()).append(",");
                result.append(address.getCountryCode());//.append(",");
                // result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        System.out.println("result" + result.toString());

        return result.toString();
    }
    public boolean isNetworkConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)BaseActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
        }
        return isConnected;
    }



    private void setDefaultExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                Log.e("BaseActivity", "Default Exception Handler : ");

                final String DOUBLE_LINE_SEP = "\r\n\r\n";
                final String SINGLE_LINE_SEP = "\r\n";
                StackTraceElement[] arr = e.getStackTrace();
                final StringBuffer report = new StringBuffer(e.toString());
                final String lineSeparator = "-------------------------------\n\n";
                report.append(DOUBLE_LINE_SEP);
                report.append("--------- Stack trace ---------\n\n");
                for (int i = 0; i < arr.length; i++) {
                    report.append("    ");
                    report.append(arr[i].toString());
                    report.append(SINGLE_LINE_SEP);
                }

                // If the exception was thrown in a background thread inside
                // AsyncTask, then the actual exception can be found with
                // getCause
                Throwable cause = e.getCause();
                if (cause != null) {
                    report.append(lineSeparator);
                    report.append("--------- Cause ---------\n\n");
                    report.append(cause.toString());
                    report.append(DOUBLE_LINE_SEP);
                    arr = cause.getStackTrace();
                    for (int i = 0; i < arr.length; i++) {
                        report.append("    ");
                        report.append(arr[i].toString());
                        report.append(SINGLE_LINE_SEP);
                    }
                }

                System.err.println(report.toString());

                // Getting the Device brand,model and sdk version details.
                report.append(lineSeparator);
                report.append("--------- Device ---------\n\n");
                report.append("Brand: ");
                report.append(Build.BRAND);
                report.append(SINGLE_LINE_SEP);
                report.append("Device: ");
                report.append(Build.DEVICE);
                report.append(SINGLE_LINE_SEP);
                report.append("Model: ");
                report.append(Build.MODEL);
                report.append(SINGLE_LINE_SEP);
                report.append("Metric: ");

                int density = getResources().getDisplayMetrics().densityDpi;

                switch (density) {
                    case DisplayMetrics.DENSITY_LOW:
                        report.append("LDPI ");
                        break;
                    case DisplayMetrics.DENSITY_MEDIUM:
                        report.append("MDPI ");
                        break;
                    case DisplayMetrics.DENSITY_HIGH:
                        report.append("HDPI ");
                        break;
                    case DisplayMetrics.DENSITY_XHIGH:
                        report.append("XHDPI ");
                        break;
                }

                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                report.append(String.valueOf(dm.widthPixels) + "x" + String.valueOf(dm.heightPixels) + "  " + String.valueOf(dm.densityDpi) + "dpi");
                report.append(SINGLE_LINE_SEP);
                report.append("Id: ");
                report.append(Build.ID);
                report.append(SINGLE_LINE_SEP);
                report.append("Product: ");
                report.append(Build.PRODUCT);
                report.append(SINGLE_LINE_SEP);
                report.append(lineSeparator);
                report.append("--------- Firmware ---------\n\n");
                report.append("SDK: ");
                report.append(Build.VERSION.SDK);
                report.append(SINGLE_LINE_SEP);
                report.append("Release: ");
                report.append(Build.VERSION.RELEASE);
                report.append(SINGLE_LINE_SEP);
                report.append("Incremental: ");
                report.append(Build.VERSION.INCREMENTAL);
                report.append(SINGLE_LINE_SEP);
                report.append(lineSeparator);

                Intent crashedIntent = new Intent(BaseActivity.this, CrashActivity.class);
                crashedIntent.putExtra("stacktrace", report.toString());
                crashedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                crashedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(crashedIntent);
                System.exit(0);

            }

        });
    }
    public String getTimer(long timeInMilliSeconds ){
        long seconds = timeInMilliSeconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        //String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
        String time = String.format("%2d",hours % 24) + ":" + String.format("%2d",minutes % 60) + ":" + String.format("%2d",seconds % 60);
        return time;
    }
    public String getTodayDate(){
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        String date = simpledateformat.format(calander.getTime());
        return date;
    }
    public String milisecondToDate(long milliSeconds){
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

    }
    public File getImageFile(Bitmap bitmap){
        if (bitmap!=null){
            File f = new File(BaseActivity.this.getCacheDir(), "profimg");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

//Convert bitmap to byte array

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bitmapdata);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return f;
        }else
            return null;
    }


    public void signIn(GoogleLoginCompleted mGoogleLoginCompleted) {
        this.mGoogleLoginCompleted=mGoogleLoginCompleted;
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {
            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }else{
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            mGoogleLoginCompleted.onGoogleCompleted(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
        }
    }

    private void facebookRegisterCallBack() {
        fbLoginManager = com.facebook.login.LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        fbLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                mFbLoginCompleted.onFaceBookCompleted(object,response);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name,location,address");
                request.setParameters(parameters);
                request.executeAsync();
            }
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    public void fbSignInClick(FbLoginCompleted mFbLoginCompleted){
        this.mFbLoginCompleted=mFbLoginCompleted;
        LoginManager.getInstance().logOut();
        fbLoginManager.logInWithReadPermissions(this, Arrays.asList("email", "public_profile", "user_birthday"));
    }

    public  void hideSoftKeyboard() {

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

}
