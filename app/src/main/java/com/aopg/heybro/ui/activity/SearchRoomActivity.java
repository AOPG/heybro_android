package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.ui.adapter.SearchRoomAdapter;
import com.aopg.heybro.ui.room.RoomDate;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.callback.CreateGroupCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;
//import com.aopg.heybro.ui.adapter.SearchRoomAdapter;

/**
 * Created by 壑过忘川 on 2018/6/21.
 * 搜索房间界面
 */

public class SearchRoomActivity extends Activity {
    private SearchRoomAdapter searchRoomAdapter;
    private ListView roomListView;
    private List<BasketRoomInfo> roomList;

    private OkHttpClient client;
    private Handler searchHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basketball_searchroom);
        //取消
        TextView searchroom_cancel = findViewById(R.id.searchroom_cancel);
        searchroom_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        /**
         * 显示房间
         */
        allRoom();


    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }

    /**
     * 查询所有房间
     * @return
     */
    public void allRoom(){
        roomList = new ArrayList<BasketRoomInfo>();
        client = HttpUtils.init(client);

        Request request = new Request.Builder().
                url(BUILD_URL("basketRoom/searchAllRoom?userCode="+ LoginInfo.user.getUserCode())).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                Log.e("msg",result);
                String success = (JSONObject.parseObject(result)).getString("success");

                if (null != success && success.equals("true")) {

                    JSONArray roomListInfo =
                            ((JSONObject) ((JSONObject.parseObject(result)).get("data"))).getJSONArray("roomlist");
                    for (int i = 0; i < roomListInfo.size(); i++) {
                        BasketRoomInfo basketRoomInfo = new BasketRoomInfo();
                        String roomId =
                                ((JSONObject) roomListInfo.get(i)).getString("roomId");
                        String roomName =
                                ((JSONObject) roomListInfo.get(i)).getString("roomName");
                        String roomNum =
                                ((JSONObject) roomListInfo.get(i)).getString("roomNum");
                        String roomMode =
                                ((JSONObject) roomListInfo.get(i)).getString("roomMode");
                        String roomRateLow =
                                ((JSONObject) roomListInfo.get(i)).getString("roomRateLow");
                        String roomRateHigh =
                                ((JSONObject) roomListInfo.get(i)).getString("roomRateHigh");
                        String roomType =
                                ((JSONObject) roomListInfo.get(i)).getString("roomType");
                        String roomPass =
                                ((JSONObject) roomListInfo.get(i)).getString("roomPass");
                        String roomRealNum =
                                ((JSONObject) roomListInfo.get(i)).getString("roomPeo");
                        String masterCode =
                                ((JSONObject) roomListInfo.get(i)).getString("masterCode");
                        basketRoomInfo.setRoomId(Long.parseLong(roomId));
                        basketRoomInfo.setRoomName(roomName);
                        basketRoomInfo.setNum(Integer.parseInt(roomNum));
                        basketRoomInfo.setMode(roomMode);
                        basketRoomInfo.setRateLow(Integer.parseInt(roomRateLow));
                        basketRoomInfo.setRateHigh(Integer.parseInt(roomRateHigh));
                        basketRoomInfo.setType(Integer.parseInt(roomType));
                        basketRoomInfo.setPassword(roomPass);
                        basketRoomInfo.setRoomRealNum(Integer.parseInt(roomRealNum));
                        basketRoomInfo.setMaster(masterCode);

                        roomList.add(basketRoomInfo);

                    }

                    Message message = searchHandler.obtainMessage(0, "formCallBack");
                    searchHandler.sendMessage(message);
                }
            }
        });
        /**
         *  向主线程发送消息
         */
        searchHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        searchRoomAdapter = new SearchRoomAdapter(SearchRoomActivity.this,R.layout.basketball_searchroom_item,roomList);
                        roomListView = findViewById(R.id.roomlist);
                        roomListView.setAdapter(searchRoomAdapter);
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
