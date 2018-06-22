package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.aopg.heybro.R;
import com.aopg.heybro.ui.inro.DateChooseWheelViewDialog;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static com.aopg.heybro.utils.HttpUtils.BASE_URL;

/**
 * Created by 陈燕博 on 2018/6/22.
 */

public class Mybirthday extends Activity {
    public static int FLAG1=0;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_ziliao_date);
        //头像显示
        ImageView image=findViewById(R.id.image);
        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();

        Glide.with(this)
                .load(BASE_URL+ LoginInfo.user.getUserPortrait())
                .apply(options)
                .into(image);
        //昵称
        final TextView nicheng=findViewById(R.id.user_name);
        nicheng.setText(LoginInfo.user.getNickName());
        final String[] name = {LoginInfo.user.getNickName()};
        nicheng.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                name[0] = String.valueOf(nicheng.getText());
            }
        });
        //性别
        TextView sexx=findViewById(R.id.user_sex);
        sexx.setText(LoginInfo.user.getUserSex());
        //生日
        TextView mybirtnday=findViewById(R.id.birthday);
        mybirtnday.setText(String.valueOf(LoginInfo.user.getBirthday()));
        //
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(getApplicationContext(),
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        FLAG1=1;
                        Intent birIntent = new Intent();
                        Bundle bundle = new Bundle();                           //创建Bundle对象
                        bundle.putString("birthday", time);     //装入数据
                        birIntent.putExtras(bundle);
                        startActivity(birIntent);
                    }
                });
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("生日选择");
        endDateChooseDialog.showDateChooseDialog();
    }
}
