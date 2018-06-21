package com.aopg.heybro.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.entity.Concern;
import com.aopg.heybro.ui.activity.AddFriendActivity;
import com.aopg.heybro.ui.activity.ChartRoomActivity;
import com.aopg.heybro.ui.activity.MyConcernActivity;
import com.aopg.heybro.ui.activity.MyMessageActivity;
import com.aopg.heybro.ui.adapter.MyConcernAdapter;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;
import com.aopg.heybro.utils.teamhead.utils.DensityUtils;
import com.aopg.heybro.utils.teamhead.view.SynthesizedImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;
import static com.aopg.heybro.utils.HttpUtils.BASE_URL;
import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;


/**
 * Created by 王伟健 on 2018-03-16.
 * 好友界面
 */

public class FragmentFriend extends Fragment {

    private View rootView;
    private OkHttpClient client;
    private MainHandler mainHandler;
    private TextView roomNameTv;
    private TextView roomPeopleNumTv;
    private RelativeLayout fangjian;
    private SynthesizedImageView roomImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e("", "onCreateView");
        if (rootView == null) {
            Log.e("", "FragmentFriend");
            rootView = inflater.inflate(R.layout.fragment_friend,container,false);
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        mainHandler = new MainHandler();
        roomNameTv = rootView.findViewById(R.id.roomName);
        roomPeopleNumTv = rootView.findViewById(R.id.roomPeopleNum);
        roomImage = rootView.findViewById(R.id.roomImage);

        /**
         * 跳转添加好友界面
         */
        ImageView addFriend = rootView.findViewById(R.id.add);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(addIntent);
            }
        });
        /**
         * 跳转消息界面
         */
        ImageView myMessage = rootView.findViewById(R.id.xinxi);
        myMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messageIntent = new Intent(getActivity(), MyMessageActivity.class);
                startActivity(messageIntent);
            }
        });
        /**
         * 跳转关注列表界面
         */
        RelativeLayout concern = rootView.findViewById(R.id.guanzhuliebiao);
        concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent concernIntent = new Intent(getActivity(), MyConcernActivity.class);
                startActivity(concernIntent);
            }
        });

        /**
         * 跳转我的房间界面
         * */
        fangjian = rootView.findViewById(R.id.fangjian);
        fangjian.setClickable(false);

        if (LoginInfo.user.getRoomId()!=0){
            loadRoomInfo();
            getRoomUserImage();
        }else {
            roomNameTv.setText("您还没有加入任何房间!");
            roomPeopleNumTv.setText("");
            roomImage.setVisibility(ImageView.GONE);
        }
        return rootView;
    }
    private void loadRoomInfo(){
        client = HttpUtils.init(client);
        Request request = new Request.Builder().
                url(BUILD_URL("BasketBallRoom/singleRoomInfo?roomId=" + LoginInfo.user.getRoomId())).build();
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
                    JSONObject roomInfo =
                            (JSONObject)((JSONObject.parseObject(result)).get("data"));
                    BasketRoomInfo basketRoomInfo = new BasketRoomInfo();
                    basketRoomInfo.setMaster(roomInfo.getString("masterCode"));
                    basketRoomInfo.setNum(Integer.parseInt(roomInfo.getString("roomNum")));
                    basketRoomInfo.setRoomRealNum(Integer.parseInt(roomInfo.getString("roomPeo")));
                    basketRoomInfo.setRoomName(roomInfo.getString("roomName"));
                    Message message = mainHandler.obtainMessage(400,basketRoomInfo);
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
                case 400:
                    fangjian.setClickable(true);
                    BasketRoomInfo basketRoomInfo = (BasketRoomInfo) msg.obj;
                    roomNameTv.setText(basketRoomInfo.getRoomName());
                    roomPeopleNumTv.setText(basketRoomInfo.getRoomRealNum()
                            +"/"+basketRoomInfo.getNum());
                    final String roomName = basketRoomInfo.getRoomName();
                    fangjian.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent wodefangjianIntent = new Intent(getActivity(), ChartRoomActivity.class);
                            if (LoginInfo.user.getRoomId()==0){
                                Toast.makeText(getApplicationContext(),"您当前没有任何房间!",Toast.LENGTH_SHORT);
                            }else {
                                wodefangjianIntent.putExtra("roomId",LoginInfo.user.getRoomId());
                                wodefangjianIntent.putExtra("roomName",roomName);
                                startActivity(wodefangjianIntent);
                            }

                        }
                    });
                    break;
                case 401:
                    ArrayList<String> imageUrls = (ArrayList<String>) msg.obj;
                    int imageSize = DensityUtils.dp2px(getApplicationContext(), 120);
                    roomImage.displayImage(imageUrls)
                        .synthesizedWidthHeight(imageSize, imageSize)
                        .defaultImage(R.mipmap.ic_launcher_round)
                        .load();
                    break;

            }
        }
    }

    private void getRoomUserImage(){
        client = HttpUtils.init(client);
        Request request = new Request.Builder().
                url(BUILD_URL("BasketBallRoom/roomAndUserInfo?roomId=" + LoginInfo.user.getRoomId())).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<String> userImageList = new ArrayList<>();
                String result = response.body().string();
                String success = (JSONObject.parseObject(result)).getString("success");
                if (null!=success&&success.equals("true")) {
                    JSONArray usersInfo =
                            ((JSONObject)((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
                    for (int i = 0; i < usersInfo.size(); i++) {
                        JSONObject user = (JSONObject) usersInfo.get(i);
                        userImageList.add(BASE_URL+user.getString("user_portrait"));
                    }
                    Message message = mainHandler.obtainMessage(401,userImageList);
                    mainHandler.sendMessage(message);
                }
            }
        });
    }

}
