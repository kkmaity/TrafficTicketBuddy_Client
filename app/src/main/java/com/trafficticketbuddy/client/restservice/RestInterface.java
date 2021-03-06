package com.trafficticketbuddy.client.restservice;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestInterface {

    String BASE_URL = "http://173.214.180.212/";

   /*   @POST("emp_track/api/appconfig.php")
  Call<AppConfigMain> appconfig(@Body AppConfigParam params);

    *//*@POST("emp_track/api/userRegister.php")
    Call<RegistrationMain> userRegister(@Body ApiRegistrationParam params);
*//*
    @POST("emp_track/api/userLogin.php")
    Call<LoginMain> userLogin(@Body ApiLoginParam params);

    @POST("emp_track/api/attendance_history.php")
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
