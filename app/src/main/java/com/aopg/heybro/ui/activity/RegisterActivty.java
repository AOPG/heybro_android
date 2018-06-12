package com.aopg.heybro.ui.activity;


import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.Common.ActivitiesManager;
import com.aopg.heybro.ui.register.isMobile;

import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.OkHttpClient;

/**
 * Created by 王攀 on 2018/5/31.
 * 登录Activty
 */

public class RegisterActivty extends AppCompatActivity{


    private ProgressDialog mProgressDialog = null;

    private String rigsterPhoneNum;
    private String Validation;
    private String registerPass;
    private EditText textPhoneNum;
    private EditText textValidation;
    private EditText textPass;
    private int flag = 1;
    private int flag2 = 1;
    //判断字符串是否为数字
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ActivitiesManager.getInstance().addActivity(this);

        TextView cancelBtn = findViewById(R.id.register_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textValidation = findViewById(R.id.register_validation);
        textPass = findViewById(R.id.register_password);
        textPhoneNum = findViewById(R.id.register_phone);

        Button btn = findViewById(R.id.register_show);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag2 %2 !=0){
                    textPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    // 使光标始终在最后位置
                    Editable etable = textPass.getText();
                    Selection.setSelection(etable, etable.length());
                }else{
                    // 显示为密码
                    textPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    // 使光标始终在最后位置
                    Editable etable = textPass.getText();
                    Selection.setSelection(etable, etable.length());
                }
                flag2++;

            }
        });


        //注册按钮
        Button rigsterBtn = findViewById(R.id.register);
        rigsterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //手机号
                rigsterPhoneNum = textPhoneNum.getText().toString();
                //验证码
                Validation = textValidation.getText().toString();
                //密码
                registerPass = textPass.getText().toString();
                flag = 1;

                //手机号验证
                if (!isMobile.isChinaPhoneLegal(rigsterPhoneNum)){
                    Toast.makeText(getApplicationContext(), "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    flag = 0;
                }

                //验证码验证
                if(Validation.length() == 0 && flag == 1){
                    Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
                    flag = 0;
                }
                if(!isInteger(Validation) && flag == 1){
                    Toast.makeText(getApplicationContext(), "验证码必须是4位数字", Toast.LENGTH_SHORT).show();
                    flag = 0;
                }
                if (Validation.length()!=4 && flag == 1){
                    Toast.makeText(getApplicationContext(), "验证码必须是4位数字", Toast.LENGTH_SHORT).show();
                    flag = 0;
                }

                //密码验证
                if (registerPass.length()!=8 && flag == 1){
                    Toast.makeText(getApplicationContext(), "请输入8位密码", Toast.LENGTH_SHORT).show();
                    flag = 0;
                }

                System.out.println(rigsterPhoneNum);
                System.out.println(Validation);
                System.out.println(registerPass);





            }
        });



        //发送验证码
        Button registerValidation = findViewById(R.id.register_validation_btn);
        registerValidation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode("86","15732167182");
            }
        });


    }


    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else{
                    // TODO 处理错误的结果
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }
        });

        textPhoneNum = findViewById(R.id.register_phone);
        rigsterPhoneNum = textPhoneNum.getText().toString();
        System.out.println(rigsterPhoneNum);
        if (isMobile.isChinaPhoneLegal(rigsterPhoneNum)){
            // 触发操作
             SMSSDK.getVerificationCode(country, phone);
        }else {
            Toast.makeText(getApplicationContext(), "手机号码格式不正确", Toast.LENGTH_SHORT).show();
        }

    }


    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                } else{
                    // TODO 处理错误的结果
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    };
}