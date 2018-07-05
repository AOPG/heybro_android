package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.Concern;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.ui.adapter.MyConcernAdapter;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mzule.activityrouter.annotation.Router;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aopg.heybro.utils.HttpUtils.BASE_URL;
import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;

/**
 * Created by L on 2018/6/25.
 */


@Router("userInfo/:userCode")
public class FriendInfoActivity extends Activity {

    private String userCode;
    private User user;
    private ImageView userimg;
    private TextView username;
    private TextView usercode;
    private TextView userprovince;
    private TextView usercity;
    private TextView usergrade;
    private TextView qianming;
    private ImageView back;
    private Button guanzhu;
    private Button xiaoxi;
    private OkHttpClient client;
    private MainHandler mainHandler = new MainHandler();
    private boolean isConcern = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_msg);

        userimg=findViewById(R.id.friend_img);
        username=findViewById(R.id.user_name);
        usercode=findViewById(R.id.userId);
        userprovince=findViewById(R.id.user_province);
        usercity=findViewById(R.id.user_city);
        usergrade=findViewById(R.id.user_class);
        qianming=findViewById(R.id.qianming);
        back=findViewById(R.id.back);
        guanzhu=findViewById(R.id.guanzhu);
        xiaoxi=findViewById(R.id.liaotian);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent=getIntent();
        userCode = intent.getStringExtra("userCode");
        loadUserInfo(userCode);
    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }
    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    xiaoxi.setVisibility(View.VISIBLE);
                    xiaoxi.setBackgroundDrawable(getResources().getDrawable(R.drawable.unlogin));
                    xiaoxi.setOnClickListener(new chatListener());
                    guanzhu.setBackgroundDrawable(getResources().getDrawable(R.drawable.unlogin));
                    guanzhu.setText("取消关注");

                    //concern/cancelConcern?userCode=123456789&concernCode=111111
