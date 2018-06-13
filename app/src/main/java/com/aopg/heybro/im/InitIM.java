package com.aopg.heybro.im;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.aopg.heybro.utils.LoginInfo;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by 王伟健 on 2018-06-11.
 */

public class InitIM {

    public static void initJmessageUser(String username, String password,final Context context){
        Looper.prepare();
        Toast.makeText(context, "正在连接聊天服务器", Toast.LENGTH_SHORT).show();
        /**=================     调用SDk登陆接口    =================*/
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String LoginDesc) {
                if (responseCode == 0) {
                    Toast.makeText(context, "登录聊天服务器成功", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                    LoginInfo.ISLOGINIM=1;
                } else {
                    Toast.makeText(context, "登录聊天服务器失败", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                    LoginInfo.ISLOGINIM=0;
                }
            }
        });
        Looper.loop();
    }
}
