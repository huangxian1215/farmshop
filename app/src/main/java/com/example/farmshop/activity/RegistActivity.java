package com.example.farmshop.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.bean.ByteData;
import com.example.farmshop.farmshop;
import com.example.farmshop.thread.MessageTransmit;
import com.example.farmshop.upfiles.utils.PackProtoUtil;
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;
import com.google.protobuf.Any;
import com.mob.MobSDK;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.CommonDialog;
import cn.smssdk.gui.RegisterPage;
import cn.smssdk.gui.util.Const;

public class RegistActivity extends AppCompatActivity implements OnClickListener ,onGetNetDataListener {

    private EditText et_name;
    private EditText et_pwd;
    private EditText et_pwd_again;
    public MessageTransmit mTransmit;
    private MainApplication app;
    private Boolean registSuccess = false;


    private boolean ready;
    private boolean gettingFriends;
    private Dialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_regist);
        et_name = (EditText) findViewById(R.id.et_rgname);
        et_pwd = (EditText) findViewById(R.id.et_rgpwd);
        et_pwd_again = (EditText) findViewById(R.id.et_rgpwd_again);
        findViewById(R.id.btn_regist).setOnClickListener(this);

        app = MainApplication.getInstance();
        app.mTransmit.addOnNetListener("RegistAcityty", this);
        //mob 短信验证初始化
        MobSDK.init(this);
        registerSDK();
    }

    @Override
    public void onClick(View v) {
        if(et_pwd.getText().toString().equals(et_pwd_again.getText().toString())){
            if (v.getId() == R.id.btn_regist) {
                farmshop.baseType.Builder regist = farmshop.baseType.newBuilder().setType(farmshop.MsgId.REGIST_REQ).setSessionId(app.mSessionId);
                farmshop.RegistRequest req = farmshop.RegistRequest.newBuilder().setName(et_name.getText().toString()).setPassword(et_pwd.getText().toString()).build();
                regist.addObject(Any.pack(req));
                PackProtoUtil.packSend(regist);
            }
        }else{
            Toast.makeText(getApplicationContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
        }
        //test mob
//        phoneAuth();
    }

    @Override
    public void onGetNetData(Object info, farmshop.MsgId msgid){
        farmshop.baseType data = (farmshop.baseType) info;
        try{
            Any any = data.getObject(0);
            farmshop.RegistResponse resp = farmshop.RegistResponse.parseFrom(any.getValue());
            if(resp.getResult() == 0){
                registSuccess = true;
            }else {
                registSuccess = false;
            }
            hd_showregist.postDelayed(rb_showregist, 0);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    Handler hd_showregist = new Handler();
    Runnable rb_showregist = new Runnable() {
        @Override
        public void run() {
            if(registSuccess){
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                app.mTransmit.deleteOnNetListener("RegistAcityty");
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void phoneAuth(){
        // 打开注册页面
        RegisterPage registerPage = new RegisterPage();
        // 使用自定义短信模板(不设置则使用默认模板)
        registerPage.setTempCode(null);
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    // 提交用户信息,慎用，提交后以后不用认证了
                    //registerUser(country, phone);
                }
            }
        });
        registerPage.show(this);
    }

    private void registerSDK() {
        // 在尝试读取通信录时以弹窗提示用户（可选功能）
        SMSSDK.setAskPermisionOnReadContact(true);
        //无UI请参考官方文档
    }

    protected void onDestroy() {
        if (ready) {
            // 销毁回调监听接口
            SMSSDK.unregisterAllEventHandler();
        }
        super.onDestroy();
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
}
