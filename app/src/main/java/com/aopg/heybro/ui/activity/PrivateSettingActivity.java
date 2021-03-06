package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.aopg.heybro.R;

/**
 * Created by 壑过忘川 on 2018/6/6.
 * 隐私设置界面
 */

public class PrivateSettingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_settings_yinsi);

        ImageView private_back = findViewById(R.id.private_back);
        //返回按钮
        private_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }
}
