package com.example.farmshop.mobphone;

import android.content.Context;

import com.example.farmshop.util.VirtureUtil;
import com.example.farmshop.util.VirtureUtil.onResultMobPhoneListener;
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import cn.smssdk.gui.util.Const;

public class MyMobphone {
    private Context mContext;
    public MyMobphone(Context context){
        mContext = context;
    }
    public void startMessage(){
        //mob 短信验证初始化
        MobSDK.init(mContext);
        // 在尝试读取通信录时以弹窗提示用户（可选功能）
        //SMSSDK.setAskPermisionOnReadContact(true);

        RegisterPage registerPage = new RegisterPage();
        // 使用自定义短信模板(不设置则使用默认模板)
        registerPage.setTempCode(null);
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                Boolean mobresult = false;
                String phonenum = "";
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    phonenum = (String) phoneMap.get("phone");
                    // 提交用户信息,慎用，提交后以后不用认证了
                    //registerUser(country, phone);
                    mobresult = true;
                }
                mListener.getMessageResult(phonenum, mobresult);
            }
        });
        registerPage.show(mContext);
    }

    public void destoryMobphoneEventHandler(){
        // 销毁回调监听接口
        SMSSDK.unregisterAllEventHandler();
    }

    // 提交用户信息
    private void registerUser(String country, String phone) {
        Random rnd = new Random();
        int id = Math.abs(rnd.nextInt());
        String uid = String.valueOf(id);
        String nickName = "SmsSDK_User_" + uid;
        String avatar = Const.AVATOR_ARR[id % Const.AVATOR_ARR.length];
        SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
    }

    private onResultMobPhoneListener mListener;
    public void setMobPhoneListener(onResultMobPhoneListener listener){
        mListener = listener;
    }
}
