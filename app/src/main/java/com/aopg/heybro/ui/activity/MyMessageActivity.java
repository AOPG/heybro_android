package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aopg.heybro.R;

/**
 * Created by 壑过忘川 on 2018/6/6.
 * 消息界面
 */

public class MyMessageActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_friend_message);
        //返回按钮
        ImageView message_back = findViewById(R.id.message_back);
        message_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LinearLayout sixin = findViewById(R.id.sixin);
        sixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyMessageActivity.this,PrivateLetterActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }
}
