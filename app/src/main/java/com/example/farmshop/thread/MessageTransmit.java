package com.example.farmshop.thread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.example.farmshop.activity.LoginActivity;
import com.example.farmshop.MainApplication;
import com.example.farmshop.activity.UserDetailEditActivity;
import com.example.farmshop.bean.ByteData;
import com.example.farmshop.activity.RegistActivity;
import com.example.farmshop.farmshop;
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;
import com.google.protobuf.Any;

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

    private byte[]  mBytearray;
    private byte[]  mBuffer;
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
            mBytearray = bytearray;
                long dataLent = bytearray.length + 0;
                byte[] buffer1 = new byte[8];

                for (int i = 0; i < 8; i++) {
                    int offset = 64 - (i + 1) * 8;
                    buffer1[i] = (byte) ((dataLent >> offset) & 0xff);
                }
                mBuffer = buffer1;
                //网络请求需要开线程, new Thread() 不用手动回收
                new Thread(runnable).start();
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                mWriter.write(mBuffer);
                mWriter.write(mBytearray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    // 二进制流接受线程
    private class RecvBytThread extends  Thread{
        @Override
        public void run(){
            long dataSize = 0;
            long getDataSize = 0;
            try {
                while(mInputStream != null){
                    //读取数据头大小(自定义8位),应该使用mInputStream.available()来判断更方便，后续优化
                    if(dataSize == 0){
                        byte[] buffer = new byte[8];
                        int count = mInputStream.read(buffer);
                        for (int i = 0; i < 8; i++) {
                            int offset = 64 - (i + 1) * 8;
                            dataSize += (long) ((buffer[i] << offset) & 0xff);
                        }
                    }
                    //根据头，读取指定大小数据(防止粘包)
                    byte[] databufferfinal = new byte[(int)dataSize];
                    byte[] databuffer = new byte[(int)dataSize];
                    getDataSize = mInputStream.read(databuffer);
                    for(int i = 0; i < dataSize; i++){
                        databufferfinal[i] = databuffer[i];
                    }
                    //数据读取不足时拼接(接收包不足时拆包错误)
                    int getLestSize = 0;
                    while (getDataSize < dataSize){
                        int lest = (int)dataSize - (int)getDataSize;
                        byte[] lestdatabuffer = new byte[(int)lest];
                        getLestSize = mInputStream.read(lestdatabuffer);
                        for(int i = 0; i < getLestSize; i++){
                            databufferfinal[i + (int)getDataSize] = lestdatabuffer[i];
                        }
                        getDataSize += getLestSize;
                    }
                    //拆包，初始化数据头
                    if(getDataSize >= dataSize){
                        getDataSize -= dataSize;
                        dataSize = 0;
                        farmshop.baseType base;
                        try{
                            base = farmshop.baseType.parseFrom(databufferfinal);
                            dealDataFromServer(base);
                        }catch (IOException e){
                            e.printStackTrace();
                        }

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
                try {
                    Any any = data.getObject(0);
                    farmshop.LoginResponse resp = farmshop.LoginResponse.parseFrom(any.getValue());
                    if (resp.getResult() == 0) {
                        dealNetDataToType("LoginAcityty","LoginAcityty" );
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            case EditUserInfo_RES:
                msg.obj = data;
                UserDetailEditActivity.mHandler.sendMessage(msg);
                break;
            default:
                break;
        }
    }

    private Map<String, onGetNetDataListener> mapListener = new HashMap<>();

    //添加监听, 不再使用时关闭监听
    public void addOnNetListener(String listenerName, onGetNetDataListener listener){
        mapListener.put(listenerName, listener);
    }

    public void deleteOnNetListener(String listenerName){
        mapListener.remove(listenerName);
    }

    public void dealNetDataToType(String type, String info){
        mapListener.get(type).onGetNetData(info);
    }

}

