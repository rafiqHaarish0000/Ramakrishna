package com.example.ramakrishna.UI_Testing.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ramakrishna.Dashboard;
import com.example.ramakrishna.FeatureFragment;
import com.example.ramakrishna.HomeActivity;
import com.example.ramakrishna.HomePagerAdapter;
import com.example.ramakrishna.OverDueFragment;
import com.example.ramakrishna.R;
import com.example.ramakrishna.TodayFragment;
import com.example.ramakrishna.UI_Testing.activities.fragments.adapters.TabFragmentAdapter;
import com.example.ramakrishna.databinding.ActivityCustomTodoBinding;
import com.google.android.material.tabs.TabLayout;

import Utils.BaseActivity;
import Utils.CommonFunction;
import Utils.SharedPrefsUtils;

public class CustomTodo extends AppCompatActivity {
    TabLayout mTabs;
    View mIndicator;
    ViewPager mViewPager;
    LinearLayout mBackbutton;
    private int indicatorWidth;
    public CommonFunction obj_commonfunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_todo);
        mTabs = findViewById(R.id.tab);
        mIndicator = findViewById(R.id.indicator);
        mViewPager = findViewById(R.id.viewPager);
        mBackbutton = (LinearLayout) findViewById(R.id.mBack);


        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(OverDueFragment.newInstance(), "Pending(" + SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.KEY_OVERDUE) + ")");
        adapter.addFragment(TodayFragment.newInstance(), "Today(" + SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.KEY_TODAY) + ")");
        adapter.addFragment(FeatureFragment.newInstance(), "Planned(" + SharedPrefsUtils.getString(SharedPrefsUtils.PREF_KEY.KEY_FEATURE) + ")");
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(SharedPrefsUtils.getInt(SharedPrefsUtils.PREF_KEY.VIEWPAGERCURRENTPOSTION));
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTabs.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));


        mBackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });


        mTabs.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = mTabs.getWidth() / mTabs.getTabCount();

                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void onBack() {
//        obj_commonfunction.navigation(CustomTodo.this, Dashboard.class);
        startActivity(new Intent(CustomTodo.this, Dashboard.class));
        finish();

    }

}