//                    guanzhu.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            client = HttpUtils.init(client);
//                            //concern/cancelConcern?userCode=123456789&concernCode=111111
//                            Request request = new Request.Builder().
//                                    url(BUILD_URL("concern/cancelConcern?userCode="+ LoginInfo.user.getUserCode()+"&concernCode="+userCode)).build();
//               0             Call call = client.newCall(request);
//                            call.enqueue(new okhttp3.Callback() {
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//
//                                }
//
//                                @Override
//                                public void onResponse(Call call, Response response) throws IOException {
//
//                                }
//                            });
//                        }
//                    });

                    //guanzhu.setOnClickListener(
                     //       null);
                case 3:

                    guanzhu.setBackgroundDrawable(getResources().getDrawable(R.drawable.tianqibgm));
                    guanzhu.setText("关注");
                    xiaoxi.setVisibility(View.INVISIBLE);
                    guanzhu.setOnClickListener(new CancelConcern());
                case 1000:
                    showUserInfo();
                    if (isConcern==true){
                        guanzhu.setBackgroundDrawable(getResources().getDrawable(R.drawable.unlogin));
                        guanzhu.setText("取消关注");
                        guanzhu.setOnClickListener(null);
                    }

            }
        }
    }

    public void showUserInfo(){
        String userPortrait=user.getUserPortrait();//intent.getStringExtra("userportrait");
        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();
        Glide.with(this)
                .load(BASE_URL+ userPortrait)
                .apply(options)
                .into(userimg);
        String userName=user.getUsername();//intent.getStringExtra("username");
        username.setText(userName);
        usercode.setText(userCode);
        String userProvince=user.getUserProvince();//intent.getStringExtra("userprovince");
        userprovince.setText(userProvince);
        String userCity=user.getUserCity();//intent.getStringExtra("usercity");
        usercity.setText(userCity);
        int userGrade = user.getUserGrade();//intent.getStringExtra("usergrade");
        //usergrade.setText(userGrade);
        String userintro=user.getUserIntro();//intent.getStringExtra("userintro");
        qianming.setText(userintro);
        if( LoginInfo.user.getUserCode()==userCode){
            guanzhu.setVisibility(View.INVISIBLE);
            xiaoxi.setVisibility(View.INVISIBLE);
        }
        guanzhu.setOnClickListener(new concernListener());
//                new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//                client = HttpUtils.init(client);
//                Request request = new Request.Builder().
//                        url(BUILD_URL("concern/concernByUserCode?userCode="+ LoginInfo.user.getUserCode()+"&concernCode="+userCode)).build();
//                Call call = client.newCall(request);
//                call.enqueue(new Callback() {//4.回调方法
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String result = response.body().string();
//                        String success = (JSONObject.parseObject(result)).getString("success");
//                        if (null!=success&&success.equals("true")) {
//                            Message message = mainHandler.obtainMessage(2,"formCallBack");
//                            mainHandler.sendMessage(message);
//                        }
//                        Log.e("msg", result);
//                    }
//                });
//            }
//        });
    }

    public void loadUserInfo(String userCode){
        client = HttpUtils.init(client);
        Request request = new Request.Builder().
                url(BUILD_URL("averageUser/userInfoConcernByCode?userCode="+LoginInfo.user.getUserCode()+"&concernCode="+userCode)).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("测试"+result+"123");
                String success = (JSONObject.parseObject(result)).getString("success");
                if (null!=success&&success.equals("true")) {
                    JSONObject userInfo = (JSONObject) (JSONObject.parseObject(result)).get("data");
                    String userName = userInfo.getString("userName");
                    String userPortrait=userInfo.getString("userPortrait");
                    String userIntro= userInfo.getString("userIntro");
                    int userGrade= Integer.parseInt(userInfo.getString("userGrade"));
                    String userCity= userInfo.getString("userCity");
                    String userProvince= userInfo.getString("userProvince");
                    String isConcernStr = userInfo.getString("isConcern");
                    String userNickName = userInfo.getString("userNickname");

                    if (isConcernStr.equals("true")){
                        isConcern = true;
                    }
                    user = new User();
                    user.setUserProvince(userProvince);
                    user.setUserCity(userCity);
                    user.setUserGrade(userGrade);
                    user.setUserIntro(userIntro);
                    user.setUserPortrait(userPortrait);
                    user.setUsername(userName);
                    user.setNickName(userNickName);
                    Message message = mainHandler.obtainMessage(1000,user);
                    mainHandler.sendMessage(message);
                }
            }
        });
    }

    private class  CancelConcern implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            client = HttpUtils.init(client);
            //concern/cancelConcern?userCode=123456789&concernCode=111111
            Request request = new Request.Builder().
                    url(BUILD_URL("concern/cancelConcern?userCode="+ LoginInfo.user.getUserCode()+"&concernCode="+userCode)).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result1 = response.body().string();
                    String success = (JSONObject.parseObject(result1)).getString("success");
                    if (null!=success&&success.equals("true")) {
                        Message message = mainHandler.obtainMessage(3,"formCallBack");
                        mainHandler.sendMessage(message);
                    }
                    Log.e("msg", result1);
                }
            });
        }
    }
    private class chatListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("note",user.getNickName());
            intent.putExtra("userConcernCode",user.getUserCode());
            intent.setClass(FriendInfoActivity.this,SingleChartActivity.class);
        }
    }

    private class  concernListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            client = HttpUtils.init(client);
            Request request = new Request.Builder().
                    url(BUILD_URL("concern/concernByUserCode?userCode="+ LoginInfo.user.getUserCode()+"&concernCode="+userCode)).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {//4.回调方法
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    String success = (JSONObject.parseObject(result)).getString("success");
                    if (null!=success&&success.equals("true")&&LoginInfo.user.getUserCode()!=userCode) {
                        Message message = mainHandler.obtainMessage(2,"formCallBack");
                        mainHandler.sendMessage(message);
                    }
                    Log.e("msg", result);
                }
            });
        }
    }
}
