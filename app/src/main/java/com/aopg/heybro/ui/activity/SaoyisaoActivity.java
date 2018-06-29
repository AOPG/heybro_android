package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.aopg.heybro.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.common.HybridBinarizer;

/**
 * Created by 壑过忘川 on 2018/6/6.
 * 扫一扫界面
 */

public class SaoyisaoActivity extends Activity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_saoyisao);

        ImageView saoyisao_back = findViewById(R.id.saoyisao_back);
        //返回按钮
        saoyisao_back.setOnClickListener(new View.OnClickListener() {
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
