package RetrofitUtils;

import RetrofitUtils.Request.DashboardRequest;
import RetrofitUtils.Request.HistoryRequest;
import RetrofitUtils.Request.LocationRequest;
import RetrofitUtils.Request.LoginRequest;
import RetrofitUtils.Response.ActivityDoneResponse;
import RetrofitUtils.Response.CountResponse;
import RetrofitUtils.Response.DashboardResponse;
import RetrofitUtils.Response.HistoryResponse;
import RetrofitUtils.Response.LocationResponse;
import RetrofitUtils.Response.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Interface_Api
{

    /*
    * #dev_api = http://172.16.40.246:3000
#dev_api = http://vilfresh-admin.pptssolutions.com
dev_api = http://172.16.40.207:3000
    * */

    // @FormUrlEncoded
//    @POST("/APP_API/APPManagement/APPVersion_List")
//    Call<AppVersionResponse> mAPPVersion(@Body AppVersionRequest objAppVersionRequest);
//
//    @GET("/APP_API/Region_Management/List")
//    Call<RegionManagementResponse> mRegionManagementList();

    // @FormUrlEncoded
    @POST("user/get_token")
    Call<LoginResponse> mLoginRequest(@Body LoginRequest objteleecgcasedismissrequest);

    // @FormUrlEncoded
    @POST("user/activity")
    Call<DashboardResponse> mAllLeadList(@Body DashboardRequest objDashboardRequest);

    // @FormUrlEncoded
    @POST("user/activity/location")
    Call<LocationResponse> mAddlocation(@Body LocationRequest objLocationRequest);

    // @FormUrlEncoded
    @POST("user/activity/mark_as_done")
    Call<LocationResponse> mMarkasdone(@Body LocationRequest objLocationRequest);


    // @FormUrlEncoded
    @POST("user/activity/count")
    Call<CountResponse> mLeadcount(@Body DashboardRequest objDashboardRequest);


    // @FormUrlEncoded
    @POST("user/monthly_activity")
    Call<DashboardResponse> mMonthlyActivity(@Body DashboardRequest objDashboardRequest);

    // @FormUrlEncoded
    @POST("user/activity_done")
    Call<ActivityDoneResponse> mActivity_done(@Body DashboardRequest objDashboardRequest);

    // @FormUrlEncoded
    @POST("user/previous/history")
    Call<HistoryResponse> mActivity_done(@Body HistoryRequest objHistoryRequest);



}

