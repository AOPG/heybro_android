package com.aopg.heybro;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.im.InitIM;
import com.aopg.heybro.ui.fragment.FragmentBall;
import com.aopg.heybro.utils.BaiduMapLocationUtil;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;

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
    CommandReceiver cmdReceiver;
    LocationClient mLocationClient;

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
        upLoadUserLoactionInfo();
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
                        if (userDetailInfo.getString("roomId")!=null){
                            LoginInfo.user.setRoomId(Long.parseLong(userDetailInfo.getString("roomId")));
                        }else {
                            LoginInfo.user.setRoomId(0L);
                        }

                        if(userInfo.getString("birthday")!=null){
                            LoginInfo.user.setBirthday(Long.parseLong(userInfo.getString("birthday")));
                        }else{
                            LoginInfo.user.setBirthday(Long.parseLong("1529973085000"));
                        }

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
//                        if (LoginInfo.user.getRoomId()==0L){
//                            FragmentBall.setCreateRoomStates(true);
//                        }else {
//                            FragmentBall.setCreateRoomStates(false);
//                        }
                    } else {

                    }
                }
            });
    }

    private class CommandReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int cmd=intent.getIntExtra("cmd", -1);
            if(cmd==MainActivity.CMD_STOP_USER_INFO_SERVICE){//如果等于0
                stopSelf();//停止服务
            }
        }

    }


    @Override
    public void onCreate() {
        cmdReceiver=new CommandReceiver();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("userInfoService","service已销毁！");
        this.unregisterReceiver(cmdReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("stopUserInfoService");
        registerReceiver(cmdReceiver, intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    private void upLoadUserLoactionInfo(){
        client = HttpUtils.init(client);
        mLocationClient = BaiduMapLocationUtil.init(getApplicationContext(),mLocationClient);
        mLocationClient.registerLocationListener(
                new BDAbstractLocationListener(){
                    @Override
                    public void onReceiveLocation(final BDLocation bdLocation) {
                        Request request = new Request.Builder().
                                url(BUILD_URL("averageUser/upLoadUserLoactionInfo?username=" + LoginInfo.user.getUsername()+
                                        "&lat="+bdLocation.getLatitude()+"&lng="+bdLocation.getLongitude())).build();
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
                                    Log.e("LoactionInfo()","上传用户位置信息成功！");
                                    LoginInfo.user.setUserLat(bdLocation.getLatitude()+"");
                                    LoginInfo.user.setUserLng(bdLocation.getLongitude()+"");
                                }
                            }
                        });
                        mLocationClient.stop();
                    }
                }
        );

    }
}
