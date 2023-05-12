package com.example.ramakrishna.UI_Testing.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ramakrishna.Dashboard;
import com.example.ramakrishna.DashboardLeadAdapter;
import com.example.ramakrishna.R;
import com.example.ramakrishna.UI_Testing.activities.fragments.CalenderFragment;
import com.example.ramakrishna.UI_Testing.activities.fragments.MainFragments;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.BaseApi;
import RetrofitUtils.Interface_Api;
import RetrofitUtils.Request.DashboardRequest;
import RetrofitUtils.Response.CountResponse;
import RetrofitUtils.Response.DashboardResponse;
import Utils.BaseActivity;
import Utils.SharedPrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends BaseActivity {
    RecyclerView recyclerView;
    List<DashboardResponse.Result> mLeadResponse = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        recyclerView = findViewById(R.id.rv_Today_1);
        OnloadTodayLead();
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


            Interface_Api apiInterface = BaseApi.getApiUrl(NavigationActivity.this).create(Interface_Api.class);
            Call<DashboardResponse> apiCall = null;
            apiCall = apiInterface.mAllLeadList(objDashboardRequest);
            apiCall.enqueue(new Callback<DashboardResponse>() {
                @Override
                public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                    obj_commonfunction.hideProgress();
                    if (response.body() != null) {
                        try {
                            mLeadResponse = response.body().getResult();
                            Log.i(TAG, "onResponseinRe "+response.body().getResult().toString());
                            if (mLeadResponse.size() != 0) {
//                                mleads_response.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                DashboardLeadAdapter mOrderHistoryAdapter = new DashboardLeadAdapter(NavigationActivity.this, NavigationActivity.this, mLeadResponse, "Today");
                                LinearLayoutManager layoutManager = new LinearLayoutManager(NavigationActivity.this);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(mOrderHistoryAdapter);
                            } else {
//                                mleads_response.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
//                            mleads_response.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            mLoadCustomToast(NavigationActivity.this, getResources().getString(R.string.accesstokenexpire));
                        }

                    }
                }

                @Override
                public void onFailure(Call<DashboardResponse> call, Throwable t) {
                    obj_commonfunction.hideProgress();
//                    mleads_response.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    mLoadCustomToast(NavigationActivity.this, getResources().getString(R.string.accesstokenexpire));
                }
            });
        } else {
//            mleads_response.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            mLoadCustomToast(NavigationActivity.this, getResources().getString(R.string.tryAgain));
        }
    }
}