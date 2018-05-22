package com.aopg.heybro;

import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 王伟健 on 2018-05-22.
 */

public class MyApplication extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
