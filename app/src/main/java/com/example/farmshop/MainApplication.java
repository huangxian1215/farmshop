package com.example.farmshop;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.example.farmshop.activity.LoginActivity;
import com.example.farmshop.bean.UserInfo;
import com.example.farmshop.thread.MessageTransmit;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static MainApplication mApp;
    public String mSessionId;
    public UserInfo mUserinfo;
    public MessageTransmit mTransmit;
    public String httpUrl = "http://192.168.6.176:8080/";
    public String Url = "http://192.168.6.176:8010/";
    public String upFileUrl = "http://192.168.6.176:8020/";
    public String savePath = "";

    public String selectFileUrl = "";
    public static MainApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mSessionId = "";
        mTransmit = new MessageTransmit();
        savePath = Environment.getExternalStorageDirectory()+"/farmshop/";
    }

}
