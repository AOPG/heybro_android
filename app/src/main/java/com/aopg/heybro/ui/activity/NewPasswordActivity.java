package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aopg.heybro.R;
import com.aopg.heybro.utils.LoginInfo;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;

/**
 * Created by 陈燕博 on 2018/6/24.
 */

public class NewPasswordActivity extends Activity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_settings_zhanghao_xiugaimima);
        final EditText newpassword1=findViewById(R.id.newpassord1);
        final EditText newpassword2=findViewById(R.id.newpassord2);
        Button xiugai=findViewById(R.id.passwordxiugai);
        xiugai.setEnabled(false);
        newpassword1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ("".equals(newpassword1.getText())){
                    Toast toastTip
                            = Toast.makeText(getApplicationContext(),
                            "密码不能为空",
                            Toast.LENGTH_LONG);
                    toastTip.setGravity(Gravity.CENTER, 0, 0);
                    toastTip.show();
                }
            }
        });
        newpassword2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ("".equals(newpassword2.getText())||newpassword1.getText().equals(newpassword2.getText())){
                    Toast toastTip
                            = Toast.makeText(getApplicationContext(),
                            "重复密码不正确",
                            Toast.LENGTH_LONG);
                    toastTip.setGravity(Gravity.CENTER, 0, 0);
                    toastTip.show();
                }
            }
        });
        //如果旧密码正确并且确认密码也正确则按钮可点击
    }
}
