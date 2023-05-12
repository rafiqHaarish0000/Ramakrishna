package com.example.ramakrishna;

import android.app.Activity;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ramakrishna.databinding.FragmentTodayBinding;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.BaseApi;
import RetrofitUtils.Interface_Api;
import RetrofitUtils.Request.DashboardRequest;
import RetrofitUtils.Response.DashboardResponse;
import Utils.CommonFunction;
import Utils.CustomToast;
import Utils.SharedPrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayFragment extends Fragment {

   FragmentTodayBinding mFragmentTodayBinding;

    List<DashboardResponse.Result> mLeadResponse = new ArrayList<>();
    public CommonFunction obj_commonfunction;


    public static TodayFragment newInstance(){
        return new TodayFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mFragmentTodayBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false);


        obj_commonfunction = new CommonFunction(getActivity());

        OnloadLead();

        mFragmentTodayBinding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFragmentTodayBinding.refreshLayout.setRefreshing(false);
                //your code on swipe refresh
                //we are checking networking connectivity
                OnloadLead();

            }
        });


        return mFragmentTodayBinding.getRoot();
    }

    private void OnloadLead()
    {
        if(obj_commonfunction.isNetworkAvailable() == true)
        {
            obj_commonfunction.showProgress();

            DashboardRequest objDashboardRequest = new DashboardRequest();
            objDashboardRequest.setUid( SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
            objDashboardRequest.setAccess_token( SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
            objDashboardRequest.setRecord_type("today");



            Interface_Api apiInterface = BaseApi.getApiUrl(getActivity()).create(Interface_Api.class);
            Call<DashboardResponse> apiCall = null;
            apiCall = apiInterface.mAllLeadList(objDashboardRequest);
            apiCall.enqueue(new Callback<DashboardResponse>()
            {
                @Override
                public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response)
                {
                    obj_commonfunction.hideProgress();
                    if(response.body() != null)
                    {
                        try
                        {
                            mLeadResponse = response.body().getResult();
                            if(mLeadResponse.size() != 0)
                            {
                                mFragmentTodayBinding.mtodayAvailable.setVisibility(View.GONE);
                                mFragmentTodayBinding.exclamatoryTxt.setVisibility(View.GONE);
                                mFragmentTodayBinding.rvToday.setVisibility(View.VISIBLE);

                                LeadAdapter mOrderHistoryAdapter = new LeadAdapter(getActivity(),getActivity(),mLeadResponse,"Today");
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                mFragmentTodayBinding.rvToday.setLayoutManager(layoutManager);
                                mFragmentTodayBinding.rvToday.setAdapter(mOrderHistoryAdapter);
                            }
                            else
                            {
                                mFragmentTodayBinding.mtodayAvailable.setVisibility(View.VISIBLE);
                                mFragmentTodayBinding.exclamatoryTxt.setVisibility(View.VISIBLE);
                                mFragmentTodayBinding.rvToday.setVisibility(View.GONE);
                            }
                        }
                        catch (Exception e) {
                            mLoadCustomToast(getActivity(),getResources().getString(R.string.accesstokenexpire));
                        }

                    }
                }

                @Override
                public void onFailure(Call<DashboardResponse> call, Throwable t)
                {
                    obj_commonfunction.hideProgress();
                    mLoadCustomToast(getActivity(),getResources().getString(R.string.accesstokenexpire));
                }
            });
        }
        else
        {
            mLoadCustomToast(getActivity(),getResources().getString(R.string.tryAgain));
        }
    }

    public void mLoadCustomToast(Activity mcontaxt, String message)
    {
        CustomToast.makeText(mcontaxt, message, CustomToast.LENGTH_SHORT, 0).show();
    }
}