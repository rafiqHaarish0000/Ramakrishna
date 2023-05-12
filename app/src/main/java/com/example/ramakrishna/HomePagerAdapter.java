package com.example.ramakrishna;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {
    int tabCount;
    String[] tabArray;
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public HomePagerAdapter(FragmentManager fm, int numberOfTabs, String[] tabArray) {
        super(fm);
        this.tabCount = numberOfTabs;
        this.tabArray = tabArray;
    }

    @Override
    public Fragment getItem(int position) {


        return mFragmentList.get(position);


    }

    public void addFrag(Fragment fragment) {
        mFragmentList.add(fragment);


    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabArray[position];
    }

    @Override
    public int getCount() {

        return tabArray.length;
    }


}