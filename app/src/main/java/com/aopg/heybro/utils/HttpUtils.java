package com.aopg.heybro.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by 王伟健 on 2018-06-11.
 */

public class HttpUtils {
    private static final String TAG = "app";
    public static String BASE_URL = "http://101.200.59.121:8082/android/";
    public static String BUILD_URL(String uri){
        if (LoginInfo.user.getAccessToken()!=null&&LoginInfo.user.getAccessToken()!=""){
            return BASE_URL+uri+"&access_token="+LoginInfo.user.getAccessToken();
        }else{
            return BASE_URL+uri;
        }
    }

    public static OkHttpClient init(OkHttpClient client){
        //创建OkHttpClient对象，添加认证头部信息
        client = new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .authenticator(new Authenticator()
                {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException
                    {//401，认证
                        String credential = Credentials.basic("heybro", "heybro");
                        return response.request().newBuilder().header("Authorization", credential).build();
                    }
                })
                .build();
        return client;
    }
}
