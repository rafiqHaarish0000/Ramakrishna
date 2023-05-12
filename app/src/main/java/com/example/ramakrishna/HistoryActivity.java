package com.example.ramakrishna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ramakrishna.databinding.ActivityHistoryBinding;
import com.example.ramakrishna.databinding.InputtypeadapterBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.BaseApi;
import RetrofitUtils.Interface_Api;
import RetrofitUtils.Request.DashboardRequest;
import RetrofitUtils.Request.HistoryRequest;
import RetrofitUtils.Request.LocationRequest;
import RetrofitUtils.Response.ActivityDoneResponse;
import RetrofitUtils.Response.DashboardResponse;
import RetrofitUtils.Response.HistoryResponse;
import Utils.BaseActivity;
import Utils.CommonFunction;
import Utils.SharedPrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends BaseActivity {

    ActivityHistoryBinding mActivityHistoryBinding;
    List<HistoryResponse.Result> mHistoryResponse = new ArrayList<>();
    List<HistoryResponse.Input_datum> mIputTypeResponse = new ArrayList<>();
    List<DashboardResponse.Result> mLeadsResponse = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_history);

        Gson gson = new Gson();
        String Str_Dashboardstore = SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.LEAD_DETAILS);
        mLeadsResponse = gson.fromJson(Str_Dashboardstore,new  TypeToken<ArrayList<DashboardResponse.Result>>(){}.getType());

        mActivityHistoryBinding.mBack.setOnClickListener(v -> {
            OnBack();
        });

        OnloadLead();

    }

    private void OnloadLead()
    {
        if(obj_commonfunction.isNetworkAvailable() == true)
        {
            obj_commonfunction.showProgress();

            HistoryRequest objHistoryRequest = new HistoryRequest();
            objHistoryRequest.setUid( SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
            objHistoryRequest.setAccess_token( SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
            objHistoryRequest.setUser_activity_id(mLeadsResponse.get(0).getUser_activity_id());




            Interface_Api apiInterface = BaseApi.getApiUrl(HistoryActivity.this).create(Interface_Api.class);
            Call<HistoryResponse> apiCall = null;
            apiCall = apiInterface.mActivity_done(objHistoryRequest);
            apiCall.enqueue(new Callback<HistoryResponse>()
            {
                @Override
                public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response)
                {
                    obj_commonfunction.hideProgress();
                    if(response.body() != null)
                    {
//                        try
//                        {
                            mHistoryResponse = response.body().getResult();

                            if(mHistoryResponse.size() == 0)
                            {
                                mActivityHistoryBinding.mOpportunityName.setText("");
                                mActivityHistoryBinding.mContactName.setText("");
                                mActivityHistoryBinding.mLocation.setText("");
                                // mActivityHistoryBinding.mPhone.setText(mHistoryResponse.get(0).getContact_mobile());
                                mActivityHistoryBinding.mPhone.setText("");
                                mActivityHistoryBinding.mNextActivity.setText("");
                                mActivityHistoryBinding.mActivityType.setText("");
                                mActivityHistoryBinding.mremarks.setText("");


                                mActivityHistoryBinding.mSRHinput.setVisibility(View.GONE);


                            }
                            else
                            {
                                mActivityHistoryBinding.mOpportunityName.setText(mHistoryResponse.get(0).getEnquiry_name());
                                mActivityHistoryBinding.mContactName.setText(mHistoryResponse.get(0).getContact_name());
                                mActivityHistoryBinding.mLocation.setText(mHistoryResponse.get(0).getLocation());
                                 mActivityHistoryBinding.mPhone.setText(mHistoryResponse.get(0).getContact_mobile());
                               // mActivityHistoryBinding.mPhone.setText("");
                                mActivityHistoryBinding.mNextActivity.setText(mHistoryResponse.get(0).getSummary());
                                mActivityHistoryBinding.mActivityType.setText(mHistoryResponse.get(0).getActivity_type());
                                mActivityHistoryBinding.mremarks.setText(mHistoryResponse.get(0).getNote());

                                mIputTypeResponse = mHistoryResponse.get(0).getInput_data();


                                if(mIputTypeResponse.size() == 0)
                                {
                                    mActivityHistoryBinding.mSRHinput.setVisibility(View.GONE);
                                }
                                else
                                {


                                    mActivityHistoryBinding.mSRHinput.setVisibility(View.VISIBLE);
                                    IputTypeAdapter mIputTypeAdapter = new IputTypeAdapter(HistoryActivity.this,HistoryActivity.this,mIputTypeResponse,"");
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(HistoryActivity.this);
                                    mActivityHistoryBinding.srhinputtype.setLayoutManager(layoutManager);
                                    mActivityHistoryBinding.srhinputtype.setAdapter(mIputTypeAdapter);
                                }
                            }





//                        }
//                        catch (Exception e) {
//                            mLoadCustomToast(HistoryActivity.this,getResources().getString(R.string.accesstokenexpire));
//                        }

                    }
                }

                @Override
                public void onFailure(Call<HistoryResponse> call, Throwable t)
                {
                    obj_commonfunction.hideProgress();
                    mLoadCustomToast(HistoryActivity.this,getResources().getString(R.string.accesstokenexpire));
                }
            });
        }
        else
        {
            mLoadCustomToast(HistoryActivity.this,getResources().getString(R.string.tryAgain));
        }
    }


    private void OnBack()
    {
        obj_commonfunction.navigation(HistoryActivity.this,AddLocation.class);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OnBack();
    }

    public class IputTypeAdapter  extends RecyclerView.Adapter<IputTypeAdapter.ViewHolder> {

        private Context context;
        List<HistoryResponse.Input_datum> mIputTypeResponse;
        CommonFunction obj_commonfunction;
        private Activity mActicity;
        String  mNotificationDate;
        String mfrom;

        public IputTypeAdapter(Context context,Activity mActicity, List<HistoryResponse.Input_datum> mIputTypeResponse,String mfrom) {
            this.context = context;
            this.mActicity = mActicity;
            this.mIputTypeResponse = mIputTypeResponse;
            this.mfrom = mfrom;
            obj_commonfunction = new CommonFunction(mActicity);
        }

        @Override
        public IputTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            InputtypeadapterBinding mInputtypeadapterBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                    , R.layout.inputtypeadapter, parent, false);

            return new IputTypeAdapter.ViewHolder(mInputtypeadapterBinding);
        }

        @Override
        public void onBindViewHolder(IputTypeAdapter.ViewHolder holder, int position)
        {

            holder.mInputtypeadapterBinding.mChkinputs.setText(mIputTypeResponse.get(position).getInput_name());
            holder.mInputtypeadapterBinding.mChkinputs.setChecked(mIputTypeResponse.get(position).getStatus());



            holder.mInputtypeadapterBinding.mChkinputs.setEnabled(false);


        }



        @Override
        public int getItemCount() {
            return mIputTypeResponse.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {
            public InputtypeadapterBinding mInputtypeadapterBinding;


            public ViewHolder(InputtypeadapterBinding mInputtypeadapterBinding) {
                super(mInputtypeadapterBinding.getRoot());
                this.mInputtypeadapterBinding = mInputtypeadapterBinding;

            }
        }



    }
}
