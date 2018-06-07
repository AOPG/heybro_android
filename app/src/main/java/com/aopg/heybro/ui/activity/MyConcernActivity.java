package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.aopg.heybro.R;

/**
 * Created by 壑过忘川 on 2018/6/6.
 * 我的关注界面
 */

public class MyConcernActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_friend_guanzhu);
        //返回按钮
        ImageView concern_back = findViewById(R.id.concern_back);
        concern_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /***************测试im模块start***************/

        View view = findViewById(R.id.user_test);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /***************测试im模块end*****************/



    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }
}
