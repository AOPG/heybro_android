package com.aopg.heybro.utils;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by L on 2018/6/13.
 */

public class BaiduMapLocationUtil {
    public static LocationClient mLocationClient;
    public static MyLocationListener myListener = new MyLocationListener();
    public static void init(Context context) {
        //声明LocationClient类
        mLocationClient = new LocationClient(context.getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        LocationClientOption locationClientOption = initOption();
        mLocationClient.setLocOption(locationClientOption);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        mLocationClient.start();
        //启动定位
    }

    public static LocationClientOption initOption() {
        LocationClientOption option = new LocationClientOption();

        //需要定位信息必须设置
//        option.setAddrType("all");//过时，由setIsNeedAddress代替
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setScanSpan(5000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        return option;
    }

    /**
     * 定位成功之后的回调函数
     *
     */
    public static class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return;
            StringBuffer sb = new StringBuffer(256);
            sb.append("时间 : ");
            sb.append(location.getTime());
            sb.append("\n返回码 : ");
            sb.append(location.getLocType());
            sb.append("\n纬度 : ");
            sb.append(location.getLatitude());
            sb.append("\n经度 : ");
            sb.append(location.getLongitude());
            sb.append("\n半径 : ");
            sb.append(location.getRadius());
            sb.append("\n省 : ");
            sb.append(location.getProvince());
            sb.append("\n市 : ");
            sb.append(location.getCity());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\n速度 : ");
                sb.append(location.getSpeed());
                sb.append("\n卫星数 : ");
                sb.append(location.getSatelliteNumber());
            }
            //tv.setText(tv.getText() + "\n" + sb.toString());
            System.out.println(sb.toString());
        }
    }
    /**
     * 关闭定位功能
     */
    public static void onStop() {
        mLocationClient.stop();
    }
}
