package com.example.farmshop.upfiles.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpSendData implements Runnable {
    private  String SOCKET_IP = "";
    private  int SOCKET_PORT = 0;

    public Socket socket = null;
    public OutputStream out = null;
    public InputStream in = null;
    private InputStream mInputStream = null;
    private File mFile;
    private String mType;

    public void setIpPort(String str, int port){
        SOCKET_IP = str;
        SOCKET_PORT = port;
    }

    public void setInfo(File file, String type){
        mFile = file;
        mType = type;
    }

    @Override
    public void run() {
        //进行访问网络操作
        socket = new Socket();
        try{
            socket.connect(new InetSocketAddress(SOCKET_IP, SOCKET_PORT), 3000);
            out = socket.getOutputStream();
            mInputStream = socket.getInputStream();

            new RecvBytThread().start();
            Looper.prepare();
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                    //回应表示接收
                    Message msg = Message.obtain();
//                    msg.obj = mFile;
                    mSendHandler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Handler mSendHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            mFile = (File)msg.obj;
            if (mFile.exists() && mFile.isFile()) {
                try {
                    /**
                     * 3.创建文件输入流，发送文件
                     *   将文件输入的内容都放在file里面
                     */
                    in = new FileInputStream(mFile);
                    long totlelength = mFile.length() + 24 + mType.getBytes().length + mFile.getName().getBytes().length;
                    // 向服务器发送[文件名字节长度 \r\n]
                    byte[] buffer1 = new byte[8];
                    for (int i = 0; i < 8; i++) {
                        int offset = 64 - (i + 1) * 8;
                        buffer1[i] = (byte) ((totlelength >> offset) & 0xff);
                    }
                    out.write(buffer1);
                    // 向服务器发送[文件字名节长度\r\n]
                    byte[] buffer2 = new byte[8];

                    long typeLength = mType.getBytes().length;
                    for (int i = 0; i < 8; i++) {
                        int offset = 64 - (i + 1) * 8;
                        buffer2[i] = (byte) ((typeLength >> offset) & 0xff);
                    }
                    out.write(buffer2);
                    // 向服务器发送[文件字名节长度\r\n]
                    byte[] buffer = new byte[8];
                    long filenamelength = mFile.getName().getBytes().length;
                    for (int i = 0; i < 8; i++) {
                        int offset = 64 - (i + 1) * 8;
                        buffer[i] = (byte) ((filenamelength >> offset) & 0xff);
                    }
                    out.write(buffer);

                    // 向服务器发送[type字节]
                    out.write(mType.getBytes());
                    // 向服务器发送[文件名字节]
                    out.write(mFile.getName().getBytes());
                    // 向服务器发送[文件字节内容]
                    byte[] data = new byte[1024];
                    int i = 0;
                    while ((i = in.read(data)) != -1) {
                        out.write(data, 0, i);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    /**
                     * 关闭Scanner，文件输入流，套接字
                     * 套接字装饰了输出流，所以不用关闭输出流
                     */

                    try {
                        if(in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        // 强制将输入流置为空
                        in = null;
                    }
                    try {
                        if(socket != null) {
                            socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        // 强制释放socket
                        socket = null;
                    }

                }
                System.out.println("文件传输完毕");
            }
        }
    };
}
