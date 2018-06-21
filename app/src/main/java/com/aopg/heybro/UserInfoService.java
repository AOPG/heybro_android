package com.aopg.heybro;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.im.InitIM;
import com.aopg.heybro.ui.activity.LoginActivty;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aopg.heybro.utils.HttpUtils.*;

/**
 * Created by 王伟健 on 2018-06-11.
 */

public class UserInfoService extends IntentService {
    private OkHttpClient client;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public UserInfoService() {
        super("LoadUserInfoService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println("service被调用！--------------------------------------");
            client = HttpUtils.init(client);
            Request request = new Request.Builder().
                    url(BUILD_URL("averageUser/userInfo?username=" + LoginInfo.user.getUsername())).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {//4.回调方法
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("接受消息--------------------------------------");
                    String result = response.body().string();

                    String success = (JSONObject.parseObject(result)).getString("success");
                    if (null!=success&&success.equals("true")) {
                        JSONObject userInfo = JSONObject.
                                parseObject((JSONObject.parseObject(result)).getString("data"));
                        JSONObject userDetailInfo = userInfo.getJSONObject("userInfo");
                        LoginInfo.user.setNickName(userInfo.getString("userNickname"));
                        LoginInfo.user.setUserSignature(userInfo.getString("userSignature"));
                        LoginInfo.user.setUserGrade(userInfo.getInteger("userGrade"));
                        LoginInfo.user.setUserPhone(userInfo.getString("userPhone"));
                        LoginInfo.user.setUserMail(userInfo.getString("userMail"));
                        LoginInfo.user.setUserCode(userInfo.getString("userCode"));
                        LoginInfo.user.setUserIntro(userInfo.getString("userIntro"));
                        LoginInfo.user.setUserCode(userInfo.getString("userCode"));
                        LoginInfo.user.setUserPortrait(userInfo.getString("userPortrait"));
                        LoginInfo.user.setHomepageBack(userInfo.getString("homepageBack"));
                        LoginInfo.user.setRoomId(Long.parseLong(userDetailInfo.getString("roomId")));
                        LoginInfo.user.setBirthday(Long.parseLong(userInfo.getString("birthday")));
                        LoginInfo.user.setUserProvince(userInfo.getString("userProvince"));
                        LoginInfo.user.setUserCity(userInfo.getString("userCity"));
                        LoginInfo.user.setUserSex(userInfo.getString("userSex"));
                        System.out.println("正在向内存中写入数据--------------------------------");
                        LoginInfo.user.updateAll("userName = ?",userInfo.getString("userName"));
                        if (LoginInfo.FragmentMYISCREATE==1){
                            System.out.println("向MainActivity发送广播！--------------------------------");
                            Intent intent = new Intent("FragmentMy");
                            sendBroadcast(intent);
                        }
                        if (LoginInfo.ISLOGINIM==0){
                            InitIM.initJmessageUser(LoginInfo.user.getUsername(),
                                    LoginInfo.user.getUserCode(),UserInfoService.this);
                        }
                    } else {

                    }
                }
            });
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
