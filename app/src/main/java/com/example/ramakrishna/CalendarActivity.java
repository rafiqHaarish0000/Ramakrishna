package com.example.ramakrishna;

import static Utils.ConstantClass.Changeformat;
import static Utils.ConstantClass.Simpleformat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.ramakrishna.databinding.ActivityCalendarBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import RetrofitUtils.BaseApi;
import RetrofitUtils.Interface_Api;
import RetrofitUtils.Request.DashboardRequest;
import RetrofitUtils.Response.DashboardResponse;
import Utils.BaseActivity;
import Utils.CommonFunction;
import Utils.SharedPrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarActivity extends BaseActivity implements ItemClickListenerEvent {


    ActivityCalendarBinding mActivityCalendarBinding;
    public GregorianCalendar mCalCurrentMonth;
    private CalendarAdapter mCalAdapter;
    ArrayList<String> mDeliveryBoyLeaveDetails = new ArrayList<String>();
    List<DashboardResponse.Result> mLeadResponse = new ArrayList<>();
    List<DashboardResponse.Result> mFilterLeadResponse = new ArrayList<>();
    String  mCurentDate;
    DateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_calendar);

        mActivityCalendarBinding = DataBindingUtil.setContentView(this,R.layout.activity_calendar);

        mActivityCalendarBinding.mBack.setOnClickListener(v -> {
            OnBack();
        });

        df = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        Date date = new Date();
        mCurentDate = df.format(date);

        mCalCurrentMonth = (GregorianCalendar) GregorianCalendar.getInstance();
        mActivityCalendarBinding.mSelectedMonthName.setText(android.text.format.DateFormat.format("MMMM yyyy", mCalCurrentMonth));

        mCalAdapter = new CalendarAdapter(CalendarActivity.this, mCalCurrentMonth, mDeliveryBoyLeaveDetails);
        mActivityCalendarBinding.mGvCalendar.setAdapter(mCalAdapter);
        mActivityCalendarBinding.mGvCalendar.setBackgroundColor(Color.parseColor("#8abc40"));
        mActivityCalendarBinding.mGvCalendar.setVerticalSpacing(1);
        mActivityCalendarBinding.mGvCalendar.setHorizontalSpacing(1);

        OnloadLead();

    }

    private void OnBack()
    {
        obj_commonfunction.navigation(CalendarActivity.this,Dashboard.class);
        finish();
    }

    private void OnloadLead()
    {
        if(obj_commonfunction.isNetworkAvailable() == true)
        {
            obj_commonfunction.showProgress();

            DashboardRequest objDashboardRequest = new DashboardRequest();
            objDashboardRequest.setUid( SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
            objDashboardRequest.setAccess_token( SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
            objDashboardRequest.setMonth_or_day("month");



            Interface_Api apiInterface = BaseApi.getApiUrl(CalendarActivity.this).create(Interface_Api.class);
            Call<DashboardResponse> apiCall = null;
            apiCall = apiInterface.mMonthlyActivity(objDashboardRequest);
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
                            mFilterLeadResponse.clear();
                            if(mLeadResponse.size() != 0)
                            {
                                for(int i=0;i<mLeadResponse.size();i++)
                                {
                                    mDeliveryBoyLeaveDetails.add(mLeadResponse.get(i).getDue_date());

                                    if(mLeadResponse.get(i).getDue_date().contentEquals(mCurentDate))
                                    {
                                        mFilterLeadResponse.add(mLeadResponse.get(i));
                                    }
                                }

                                mCalAdapter = new CalendarAdapter(CalendarActivity.this, mCalCurrentMonth, mDeliveryBoyLeaveDetails);
                                mActivityCalendarBinding.mGvCalendar.setAdapter(mCalAdapter);
                                mActivityCalendarBinding.mGvCalendar.setBackgroundColor(Color.parseColor("#8abc40"));
                                mActivityCalendarBinding.mGvCalendar.setVerticalSpacing(1);
                                mActivityCalendarBinding.mGvCalendar.setHorizontalSpacing(1);


                                if(mFilterLeadResponse.size() != 0)
                                {
                                    mActivityCalendarBinding. mtodayAvailable.setVisibility(View.GONE);
                                    mActivityCalendarBinding.mLeadList.setVisibility(View.VISIBLE);

                                    DashboardLeadAdapter mOrderHistoryAdapter = new DashboardLeadAdapter(CalendarActivity.this,CalendarActivity.this,mFilterLeadResponse,"");
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(CalendarActivity.this);
                                    mActivityCalendarBinding.mLeadList.setLayoutManager(layoutManager);
                                    mActivityCalendarBinding.mLeadList.setAdapter(mOrderHistoryAdapter);
                                }
                                else
                                {
                                    mActivityCalendarBinding.mtodayAvailable.setVisibility(View.VISIBLE);
                                    mActivityCalendarBinding.mLeadList.setVisibility(View.GONE);


                                    mActivityCalendarBinding.mtodayAvailable.setText("Lead not available on " + CommonFunction.formatDate(mCurentDate, Simpleformat, Changeformat));
                                }

                            }
                            else
                            {

                            }
                        }
                        catch (Exception e) {
                            mLoadCustomToast(CalendarActivity.this,getResources().getString(R.string.accesstokenexpire));
                        }

                    }
                }

                @Override
                public void onFailure(Call<DashboardResponse> call, Throwable t)
                {
                    obj_commonfunction.hideProgress();
                    mLoadCustomToast(CalendarActivity.this,getResources().getString(R.string.accesstokenexpire));
                }
            });
        }
        else
        {
            mLoadCustomToast(CalendarActivity.this,getResources().getString(R.string.tryAgain));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OnBack();
    }

    @Override
    public void onClickEvent(View view, int position, String selectdate) {

        //mLoadCustomToast(CalendarActivity.this,selectdate);

        mFilterLeadResponse.clear();
        for(int i=0;i<mLeadResponse.size();i++)
        {
            if(mLeadResponse.get(i).getDue_date().contentEquals(selectdate))
            {
                mFilterLeadResponse.add(mLeadResponse.get(i));
            }
        }

        if(mFilterLeadResponse.size() != 0)
        {
            mActivityCalendarBinding. mtodayAvailable.setVisibility(View.GONE);
            mActivityCalendarBinding.mLeadList.setVisibility(View.VISIBLE);

            DashboardLeadAdapter mOrderHistoryAdapter = new DashboardLeadAdapter(CalendarActivity.this,CalendarActivity.this,mFilterLeadResponse,"cal");
            LinearLayoutManager layoutManager = new LinearLayoutManager(CalendarActivity.this);
            mActivityCalendarBinding.mLeadList.setLayoutManager(layoutManager);
            mActivityCalendarBinding.mLeadList.setAdapter(mOrderHistoryAdapter);
        }
        else
        {
            mActivityCalendarBinding.mtodayAvailable.setVisibility(View.VISIBLE);
            mActivityCalendarBinding.mLeadList.setVisibility(View.GONE);
            mActivityCalendarBinding.mtodayAvailable.setText("Lead not available on " + CommonFunction.formatDate(selectdate, Simpleformat, Changeformat));

        }
    }





}