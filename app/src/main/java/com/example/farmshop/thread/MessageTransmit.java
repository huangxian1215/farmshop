package com.example.farmshop.thread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.farmshop.MainApplication;
import com.example.farmshop.bean.ByteData;
import com.example.farmshop.farmshop;
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;



public class MessageTransmit implements Runnable {
    private static final String TAG = "MessageTransmit";
    // 以下为Socket服务器的ip和端口，根据实际情况修改
    private  String SOCKET_IP = "";
    private  int SOCKET_PORT = 0;
    //缓冲字符流用于字符串
    private BufferedReader mReader = null;
    private InputStream mInputStream = null;
    private OutputStream mWriter = null;
    private MainApplication app;

    //数据写状态
    private Boolean isWritting = false;
    //数据列表
    private ArrayList<SendDataStruct> sendDataList = new ArrayList<>();
    private Map<farmshop.MsgId,String> mapDealData = new HashMap<>();
    private Map<String, onGetNetDataListener> mapListener = new HashMap<>();

    public MessageTransmit(){
        mapDealData.put(farmshop.MsgId.CONNECT_RES, "LoginActivity");
        mapDealData.put(farmshop.MsgId.REGIST_RES, "RegistAcityty");
        mapDealData.put(farmshop.MsgId.LOGIN_RES, "LoginActivity");
        mapDealData.put(farmshop.MsgId.EditUserInfo_RES, "UserDetailEditActivity");
        mapDealData.put(farmshop.MsgId.SEND_MESSAGE_RES, "CommunityFragment");
        mapDealData.put(farmshop.MsgId.UPORDER_RES, "BasketActivity");
        mapDealData.put(farmshop.MsgId.QUERYORDER_RES, "QueryOrdersActivity");
        mapDealData.put(farmshop.MsgId.DELETEORDER_RES, "QueryOrdersActivity");
        mapDealData.put(farmshop.MsgId.RECEIVEORDER_EVENT, "CenterAcitivity");
    }
    public void setIpPort(String str, int port){
        SOCKET_IP = str;
        SOCKET_PORT = port;
        app = MainApplication.getInstance();
    }

    @Override
    public void run() {
        Socket mSocket = new Socket();
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
            long dataLent = bytearray.length + 0;
            byte[] head = new byte[8];
            for (int i = 0; i < 8; i++) {
                int offset = 64 - (i + 1) * 8;
                head[i] = (byte) ((dataLent >> offset) & 0xff);
            }

            sendDataList.add(new SendDataStruct(head, bytearray));
            //网络请求需要开线程, new Thread() 不用手动回收
            new Thread(rb_sendtoserver).start();
        }
    };

    private Runnable rb_sendtoserver = new Runnable() {
        @Override
        public void run() {
            while (sendDataList.size() > 0 && !isWritting){
                byte[] t_head = sendDataList.get(0).head;
                byte[] t_body = sendDataList.get(0).body;
                if( !isWritting){
                    isWritting = true;
                    try {
                        //写数据大小头
                        mWriter.write(t_head);
                        int i = 0;
                        //写数据
                        while (i < t_body.length){
                            int lest = t_body.length - i;
                            if(lest < 512){
                                mWriter.write(t_body, i,lest);
                                i += lest;
                            }else{
                                mWriter.write(t_body, i,512);
                                i += 512;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sendDataList.remove(0);
                    isWritting = false;
                }
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
                        mInputStream.read(buffer);
                        ByteBuffer btbf = ByteBuffer.allocate(8);
                        btbf.put(buffer, 0, buffer.length);
                        btbf.flip();
                        dataSize = btbf.getLong();
                    }
                    //根据头，读取指定大小数据(防止粘包)
                    byte[] databufferfinal = new byte[(int)dataSize];
                    byte[] databuffer = new byte[(int)dataSize];
                    getDataSize = mInputStream.read(databuffer);
                    System.arraycopy(databuffer,0,databufferfinal,0,(int)dataSize);
                    //数据读取不足时拼接(接收包不足时拆包错误)
                    int getLestSize = 0;
                    while (getDataSize < dataSize){
                        int lest = (int)dataSize - (int)getDataSize;
                        byte[] lestdatabuffer = new byte[(int)lest];
                        getLestSize = mInputStream.read(lestdatabuffer);
                        System.arraycopy(lestdatabuffer,0,databufferfinal,(int)getDataSize,getLestSize);
                        getDataSize += getLestSize;
                    }
                    //拆包，初始化数据头
                    if(getDataSize >= dataSize){
                        getDataSize -= dataSize;
                        dataSize = 0;
                        farmshop.baseType base;
                        try{
                            base = farmshop.baseType.parseFrom(databufferfinal);
                            app.mSessionId = base.getSessionId();
                            //处理消息
                            dealNetDataToType(mapDealData.get(base.getType()), base, base.getType());
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

    //添加监听, 不再使用时关闭监听
    public void addOnNetListener(String listenerName, onGetNetDataListener listener){
        mapListener.put(listenerName, listener);
    }

    public void deleteOnNetListener(String listenerName){
        mapListener.remove(listenerName);
    }

    private void dealNetDataToType(String which, Object info, farmshop.MsgId type){
        mapListener.get(which).onGetNetData(info, type);
    }

    public class SendDataStruct{
        byte[] head;
        byte[] body;
        private SendDataStruct(byte[] headdata, byte[] bodydata){
            head = headdata;
            body = bodydata;
        }
    }
}

