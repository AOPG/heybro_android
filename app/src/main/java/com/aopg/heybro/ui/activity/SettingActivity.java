package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aopg.heybro.R;

/**
 * Created by 壑过忘川 on 2018/6/1.
 * 设置界面
 */

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_settings);

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
        LinearLayout codeSafe = findViewById(R.id.zhanghaoanquan);
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
        LinearLayout privateSetting = findViewById(R.id.yinsi);
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
        LinearLayout commonSetting = findViewById(R.id.tongyong);
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
        LinearLayout quitSetting = findViewById(R.id.tongyong);
        quitSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }
}
