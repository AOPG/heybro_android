package com.aopg.heybro.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.MainActivity;
import com.aopg.heybro.UserInfoService;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.ui.activity.LoginActivty;

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
 * Created by 王伟健 on 2018-06-11.
 */

public class LoginInfo {
    public static User user = new User();
    //0为未创建FragementMy界面，1为已创建
    public static Integer FragmentMYISCREATE = 0;
    //0为未创建FragementDriend界面，1为已创建
    public static Integer FragmentFriendISCREATE = 0;
    //0为未登录IM，1为已登录IM
    public static Integer ISLOGINIM = 0;

}
