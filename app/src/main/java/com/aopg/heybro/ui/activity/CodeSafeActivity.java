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
 * 账号与安全界面
 */

public class CodeSafeActivity extends Activity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_settings_zhanghao);

        ImageView code_back = findViewById(R.id.code_back);
        //返回按钮
        code_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //修改密码
        LinearLayout linearLayout=findViewById(R.id.yinsi);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CodeSafeActivity.this,NewPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }
}
