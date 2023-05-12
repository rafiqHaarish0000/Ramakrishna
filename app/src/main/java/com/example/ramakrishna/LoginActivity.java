package com.example.ramakrishna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ramakrishna.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import RetrofitUtils.BaseApi;
import RetrofitUtils.Interface_Api;
import RetrofitUtils.Request.LoginRequest;
import RetrofitUtils.Response.LoginResponse;
import Utils.BaseActivity;
import Utils.SharedPrefsUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding mActivityLoginBinding;
    String GetDatabase,GetUserName,GetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);

//        obj_commonfunction.showEditTextAsMandatory(mActivityLoginBinding.mETDatabase);
//        obj_commonfunction.showEditTextAsMandatory(mActivityLoginBinding.mETUserName);
//        obj_commonfunction.showEditTextAsMandatory(mActivityLoginBinding.mETPwd);

        mActivityLoginBinding.mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetDatabase = mActivityLoginBinding.mETDatabase.getText().toString();
                GetUserName = mActivityLoginBinding.mETUserName.getText().toString();
                GetPwd = mActivityLoginBinding.mETPwd.getText().toString();

                if(validationUtils_obj.isEmptyEditText(GetDatabase) == false)
                {
                    mLoadCustomToast(LoginActivity.this,getResources().getString(R.string.logindbinvalid));
                }
                else if(validationUtils_obj.isEmptyEditText(GetDatabase) == false)
                {
                    mLoadCustomToast(LoginActivity.this,getResources().getString(R.string.loginusernameinvalid));
                }
                else  if(validationUtils_obj.isEmptyEditText(GetDatabase) == false)
                {
                    mLoadCustomToast(LoginActivity.this,getResources().getString(R.string.loginpwdinvalid));
                }
                else
                {
                    if(obj_commonfunction.isNetworkAvailable() == true)
                    {
                        LoginRequest mLoginRequest = new LoginRequest();
                        mLoginRequest.setLogin(GetUserName);
                        mLoginRequest.setDb(GetDatabase);
                        mLoginRequest.setPassword(GetPwd);

                        obj_commonfunction.showProgress();
                       // obj_commonfunction.show(LoginActivity.this,"","Please Wait");
                        Interface_Api apiInterface = BaseApi.getApiUrl(LoginActivity.this).create(Interface_Api.class);
                        Call<LoginResponse> apiCall = null;
                        apiCall = apiInterface.mLoginRequest(mLoginRequest);


                        apiCall.enqueue(new Callback<LoginResponse>() {
                            @Override
                            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
                            {
                                obj_commonfunction.hideProgress();
                                if(response.body() != null)
                                {
                                    try {
                                        SharedPrefsUtils.putInt(SharedPrefsUtils.PREF_KEY.UID,response.body().getResult().get(0).getUid());
                                        SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.ACCESS_TOKEN, response.body().getResult().get(0).getAccess_token());
                                        SharedPrefsUtils.putBoolean(SharedPrefsUtils.PREF_KEY.LOGIN_SESSION, true);

                                        Gson gson = new Gson();
                                        List<LoginResponse.Result> Senduserdetails = new ArrayList<LoginResponse.Result>();
                                        Senduserdetails.addAll(response.body().getResult());
                                        String Struserdetails = gson.toJson(Senduserdetails);
                                        SharedPrefsUtils.putString(SharedPrefsUtils.PREF_KEY.USER_DETAILS, Struserdetails);

                                      //  obj_commonfunction.navigation(LoginActivity.this, Dashboard.class); //IntroPage
                                              obj_commonfunction.navigation(LoginActivity.this, Dashboard.class); //IntroPage
                                        finish();
                                    } catch (Exception e) {
                                        mLoadCustomToast(LoginActivity.this,getResources().getString(R.string.loginpwderror));

                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<LoginResponse> call, Throwable t)
                            {
                                obj_commonfunction.hideProgress();
                                mLoadCustomToast(LoginActivity.this,getResources().getString(R.string.wentwrong));
                            }
                        });
                    }
                    else
                    {
                        mLoadCustomToast(LoginActivity.this,getResources().getString(R.string.tryAgain));
                    }
                }

            }
        });

    }
}