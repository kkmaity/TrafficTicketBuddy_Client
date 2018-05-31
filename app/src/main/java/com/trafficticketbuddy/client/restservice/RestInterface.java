package com.trafficticketbuddy.client.restservice;

import com.trafficticketbuddy.client.model.StateNameMain;
import com.trafficticketbuddy.client.model.cases.GetAllCasesMain;
import com.trafficticketbuddy.client.model.city.CityMain;
import com.trafficticketbuddy.client.model.country.CountryMain;
import com.trafficticketbuddy.client.model.homeBanner.HomeBannerMain;
import com.trafficticketbuddy.client.model.login.LoginMain;
import com.trafficticketbuddy.client.model.registration.RegistrationMain;
import com.trafficticketbuddy.client.utils.Constant;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RestInterface {

   // String BASE_URL = "http://13.58.150.208/buddy/";
   String BASE_URL = Constant.BASE_URL;

    @FormUrlEncoded
    @POST("api/v1/user/register")
    Call<ResponseBody> registrtion(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("api/v1/user/login")
    Call<LoginMain> login(@FieldMap Map<String,String> params);

    @Multipart
    @POST("api/v1/user/updateClientProfile")
    Call<LoginMain> editprofile(@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part profile_image, @Part MultipartBody.Part license_image);

    @POST("api/v1/user/country")
    Call<CountryMain> getCountry();

    @FormUrlEncoded
    @POST("api/v1/user/states")
    Call<StateNameMain> getStateName(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("api/v1/user/cities")
    Call<CityMain> getCityName(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("api/v1/user/resend_otp")
    Call<ResponseBody> resend_otp(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("api/v1/user/send_mail_otp")
    Call<ResponseBody> send_mail_otp(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("api/v1/user/validate_otp")
    Call<ResponseBody> validate_otp(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("api/v1/user/validate_email_otp")
    Call<ResponseBody> validate_email_otp(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("api/v1/user/register_facebook")
    Call<LoginMain> fblogin(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("api/v1/user/register_google")
    Call<LoginMain> Googlelogin(@FieldMap Map<String,String> params);



   @FormUrlEncoded
    @POST("api/v1/user/getAllCases")
    Call<GetAllCasesMain> getAllCases(@FieldMap Map<String,String> params);


 @POST("api/v1/user/banners")
 Call<HomeBannerMain> banners();
 /*params=> user_id, case_details, city, state, case_front_img, case_rear_img, driving_license*/

 @Multipart
 @POST("api/v1/user/caseFile")
 Call<ResponseBody> fileACase(
         @Part("user_id") RequestBody user_id,
         @Part("case_details") RequestBody case_details,
         @Part("city") RequestBody city,
         @Part("state") RequestBody state,
         @Part MultipartBody.Part files1,
         @Part MultipartBody.Part files2,
         @Part MultipartBody.Part files3);

   /* @POST("emp_track/api/userRegister.php")
    Call<RegistrationMain> userRegister(@Body ApiRegistrationParam params);

    @POST("emp_track/api/userLogin.php")
    Call<LoginMain> userLogin(@Body ApiLoginParam params);
*/
  /*   @POST("emp_track/api/attendance_history.php")
    Call<AttendenceHistoryMain> attendance_history(@Body ApiAttendenceHistoryParam params);

    @POST("emp_track/api/attendanceStart.php")
    Call<AttendenceStartMain> attendance_start(@Body ApiAttendenceStartParam params);

    @POST("emp_track/api/attendanceStop.php")
    Call<AttendenceStopMain> attendance_stop(@Body ApiAttendenceStopParam params);

    @POST("emp_track/api/attendanceStatus.php")
    Call<AttendanceStatusMain> attendanceStatus(@Body ApiAttendanceStatusParam params);

    @POST("emp_track/api/getemployeeList.php")
    Call<EmpListMain> getemployeeList(@Body ApiEmpListParam params);


    @POST("emp_track/api/leave_application_list.php")
    Call<AppliedLeaveList> leave_application_list(@Body LeaveParam params);

    @POST("emp_track/api/notification.php")
    Call<NotificationMain> notification(@Body NotificationParam params);

    



    @Multipart
    @POST("emp_track/api/userRegister.php")
    Call<RegistrationMain> userRegister(
            @Part("ApiKey") RequestBody user_family_id,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password,
            @Part("deviceToken") RequestBody deviceToken,
            @Part MultipartBody.Part files);


    @Multipart
    @POST("emp_track/api/outdoorworkStart.php")
    Call<ResponseBody> outdoorworkStart(
            @Part("ApiKey") RequestBody ApiKey,
            @Part("userid") RequestBody userid,
            @Part("job_category") RequestBody job_category,
            @Part("challan_no") RequestBody challan_no,
            @Part("challan_date") RequestBody challan_date,
            @Part("hospital_id") RequestBody hospital_id,
            @Part("doctor_id") RequestBody doctor_id,
            @Part("invoice_number") RequestBody invoice_number,
            @Part("invoice_date") RequestBody invoice_date,
            @Part("mode_of_transport") RequestBody mode_of_transport,
            @Part("office_bike_id") RequestBody office_bike_id,
            @Part("expense") RequestBody expense,
            @Part("startLat") RequestBody startLat,
            @Part("startLong") RequestBody startLong,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part picture1,
            @Part MultipartBody.Part picture2,
            @Part MultipartBody.Part picture3,
            @Part MultipartBody.Part picture4,
            @Part MultipartBody.Part picture5);

  //  http://173.214.180.212/emp_track/api/update_outdoor_work.php


    @Multipart
    @POST("emp_track/api/update_outdoor_work.php")
    Call<ResponseBody> upload(
            @Part("ApiKey") RequestBody ApiKey,
            @Part("jobid") RequestBody userid,
            @Part("description") RequestBody desc,
            @Part List<MultipartBody.Part> picture);*/

}
