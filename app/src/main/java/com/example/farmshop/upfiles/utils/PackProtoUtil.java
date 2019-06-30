package com.example.farmshop.upfiles.utils;

import android.os.Message;

import com.example.farmshop.MainApplication;
import com.example.farmshop.bean.ByteData;
import com.example.farmshop.farmshop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PackProtoUtil {
    public static byte[] pack(farmshop.baseType.Builder builder){
        byte[] bytearray = {};
        farmshop.baseType bt =  builder.build();
        ByteArrayOutputStream outp1 = new ByteArrayOutputStream();
        try{
            bt.writeTo(outp1);
            bytearray = outp1.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return bytearray;
    }

    public static void packSend(farmshop.baseType.Builder builder){
        Message smsg = Message.obtain();
        ByteData buffdata = new ByteData(pack(builder));
        smsg.obj = buffdata;
        MainApplication.getInstance().mTransmit.mRecvHandler.sendMessage(smsg);
    }
}
