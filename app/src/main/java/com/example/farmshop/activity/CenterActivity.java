package com.example.farmshop.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.example.farmshop.util.MyUtil;

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
    private Boolean isDown = false;
    LoadFilesTask mloadconfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_center);
        tlFile = findViewById(R.id.tl_file);
        vpFile = findViewById(R.id.vp_file);
        app = MainApplication.getInstance();
        //检查新同学去填写详细资料
        checkNewUser();

        downLoadConfigs();
        //for test 0606
//        initData();
    }

    private void checkNewUser(){
        SharedPreferences shareinfo = getSharedPreferences("farmshop", MODE_PRIVATE);
        SharedPreferences.Editor editor = shareinfo.edit();
        Boolean flag = shareinfo.getBoolean("is_new_user", true);
        if(flag){
            editor.putBoolean("is_new_user", false);
            editor.commit();
            Intent intent = new Intent(this, UserDetailEditActivity.class);
            startActivity(intent);
        }
    }

    private void downLoadConfigs(){
        File file = new File(app.savePath);
        if(!file.exists()){
            Boolean flag = file.mkdirs();
        }
        String[] configFile = {"vegetableConfig.json", "pictureConfig.json"};
        for(int i = 0; i < configFile.length; i++){
            configUrl = app.httpUrl + configFile[i];
            savePath = app.savePath + configFile[i];
            mloadconfig = new LoadFilesTask();
            mloadconfig.setOnGetFileListener(this);
            mloadconfig.execute(configUrl, savePath, "json");
        }
    }
    //下载好一个就先加载
    @Override
    public void onGetFile(String info){
        if(!isDown) {
            initData();
            isDown = true;
        }
    }

    private void initData() {
        mTabTitle = new ArrayList<>();
        mFragment = new ArrayList<>();
        mTabTitle.add("菜园");
        mTabTitle.add("活动");
        mTabTitle.add("我");

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
