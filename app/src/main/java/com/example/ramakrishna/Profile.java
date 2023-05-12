package com.example.ramakrishna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.example.ramakrishna.databinding.ActivityProfileBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.Response.LoginResponse;
import Utils.BaseActivity;
import Utils.SharedPrefsUtils;

public class Profile extends BaseActivity {

    ActivityProfileBinding mActivityProfileBinding;

    List<LoginResponse.Result> objUserDetails = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mActivityProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_profile);

        Gson gson = new Gson();
        String Str_Dashboardstore = SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.USER_DETAILS);
        objUserDetails = gson.fromJson(Str_Dashboardstore,new  TypeToken<ArrayList<LoginResponse.Result>>(){}.getType());

        mActivityProfileBinding.mUserName.setText(objUserDetails.get(0).getEmployee_name());
        mActivityProfileBinding.mjobposition.setText(objUserDetails.get(0).getJob_position());
        mActivityProfileBinding.mdepartment.setText(objUserDetails.get(0).getDepartment());
        mActivityProfileBinding.mworkemail.setText(objUserDetails.get(0).getWork_email());
        mActivityProfileBinding.mmobile.setText(objUserDetails.get(0).getMobile());

        mActivityProfileBinding.mBack.setOnClickListener(v -> {
            OnBack();
        });

        if(objUserDetails.get(0).getEmployee_image().isEmpty())
        {

        }
        else
        {
            byte[] imageAsBytes = Base64.decode(objUserDetails.get(0).getEmployee_image().getBytes(), Base64.DEFAULT);
            mActivityProfileBinding.mUserImage.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
    }

    private void OnBack()
    {
        obj_commonfunction.navigation(Profile.this,Dashboard.class);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OnBack();
    }
}