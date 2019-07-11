package com.example.farmshop.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.adapter.PublicTabViewPagerAdapter;
import com.example.farmshop.farmshop;
import com.example.farmshop.fragment.CommunityFragment;
import com.example.farmshop.fragment.MyselfFragment;
import com.example.farmshop.fragment.TodaySellFragment;
import com.example.farmshop.fragment.VegetableGardenFragment;
import com.example.farmshop.task.LoadFilesTask;
import com.example.farmshop.task.LoadFilesTask.onGetFileListener;
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CenterActivity extends AppCompatActivity implements onGetFileListener, onGetNetDataListener {
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
//        app.mTransmit.setOnNetListener(this);
        app.mTransmit.addOnNetListener("CenterAcitivity", this);
        //检查新同学去填写详细资料
        checkNewUser();
        downLoadConfigs();
    }

    private void checkNewUser(){
        if(!app.getLocalStore("is_new_user").equals("no")){
            app.setLocalStore("is_new_user", "no");
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

    @Override
    public void onGetNetData(Object info, farmshop.MsgId msgid){

    }

    private void initData() {
        mTabTitle = new ArrayList<>();
        mFragment = new ArrayList<>();
        mTabTitle.add("菜园");
        mTabTitle.add("活动");
        mTabTitle.add("社区");
        mTabTitle.add("我");

        VegetableGardenFragment vgtb = new VegetableGardenFragment();
        mFragment.add(vgtb);
        TodaySellFragment tdsl = new TodaySellFragment();
        MyselfFragment mysf = new MyselfFragment();
        CommunityFragment cmnt = new CommunityFragment();
        mFragment.add(tdsl);
        mFragment.add(cmnt);
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

    private void sendSimpleNotify(String title, String message, String id, String name) {
        //android 8.0 的坑，使用NotificationChannel

        Intent clickIntent = new Intent(this, LoginActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                R.string.app_name, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setContentIntent(contentIntent)
                    .setChannelId(id)
                    .setContentTitle("5 new messages")
                    .setContentText("hahaha")
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentIntent(contentIntent)
                    .setContentTitle("5 new messages")
                    .setContentText("hahaha")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(111123, notification);
    }
}
