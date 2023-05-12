package com.example.ramakrishna;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ramakrishna.UI_Testing.activities.CustomTodo;
import com.example.ramakrishna.databinding.ActivityDashboardBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.BaseApi;
import RetrofitUtils.Interface_Api;
import RetrofitUtils.Request.DashboardRequest;
import RetrofitUtils.Response.CountResponse;
import RetrofitUtils.Response.DashboardResponse;
import RetrofitUtils.Response.LoginResponse;
import Utils.BaseActivity;
import Utils.SharedPrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends BaseActivity implements View.OnClickListener {
    private static final String TAG = Dashboard.class.getCanonicalName();
    ActivityDashboardBinding mActivityDashboardBinding;
    List<CountResponse.Result> mLeadCountResponse = new ArrayList<>();


    ImageView mMenubutton, mUserImage;
    String[] mSideMenuitem = {"Dashboard", "Calendar", "TODO List", "Completed Activity","Scan Card", "Profile"};
    Integer[] mSideMenuimages = {R.drawable.ic_home, R.drawable.ic_calendar, R.drawable.ic_todo, R.drawable.ic_completed,R.drawable.ic_scanner_icon,R.drawable.ic_profile};
    SidemenuAdapter mSideMenuAdapter;

    TextView mOverdueCount, mTodayCount, mPlannedCount, mtodayAvailable, mUserName, mUserEmail;
    List<DashboardResponse.Result> mLeadResponse = new ArrayList<>();
    RecyclerView rvToday;
    LinearLayout mPlanned, mToday, mOverdue, mleads_response, mHome, mCalender, mTodo, mCompleteActivity, mProfile;
    List<LoginResponse.Result> objUserDetails = new ArrayList<>();
    ImageView mBHome, mBCalender, mBTodo, mBCompleteActivity, mBProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        //   SharedPrefsUtils.putInt(SharedPrefsUtils.PREF_KEY.VIEWPAGERCURRENTPOSTION, 0);
        mMenubutton = (ImageView) findViewById(R.id.menubutton_dashboard);
        mUserImage = (ImageView) findViewById(R.id.mUserImage);
        mOverdueCount = (TextView) findViewById(R.id.mOverdueCount);
        mTodayCount = (TextView) findViewById(R.id.mTodayCount);
        mPlannedCount = (TextView) findViewById(R.id.mPlannedCount);
        mtodayAvailable = (TextView) findViewById(R.id.mtodayAvailable);
        mUserName = (TextView) findViewById(R.id.mUserName);
        mUserEmail = (TextView) findViewById(R.id.mUserEmail);
        rvToday = (RecyclerView) findViewById(R.id.rv_Today_1);
        mPlanned = (LinearLayout) findViewById(R.id.mPlanned);
        mToday = (LinearLayout) findViewById(R.id.mToday);
        mOverdue = (LinearLayout) findViewById(R.id.mOverdue);
        mleads_response = (LinearLayout) findViewById(R.id.leads_no_response);
        //bottom navigation


        Gson gson = new Gson();
        String Str_Dashboardstore = SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.USER_DETAILS);
        objUserDetails = gson.fromJson(Str_Dashboardstore, new TypeToken<ArrayList<LoginResponse.Result>>() {
        }.getType());

        mUserName.setText(objUserDetails.get(0).getEmployee_name());
        mUserEmail.setText(objUserDetails.get(0).getIdentification_id());

        mActivityDashboardBinding.mDashboardUserName.setText(objUserDetails.get(0).getEmployee_name());
        mActivityDashboardBinding.mDashboardDestination.setText(objUserDetails.get(0).getJob_position());

        if (objUserDetails.get(0).getEmployee_image() == null) {

        } else {
            if (objUserDetails.get(0).getEmployee_image().isEmpty()) {
            } else {

                byte[] imageAsBytes = Base64.decode(objUserDetails.get(0).getEmployee_image().getBytes(), Base64.DEFAULT);
                mActivityDashboardBinding.mSideUserImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                mUserImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            }
        }


        mActivityDashboardBinding.mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsUtils.putBoolean(SharedPrefsUtils.PREF_KEY.LOGIN_SESSION, false);
                obj_commonfunction.navigation(Dashboard.this, LoginActivity.class); //IntroPage
                finish();
            }
        });

        onloadCount();

        OnloadTodayLead();

        mPlanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsUtils.putInt(SharedPrefsUtils.PREF_KEY.VIEWPAGERCURRENTPOSTION, 2);
                obj_commonfunction.navigation(Dashboard.this, CustomTodo.class); //IntroPage
                finish();
            }
        });

        mToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsUtils.putInt(SharedPrefsUtils.PREF_KEY.VIEWPAGERCURRENTPOSTION, 1);
                obj_commonfunction.navigation(Dashboard.this, CustomTodo.class); //IntroPage
                finish();
            }
        });

        mOverdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefsUtils.putInt(SharedPrefsUtils.PREF_KEY.VIEWPAGERCURRENTPOSTION, 0);
                obj_commonfunction.navigation(Dashboard.this, CustomTodo.class); //IntroPage
                finish();
            }
        });


        mSideMenuAdapter = new SidemenuAdapter(Dashboard.this, this, R.layout.sidemenulistitem, mSideMenuitem, mSideMenuimages, mActivityDashboardBinding.mNavicationDrawer);
        mActivityDashboardBinding.mSidemenulist.setAdapter(mSideMenuAdapter);

        mMenubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivityDashboardBinding.mNavicationDrawer.isDrawerOpen(GravityCompat.START) == false) {
                    mActivityDashboardBinding.mNavicationDrawer.openDrawer(GravityCompat.START);
                }
            }
        });

