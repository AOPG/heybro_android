package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.aopg.heybro.R;

/**
 * Created by asus on 2018/6/25.
 */

public class ActivityzhiboActivity extends Activity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_zhibo);

        //返回按钮
        ImageView info_back = findViewById(R.id.info_back);
        info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
