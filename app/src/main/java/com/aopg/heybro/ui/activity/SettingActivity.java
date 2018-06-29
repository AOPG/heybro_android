package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.im.InitIM;
import com.aopg.heybro.ui.Common.ActivitiesManager;
import com.aopg.heybro.utils.LoginInfo;

import static com.aopg.heybro.utils.ThreadUtils.findAllThreads;

/**
 * Created by 壑过忘川 on 2018/6/1.
 * 设置界面
 */

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_my_settings);
        ActivitiesManager.getInstance().addActivity(this);

        ImageView set_back = findViewById(R.id.set_back);
        //返回按钮
        set_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        /**
         * 账号与安全界面
         */
        RelativeLayout codeSafe = findViewById(R.id.zhanghaoanquan);
        codeSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent concernIntent = new Intent();
                ComponentName componentName = new ComponentName(SettingActivity.this,CodeSafeActivity.class);
                concernIntent.setComponent(componentName);
                startActivity(concernIntent);
            }
        });
        /**
         * 隐私设置界面
         */
        RelativeLayout privateSetting = findViewById(R.id.yinsi);
        privateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent privateSettingIntent = new Intent();
                ComponentName componentName = new ComponentName(SettingActivity.this,PrivateSettingActivity.class);
                privateSettingIntent.setComponent(componentName);
                startActivity(privateSettingIntent);
            }
        });
        /**
         * 通用设置界面
         */
        RelativeLayout commonSetting = findViewById(R.id.tongyong);
        commonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commonSettingIntent = new Intent();
                ComponentName componentName = new ComponentName(SettingActivity.this,CommonSettingActivity.class);
                commonSettingIntent.setComponent(componentName);
                startActivity(commonSettingIntent);
            }
        });
        /**
         * 退出登录*/
        LinearLayout quitSetting = findViewById(R.id.tuichu);
        quitSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread[] threads = findAllThreads();
                for (int i = 0; i < threads.length; i++) {
                    if (threads[i].getName().equals("userInfoThread")){
                        ((MainActivity.UserInfoThread)threads[i]).stopMe();
                    }
                }
                LoginInfo.ISLOGINIM=0;
                LoginInfo.user.setIsLogin(0);
                LoginInfo.user.updateAll("userName = ?",LoginInfo.user.getUsername());
                LoginInfo.user = new User();
                //刷新用户信息,向MainActivity发送信息刷新请求
                Intent intentRefresh = new Intent("FragmentMy");
                sendBroadcast(intentRefresh);

                InitIM.logout(SettingActivity.this);
                Intent intent = new Intent(SettingActivity.this, LoginActivty.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }

}
