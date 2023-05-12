package Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.example.ramakrishna.R;
import com.example.ramakrishna.Splashscreen;
import com.example.ramakrishna.databinding.ActivityNointernetBinding;

public class ActivityNoInternet extends BaseActivity {

    ActivityNointernetBinding mActivityNointernetbinding;
    boolean mDoubleBackToExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityNointernetbinding =  DataBindingUtil.setContentView(this, R.layout.activity_nointernet);


        mActivityNointernetbinding.mLinearLayoutTryagein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(obj_commonfunction.isNetworkAvailable() == true)
                {

                  /* if(SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.NOINTERNETACTIVTYCLASS).contentEquals("Dashboard"))
                   {*/
                       obj_commonfunction.navigation(ActivityNoInternet.this, Splashscreen.class);
                       finish();
                //   }



                }
                else
                {

                    mLoadCustomToast(getResources().getString(R.string.tryAgain));
                }

            }
        });

    }



    private void mLoadCustomToast(String message)
    {
        CustomToast.makeText(ActivityNoInternet.this, message, CustomToast.LENGTH_SHORT, 0).show();
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return isConnected;
    }

    @Override
    public void onBackPressed() {
        if (mDoubleBackToExit) {
            super.onBackPressed();
            return;
        }

        this.mDoubleBackToExit = true;
        CustomToast.makeText(ActivityNoInternet.this, "Press BACK again to exit", CustomToast.LENGTH_SHORT, 0).show();
        new Handler().postDelayed(() -> mDoubleBackToExit = false, 2000);
    }
}
