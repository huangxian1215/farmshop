package com.example.farmshop.activity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.farmshop.MainApplication;
import com.example.farmshop.R;
import com.example.farmshop.bean.ByteData;
import com.example.farmshop.bean.UserInfo;
import com.example.farmshop.dialog.MyDialog;
import com.example.farmshop.farmshop;
import com.example.farmshop.functions.MyLocation;
import com.example.farmshop.iflytek.IflayMainActivity;
import com.example.farmshop.iflytek.MySpeakOut;
import com.example.farmshop.music.MusicMainActivity;
import com.example.farmshop.upfiles.utils.PackProtoUtil;
import com.example.farmshop.util.VirtureUtil.onMyLocationListener;
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;
import com.example.farmshop.util.VirtureUtil.onClickSureListener;
import com.example.farmshop.util.VirtureUtil.onPlayVoiceListener;
import com.google.protobuf.Any;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ouyangshen on 2016/11/11.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, onGetNetDataListener ,onClickSureListener ,onPlayVoiceListener
        , onMyLocationListener {
    private final static String TAG = "LoginActivity";
    private EditText et_ip;
    private EditText et_port;
    private EditText s_et_name;
    private EditText s_et_pwd;
    public TextView tv_location;
    public TextView ev_location;
    private MainApplication app;
    public Context mContext;
    public Boolean isConnect = false;
    public Boolean loginSuccess = false;
    public int connectType = 0; //0 登录 1 注册
    public String mLocation  = "";
    private LocationManager lm = null;
    //
    public MyLocation myLocation;
    private MyDialog mDialog;
    private int progress = 0;

    //语音测试
    private ArrayList<String> mSpeakList = new ArrayList<>();
    private MySpeakOut mSpk;
    private String[] str = {"好的", "是的", "对的", "了不起的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myLocation = new MyLocation(this);
        myLocation.setMyLocationListener(this);
        et_ip = (EditText) findViewById(R.id.et_ip);
//        et_port = (EditText) findViewById(R.id.et_port);
        s_et_name = (EditText) findViewById(R.id.et_name);
        s_et_pwd = (EditText) findViewById(R.id.et_pwd);
//        tv_location = (TextView) findViewById(R.id.tv_location);
        ev_location = (TextView) findViewById(R.id.ev_location);
        findViewById(R.id.btn_regist).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
//        findViewById(R.id.btn_iflay).setOnClickListener(this);
        app = MainApplication.getInstance();
        mContext = this;

        getPermissions();
        fillLocalStoreInfo();
        //test
//        findViewById(R.id.btn_test).setOnClickListener(this);
        //test map SDK
        //test Dialog
//        showDialog();
        mDialog = new MyDialog();
//        mydialog.createAlerDialog(this);
//        mydialog.showDialog("提示","登录成功", "确定");
//        mydialog.showDialog("提示","登录成功", "确定", "取消");
//        String []item = {"1","2","3","4"};
//        mydialog.showChooseOneDialog("提示",item, "确定", "取消");
//        mydialog.showChooseMultipleDialog("提示",item, "确定", "取消");
//        mydialog.setClickListener(this);
//        mDialog.createProgressDialog(this);
//        mDialog.showNoProgressDialog("提示","正在登录");
//        mDialog.showProgressDialog("正在下载", "当前进度");
//        hd.postDelayed(rnb, 100);


    }

//    Handler hd = new Handler();
//    Runnable rnb = new Runnable() {
//        @Override
//        public void run() {
//            progress += 1;
//            mDialog.freshProgress(progress);
//            if(progress < 100){
//                hd.postDelayed(rnb, 100);
//            }else if(progress >= 100){
//                mDialog.hideProgressDialog();
//            }
//        }
//    };

    private void getPermissions(){
        //申请SD卡读写权限
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.DELETE_CACHE_FILES,
                Manifest.permission.VIBRATE,
                Manifest.permission.RECEIVE_SMS
        }, 1);
    }


    @Override
    public void onClick(View v) {
        //注册
        if (v.getId() == R.id.btn_regist) {
            connectType = 1;
            connetcServer();
            Intent intent = new Intent(this, RegistActivity.class);
            startActivity(intent);
        }
        //登录
        if(v.getId() == R.id.btn_login){
            connectType = 0;
            connetcServer();
            //保存输入
            app.setLocalStore("login_ip", et_ip.getText().toString());
//            app.setLocalStore("login_port", et_port.getText().toString());
            app.setLocalStore("login_name", s_et_name.getText().toString());
            app.setLocalStore("login_pwd", s_et_pwd.getText().toString());
        }
        //讯飞语音setOnNetListener
//        if(v.getId() == R.id.btn_iflay){
//            Intent intent = new Intent(this, IflayMainActivity.class);
//            startActivity(intent);
//            }
    }

    @Override
    public void finishSpeak(){
        mSpeakList.remove(0);
        if(mSpeakList.size() != 0){
            mSpk.startSpeakOut(mSpeakList.get(0));
        }
    }

    private Handler strhd = new Handler();
    private Runnable strRb = new Runnable() {
        @Override
        public void run() {
            if(mSpeakList.size() <= 5){
                Random ra =new Random();
                int index = ra.nextInt(4);
                mSpeakList.add(str[index]);
            }
            strhd.postDelayed(strRb, 500);
        }
    };

    private void fillLocalStoreInfo(){
        if(!app.getLocalStore("login_ip").equals("")){
            et_ip.setText(app.getLocalStore("login_ip"));
//            et_port.setText(app.getLocalStore("login_port"));
            s_et_name.setText(app.getLocalStore("login_name"));
            s_et_pwd.setText(app.getLocalStore("login_pwd"));
        }
    }

    private void connetcServer(){
        if(!isConnect){
            String ipstr = et_ip.getText().toString();
//            String strport = et_port.getText().toString();
//            int port = Integer.parseInt(strport);
            app.httpUrl = "http://"+ ipstr + ":8080/";
            app.mTransmit.setIpPort(ipstr, 8010);
            app.mTransmit.addOnNetListener("LoginActivity", this);
            new Thread(  app.mTransmit).start();
        }else {
            loginUgly();
        }
    }

    public void loginUgly(){
        farmshop.baseType.Builder login = farmshop.baseType.newBuilder().setType(farmshop.MsgId.LOGIN_REQ).setSessionId(MainApplication.getInstance().mSessionId);
        farmshop.RegistRequest req = farmshop.RegistRequest.newBuilder().setName(s_et_name.getText().toString()).setPassword(s_et_pwd.getText().toString()).build();
        login.addObject(Any.pack(req));
        PackProtoUtil.packSend(login);
    }

    @Override
    public void onGetNetData(Object info, farmshop.MsgId msgid){
        if(isConnect){
            farmshop.baseType data = (farmshop.baseType)info;
            try{
                Any any = data.getObject(0);
                farmshop.LoginResponse resp = farmshop.LoginResponse.parseFrom(any.getValue());
                if(resp.getResult() == 0){
                    loginSuccess = true;
                    MainApplication.getInstance().mUserinfo = new UserInfo();
                    MainApplication.getInstance().mUserinfo.name = resp.getName();
                    MainApplication.getInstance().mUserinfo.detail = resp.getUserinfo();
                }else{
                    loginSuccess = false;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            hd_showlogin.postDelayed(rb_showlogin, 0);
        }else {
            isConnect = true;
            if(connectType == 1) return;
            loginUgly();
        }
    }

    Handler hd_showlogin = new Handler();
    Runnable rb_showlogin = new Runnable() {
        @Override
        public void run() {
            if(loginSuccess){
                Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, CenterActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public void getMyAllLocation(String info){
        ev_location.setText(info);
    }

    @Override
    public void onClickSure(int index){
        Toast.makeText(mContext, String.valueOf(index), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        myLocation.stopLocation();
        super.onStop();
    }

    //读取进度条 静态圆进度形条可取消
    private void showProgressDialog(){
        //静态圆进度形条可取消
//        ProgressDialog dialog4 = ProgressDialog.show(this, "提示", "正在登陆中", false, true);
    }

}
