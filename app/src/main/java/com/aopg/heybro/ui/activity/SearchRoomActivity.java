package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.ui.adapter.SearchRoomAdapter;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;

/**
 * Created by 壑过忘川 on 2018/6/21.
 * 搜索房间界面
 */

public class SearchRoomActivity extends Activity {
    private SearchRoomAdapter searchRoomAdapter;
    private ListView roomListView;
    private SearchView searchView;
    private List<BasketRoomInfo> roomList;

    private OkHttpClient client;
    private Handler searchHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basketball_searchroom);
        /**
         * 显示房间
         */
        allRoom();
        /**
         * 搜索房间
         */
        searchRoom();
        //取消
        TextView searchroom_cancel = findViewById(R.id.searchroom_cancel);
        searchroom_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
    /**
     * 搜索房间
     */
    public void searchRoom(){
        final List<BasketRoomInfo> roomSearchList = new ArrayList<BasketRoomInfo>();
        searchView = findViewById(R.id.searchroom);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //输入完成后，提交时触发的方法，一般情况是点击输入法中的搜索按钮才会触发，表示现在正式提交了
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query))
                {
                    Toast.makeText(SearchRoomActivity.this, "请输入查找内容！", Toast.LENGTH_SHORT).show();
                    roomListView.setAdapter(searchRoomAdapter);
                }
                else
                {
                    roomSearchList.clear();
                    for(int i = 0; i < roomList.size(); i++)
                    {
                        BasketRoomInfo basketRoom = new BasketRoomInfo();
                        basketRoom = roomList.get(i);
                        if(basketRoom.getRoomName().equals(query))
                        {
                            roomSearchList.add(basketRoom);
                            break;
                        }
                    }
                    if(roomSearchList.size() == 0)
                    {
                        Toast.makeText(SearchRoomActivity.this, "查找的商品不在列表中", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        searchRoomAdapter = new SearchRoomAdapter(SearchRoomActivity.this,R.layout.basketball_searchroom_item,roomSearchList);
                        roomListView.setAdapter(searchRoomAdapter);
                    }
                }
                return true;
            }

            @Override
            //在输入时触发的方法，当字符真正显示到searchView中才触发，像是拼音，在输入法组词的时候不会触发
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText))
                {
                    roomListView.setAdapter(searchRoomAdapter);
                }
                else
                {
                    roomSearchList.clear();
                    for(int i = 0; i < roomList.size(); i++)
                    {
                        BasketRoomInfo basketRoom = new BasketRoomInfo();
                        basketRoom = roomList.get(i);
                        if(basketRoom.getRoomName().contains(newText))
                        {
                            roomSearchList.add(basketRoom);
                        }
                    }
                    searchRoomAdapter = new SearchRoomAdapter(SearchRoomActivity.this,R.layout.basketball_searchroom_item,roomSearchList);
                    searchRoomAdapter.notifyDataSetChanged();
                    roomListView.setAdapter(searchRoomAdapter);
                }
                return true;
            }
        });
    }
}
