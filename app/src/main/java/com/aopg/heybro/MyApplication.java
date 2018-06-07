package com.aopg.heybro;

import android.util.Log;

import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by 王伟健 on 2018-05-22.
 */

public class MyApplication extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        Log.i("IMDebugApplication", "init");
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);
        //注册全局事件监听类
        JMessageClient.registerEventReceiver(new GlobalEventListener(getApplicationContext()));
    }
}
