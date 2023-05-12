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

import com.example.ramakrishna.databinding.FragmentFeatureBinding;

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

public class FeatureFragment extends Fragment {

    FragmentFeatureBinding mFragmentFeatureBinding;

    List<DashboardResponse.Result> mLeadResponse = new ArrayList<>();
    public CommonFunction obj_commonfunction;

    public static FeatureFragment newInstance(){
        return new FeatureFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mFragmentFeatureBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_feature, container, false);


        obj_commonfunction = new CommonFunction(getActivity());

        OnloadLead();

        mFragmentFeatureBinding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFragmentFeatureBinding.refreshLayout.setRefreshing(false);
                //your code on swipe refresh
                //we are checking networking connectivity
                OnloadLead();

            }
        });

        return mFragmentFeatureBinding.getRoot();

    }

    private void OnloadLead()
    {
        if(obj_commonfunction.isNetworkAvailable() == true)
        {
            obj_commonfunction.showProgress();

            DashboardRequest objDashboardRequest = new DashboardRequest();
            objDashboardRequest.setUid( SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
            objDashboardRequest.setAccess_token( SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
            objDashboardRequest.setRecord_type("future");



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
                                mFragmentFeatureBinding.mFeatureAvailable.setVisibility(View.GONE);
                                mFragmentFeatureBinding.exclamatoryTxt.setVisibility(View.GONE);
                                mFragmentFeatureBinding.rvFeature.setVisibility(View.VISIBLE);

                                LeadAdapter mOrderHistoryAdapter = new LeadAdapter(getActivity(),getActivity(),mLeadResponse,"Feature");
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                mFragmentFeatureBinding.rvFeature.setLayoutManager(layoutManager);
                                mFragmentFeatureBinding.rvFeature.setAdapter(mOrderHistoryAdapter);
                            }
                            else
                            {
                                mFragmentFeatureBinding.mFeatureAvailable.setVisibility(View.VISIBLE);
                                mFragmentFeatureBinding.exclamatoryTxt.setVisibility(View.VISIBLE);
                                mFragmentFeatureBinding.rvFeature.setVisibility(View.GONE);
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