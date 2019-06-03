package com.example.farmshop.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.bean.ByteData;
import com.example.farmshop.farmshop;
import com.example.farmshop.thread.MessageTransmit;
import com.google.protobuf.Any;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_pwd;
    public MessageTransmit mTransmit;
    private MainApplication app;
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_regist);
        et_name = (EditText) findViewById(R.id.et_rgname);
        et_pwd = (EditText) findViewById(R.id.et_rgpwd);
        findViewById(R.id.btn_regist).setOnClickListener(this);

        app = MainApplication.getInstance();
        mContext = this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_regist) {
            farmshop.baseType.Builder regist = farmshop.baseType.newBuilder().setType(farmshop.MsgId.REGIST_REQ).setSessionId(app.mSessionId);
            farmshop.RegistRequest req = farmshop.RegistRequest.newBuilder().setName(et_name.getText().toString()).setPassword(et_pwd.getText().toString()).build();
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
            Message msg = Message.obtain();
            ByteData buffdata = new ByteData(bytearray);
            msg.obj = buffdata;
            app.mTransmit.mRecvHandler.sendMessage(msg);
        }
    }

    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            farmshop.baseType data = (farmshop.baseType) msg.obj;
            try{
                Any any = data.getObject(0);
                farmshop.RegistResponse resp = farmshop.RegistResponse.parseFrom(any.getValue());
                if(resp.getResult() == 0){
                    Toast.makeText(mContext, "注册成功", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext, "注册失败", Toast.LENGTH_LONG).show();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    };
}
