package com.example.farmshop.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.adapter.PublicTabViewPagerAdapter;
import com.example.farmshop.fragment.MyselfFragment;
import com.example.farmshop.fragment.TodaySellFragment;
import com.example.farmshop.fragment.VegetableGardenFragment;
import com.example.farmshop.task.LoadFilesTask;
import com.example.farmshop.task.LoadFilesTask.OnGetFileListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CenterActivity extends AppCompatActivity implements OnGetFileListener {
    private TabLayout tlFile;
    private ViewPager vpFile;
    private MainApplication app;
    private String configUrl;
    private String savePath;
    private List<String> mTabTitle = new ArrayList<>();
    private List<Fragment> mFragment = new ArrayList<>();

    LoadFilesTask mloadconfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        tlFile = findViewById(R.id.tl_file);
        vpFile = findViewById(R.id.vp_file);
        app = MainApplication.getInstance();
        downLoadConfig();
        //for test 0606
        initData();
    }

    private void downLoadConfig(){
        configUrl = app.httpUrl + "pictureConfig.json";
        savePath = app.savePath + "pictureConfig.json";
        File file = new File(app.savePath);
        if(!file.exists()){
            Boolean flag = file.mkdirs();
        }

        //判断是否需要下载，并初始化数据
        mloadconfig = new LoadFilesTask();
        mloadconfig.setOnGetFileListener(this);
        mloadconfig.execute(configUrl, savePath, "json");
    }

    @Override
    public void onGetFile(String info){
//        Toast.makeText(this, "下载pictureConfig.json成功", Toast.LENGTH_SHORT).show();
        initData();
    }

    private void initData() {
        mTabTitle = new ArrayList<>();
        mFragment = new ArrayList<>();
        mTabTitle.add("garden");
        mTabTitle.add("todaySell");
        mTabTitle.add("me");

        VegetableGardenFragment vgtb = new VegetableGardenFragment();
        mFragment.add(vgtb);
        TodaySellFragment tdsl = new TodaySellFragment();
        MyselfFragment mysf = new MyselfFragment();
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
