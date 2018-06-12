package com.aopg.heybro.im;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by 王伟健 on 2018-06-11.
 */

public class InitIM {

    public static void initJmessageUser(String username, String password,final Context context){
        final ProgressDialog mProgressDialog;
        mProgressDialog = ProgressDialog.show(context, "提示:", "正在加载中...");
        mProgressDialog.setCanceledOnTouchOutside(true);
        /**=================     调用SDk登陆接口    =================*/
        JMessageClient.login(username, password, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String LoginDesc) {
                if (responseCode == 0) {
                    mProgressDialog.dismiss();
                    Toast.makeText(context, "登录聊天服务器成功", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(context, "登录聊天服务器失败", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                }
            }
        });
    }
}
