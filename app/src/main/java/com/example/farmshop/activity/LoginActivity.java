package com.example.farmshop.activity;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.bean.ByteData;
import com.example.farmshop.farmshop;
import com.example.farmshop.thread.MessageTransmit;
import com.example.farmshop.thread.MessageTransmit.OnGetNetDataListener;
import com.google.protobuf.Any;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by ouyangshen on 2016/11/11.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, OnGetNetDataListener {
    private final static String TAG = "LoginActivity";
    private EditText et_ip;
    private EditText et_port;
    private static EditText s_et_name;
    private static EditText s_et_pwd;
    public static Button et_regst;
    private MainApplication app;
    public static Context mContext;
    public static Boolean isConnect = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        et_ip = (EditText) findViewById(R.id.et_ip);
        et_port = (EditText) findViewById(R.id.et_port);
        s_et_name = (EditText) findViewById(R.id.et_name);
        s_et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_regst = (Button) findViewById(R.id.btn_regist);
        findViewById(R.id.btn_regist).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        app = MainApplication.getInstance();
        mContext = this;
    }

    @Override
    public void onClick(View v) {
        //注册
        if (v.getId() == R.id.btn_regist) {
            Intent intent = new Intent(this, RegistActivity.class);
            startActivity(intent);
        }
        //登录
        if(v.getId() == R.id.btn_login){
            String ipstr = et_ip.getText().toString();
            String strport = et_port.getText().toString();
            int port = Integer.parseInt(strport);
            app.mTransmit.setIpPort(ipstr, port);
            app.mTransmit.setOnNetListener(this);
            new Thread( app.mTransmit).start();
//            Intent intent = new Intent(mContext, CenterActivity.class);
//            startActivity(intent);
        }
    }

    @Override
    public void onGetNetData(String info){
        if(info.equals("login")){
            Intent intent = new Intent(mContext, CenterActivity.class);
            startActivity(intent);
        }
    }

    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(isConnect){
                farmshop.baseType data = (farmshop.baseType) msg.obj;
                try{
                    Any any = data.getObject(0);
                    farmshop.LoginResponse resp = farmshop.LoginResponse.parseFrom(any.getValue());
                    if(resp.getResult() == 0){
                        Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {
                isConnect = true;
                farmshop.baseType.Builder regist = farmshop.baseType.newBuilder().setType(farmshop.MsgId.LOGIN_REQ).setSessionId(MainApplication.getInstance().mSessionId);
                farmshop.RegistRequest req = farmshop.RegistRequest.newBuilder().setName(s_et_name.getText().toString()).setPassword(s_et_pwd.getText().toString()).build();
                regist.addObject(Any.pack(req));
                byte[] bytearray = {};
                farmshop.baseType bt =  regist.build();
                ByteArrayOutputStream outp1 = new ByteArrayOutputStream();
                try{
                    bt.writeTo(outp1);
                    bytearray = outp1.toByteArray();
                }catch (IOException e){
                    e.printStackTrace();
                }
                Message smsg = Message.obtain();
                ByteData buffdata = new ByteData(bytearray);
                smsg.obj = buffdata;
                MainApplication.getInstance().mTransmit.mRecvHandler.sendMessage(smsg);
            }

        }
    };



}
