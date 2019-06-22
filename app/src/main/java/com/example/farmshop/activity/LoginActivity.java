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
import com.example.farmshop.iflytek.IflayMainActivity;
import com.example.farmshop.util.VirtureUtil;
import com.example.farmshop.util.VirtureUtil.onGetNetDataListener;
import com.example.farmshop.util.VirtureUtil.onClickSureListener;
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

/**
 * Created by ouyangshen on 2016/11/11.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener, onGetNetDataListener ,onClickSureListener {
    private final static String TAG = "LoginActivity";
    private EditText et_ip;
    private EditText et_port;
    private static EditText s_et_name;
    private static EditText s_et_pwd;
    public TextView tv_location;
    private MainApplication app;
    public static Context mContext;
    public static Boolean isConnect = false;
    public static int connectType = 0; //0 登录 1 注册
    public String mLocation  = "";
    private LocationManager lm = null;
    //
    public LocationClient mLocationClient = null;
    private MyDialog mDialog;
    private int progress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 注意该方法要在setContentView方法之前实现
        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数
        setBDMapSDKparm();

        et_ip = (EditText) findViewById(R.id.et_ip);
        et_port = (EditText) findViewById(R.id.et_port);
        s_et_name = (EditText) findViewById(R.id.et_name);
        s_et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_location = (TextView) findViewById(R.id.tv_location);
        findViewById(R.id.btn_regist).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_iflay).setOnClickListener(this);
        app = MainApplication.getInstance();
        mContext = this;

        getPermissions();
        fillLocalStoreInfo();
        //test
        findViewById(R.id.btn_test).setOnClickListener(this);
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

    Handler hd = new Handler();
    Runnable rnb = new Runnable() {
        @Override
        public void run() {
            progress += 1;
            mDialog.freshProgress(progress);
            if(progress < 100){
                hd.postDelayed(rnb, 100);
            }else if(progress >= 100){
                mDialog.hideProgressDialog();
            }
        }
    };

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
            app.setLocalStore("login_port", et_port.getText().toString());
            app.setLocalStore("login_name", s_et_name.getText().toString());
            app.setLocalStore("login_pwd", s_et_pwd.getText().toString());
        }
        //讯飞语音setOnNetListener
        if(v.getId() == R.id.btn_iflay){
            Intent intent = new Intent(this, IflayMainActivity.class);
            startActivity(intent);
        }
        //测试
        if(v.getId() == R.id.btn_test){
            //通知消息
//            Intent intent = new Intent(this, NotificationActivity.class);
            //简单网页
//            Intent intent = new Intent(this, WebBrowserActivity.class);
//            startActivity(intent);
        }
    }

    private void fillLocalStoreInfo(){
        if(!app.getLocalStore("login_ip").equals("")){
            et_ip.setText(app.getLocalStore("login_ip"));
            et_port.setText(app.getLocalStore("login_port"));
            s_et_name.setText(app.getLocalStore("login_name"));
            s_et_pwd.setText(app.getLocalStore("login_pwd"));
        }
    }


    private void connetcServer(){
        if(!isConnect){
            String ipstr = et_ip.getText().toString();
            String strport = et_port.getText().toString();
            int port = Integer.parseInt(strport);
            //httpUrl = "http://192.168.6.176:8080/";
            app.httpUrl = "http://"+ ipstr + ":8080/";
            app.mTransmit.setIpPort(ipstr, port);
//            app.mTransmit.setOnNetListener(this);
            app.mTransmit.addOnNetListener("LoginAcityty", this);
            new Thread(  app.mTransmit).start();
        }else {
            loginUgly();
        }
    }

    public static void loginUgly(){
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
                        MainApplication.getInstance().mUserinfo = new UserInfo();
                        MainApplication.getInstance().mUserinfo.name = resp.getName();
                        MainApplication.getInstance().mUserinfo.detail = resp.getUserinfo();
                    }else{
                        Toast.makeText(mContext, "登陆失败", Toast.LENGTH_SHORT).show();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {
                isConnect = true;
                if(connectType == 1) return;
                loginUgly();
            }
        }
    };

    private void setBDMapSDKparm(){
        LocationClientOption mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mOption.setOpenGps(true);
        mOption.setCoorType("bd09ll");
        mOption.setScanSpan(3000);
        mOption.setIsNeedAddress(true);
        mOption.setIsNeedLocationDescribe(true);
        mOption.setNeedDeviceDirect(false);
        mOption.setLocationNotify(false);
        mOption.setIgnoreKillProcess(true);
        mOption.setIsNeedLocationPoiList(true);
        mOption.SetIgnoreCacheException(false);
        mOption.setIsNeedAltitude(false);
        mLocationClient.setLocOption(mOption);//设置定位参数
        mLocationClient.start();//开始定位
    }

    //伪代码
    private BDAbstractLocationListener myListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //定位sdk获取位置后回调
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                String mapDesc = "";

                /**
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                mapDesc += "上次定位时间:" + location.getTime();


                /**
                 * 定位类型
                 BDLocation.TypeGpsLocation----gps定位
                 BDLocation.TypeNetWorkLocation----网络定位(wifi基站定位)
                 以及其他定位失败信息
                 */
                mapDesc += "\n定位判定：" + String.valueOf(location.getLocType());

                /**
                 * 对应的定位类型说明
                 * 比如"NetWork location successful"之类的信息
                 */
                mapDesc += "\n定位说明：" + location.getLocTypeDescription();

                /**
                 * 纬度
                 */
                mapDesc += "\n纬度：" + String.valueOf(location.getLatitude());

                /**
                 * 经度
                 */
                mapDesc += "\n经度：" + String.valueOf(location.getLongitude());

                /**
                 * 误差半径，代表你的真实位置在这个圆的覆盖范围内，
                 * 半径越小代表定位精度越高，位置越真实
                 * 在同一个地点，可能每次返回的经纬度都有微小的变化，
                 * 是因为返回的位置点并不是你真实的位置，有误差造成的。
                 */
                mapDesc += "\n精度m：" +  String.valueOf(location.getRadius());

                mapDesc += "\n国家码：" + location.getCountryCode();//国家码，null代表没有信息
                mapDesc += "\n国家：" + location.getCountry();//国家名称
                mapDesc += "\n城市编码：" + location.getCityCode();//城市编码
                mapDesc += "\n城市：" + location.getCity();//城市
                mapDesc += "\n区：" + location.getDistrict();//区
                mapDesc += "\n街道：" + location.getStreet();//街道
                mapDesc += "\n地址信息：" + location.getAddrStr();//地址信息
                mapDesc += "\n位置描述信息：" + location.getLocationDescribe();//位置描述信息

                /**
                 * 判断用户是在室内，还是在室外
                 * 1：室内，0：室外，这个判断不一定是100%准确的
                 */
                mapDesc += "\n室内/外：" + String.valueOf(location.getUserIndoorState());

                /**
                 * 获取方向
                 */
                mapDesc += "\n方向：" + String.valueOf(location.getDirection());

                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        mapDesc += "\n周边：" + String.valueOf(i) + poi.getName();//获取位置附近的一些商场、饭店、银行等信息

                    }
                }

                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS类型定位结果
                    mapDesc += "\n速度 单位：km/h：" + String.valueOf(location.getSpeed());//速度 单位：km/h，注意：网络定位结果是没有速度的
                    mapDesc += "\n卫星数目：" + String.valueOf(location.getSatelliteNumber());//卫星数目，gps定位成功最少需要4颗卫星
                    mapDesc += "\n海拔高度(米)：" + String.valueOf(location.getAltitude());//海拔高度 单位：米
                    mapDesc += "\ngps质量：" + String.valueOf(location.getGpsAccuracyStatus());//gps质量判断
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {//网络类型定位结果
                    if (location.hasAltitude()) {//如果有海拔高度
                        mapDesc += "\n海拔高度(米)：" + String.valueOf(location.getAltitude());//单位：米
                    }
                    mapDesc += "\n运营商信息：" + String.valueOf(location.getOperators());//运营商信息
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    //离线定位成功，离线定位结果也是有效的;
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    //服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com;
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    //网络不同导致定位失败，请检查网络是否通畅;
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机;
                }

                if(location.getTime() != null){
                    tv_location.setText(mapDesc);
                }

            }
        }
    };

    @Override
    public void onGetNetData(String info){
        if(info.equals("LoginAcityty")){
            Intent intent = new Intent(mContext, CenterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClickSure(int index){
        Toast.makeText(mContext, String.valueOf(index), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop(); //停止定位
    }

    //读取进度条 静态圆进度形条可取消
    private void showProgressDialog(){
        //静态圆进度形条可取消
//        ProgressDialog dialog4 = ProgressDialog.show(this, "提示", "正在登陆中", false, true);
    }

}
