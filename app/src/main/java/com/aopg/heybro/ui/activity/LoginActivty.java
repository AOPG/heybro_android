package com.aopg.heybro.ui.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.UserInfoService;
import com.aopg.heybro.entity.User;

import com.aopg.heybro.ui.Common.ActivitiesManager;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by 王攀 on 2018/5/31.
 * 登录Activty
 */

public class LoginActivty extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameEt;
    private EditText passwordEt;
    private String userNameFlag = null;
    private OkHttpClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ActivitiesManager.getInstance().addActivity(this);

        usernameEt = findViewById(R.id.login_user);
        passwordEt = findViewById(R.id.login_password);

        Button subBtn1 = findViewById(R.id.login);
        subBtn1.setOnClickListener(this);

        //注册界面
        TextView registerBtn = findViewById(R.id.login_register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(LoginActivty.this, RegisterActivty.class));
                intent.putExtra("position", userNameFlag);
                startActivity(intent);
            }
        });
        client = HttpUtils.init(client);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    ActivitiesManager.getInstance().exit();
                        break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };


    private boolean doLogin(final String username,final String password){

        RequestBody requestBody=new FormBody.Builder()
                .add("username",username)
                .add("password",password)
                .add("grant_type","password")
                .build();
        Request request=new Request.Builder()
                .url(HttpUtils.BASE_URL+"oauth/token")
                .post(requestBody)
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("++++++++++++++",e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws
                    IOException {
                JSONObject jsonRes = JSONObject.parseObject(response.body().string());
                String error = jsonRes.getString("error");
                if (error!=null&&error!=""&&error!="null"){
                    String msgRes = jsonRes.getString("error_description");
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), msgRes, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else {
                    String accessToken = jsonRes.getString("access_token");
                    String refreshToken = jsonRes.getString("refresh_token");
                    Integer expiresIn = jsonRes.getInteger("expires_in");
                    User user = new User();
                    user.setAccessToken(accessToken);
                    user.setRefreshToken(refreshToken);
                    user.setExpiresIn(expiresIn);
                    user.setLoginTime(new java.util.Date().getTime());
                    user.setIsLogin(1);
                    Looper.prepare();
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(LoginActivty.this, MainActivity.class));
                    intent.putExtra("position", userNameFlag);
                    int isHave = DataSupport.where("userName = ?", username).count(User.class);
                    if (isHave>0){
                        int upCount = user.updateAll("userName = ?",username);
                        if (upCount>0){
                            LoginInfo.user =  DataSupport.where("isLogin = ?", "1").findFirst(User.class);
                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        user.setUsername(username);
                        boolean flag =  user.save();
                        if (flag) {
                            LoginInfo.user =  DataSupport.where("isLogin = ?", "1").findFirst(User.class);
                            Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    Looper.loop();
                }
            }
        });
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (usernameEt.length() == 0 || passwordEt.length() == 0) {
                    Toast.makeText(getApplicationContext(), "账号或密码输入为空", Toast.LENGTH_SHORT).show();
                } else {
                    String username = usernameEt.getText().toString();
                    String password = passwordEt.getText().toString();
                    doLogin(username, password);
                }
                break;
        }
    }
}