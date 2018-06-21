package com.aopg.heybro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.entity.Concern;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.ui.adapter.MyConcernAdapter;
import com.aopg.heybro.ui.adapter.RoomDetailUserAdapter;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String roomId;
    private String roomModel;
    private String roomPassword;
    private ImageView back;
    private GridView userGrid;
    private MainHandler mainHandler;


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
        roomId = getIntent().getStringExtra("roomId");
        roomNameTv.setText(roomName);
    }

    public void initView(){
        roomNameTv = findViewById(R.id.roomName);
        roomModelTv = findViewById(R.id.roomModel);
        roomPasswordTv = findViewById(R.id.roomPassword);
        back = findViewById(R.id.back);
        roomId = getIntent().getStringExtra("roomId");
        roomNameTv.setText(getIntent().getStringExtra("roomName"));
        userGrid = findViewById(R.id.user_list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    BasketRoomInfo basketRoomInfo = new BasketRoomInfo();
                    basketRoomInfo.setMaster(roomInfo.getString("roomMasterCode"));
                    basketRoomInfo.setRoomName(roomInfo.getString("roomName"));
                    basketRoomInfo.setRoomId(Integer.parseInt(roomInfo.getString("roomId")));
                    basketRoomInfo.setPassword(roomInfo.getString("roomPass"));
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
                        roomId = basketRoomInfo.getRoomId()+"";
                        roomModel = basketRoomInfo.getMode();
                        roomModelTv.setText(roomModel);
                        roomName = basketRoomInfo.getRoomName();
                        roomNameTv.setText(roomName);
                        roomPassword = basketRoomInfo.getPassword();
                        roomPasswordTv.setText(roomPassword);
                        RoomDetailUserAdapter roomDetailUserAdapter = new
                                RoomDetailUserAdapter(RoomDetailActivity.this,userList);
                        userGrid.setAdapter(roomDetailUserAdapter);
                    }
                    break;
            }
        }
    }
}
