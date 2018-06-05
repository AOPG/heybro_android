package com.aopg.heybro.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/31.
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