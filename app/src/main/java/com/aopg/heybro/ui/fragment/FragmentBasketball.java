package com.aopg.heybro.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.ui.activity.MyConcernActivity;
import com.aopg.heybro.ui.activity.SearchRoomActivity;
import com.aopg.heybro.ui.adapter.BasketBallFragmentPagerAdapter;
import com.aopg.heybro.ui.adapter.MyConcernAdapter;
import com.aopg.heybro.ui.room.CustomViewPager;
import com.aopg.heybro.utils.BaiduMapLocationUtil;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;


/**
 * Created by 王伟健 on 2018-03-16.
 * 篮球页面
 */

public class FragmentBasketball extends Fragment{

    private View rootView;
    private Button btn_ball;
    private Button btn_game;
    private CustomViewPager myViewPager;
    private List<Fragment> list;
    private BasketBallFragmentPagerAdapter adapter;
    private View ball_selected;
    private View game_selected;
    private Button btn_searchRoom;
    private MainHandler mainHandler;
    private LocationClient mLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        Log.e("", "onCreateView");
        if (rootView == null) {
            Log.e("", "FragmentBasketball");
            rootView = inflater.inflate(R.layout.fragment_basketball,container,false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        mainHandler = new MainHandler();

        initView();
        // 设置菜单栏的点击事件
        /**
         * 选择打球
         */
        btn_ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewPager.setCurrentItem(0);
                ball_selected.setVisibility(View.VISIBLE);
                game_selected.setVisibility(View.GONE);
            }
        });
        /**
         * 选择比赛
         */
        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewPager.setCurrentItem(1);
                game_selected.setVisibility(View.VISIBLE);
                ball_selected.setVisibility(View.GONE);
            }
        });
        /**
         * 搜索房间
         */
        btn_searchRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchRoomIntent = new Intent(getActivity(), SearchRoomActivity.class);
                startActivity(searchRoomIntent);
            }
        });
        myViewPager.setOnPageChangeListener(new MyPagerChangeListener());
        myViewPager.setScanScroll(false);
        //把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(new FragmentBall());
        list.add(new FragmentGame());
        adapter = new BasketBallFragmentPagerAdapter(getChildFragmentManager(), list);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面
        game_selected.setVisibility(View.GONE);
        return rootView;
    }
    /**
     * 初始化控件
     * */
    private void initView(){
        ball_selected = rootView.findViewById(R.id.ball_selected);
        game_selected = rootView.findViewById(R.id.game_selected);
        btn_ball = rootView.findViewById(R.id.date_ball);
        btn_game = rootView.findViewById(R.id.date_game);
        myViewPager = rootView.findViewById(R.id.basketball_viewPager);
        btn_searchRoom = rootView.findViewById(R.id.basket_search);
        getWeather();
    }
    /**
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     *
     */
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    ball_selected = rootView.findViewById(R.id.ball_selected);
                    ball_selected.setVisibility(View.VISIBLE);
                    game_selected.setVisibility(View.GONE);
                    break;
                case 1:
                    game_selected = rootView.findViewById(R.id.game_selected);
                    game_selected.setVisibility(View.VISIBLE);
                    ball_selected.setVisibility(View.GONE);
                    break;
            }
        }
    }
    private void getWeather(){
        mLocationClient = BaiduMapLocationUtil.init(getApplicationContext(),mLocationClient);
        mLocationClient.registerLocationListener(
                new BDAbstractLocationListener(){
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        OkHttpClient clientWeather;
                        clientWeather = new OkHttpClient.Builder()
                                .connectTimeout(900, TimeUnit.SECONDS)
                                .readTimeout(900, TimeUnit.SECONDS)
                                .build();
                        //维度
                        double lat;
                        //经度
                        double lon;
                        if(mLocationClient.getLastKnownLocation() == null){
                            lat =  Double.parseDouble("38.003585");
                            lon = Double.parseDouble("114.529362");
                        }else {
                            lat = mLocationClient.getLastKnownLocation().getLatitude();
                            lon = mLocationClient.getLastKnownLocation().getLongitude();
                        }

                        Request request = new Request.Builder().
                                url("http://jisutqybmf.market.alicloudapi.com/weather/query?location="+lat+","+lon).
                                addHeader("Authorization","APPCODE 961c9d9cae0443ffa56f81a6f9d96bdf").build();
                        Call call = clientWeather.newCall(request);

                        call.enqueue(new Callback() {//4.回调方法
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result = response.body().string();
                                Log.e("getWeather",result);
                                if(!result.equals("")){
                                    String success = (JSONObject.parseObject(result)).getString("msg");
                                    if (null!=success&&success.equals("ok")) {
                                        JSONObject weatherInfo =
                                                ((JSONObject)((JSONObject.parseObject(result)).get("result")));
                                        JSONObject api =
                                                ((JSONObject)(((JSONObject)((JSONObject.parseObject(result)).get("result"))).get("aqi")));
                                        String city = weatherInfo.getString("city");
                                        String weather = weatherInfo.getString("weather");
                                        String temp = weatherInfo.getString("temp");
                                        String pm2_5 = api.getString("pm2_5");
                                        Map weatherMap = new HashMap();
                                        weatherMap.put("city",city);
                                        weatherMap.put("weather",weather);
                                        weatherMap.put("temp",temp+"°");
                                        weatherMap.put("pm2_5",pm2_5);
                                        mLocationClient.stop();
                                        Message message = mainHandler.obtainMessage(900,weatherMap);
                                        mainHandler.sendMessage(message);
                                    }
                                }

                            }
                        });

                    }
                }

        );

    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 900:
                    Map weatherMap = (Map) msg.obj;
                    TextView weatherInfo =rootView.findViewById(R.id.weather_info);
                    weatherInfo.setText((String)weatherMap.get("weather"));
                    TextView weatherPm25 =rootView.findViewById(R.id.weather_pm2_5);
                    weatherPm25.setText((String)weatherMap.get("pm2_5"));
                    TextView weatherTemp =rootView.findViewById(R.id.weather_temp);
                    weatherTemp.setText((String)weatherMap.get("temp"));
                    String weather = (String) weatherMap.get("weather");
                    ImageView weatherImage = rootView.findViewById(R.id.weather_img);
                    ArrayList<String> shower = new ArrayList<String>(){{add("阵雨"); add("小雨");add("中雨");add("大雨");add("暴雨");
                        add("大暴雨");add("特大暴雨");add("小雨-中雨");add("中雨-大雨");add("大雨-暴雨");add("暴雨-大暴雨");add("大暴雨-特大暴雨");
                        add("大雨-暴雨");add("雨");add("冻雨");}};
                    ArrayList<String> snow = new ArrayList<String>(){{add("阵雪");add("中雪");add("小雪");add("大雪");add("暴雪");add("小雪-中雪");
                        add("中雪-大雪");add("大雪-暴雪");add("雪");}};

                    ArrayList<String> thunder = new ArrayList<String>(){{add("雷阵雨");add("雷阵雨伴有冰雹");}};

                    ArrayList<String> haze = new ArrayList<String>(){{add("雾");add("沙尘暴");add("特强浓雾");add("大雾");add("严重霾");add("重度霾");
                        add("中毒霾");add("霾");add("强浓雾");add("浓雾");add("强沙尘暴");add("扬沙");add("浮尘");}};

                    if (weather.equals("晴")){
                        weatherImage.setImageResource(R.drawable.weather_sunny);
                    }else if (weather.equals("多云")){
                        weatherImage.setImageResource(R.drawable.weather_cloudy);
                    }else if (weather.equals("阴")){
                        weatherImage.setImageResource(R.drawable.weather_overcast);
                    }else if (shower.contains(weather)){
                        weatherImage.setImageResource(R.drawable.weather_shower);
                    }else if (snow.contains(weather)){
                        weatherImage.setImageResource(R.drawable.weather_snow);
                    }else if (thunder.contains(weather)){
                        weatherImage.setImageResource(R.drawable.weather_thunder);
                    }else if (haze.contains(weather)){
                        weatherImage.setImageResource(R.drawable.weather_haze);
                    }else if (weather.equals("雨夹雪")){
                        weatherImage.setImageResource(R.drawable.weather_sleet);
                    }
            }
        }
    }


}
