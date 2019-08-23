/*
* 单例模式
* 存放全局变量，部分通用方法
* */
package com.example.farmshop;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Environment;

import com.example.farmshop.bean.BuyBuyBuyList;
import com.example.farmshop.bean.UserInfo;
import com.example.farmshop.music.bean.MusicInfo;
import com.example.farmshop.music.bean.qqMusicInfo;
import com.example.farmshop.thread.MessageTransmit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static MainApplication mApp;
    public String mSessionId;
    public UserInfo mUserinfo;
    public MessageTransmit mTransmit;
    public String httpUrl = "http://192.168.6.176:8080/";
    public String Url = "http://192.168.6.176:8010/";
    public static int upFilePort = 8020;
    public String savePath = "";
    public String selectFileUrl = "";

    public ArrayList<BuyBuyBuyList> mBasketList = new ArrayList<>();
    public static MainApplication getInstance() {
        return mApp;
    }

    //qq music
    public MediaPlayer mMediaPlayer;
    public String mSong;
    public String mFilePath;
    public String vkey;
    public int downtype = 0;
    public String qqsongid;
    public ArrayList<qqMusicInfo> mqqMusicInfo;
    public MusicInfo mMusic;
    public int mMode = 1;
    public String QQmusicUrl = "http://183.131.60.16/amobile.music.tc.qq.com/";

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mSessionId = "";
        mTransmit = new MessageTransmit();
        savePath = Environment.getExternalStorageDirectory()+"/farmshop/";
        mMediaPlayer = new MediaPlayer();
    }

    //先只支持返回string
    public String getLocalStore(String key){
        SharedPreferences shareinfo = getSharedPreferences("farmshop", MODE_PRIVATE);
        SharedPreferences.Editor editor = shareinfo.edit();
        return shareinfo.getString(key, "");
    }
    //先只支持string
    public void setLocalStore(String key, String value){
        SharedPreferences shareinfo = getSharedPreferences("farmshop", MODE_PRIVATE);
        SharedPreferences.Editor editor = shareinfo.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
