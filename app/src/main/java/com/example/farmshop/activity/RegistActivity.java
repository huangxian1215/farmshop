package com.example.farmshop.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.farmshop;
import com.example.farmshop.upfiles.utils.PackProtoUtil;
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;
import com.google.protobuf.Any;

import java.io.IOException;


public class RegistActivity extends AppCompatActivity implements OnClickListener ,onGetNetDataListener {
    private EditText et_name;
    private EditText et_pwd;
    private EditText et_pwd_again;
    private MainApplication app;
    private Boolean registSuccess = false;
    private int responseCode = -1;

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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_regist) {
            if(et_pwd.getText().toString().equals(et_pwd_again.getText().toString())){
                farmshop.baseType.Builder regist = farmshop.baseType.newBuilder().setType(farmshop.MsgId.REGIST_REQ).setSessionId(app.mSessionId);
                farmshop.RegistRequest req = farmshop.RegistRequest.newBuilder().setName(et_name.getText().toString()).setPassword(et_pwd.getText().toString()).build();
                regist.addObject(Any.pack(req));
                PackProtoUtil.packSend(regist);
            }else{
                Toast.makeText(getApplicationContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onGetNetData(Object info, farmshop.MsgId msgid){
        farmshop.baseType data = (farmshop.baseType) info;
        try{
            Any any = data.getObject(0);
            farmshop.RegistResponse resp = farmshop.RegistResponse.parseFrom(any.getValue());
            responseCode = resp.getResult();
            registSuccess = resp.getResult() == 0;
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
                if(responseCode == 1){
                    Toast.makeText(getApplicationContext(), "该用户名已存在", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                }
            }

        }
    };

    protected void onDestroy() {
        super.onDestroy();
    }

}
