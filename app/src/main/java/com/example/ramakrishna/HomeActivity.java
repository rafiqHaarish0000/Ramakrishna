package com.example.ramakrishna;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ramakrishna.UI_Testing.activities.CustomTodo;
import com.example.ramakrishna.databinding.ActivityHomeBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.BaseApi;
import RetrofitUtils.Interface_Api;
import RetrofitUtils.Request.DashboardRequest;
import RetrofitUtils.Response.CountResponse;
import Utils.BaseActivity;
import Utils.CommonFunction;
import Utils.SharedPrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {


    HomePagerAdapter mHomePagerAdapter;
    String[] tabArray;
    Integer[] tabColorArray;
    ActivityHomeBinding mActivityHomeBinding;
    public CommonFunction obj_commonfunction;
    int mProductCount = 0;
    FrameLayout.LayoutParams params;
    List<CountResponse.Result> mLeadCountResponse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_home);
        mActivityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);


        //  onloadCount();


        // tabArray = new String[]{getResources().getString(R.string.tabone), getResources().getString(R.string.tabtwo), getResources().getString(R.string.tabthree)};
        // tabArray = new String[]{"Overdue(99)", "Today(99)", "Feature(99)"};

        tabArray = new String[]{"Pending(" + SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.KEY_OVERDUE) + ")", "Today(" + SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.KEY_TODAY) + ")", "Planned(" + SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.KEY_FEATURE) + ")"};
        tabColorArray = new Integer[]{getResources().getColor(R.color.overdue), getResources().getColor(R.color.today), getResources().getColor(R.color.future)};
        params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);


        // Back button
        mActivityHomeBinding.mBack.setOnClickListener(v ->
                onBack());


        setupViewPager(mActivityHomeBinding.productlistPager);
        mActivityHomeBinding.producttabs.setupWithViewPager(mActivityHomeBinding.productlistPager);

        // Set View pager Current Item
        // mActivityHomeBinding.productlistPager.setCurrentItem(0);
        mActivityHomeBinding.productlistPager.setCurrentItem(SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.VIEWPAGERCURRENTPOSTION));

        // Set Custom Tabs
        setUpCustomTabs();

    }

    private void onloadCount() {
        if (obj_commonfunction.isNetworkAvailable() == true) {
            // obj_commonfunction.showProgress();

            DashboardRequest objDashboardRequest = new DashboardRequest();
            objDashboardRequest.setUid(SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
            objDashboardRequest.setAccess_token(SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));


            Interface_Api apiInterface = BaseApi.getApiUrl(HomeActivity.this).create(Interface_Api.class);
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
                                tabArray = new String[]{"Overdue(" + String.valueOf(mLeadCountResponse.get(2).getOverdue()) + ")", "Today(" + String.valueOf(mLeadCountResponse.get(0).getToday()) + ")", "Future(" + String.valueOf(mLeadCountResponse.get(1).getFuture()) + ")"};
                                tabColorArray = new Integer[]{getResources().getColor(R.color.overdue), getResources().getColor(R.color.today), getResources().getColor(R.color.future)};
                                params = new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.WRAP_CONTENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT);

                                // Back button
                                mActivityHomeBinding.mBack.setOnClickListener(v ->
                                        onBack());

                                setupViewPager(mActivityHomeBinding.productlistPager);
                                mActivityHomeBinding.producttabs.setupWithViewPager(mActivityHomeBinding.productlistPager);

                                // Set View pager Current Item
                                mActivityHomeBinding.productlistPager.setCurrentItem(0);

                                // Set Custom Tabs
                                setUpCustomTabs();
                            } else {

                            }
                        } catch (Exception e) {
                            mLoadCustomToast(HomeActivity.this, getResources().getString(R.string.accesstokenexpire));
                        }

                    }
                }

                @Override
                public void onFailure(Call<CountResponse> call, Throwable t) {
                    // obj_commonfunction.hideProgress();
                    mLoadCustomToast(HomeActivity.this, getResources().getString(R.string.accesstokenexpire));
                }
            });
        } else {
            mLoadCustomToast(HomeActivity.this, getResources().getString(R.string.tryAgain));
        }
    }

    // Back button
    private void onBack() {

        startActivity(new Intent(HomeActivity.this, Dashboard.class));
        finish();

    }

    // Setup the View Pager
    private void setupViewPager(ViewPager viewPager) {

        mHomePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), mActivityHomeBinding.producttabs.getTabCount(), tabArray);
        mHomePagerAdapter.addFrag(new OverDueFragment());
        mHomePagerAdapter.addFrag(new TodayFragment());
        mHomePagerAdapter.addFrag(new FeatureFragment());
        viewPager.setAdapter(mHomePagerAdapter);

        int limit = (mHomePagerAdapter.getCount() > 1 ? mHomePagerAdapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);
    }

    // Set Custom tabs
    private void setUpCustomTabs() {
        for (int i = 0; i < tabArray.length; i++) {
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.tab_home, null);
            TextView tv = (TextView) dialogLayout.findViewById(R.id.txt_view);
            tv.setText(tabArray[i]);
            tv.setTextColor(tabColorArray[i]);

            TabLayout.Tab tab = mActivityHomeBinding.producttabs.getTabAt(i);

            if (tab != null)
                tab.setCustomView(dialogLayout);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack();
    }
}