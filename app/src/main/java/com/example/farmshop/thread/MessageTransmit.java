package com.example.farmshop.thread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.example.farmshop.activity.LoginActivity;
import com.example.farmshop.MainApplication;
import com.example.farmshop.bean.ByteData;
import com.example.farmshop.activity.RegistActivity;
import com.example.farmshop.farmshop;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MessageTransmit implements Runnable {
    private static final String TAG = "MessageTransmit";
    // 以下为Socket服务器的ip和端口，根据实际情况修改
    private  String SOCKET_IP = "";
    private  int SOCKET_PORT = 0;

    private Socket mSocket;
    //缓冲字符流用于字符串
    private BufferedReader mReader = null;
    private InputStream mInputStream = null;
    private OutputStream mWriter = null;
    //sessionId
    private String sessionId = "";
    private MainApplication app;
    public void setIpPort(String str, int port){
        SOCKET_IP = str;
        SOCKET_PORT = port;

        app = MainApplication.getInstance();
    }

    @Override
    public void run() {
        mSocket = new Socket();
        try {
            mSocket.connect(new InetSocketAddress(SOCKET_IP, SOCKET_PORT), 3000);
            mInputStream = mSocket.getInputStream();
            mWriter = mSocket.getOutputStream();
            // 启动一条子线程来读取服务器的返回数据
            new RecvBytThread().start();
            Looper.prepare();
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 定义接收UI线程的Handler对象，App向后台服务器发送消息
    public Handler mRecvHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] bytearray = ((ByteData)msg.obj).data;
            try{
                mWriter.write(bytearray);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    };

    // 二进制流接受线程
    private class RecvBytThread extends  Thread{
        @Override
        public void run(){
            try {
                while(mInputStream != null){
                    byte len[] = new byte[1024];
                    int count = mInputStream.read(len);
                    byte[] temp = new byte[count];
                    for(int i = 0; i < count; i++){
                        temp[i] = len[i];
                    }
                    farmshop.baseType base;
                    try{
                        base = farmshop.baseType.parseFrom(temp);
                        dealDataFromServer(base);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //处理消息
    public void dealDataFromServer(farmshop.baseType data){
        farmshop.MsgId type = data.getType();
        Log.d("MessageTtrnsmit: ", "getserver data" + String.valueOf(type));
        Message msg = Message.obtain();
        switch (type){
            case CONNECT_RES:
                sessionId = data.getSessionId();
                app.mSessionId = sessionId;
                msg.obj = data;
                LoginActivity.mHandler.sendMessage(msg);
                break;
            case REGIST_RES:
                msg.obj = data;
                RegistActivity.mHandler.sendMessage(msg);
                break;
            case LOGIN_RES:
                msg.obj = data;
                LoginActivity.mHandler.sendMessage(msg);
                mListener.onGetNetData("login");
                break;
            default:
                break;
        }
    }

    private OnGetNetDataListener mListener;
    public void setOnNetListener(OnGetNetDataListener listener){
        mListener = listener;
    }
    public static interface OnGetNetDataListener{
        public abstract void onGetNetData(String info);
    }

}

