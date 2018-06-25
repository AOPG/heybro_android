package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static com.aopg.heybro.utils.HttpUtils.BASE_URL;

/**
 * Created by L on 2018/6/25.
 */

public class FriendInfoActivity extends Activity {
    private ImageView userimg;
    private TextView username;
    private TextView usercode;
    private TextView userprovince;
    private TextView usercity;
    private TextView usergrade;
    private TextView qianming;
    private ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_msg);
        userimg=findViewById(R.id.friend_img);
        username=findViewById(R.id.user_name);
        usercode=findViewById(R.id.userId);
        userprovince=findViewById(R.id.user_province);
        usercity=findViewById(R.id.user_city);
        usergrade=findViewById(R.id.user_class);
        qianming=findViewById(R.id.qianming);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent=getIntent();
        String userPortrait=intent.getStringExtra("userportrait");

        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();
        Glide.with(this)
                .load(BASE_URL+ userPortrait)
                .apply(options)
                .into(userimg);
        String userName=intent.getStringExtra("username");
        username.setText(userName);
        String userCode=intent.getStringExtra("usercode");
        usercode.setText(userCode);
        String userProvince=intent.getStringExtra("userprovince");
        userprovince.setText(userProvince);
        String userCity=intent.getStringExtra("usercity");
        usercity.setText(userCity);
        String userGrade = intent.getStringExtra("usergrade");
        usergrade.setText(userGrade);
        String userintro=intent.getStringExtra("userintro");
        qianming.setText(userintro);
    }
}
