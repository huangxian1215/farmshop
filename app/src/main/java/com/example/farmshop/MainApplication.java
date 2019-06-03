package com.example.farmshop;

import android.app.Application;
import android.os.Environment;

import com.example.farmshop.thread.MessageTransmit;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static MainApplication mApp;
    public String mSessionId;
    public MessageTransmit mTransmit;
    public String Url = "http://192.168.6.179:8080/";
    public String savePath = "";

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
