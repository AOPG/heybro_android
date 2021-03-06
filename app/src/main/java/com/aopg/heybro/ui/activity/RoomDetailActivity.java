package com.aopg.heybro.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.ui.adapter.RoomDetailUserAdapter;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;

/**
 * Created by 王伟健 on 2018-06-20.
 */

public class RoomDetailActivity extends AppCompatActivity {
    private OkHttpClient client;
    private TextView roomNameTv;
    private TextView roomModelTv;
    private TextView roomPasswordTv;
    private String roomName;
    private Long roomId;
    private String roomModel;
    private String roomPassword;
    private ImageView back;
    private GridView userGrid;
    private MainHandler mainHandler;
    private Button finishGame;
    private Dialog dialog;
    private BasketRoomInfo basketRoomInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_detailmsg_master);

        client = HttpUtils.init(client);
        mainHandler = new MainHandler();
        initView();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        roomName = getIntent().getStringExtra("roomName");
        roomId = getIntent().getLongExtra("roomId",-1L);
        roomNameTv.setText(roomName);
    }

    public void initView(){
        roomNameTv = findViewById(R.id.roomName);
        roomModelTv = findViewById(R.id.roomModel);
        roomPasswordTv = findViewById(R.id.roomPassword);
        back = findViewById(R.id.back);
        roomId = getIntent().getLongExtra("roomId",-1L);
        roomNameTv.setText(getIntent().getStringExtra("roomName"));
        userGrid = findViewById(R.id.user_list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        finishGame = findViewById(R.id.finish_game);
        dialog = new Dialog(this,R.style.finishGameDialog);
        dialog.setContentView(R.layout.game_over_dialog);
        finishGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        RadioGroup zhanji = dialog.findViewById(R.id.zhanjiID);
        zhanji.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton choise = dialog.findViewById(id);

//                String output = choise.getText().toString();
               // Toast.makeText(RoomDetailActivity.this, "你的选择为：" + output, Toast.LENGTH_SHORT).show();

            }
        });



        Request request = new Request.Builder().
                url(BUILD_URL("BasketBallRoom/roomAndUserInfo?roomId="+roomId)).build();
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
                if (null!=success&&success.equals("true")) {
                    JSONObject roomInfo = (JSONObject)((JSONObject.parseObject(result)).get("data"));
                    JSONArray userInfo = roomInfo.getJSONArray("list");
                    basketRoomInfo = new BasketRoomInfo();
                    basketRoomInfo.setMaster(roomInfo.getString("roomMasterCode"));
                    basketRoomInfo.setRoomName(roomInfo.getString("roomName"));
                    basketRoomInfo.setRoomId(Long.parseLong(roomInfo.getString("roomId")));
                    basketRoomInfo.setPassword(roomInfo.getString("roomPass"));
                    basketRoomInfo.setMode(roomInfo.getString("roomMode"));
                    List<User> userList = new ArrayList<>();
                    for (int i = 0; i < userInfo.size(); i++) {
                        User user = new User();
                        user.setUserCode(((JSONObject)userInfo.get(i)).getString("user_code"));
                        user.setUsername(((JSONObject)userInfo.get(i)).getString("user_name"));
                        user.setUserIntro(((JSONObject)userInfo.get(i)).getString("user_Intro"));
                        user.setUserPortrait(((JSONObject)userInfo.get(i)).getString("user_portrait"));
                        user.setNickName(((JSONObject)userInfo.get(i)).getString("user_nickname"));
                        userList.add(user);
                    }
                    Map roomAndUserInfo = new HashMap();
                    roomAndUserInfo.put("userList",userList);
                    roomAndUserInfo.put("roomInfo",basketRoomInfo);
                    Message message = mainHandler.obtainMessage(300,roomAndUserInfo);
                    mainHandler.sendMessage(message);
                }
            }
        });



    }
    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 300:
                    if (msg.obj!=null){
                        Map roomAndUserInfo = (Map) msg.obj;
                        List<User> userList = (List<User>) roomAndUserInfo.get("userList");
                        BasketRoomInfo basketRoomInfo = (BasketRoomInfo) roomAndUserInfo.get("roomInfo");
                        roomId = basketRoomInfo.getRoomId();
                        roomModel = basketRoomInfo.getMode();
                        roomModelTv.setText(roomModel);
                        roomName = basketRoomInfo.getRoomName();
                        roomNameTv.setText(roomName);
                        roomPassword = basketRoomInfo.getPassword();
                        roomPasswordTv.setText(roomPassword);
                        RoomDetailUserAdapter roomDetailUserAdapter = new
                                RoomDetailUserAdapter(RoomDetailActivity.this,userList);
                        userGrid.setAdapter(roomDetailUserAdapter);

                        dealDialogEvent();
                    }
                    break;
                case 301:
                    if ((msg.obj).equals("exitSuccess")){
                        LoginInfo.user.setRoomId(0l);
                        Intent intentToMainActivity = new Intent();
                        intentToMainActivity.setClass(RoomDetailActivity.this, MainActivity.class);
                        startActivity(intentToMainActivity);
                        Toast.makeText(getApplicationContext(),"退出群聊成功!",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"退出群聊失败!",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    //dialog框内事件绑定
    public void dealDialogEvent(){
        Button directExit = dialog.findViewById(R.id.cancel_score);
        Button ensureScore = dialog.findViewById(R.id.ensure_score);
        directExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接退出事件
                if (basketRoomInfo.getMaster().equals(LoginInfo.user.getUserCode())){
                    //解散聊天室
                    JMessageClient.adminDissolveGroup(basketRoomInfo.getRoomId(), new BasicCallback() {
                        @Override
                        public void gotResult(int responseCode, String responseMessage) {
                            if (0 == responseCode) {
                                Toast.makeText(getApplicationContext(), "解散群组成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "解散群组失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    JMessageClient.exitGroup(basketRoomInfo.getRoomId(), new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                LoginInfo.user.setRoomId(0L);
                                Toast.makeText(getApplicationContext(), "退出聊天成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "退出聊天失败", Toast.LENGTH_SHORT).show();
                                Log.i("ExitGroupActivity", "JMessageClient.exitGroup " + ", responseCode = " + i + " ; Desc = " + s);
                            }
                        }
                    });
                }
                //删除服务器
                exitRoom();

            }
        });

        ensureScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接退出事件
                if (basketRoomInfo.getMaster().equals(LoginInfo.user.getUserCode())){
                    //解散聊天室
                    JMessageClient.adminDissolveGroup(basketRoomInfo.getRoomId(), new BasicCallback() {
                        @Override
                        public void gotResult(int responseCode, String responseMessage) {
                            if (0 == responseCode) {
                                Toast.makeText(getApplicationContext(), "解散群组成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "解散群组失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    JMessageClient.exitGroup(basketRoomInfo.getRoomId(), new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                LoginInfo.user.setRoomId(0L);
                                Toast.makeText(getApplicationContext(), "退出聊天成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "退出聊天失败", Toast.LENGTH_SHORT).show();
                                Log.i("ExitGroupActivity", "JMessageClient.exitGroup " + ", responseCode = " + i + " ; Desc = " + s);
                            }
                        }
                    });
                }
                //删除服务器
                exitRoom();
            }
        });
    }

    public void exitRoom(){
        client = HttpUtils.init(client);
        Request request = new Request.Builder().
                url(BUILD_URL("BasketBallRoom/exitRoom?roomId=" + basketRoomInfo.getRoomId()
                +"&userCode="+LoginInfo.user.getUserCode())).build();
        Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                String success = (JSONObject.parseObject(result)).getString("success");
                if (null!=success&&success.equals("true")) {
                    Message message = mainHandler.obtainMessage(301,"exitSuccess");
                    mainHandler.sendMessage(message);
                }else {
                    Message message = mainHandler.obtainMessage(301,"exitFailure");
                    mainHandler.sendMessage(message);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();
    }
}
