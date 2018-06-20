package com.aopg.heybro.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.aopg.heybro.utils.LoginInfo.user;

/**
 * Created by 王伟健 on 2018-06-20.
 */

public class TimerTaskService extends Service {

    private OkHttpClient client;
    private final Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 200;
            handler.sendMessage(message);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==200){
                if (LoginInfo.user.getIsLogin()==1){
                    client = HttpUtils.init(client);
                    relink(client);
                }else {
                    Log.e("handleMessage","您还未登录！----------");
                }
            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("TimerTaskService","TimerTaskService服务生成！");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TimerTaskService","TimerTaskService服务被调用！");
        Long loginTime = user.getLoginTime();
        Integer expiresIn = user.getExpiresIn();
        Long expirTime = loginTime+(expiresIn-10)*1000;
        Long nowTime = new java.util.Date().getTime();
        if (expirTime>nowTime){
            //此时token未过期，在residualTime后刷新，时间间隔为7200秒
            Long residualTime = expirTime-nowTime;
            Log.e("relink","正在等待刷新access_token，剩余时间"+residualTime);
            timer.schedule(task, residualTime, 7200000);
        }else{
            //此时已经access_token已经过期，用户界面已经出错需立即重连，每过52000000刷新token
            Log.e("relink","access_token已经过期，立刻重连");
            timer.schedule(task, 0, 5200000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy invoke");
        super.onDestroy();
    }

    //超时重连
    public void relink(OkHttpClient client){
            RequestBody requestBody=new FormBody.Builder()
                    .add("grant_type","refresh_token")
                    .add("refresh_token",user.getRefreshToken())
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
                        user.setIsLogin(0);
                        Log.e("relink","重连失败！----------"+msgRes);
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
                        user.updateAll("userName = ?",LoginInfo.user.getUsername());
                        LoginInfo.user =  DataSupport.where("userName = ?",LoginInfo.user.getUsername()).findFirst(User.class);
                        Log.e("relink","重连成功！----------");
                    }
                }
            });

    }
}
