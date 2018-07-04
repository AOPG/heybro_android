package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.ui.activity.ChartRoomActivity;
import com.aopg.heybro.ui.fragment.FragmentFriend;
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
 * Created by 壑过忘川 on 2018/6/21.
 * 显示所有房间的适配器
 */

public class SearchRoomAdapter extends BaseAdapter {
    private Context context;    // 上下文环境
    private int item_layout_id;// item视图布局文件
    private List<BasketRoomInfo> roomList; // 所有房间
    private BasketRoomInfo basketRoomInfo;

    private OkHttpClient client;
    private SearchRoomAdapter.MainHandler mainHandler;

    private TextView roomName;
    private TextView roomRealNum;
    private TextView roomFullNum;
    private SynthesizedImageView roomImage;//房间头像

    public SearchRoomAdapter(Context context,
                       int item_layout_id,
                       List<BasketRoomInfo> roomList) {
        this.context = context;
        this.item_layout_id = item_layout_id;
        this.roomList = roomList;
    }
    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView =  mInflater.inflate(item_layout_id,null);
        }
        mainHandler = new SearchRoomAdapter.MainHandler();
        roomImage = convertView.findViewById(R.id.member_imgs);
        roomName = convertView.findViewById(R.id.roomName);
        roomRealNum = convertView.findViewById(R.id.roomRealNum);
        roomFullNum = convertView.findViewById(R.id.roomFullNum);
        getRoomUserImage();
        basketRoomInfo = roomList.get(position);
        if(basketRoomInfo.getType() == 0) {
            roomName.setText(basketRoomInfo.getRoomName()+"(打球)");
        }else if(basketRoomInfo.getType() == 1){
            roomName.setText(basketRoomInfo.getRoomName()+"(比赛)");
        }else{
            roomName.setText(basketRoomInfo.getRoomName()+"(已解散)");
        }
        roomRealNum.setText(basketRoomInfo.getRoomRealNum()+"");
        roomFullNum.setText(basketRoomInfo.getNum()+"");
        return convertView;
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
                    Message message = mainHandler.obtainMessage(0,userImageList);
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
                case 0:
                    ArrayList<String> imageUrls = (ArrayList<String>) msg.obj;
                    int imageSize = DensityUtils.dp2px(getApplicationContext(), 120);
                    roomImage.setVisibility(ImageView.VISIBLE);
                    roomImage.displayImage(imageUrls)
                            .synthesizedWidthHeight(imageSize, imageSize)
                            .defaultImage(R.mipmap.ic_launcher_round)
                            .load();
                    Log.e("roomImage","房间图片已生成！");
                    break;
            }
        }
    }
}
