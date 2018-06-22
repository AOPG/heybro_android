package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;


import com.aopg.heybro.R;
import com.aopg.heybro.ui.inro.DateChooseWheelViewDialog;

/**
 * Created by 陈燕博 on 2018/6/22.
 */

public class Mybirthday extends Activity {
    public static int FLAG1=0;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_ziliao_date);
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
