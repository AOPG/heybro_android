package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.Common.ActivitiesManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;

/**
 * Created by L on 2018/6/12.
 */

public class MapActivity extends Activity {
    private TextView tv;
    // 定位client类
    public LocationClient mLocationClient = null;
    // 定位监听器类
    public BDLocationListener myListener = new MyLocationListener();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化
        mLocationClient = new LocationClient(getApplicationContext());
        // 设置定位參数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPRS
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
        // 设置获取地址信息
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        // 注冊监听函数
        mLocationClient.registerLocationListener(myListener);
        // 调用此方法開始定位
        mLocationClient.start();
    }
    /**
     * 定位成功之后的回调函数
     *
     */
    public class MyLocationListener implements BDLocationListener {
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
            tv.setText(tv.getText() + "\n" + sb.toString());
        }
    }
}
