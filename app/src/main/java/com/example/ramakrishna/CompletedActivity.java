package com.example.ramakrishna;

import static Utils.ConstantClass.Changeformat;
import static Utils.ConstantClass.Simpleformat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.ramakrishna.databinding.ActivityCompletedBinding;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.BaseApi;
import RetrofitUtils.Interface_Api;
import RetrofitUtils.Request.DashboardRequest;
import RetrofitUtils.Response.ActivityDoneResponse;
import RetrofitUtils.Response.DashboardResponse;
import Utils.BaseActivity;
import Utils.CommonFunction;
import Utils.SharedPrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedActivity extends BaseActivity {


    ActivityCompletedBinding mActivityCompletedBinding;
    List<ActivityDoneResponse.Result> mLeadResponse = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityCompletedBinding = DataBindingUtil.setContentView(this,R.layout.activity_completed);

        mActivityCompletedBinding.mBack.setOnClickListener(v -> {
            OnBack();
        });


        OnloadLead();

    }


    private void OnloadLead()
    {
        if(obj_commonfunction.isNetworkAvailable() == true)
        {
            obj_commonfunction.showProgress();

            DashboardRequest objDashboardRequest = new DashboardRequest();
            objDashboardRequest.setUid(SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
            objDashboardRequest.setAccess_token( SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));




            Interface_Api apiInterface = BaseApi.getApiUrl(CompletedActivity.this).create(Interface_Api.class);
            Call<ActivityDoneResponse> apiCall = null;
            apiCall = apiInterface.mActivity_done(objDashboardRequest);
            apiCall.enqueue(new Callback<ActivityDoneResponse>()
            {
                @Override
                public void onResponse(Call<ActivityDoneResponse> call, Response<ActivityDoneResponse> response)
                {
                    obj_commonfunction.hideProgress();
                    if(response.body() != null)
                    {
                        try
                        {
                            mLeadResponse = response.body().getResult();

                            if(mLeadResponse.size() != 0)
                            {
                                mActivityCompletedBinding.mcompletedAvailable.setVisibility(View.GONE);
                                mActivityCompletedBinding.exclamatoryTxt.setVisibility(View.GONE);
                                mActivityCompletedBinding.rvcompleted.setVisibility(View.VISIBLE);

                                CompletedLeadAdapter mOrderHistoryAdapter = new CompletedLeadAdapter(CompletedActivity.this,CompletedActivity.this,mLeadResponse,"");
                                LinearLayoutManager layoutManager = new LinearLayoutManager(CompletedActivity.this);
                                mActivityCompletedBinding.rvcompleted.setLayoutManager(layoutManager);
                                mActivityCompletedBinding.rvcompleted.setAdapter(mOrderHistoryAdapter);

                            }
                            else
                            {
                                mActivityCompletedBinding.mcompletedAvailable.setVisibility(View.VISIBLE);
                                mActivityCompletedBinding.exclamatoryTxt.setVisibility(View.VISIBLE);
                                mActivityCompletedBinding.rvcompleted.setVisibility(View.GONE);
                            }
                        }
                        catch (Exception e) {
                            mLoadCustomToast(CompletedActivity.this,getResources().getString(R.string.accesstokenexpire));
                        }

                    }
                }

                @Override
                public void onFailure(Call<ActivityDoneResponse> call, Throwable t)
                {
                    obj_commonfunction.hideProgress();
                    mLoadCustomToast(CompletedActivity.this,getResources().getString(R.string.accesstokenexpire));
                }
            });
        }
        else
        {
            mLoadCustomToast(CompletedActivity.this,getResources().getString(R.string.tryAgain));
        }
    }

    private void OnBack()
    {
        obj_commonfunction.navigation(CompletedActivity.this,Dashboard.class);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OnBack();
    }

}