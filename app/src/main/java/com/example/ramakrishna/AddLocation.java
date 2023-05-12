package com.example.ramakrishna;

import static Utils.ConstantClass.Changeformat;
import static Utils.ConstantClass.Simpleformat;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ramakrishna.UI_Testing.activities.CustomTodo;
import com.example.ramakrishna.databinding.ActivityAddlocationBinding;
import com.example.ramakrishna.databinding.InputtypeadapterBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.BaseApi;
import RetrofitUtils.Interface_Api;
import RetrofitUtils.Request.LocationRequest;
import RetrofitUtils.Response.DashboardResponse;
import RetrofitUtils.Response.LocationResponse;
import Utils.BaseActivity;
import Utils.CommonFunction;
import Utils.SharedPrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLocation extends BaseActivity {

    ActivityAddlocationBinding mActivityAddlocationBinding;

    LocationManager locationManager;
    String latitude = "", longitude = "";

    List<DashboardResponse.Result> mLeadsResponse = new ArrayList<>();
    List<DashboardResponse.Input_datum> mIputTypeResponse = new ArrayList<>();
    List<LocationRequest.Input_datum> mLocationIputTypeResponse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityAddlocationBinding = DataBindingUtil.setContentView(this, R.layout.activity_addlocation);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }

        Gson gson = new Gson();
        String Str_Dashboardstore = SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.LEAD_DETAILS);
        mLeadsResponse = gson.fromJson(Str_Dashboardstore,new  TypeToken<ArrayList<DashboardResponse.Result>>(){}.getType());

        mActivityAddlocationBinding.mOpportunityName.setText(mLeadsResponse.get(0).getEnquiry_name());
        mActivityAddlocationBinding.mContactName.setText(mLeadsResponse.get(0).getContact_name());
        mActivityAddlocationBinding.mLocation.setText(mLeadsResponse.get(0).getLocation());
        mActivityAddlocationBinding.mPhone.setText(mLeadsResponse.get(0).getContact_mobile());
        mActivityAddlocationBinding.mNextActivity.setText(mLeadsResponse.get(0).getSummary());
        mActivityAddlocationBinding.mActivityType.setText(mLeadsResponse.get(0).getActivity_type());

        mIputTypeResponse = mLeadsResponse.get(0).getInput_data();


        if(mIputTypeResponse.size() == 0)
        {
            mActivityAddlocationBinding.mSRHinput.setVisibility(View.GONE);
        }
        else
        {

            for(int i=0;i<mIputTypeResponse.size();i++)
            {
                LocationRequest.Input_datum objinputtype = new LocationRequest.Input_datum();
                objinputtype.setInput_name(mIputTypeResponse.get(i).getInput_name());
                objinputtype.setInput_type(mIputTypeResponse.get(i).getInput_type());
                objinputtype.setInput_id(mIputTypeResponse.get(i).getInput_id());
                objinputtype.setStatus(mIputTypeResponse.get(i).getStatus());

                mLocationIputTypeResponse.add(objinputtype);
            }

            mActivityAddlocationBinding.mSRHinput.setVisibility(View.VISIBLE);
            IputTypeAdapter mIputTypeAdapter = new IputTypeAdapter(AddLocation.this,AddLocation.this,mIputTypeResponse,"");
            LinearLayoutManager layoutManager = new LinearLayoutManager(AddLocation.this);
            mActivityAddlocationBinding.srhinputtype.setLayoutManager(layoutManager);
            mActivityAddlocationBinding.srhinputtype.setAdapter(mIputTypeAdapter);
        }


        if(mLeadsResponse.get(0).getDue_date().contentEquals(""))
        {
            mActivityAddlocationBinding.mDuedate.setText("");
        }
        else {
           // mActivityAddlocationBinding.mDuedate.setText("My Deadline  " + CommonFunction.formatDate(SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.KEY_DUEDATE), Simpleformat, Changeformat));
            mActivityAddlocationBinding.mDuedate.setText(CommonFunction.formatDate(mLeadsResponse.get(0).getDue_date(), Simpleformat, Changeformat));
        }

        mActivityAddlocationBinding.mgetlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(latitude.length() == 0)
                {
                    new AlertDialog.Builder(AddLocation.this)
                            .setMessage("Location Permission denied")
                            .setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
                else
                {
                    if(obj_commonfunction.isNetworkAvailable() == true)
                    {
                        obj_commonfunction.show(AddLocation.this,"","Please Wait");

                        LocationRequest objLocationRequest = new LocationRequest();
                        objLocationRequest.setUid( SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
                        objLocationRequest.setAccess_token( SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
                        objLocationRequest.setUser_activity_id( mLeadsResponse.get(0).getUser_activity_id());
                        objLocationRequest.setLatitude(Double.valueOf(latitude));
                        objLocationRequest.setLongitude(Double.valueOf(longitude));


                        Interface_Api apiInterface = BaseApi.getApiUrl(AddLocation.this).create(Interface_Api.class);
                        Call<LocationResponse> apiCall = null;
                        apiCall = apiInterface.mAddlocation(objLocationRequest);
                        apiCall.enqueue(new Callback<LocationResponse>()
                        {
                            @Override
                            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response)
                            {
                                obj_commonfunction.dismiss();
                                if(response.body() != null)
                                {
                                    try
                                    {
                                        if(response.body().getResult().getSuccess())
                                        {
                                            mLoadCustomToast(AddLocation.this,response.body().getResult().getMessage());
                                            mActivityAddlocationBinding.mdonemyjob.setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            mLoadCustomToast(AddLocation.this,response.body().getResult().getMessage());
                                            mActivityAddlocationBinding.mdonemyjob.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                    catch (Exception e) {
                                        mLoadCustomToast(AddLocation.this,getResources().getString(R.string.accesstokenexpire));
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<LocationResponse> call, Throwable t)
                            {
                                obj_commonfunction.dismiss();
                                mLoadCustomToast(AddLocation.this,getResources().getString(R.string.accesstokenexpire));
                            }
                        });
                    }
                    else
                    {
                        mLoadCustomToast(AddLocation.this,getResources().getString(R.string.tryAgain));
                    }
                }
            }
        });




        mActivityAddlocationBinding.mHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                obj_commonfunction.navigation(AddLocation.this, HistoryActivity.class); //IntroPage
                finish();


            }
        });

        mActivityAddlocationBinding.mdonemyjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(mIputTypeResponse);
                System.out.println(mIputTypeResponse);

                String mremarks = mActivityAddlocationBinding.mremarks.getText().toString();

                if(obj_commonfunction.isNetworkAvailable() == true)
                {
                    obj_commonfunction.show(AddLocation.this,"","Please Wait");

                    LocationRequest objLocationRequest = new LocationRequest();
                    objLocationRequest.setUid( SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.UID));
                    objLocationRequest.setAccess_token( SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN));
                    objLocationRequest.setUser_activity_id(mLeadsResponse.get(0).getUser_activity_id());
                    objLocationRequest.setInput_data(mLocationIputTypeResponse);
                    objLocationRequest.setRemarks(mremarks);

                    Interface_Api apiInterface = BaseApi.getApiUrl(AddLocation.this).create(Interface_Api.class);
                    Call<LocationResponse> apiCall = null;
                    apiCall = apiInterface.mMarkasdone(objLocationRequest);
                    apiCall.enqueue(new Callback<LocationResponse>()
                    {
                        @Override
                        public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response)
                        {
                            obj_commonfunction.dismiss();
                            if(response.body() != null)
                            {
                                try
                                {
                                    if(response.body().getResult().getSuccess())
                                    {
                                        mLoadCustomToast(AddLocation.this,response.body().getResult().getMessage());
                                        obj_commonfunction.navigation(AddLocation.this, Dashboard.class); //IntroPage
                                        finish();
                                    }
                                    else
                                    {
                                        mLoadCustomToast(AddLocation.this,response.body().getResult().getMessage());

                                    }
                                }
                                catch (Exception e) {
                                    mLoadCustomToast(AddLocation.this,getResources().getString(R.string.accesstokenexpire));
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<LocationResponse> call, Throwable t)
                        {
                            obj_commonfunction.dismiss();
                            mLoadCustomToast(AddLocation.this,getResources().getString(R.string.accesstokenexpire));
                        }
                    });
                }
                else
                {
                    mLoadCustomToast(AddLocation.this,getResources().getString(R.string.tryAgain));
                }

            }
        });

        mActivityAddlocationBinding.mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBack();
            }
        });

    }

    private void OnBack()
    {
        if(SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.MFROM) == 1)
        {
            obj_commonfunction.navigation(AddLocation.this, Dashboard.class); //IntroPage
            finish();
        }
        else if(SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.MFROM) == 2)
        {
            obj_commonfunction.navigation(AddLocation.this, CustomTodo.class); //IntroPage
            finish();
        }
        else if(SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.MFROM) == 3)
        {
            obj_commonfunction.navigation(AddLocation.this, CalendarActivity.class); //IntroPage
            finish();
        }
        else
        {
            obj_commonfunction.navigation(AddLocation.this, Dashboard.class); //IntroPage
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OnBack();
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
               // showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);

               // Toast.makeText(this, "Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude, Toast.LENGTH_SHORT).show();
            } else {
               // Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                mLoadCustomToast(AddLocation.this,"Unable to find location.");
            }

    }



    public class IputTypeAdapter  extends RecyclerView.Adapter<IputTypeAdapter.ViewHolder> {

        private Context context;
        List<DashboardResponse.Input_datum> mIputTypeResponse;
        CommonFunction obj_commonfunction;
        private Activity mActicity;
        String  mNotificationDate;
        String mfrom;

        public IputTypeAdapter(Context context,Activity mActicity, List<DashboardResponse.Input_datum> mIputTypeResponse,String mfrom) {
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



            holder.mInputtypeadapterBinding.mChkinputs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.mInputtypeadapterBinding.mChkinputs.isChecked())
                    {
                        mIputTypeResponse.get(position).setStatus(true);
                        mLocationIputTypeResponse.get(position).setStatus(true);
                    }
                    else
                    {
                        mIputTypeResponse.get(position).setStatus(false);
                        mLocationIputTypeResponse.get(position).setStatus(false);
                    }
                }

            });


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