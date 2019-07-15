package com.example.farmshop.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;
import com.google.protobuf.Any;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.farmshop.farmshop.OrderState.ORDER_RECIEVE;

public class CenterActivity extends AppCompatActivity implements onGetNetDataListener {
    private TabLayout tlFile;
    private ViewPager vpFile;
    private MainApplication app;
    private String configUrl;
    private String savePath;
    private List<String> mTabTitle = new ArrayList<>();
    private List<Fragment> mFragment = new ArrayList<>();
    private Boolean isDown = false;

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
            downLoadConfig(configUrl, savePath);
        }
    }

    @Override
    public void onGetNetData(Object info, farmshop.MsgId msgid){
        farmshop.baseType data = (farmshop.baseType) info;
        try{
            Any any = data.getObject(0);
            farmshop.ReceiveOrderEvent recvdata = farmshop.ReceiveOrderEvent.parseFrom(any.getValue());
            int id = recvdata.getId();
            if(recvdata.getState() == ORDER_RECIEVE){
                sendSimpleNotify("您的订单已接收","订单号"+String.valueOf(id),"1","getorder");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
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

        Intent clickIntent = new Intent(this, QueryOrdersActivity.class);
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
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentIntent(contentIntent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(111123, notification);
    }

    public void downLoadConfig(String loadurl, final String saveurl){
        new AsyncTask<String, Integer, String>(){
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL link = new URL(params[0]);
                    HttpURLConnection con = (HttpURLConnection) link.openConnection();
                    int code = con.getResponseCode();
                    if (code == 200) {
                        //获取下载总大小
                        int len = con.getContentLength();
                        RandomAccessFile rf = new RandomAccessFile(params[1], "rw");
                        rf.setLength(len);
                        byte[] buf = new byte[1024];
                        //当次读取的数量
                        int num;
                        //当前下载的量
                        int count = 0;
                        InputStream in = con.getInputStream();
                        while ((num = in.read(buf)) != -1) {
                            rf.write(buf, 0, num);
                            count += num;
                        }
                        rf.close();
                        in.close();
                    }
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                if(!isDown) {
                    initData();
                    isDown = true;
                }
            }
        }.execute(loadurl, saveurl);
    }
}


