package com.example.farmshop.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.farmshop.R;
import com.example.farmshop.adapter.PublicTabViewPagerAdapter;
import com.example.farmshop.fragment.VegetableGardenFragment;

import java.util.ArrayList;
import java.util.List;

public class CenterActivity extends AppCompatActivity {
    private TabLayout tlFile;
    private ViewPager vpFile;

    private List<String> mTabTitle = new ArrayList<>();
    private List<Fragment> mFragment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        tlFile = findViewById(R.id.tl_file);
        vpFile = findViewById(R.id.vp_file);

        initData();
    }

    private void initData() {
        mTabTitle = new ArrayList<>();
        mFragment = new ArrayList<>();
        mTabTitle.add("菜园");
        mTabTitle.add("今日推荐");
        mTabTitle.add("我");

        VegetableGardenFragment vgtb = new VegetableGardenFragment();
        VegetableGardenFragment tdsl = new VegetableGardenFragment();
        VegetableGardenFragment mysf = new VegetableGardenFragment();
        mFragment.add(vgtb);
        mFragment.add(tdsl);
        mFragment.add(mysf);

        FragmentManager fragmentManager = getSupportFragmentManager();
        PublicTabViewPagerAdapter tabViewPagerAdapter = new PublicTabViewPagerAdapter(fragmentManager, mFragment, mTabTitle);
        vpFile.setAdapter(tabViewPagerAdapter);
        tlFile.setupWithViewPager(vpFile);

        tlFile.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpFile.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
