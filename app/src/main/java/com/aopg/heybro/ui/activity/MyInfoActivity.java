package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.inro.DateChooseWheelViewDialog;
import com.aopg.heybro.utils.LoginInfo;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;

/**
 * Created by 壑过忘川 on 2018/6/6.
 * 我的资料界面
 */

public class MyInfoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_ziliao);
        //返回按钮
        ImageView info_back = findViewById(R.id.info_back);
        info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //头像

        //昵称
        EditText nicheng=findViewById(R.id.myName);
        String name= String.valueOf(nicheng.getText());
        //ID(不可修改)
        TextView id=findViewById(R.id.userId);
        //地区
        TextView userpo=findViewById(R.id.user_location);
        userpo.setText(LoginInfo.user.getUserProvince()+"  "+LoginInfo.user.getUserCity());
        //简介
        EditText userintro=findViewById(R.id.user_intro);
        String intro= String.valueOf(userintro.getText());
        //性别
        TextView sex=findViewById(R.id.sex);
        Intent intent = this.getIntent();        //获取已有的intent对象
        Bundle bundle = intent.getExtras();    //获取intent里面的bundle对象
        String s = bundle.getString("sex");    //获取Bundle里面的字符串
        if (s==null||s.equals("")||s==""){
            sex.setText(LoginInfo.user.getUserSex());
        }else {
            sex.setText(s);
        }
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sexIntent = new Intent(getApplicationContext(), Mysex.class);
                startActivity(sexIntent);
            }
        });
        //二维码
        LinearLayout erweima=findViewById(R.id.erweima);
        erweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toastTip
                        = Toast.makeText(getApplicationContext(),
                        "该模块待开发",
                        Toast.LENGTH_LONG);
                toastTip.setGravity(Gravity.CENTER, 0, 0);
                toastTip.show();
            }
        });
        //生日
        LinearLayout mybirtnday=findViewById(R.id.birthday);
        mybirtnday.setOnClickListener(new View.OnClickListener() {
            private TextView mShowContentTextView;
            @Override
            public void onClick(View v) {
                DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(MyInfoActivity.this,
                        new DateChooseWheelViewDialog.DateChooseInterface() {
                            @Override
                            public void getDateTime(String time, boolean longTimeChecked) {
                                mShowContentTextView.setText(time);
                            }
                        });
                endDateChooseDialog.setTimePickerGone(true);
                endDateChooseDialog.setDateDialogTitle("结束时间");
                endDateChooseDialog.showDateChooseDialog();
            }
        });
        //修改
        Button xiugai=findViewById(R.id.xiugai);
        xiugai.setOnClickListener(new View.OnClickListener() {
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
