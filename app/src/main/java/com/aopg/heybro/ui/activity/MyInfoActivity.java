package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.inro.DateChooseWheelViewDialog;

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
    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }
}
