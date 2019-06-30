package com.example.farmshop.functions;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.farmshop.util.VirtureUtil.onMyLocationListener;

public class MyLocation {
    private String mLocationInfo;
    public LocationClient mLocationClient = null;

    public MyLocation(Context context){
        // 注意该方法要在setContentView方法之前实现
        mLocationClient = new LocationClient(context);//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数
        setBDMapSDKparm();
    }

    public void stopLocation(){
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop(); //停止定位
    }

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
                    mLocationInfo = mapDesc;
                    mListener.getMyAllLocation(mapDesc);
                }
            }
        }
    };

    private onMyLocationListener mListener;

    public void setMyLocationListener(onMyLocationListener listener){
        mListener = listener;
    }
}
