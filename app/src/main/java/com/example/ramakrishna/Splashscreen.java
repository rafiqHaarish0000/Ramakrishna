package com.example.ramakrishna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import Utils.BaseActivity;
import Utils.PermissionUtils;
import Utils.SharedPrefsUtils;

public class Splashscreen extends BaseActivity {
    TextView mTextView;
    String mCopyrightsSymbol,mCopyrightsText,mCopyrightsYear;
    Calendar mGetcurrentyear = Calendar.getInstance();
    // Set SplashScreen time
    private static int SPLASH_TIME_OUT = 2000;
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1000;
    private static final int PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mTextView = (TextView)findViewById(R.id.mTextView);

        mCopyrightsSymbol = getResources().getString(R.string.copyrightssymbol);
        mCopyrightsText = getResources().getString(R.string.copyrights);
        mCopyrightsYear = String.valueOf(mGetcurrentyear.get(Calendar.YEAR));
        mTextView.setText(mCopyrightsSymbol+" "+mCopyrightsYear+" "+mCopyrightsText);


        // Check Android Run time permission
        if (!checkPermission())
        {

            new AlertDialog.Builder(this)
                    .setTitle("Allow Permission")
                    .setMessage("Please allow all permission to use uninterrupted Services.")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            requestPermission();
                        }
                    })
                    .show();
        }
        else
        {
           onloadNextPage();

        }


    }

    private void onloadNextPage()
    {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if(SharedPrefsUtils.getBoolean(SharedPrefsUtils.PREF_KEY.LOGIN_SESSION))
                {
                   // obj_commonfunction.navigation(Splashscreen.this, Dashboard.class); //IntroPage
                    obj_commonfunction.navigation(Splashscreen.this, Dashboard.class); //IntroPage
                    finish();
                }
                else
                {
                    obj_commonfunction.navigation(Splashscreen.this, LoginActivity.class); //IntroPage
                    finish();
                }



            }
        },SPLASH_TIME_OUT);
    }

    private boolean checkPermission()
    {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED  )
        {
            //  return true;
            return false;
        }
        else
        {
            // return false;
            return true;
        }
    }

    private void requestPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED )
        {
            //You can show permission rationale if shouldShowRequestPermissionRationale() returns true.
            //I will skip it for this demo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionUtils.neverAskAgainSelected(this, Manifest.permission.ACCESS_FINE_LOCATION) )
                {
                    displayNeverAskAgainDialog();
                } else
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.VIBRATE,Manifest.permission.ACCESS_NETWORK_STATE},
                            SEND_SMS_PERMISSION_REQUEST_CODE);

                }
            }

        }
        else
        {
            // onLoadNextPage();
            onloadNextPage();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // onLoadNextPage();
                    onloadNextPage();
                } else {
                    displayNeverAskAgainDialog();
                }
                break;
        }
    }

    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please allow all Permissions to use the app.Please permit the permission through Settings screen."
                + "\n\nSelect Permissions -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.show();
    }
}