//        mActivityDashboardBinding.mLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPrefsUtils.putBoolean(SharedPrefsUtils.PREF_KEY.LOGIN_SESSION, false);
//                obj_commonfunction.navigation(Dashboard.this, LoginActivity.class); //IntroPage
//                finish();
//            }
//        });
//
//        mActivityDashboardBinding.mTodo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                obj_commonfunction.navigation(Dashboard.this, HomeActivity.class); //IntroPage
//                finish();
//            }
//        });

    }

    private void OnloadTodayLead() {

        if (obj_commonfunction.isNetworkAvailable() == true) {
            obj_commonfunction.showProgress();

            DashboardRequest objDashboardRequest = new DashboardRequest();
            objDashboardRequest.setUid(SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
            Log.i(TAG, "uid_response: " + SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
            objDashboardRequest.setAccess_token(SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
            Log.i(TAG, "access_token: " + SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
            objDashboardRequest.setRecord_type("today");


            Interface_Api apiInterface = BaseApi.getApiUrl(Dashboard.this).create(Interface_Api.class);
            Call<DashboardResponse> apiCall = null;
            apiCall = apiInterface.mAllLeadList(objDashboardRequest);
            apiCall.enqueue(new Callback<DashboardResponse>() {
                @Override
                public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                    obj_commonfunction.hideProgress();
                    if (response.body() != null) {
                        try {
                            mLeadResponse = response.body().getResult();
                            if (mLeadResponse.size() != 0) {
                                mleads_response.setVisibility(View.GONE);
                                rvToday.setVisibility(View.VISIBLE);

                                DashboardLeadAdapter mOrderHistoryAdapter = new DashboardLeadAdapter(Dashboard.this, Dashboard.this, mLeadResponse, "Today");
                                LinearLayoutManager layoutManager = new LinearLayoutManager(Dashboard.this);
                                rvToday.setLayoutManager(layoutManager);
                                rvToday.setAdapter(mOrderHistoryAdapter);
                            } else {
                                mleads_response.setVisibility(View.VISIBLE);
                                rvToday.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            mleads_response.setVisibility(View.VISIBLE);
                            rvToday.setVisibility(View.GONE);
                            mLoadCustomToast(Dashboard.this, getResources().getString(R.string.accesstokenexpire));
                        }

                    }
                }

                @Override
                public void onFailure(Call<DashboardResponse> call, Throwable t) {
                    obj_commonfunction.hideProgress();
                    mleads_response.setVisibility(View.VISIBLE);
                    rvToday.setVisibility(View.GONE);
                    mLoadCustomToast(Dashboard.this, getResources().getString(R.string.accesstokenexpire));
                }
            });
        } else {
            mleads_response.setVisibility(View.VISIBLE);
            rvToday.setVisibility(View.GONE);
            mLoadCustomToast(Dashboard.this, getResources().getString(R.string.tryAgain));
        }
    }

    private void onloadCount() {
        if (obj_commonfunction.isNetworkAvailable() == true) {
            // obj_commonfunction.showProgress();

            DashboardRequest objDashboardRequest = new DashboardRequest();
            objDashboardRequest.setUid(SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
            objDashboardRequest.setAccess_token(SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));

            Interface_Api apiInterface = BaseApi.getApiUrl(Dashboard.this).create(Interface_Api.class);
            Call<CountResponse> apiCall = null;
            apiCall = apiInterface.mLeadcount(objDashboardRequest);
            apiCall.enqueue(new Callback<CountResponse>() {
                @Override
                public void onResponse(Call<CountResponse> call, Response<CountResponse> response) {
                    // obj_commonfunction.hideProgress();
                    if (response.body() != null) {
                        try {
                            mLeadCountResponse = response.body().getResult();
                            if (mLeadCountResponse.size() != 0) {
                                //tabArray = new String[]{"Overdue("+ String.valueOf(mLeadCountResponse.get(2).getOverdue()) + ")", "Today("+ String.valueOf(mLeadCountResponse.get(0).getToday()) + ")", "Feature("+ String.valueOf(mLeadCountResponse.get(1).getFuture()) + ")"};
                                SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_TODAY, String.valueOf(mLeadCountResponse.get(0).getToday()));
                                SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_OVERDUE, String.valueOf(mLeadCountResponse.get(2).getOverdue()));
                                SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.KEY_FEATURE, String.valueOf(mLeadCountResponse.get(1).getFuture()));

                                int mcount = mLeadCountResponse.get(0).getToday() + mLeadCountResponse.get(2).getOverdue() + mLeadCountResponse.get(1).getFuture();
                                //mActivityDashboardBinding.mleadCount.setText(String.valueOf(mcount));

                                mOverdueCount.setText(String.valueOf(mLeadCountResponse.get(2).getOverdue()));
                                mTodayCount.setText(String.valueOf(mLeadCountResponse.get(0).getToday()));
                                mPlannedCount.setText(String.valueOf(mLeadCountResponse.get(1).getFuture()));
                            }else {

                            }
                        } catch (Exception e) {
                            mLoadCustomToast(Dashboard.this, getResources().getString(R.string.accesstokenexpire));
                        }

                    }
                }

                @Override
                public void onFailure(Call<CountResponse> call, Throwable t) {
                    // obj_commonfunction.hideProgress();
                    mLoadCustomToast(Dashboard.this, getResources().getString(R.string.accesstokenexpire));
                }
            });
        } else {
            mLoadCustomToast(Dashboard.this, getResources().getString(R.string.tryAgain));
        }
    }


    @Override
    public void onClick(View v) {

    }
}