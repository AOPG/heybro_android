package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aopg.heybro.R;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;
import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;

/**
 * Created by 陈燕博 on 2018/6/24.
 */

public class NewPasswordActivity extends Activity {
    private OkHttpClient client;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_settings_zhanghao_xiugaimima);
        //返回按钮
        ImageView info_back = findViewById(R.id.info_back);
        info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        final EditText oldpassword=findViewById(R.id.oldpassord);
        final EditText newpassword1 = findViewById(R.id.newpassord1);
        final EditText newpassword2 = findViewById(R.id.newpassord2);
        Button xiugai = findViewById(R.id.passwordxiugai);
        xiugai.setEnabled(false);
        oldpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ("".equals(newpassword1.getText())) {
                    Toast toastTip
                            = Toast.makeText(getApplicationContext(),
                            "密码不能为空",
                            Toast.LENGTH_LONG);
                    toastTip.setGravity(Gravity.CENTER, 0, 0);
                    toastTip.show();
                }
            }
        });
        newpassword1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ("".equals(newpassword1.getText())) {
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
                if ("".equals(newpassword2.getText()) || newpassword1.getText().equals(newpassword2.getText())) {
                    Toast toastTip
                            = Toast.makeText(getApplicationContext(),
                            "重复密码不正确",
                            Toast.LENGTH_LONG);
                    toastTip.setGravity(Gravity.CENTER, 0, 0);
                    toastTip.show();
                }
            }
        });
        //如果确认密码也正确则按钮可点击
        if (newpassword1.equals(newpassword2)) {
            xiugai.setEnabled(true);
        }
        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改数据库信息
                client = HttpUtils.init(client);
                Request request = new Request.Builder().
                        url(BUILD_URL("averageUser/resetPassword?userCode=" + LoginInfo.user.getUserCode()+"&oldPassword="+oldpassword.getText()+"&newPassword="+newpassword1.getText()
                        )).build();
                //测试-------------------
                Call call = client.newCall(request);
                call.enqueue(new Callback() {//4.回调方法
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String result = response.body().string();
                        Log.e("msg", result);
                    }
                });
            }
        });
    }
}