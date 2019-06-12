package com.example.farmshop.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.bean.ByteData;
import com.example.farmshop.bean.UserInfo;
import com.example.farmshop.farmshop;
import com.google.protobuf.Any;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserDetailEditActivity extends AppCompatActivity {
    private TextView et_realname;
    private TextView et_age;
    private TextView et_phone;
    private TextView et_location;
    private Button btn_save;
    private static Context mContext;

    RadioGroup rg_admit;
    RadioButton rb_boy;
    RadioButton rb_girl;

    private UserInfo mUserinfo;
    private farmshop.EditUserInfoRequest.Builder eduser;
    private static UserInfo tempUserinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetailedit);
        initwidget();
        eduser = farmshop.EditUserInfoRequest.newBuilder();
        mUserinfo = MainApplication.getInstance().mUserinfo;
        tempUserinfo = mUserinfo;
        filldata();
        mContext = this;
    }

    public void initwidget(){
        et_realname = (TextView)findViewById(R.id.et_realname);
        rg_admit = (RadioGroup)findViewById(R.id.rg_admit);
        rb_boy = (RadioButton)findViewById(R.id.rb_boy_0);
        rb_girl = (RadioButton)findViewById(R.id.rb_girl_1);
        et_age = (TextView)findViewById(R.id.et_age);
        et_phone = (TextView)findViewById(R.id.et_phone);
        et_location = (TextView)findViewById(R.id.et_location);
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setEnabled(true);
        rg_admit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_boy_0:
                        eduser.setSex(false);
                        break;
                    case R.id.rb_girl_1:
                        eduser.setSex(true);
                        break;
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                eduser.setRealName(et_realname.getText().toString());
                eduser.setAge(Integer.parseInt(et_age.getText().toString()));
                eduser.setPhoneNumber(et_phone.getText().toString());
                eduser.setLocation(et_location.getText().toString());

                farmshop.baseType.Builder regist = farmshop.baseType.newBuilder().setType(farmshop.MsgId.EditUserInfo_REQ).setSessionId(MainApplication.getInstance().mSessionId);
                farmshop.EditUserInfoRequest req = eduser.build();
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

                tempUserinfo.detail = req;
                btn_save.setEnabled(false);
                finish();
            }
        });
    }

    public void filldata(){
        if(mUserinfo == null) return;
        eduser.setUid(mUserinfo.detail.getUid()).
                setHeadimg(mUserinfo.detail.getHeadimg()).
                setRealName(mUserinfo.detail.getRealName()).
                setSex(mUserinfo.detail.getSex()).
                setAge(mUserinfo.detail.getAge()).
                setPhoneNumber(mUserinfo.detail.getPhoneNumber()).
                setLocation(mUserinfo.detail.getLocation()).
                setIsManager(mUserinfo.detail.getIsManager());
        et_realname.setText(mUserinfo.detail.getRealName());
        if(mUserinfo.detail.getSex() == false){
            rb_boy.setChecked(true);
        }else{
            rb_girl.setChecked(true);
        }
        et_age.setText(String.valueOf(mUserinfo.detail.getAge()));
        et_phone.setText(mUserinfo.detail.getPhoneNumber());
        et_location.setText(mUserinfo.detail.getLocation());
    }

    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            farmshop.baseType data = (farmshop.baseType) msg.obj;
            try{
                Any any = data.getObject(0);
                farmshop.EditUserInfoResponse resp = farmshop.EditUserInfoResponse.parseFrom(any.getValue());
                if(resp.getResult() == 0){
                    Toast.makeText(mContext, "修改成功", Toast.LENGTH_LONG).show();
                    MainApplication.getInstance().mUserinfo = tempUserinfo;
                }else{
                    Toast.makeText(mContext, "修改失败", Toast.LENGTH_LONG).show();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    };
}
