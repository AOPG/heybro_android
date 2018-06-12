package com.aopg.heybro;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.entity.User;
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
                    String result = response.body().string();
                    JSONObject userInfo = JSONObject.
                            parseObject((JSONObject.parseObject(result)).getString("data"));
                    String success = (JSONObject.parseObject(result)).getString("success");
                    if (null!=success&&success.equals("true")) {

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
                        System.out.println(LoginInfo.user.getNickName());
                        LoginInfo.user.updateAll("userName = ?",userInfo.getString("userName"));
                        if (LoginInfo.FragmentMYISCREATE==1){
                            Intent intent = new Intent("FragmentMy");
                            sendBroadcast(intent);
                        }
                    } else {
//                        User user = new User();
//                        user.
                    }
                }
            });

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
