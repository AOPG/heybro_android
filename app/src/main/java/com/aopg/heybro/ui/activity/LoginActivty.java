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
import android.widget.Toast;

import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.ui.Common.ActivitiesManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 王攀 on 2018/5/31.
 * 登录Activty
 */

public class LoginActivty extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_PERMISSION = 2;
    private OkHttpClient okHttp;
    private static final String TAG = "app";
    private String BASE_URL = "http://192.168.23.1:8082/android/averageUser/";
    private EditText username;
    private EditText password;
    private String userNameFlag = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ActivitiesManager.getInstance().addActivity(this);

        username = findViewById(R.id.login_user);
        password = findViewById(R.id.login_password);

        Button subBtn1 = findViewById(R.id.login);
        subBtn1.setOnClickListener(this);


        //创建OkHttpClient对象
        okHttp = new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .build();
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


    /*
      同步get请求
       */
    private void doGetSync(final String username, String password) {
        Log.e(TAG, "doGetSync: ");
        //创建请求对象

        System.out.println(username);
        System.out.println(password);
        Request request = new Request.Builder()
                .get()
                .url(BASE_URL + "ASLogin?userName=" + username + "&userPass=" + password)
                .build();

        //创建call对象，并执行请求，获得响应
        final Call call = okHttp.newCall(request);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    if (response.isSuccessful()) {

                        String flag = response.body().string();
                        System.out.println(flag);
                        System.out.println("2222");
                        if (flag.equals("true")) {
                            userNameFlag = username;
                            Intent intent2 = new Intent();
                            intent2.setComponent(new ComponentName(LoginActivty.this, MainActivity.class));
                            intent2.putExtra("position", userNameFlag);
                            startActivity(intent2);
                        } else {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    } else {
                        Log.e(TAG, response.code() + "");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                //  doGetSync();
                if (username.length() == 0 || password.length() == 0) {
                    Toast.makeText(getApplicationContext(), "账号或密码输入为空", Toast.LENGTH_SHORT).show();
                } else {
                    String Uusername = username.getText().toString();
                    String Upassword = password.getText().toString();
//                    doGetSync(Uusername, Upassword);
                    Intent intent2 = new Intent();
                    intent2.setComponent(new ComponentName(LoginActivty.this, MainActivity.class));
                    intent2.putExtra("position", userNameFlag);
                    startActivity(intent2);
                }
                break;

        }
    }
}