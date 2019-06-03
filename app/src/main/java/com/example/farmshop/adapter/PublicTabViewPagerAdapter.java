package com.example.farmshop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class PublicTabViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;//fragment列表
    private List<String> mTitles;//tab名的列表

    public PublicTabViewPagerAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        super(fm);
        this.mFragments = list_fragment;
        this.mTitles = list_Title;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position % mTitles.size());
    }
}
