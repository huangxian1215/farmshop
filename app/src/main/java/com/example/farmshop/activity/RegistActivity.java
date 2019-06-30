package com.example.farmshop.activity;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegistActivity extends AppCompatActivity implements OnClickListener ,onGetNetDataListener {

    private EditText et_name;
    private EditText et_pwd;
    public MessageTransmit mTransmit;
    private MainApplication app;
    private Boolean registSuccess = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_regist);
        et_name = (EditText) findViewById(R.id.et_rgname);
        et_pwd = (EditText) findViewById(R.id.et_rgpwd);
        findViewById(R.id.btn_regist).setOnClickListener(this);

        app = MainApplication.getInstance();
        app.mTransmit.addOnNetListener("RegistAcityty", this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_regist) {
            farmshop.baseType.Builder regist = farmshop.baseType.newBuilder().setType(farmshop.MsgId.REGIST_REQ).setSessionId(app.mSessionId);
            farmshop.RegistRequest req = farmshop.RegistRequest.newBuilder().setName(et_name.getText().toString()).setPassword(et_pwd.getText().toString()).build();
            regist.addObject(Any.pack(req));
            PackProtoUtil.packSend(regist);
        }
    }

    @Override
    public void onGetNetData(Object info){
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
}
