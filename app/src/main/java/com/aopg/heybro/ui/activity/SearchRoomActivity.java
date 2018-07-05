package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.ui.adapter.SearchRoomAdapter;
import com.aopg.heybro.ui.room.RoomDate;
import com.aopg.heybro.ui.room.RoomJoinAdapter;
import com.aopg.heybro.ui.room.RoomUser;
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
import static cn.jpush.im.android.tasks.RegisterTask.TAG;
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
    private PopupWindow window;

    private OkHttpClient client;
    private Handler searchHandler;

    //加入房间
    List<RoomDate> roomDateList;
    List<Map<String,Object>> roomUserList;
    private  List<Map<String,Object>> list;
    List<RoomDate> userRoomInfoT;
    private Long roomId = 0L;
    private View joinRoomView;
    private View viewRoomView;
    private Handler ReduceHandler;
    private Integer joinFlag = 1;
    private String joinRoomPass;
    private EditText textJoinRoomPass;
    private TextView textViewJoinRoomPass;
    private Integer flag = 1;
    private int roomProT;
    private int roomNumT;
    private String roomPass;
    private Handler handler;
    private Handler judgeUserHandler;
    private Handler joinRoomHandker;
    private int joinRoomPassSet;
    private String roomName;

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
      //  searchRoom();
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
                Log.e("room",result);
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
        searchHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        viewRoomView = LayoutInflater.from(SearchRoomActivity.this).inflate(R.layout.join_room, null, false);
                        searchRoomAdapter = new SearchRoomAdapter(SearchRoomActivity.this, R.layout.basketball_searchroom_item, roomList);
                        roomListView = findViewById(R.id.roomlist);
                        roomListView.setAdapter(searchRoomAdapter);
                        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long l) {
                                roomId = roomList.get(position).getRoomId();
                                roomPass = roomList.get(position).getPassword();
                                joinRoomPassSet = roomList.get(position).getRoomPassSet();
                                roomName = roomList.get(position).getRoomName();
                                roomProT = roomList.get(position).getRoomRealNum();
                                roomNumT = roomList.get(position).getNum();

                                /**
                                 * 查询房间详情信息
                                 */
                                Request request = new Request.Builder().
                                        url(BUILD_URL("BasketBallRoom/BallRoomDetailedInfo?roomId=" + roomId + "&userCode=" + LoginInfo.user.getUserCode())).build();
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
                                        if (null != success && success.equals("true")) {
                                            roomUserList = new ArrayList<>();
                                            JSONArray userInfo =
                                                    ((JSONObject) ((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
                                            for (int i = 0; i < userInfo.size(); i++) {
                                                RoomUser roomUser = new RoomUser();
                                                String userName =
                                                        ((JSONObject) userInfo.get(i)).getString("userName");
                                                String userIntro =
                                                        ((JSONObject) userInfo.get(i)).getString("userIntro");
                                                String userPortrait =
                                                        ((JSONObject) userInfo.get(i)).getString("userPortrait");


                                                Map<String, Object> map = new HashMap<>();
                                                map.put("img", userPortrait);
                                                map.put("user_name", userName);
                                                map.put("user_Intro", userIntro);

                                                roomUserList.add(map);

                                            }
                                            joinFlag = 1;
                                            Message message = ReduceHandler.obtainMessage(1, "formCallBack");
                                            ReduceHandler.sendMessage(message);
                                        } else {
                                            roomUserList = new ArrayList<>();
                                            joinFlag = 0;
                                            Message message = ReduceHandler.obtainMessage(1, "formCallBackT");
                                            ReduceHandler.sendMessage(message);
                                        }
                                    }
                                });


                                /**
                                 *  房间详情向主线程发送信息
                                 */
                                ReduceHandler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        switch (msg.what) {
                                            case 1:
                                                viewRoomView = LayoutInflater.from(SearchRoomActivity.this).inflate(R.layout.join_room, null, false);

                                                //获取密码框
                                                textJoinRoomPass = viewRoomView.findViewById(R.id.roomPassword);
                                                textViewJoinRoomPass = viewRoomView.findViewById(R.id.textViewRoomPass);

                                                if (null == window || !window.isShowing()) {
                                                    //加入listView
                                                    if (joinFlag == 1) {
                                                        ListView lv = viewRoomView.findViewById(R.id.roomDetail_list);
                                                        lv.setAdapter(new RoomJoinAdapter(viewRoomView.getContext(), roomUserList, R.layout.basketball_joinroom_item));
                                                    }


                                                    //该房间设立密码为1 没有设立密码为0
                                                    //该功能暂时没因为进入讨论组而能修改密码改善
                                                    if ("1".equals(joinRoomPassSet)) {


                                                    } else if ("0".equals(joinRoomPassSet)) {
                                                        textJoinRoomPass.setVisibility(View.GONE);
                                                        textViewJoinRoomPass.setVisibility(View.GONE);
                                                    }


                                                    /**
                                                     *  查询该用户拥有的房间信息
                                                     *  该用户只能进入自己的房间
                                                     */

                                                    Request request = new Request.Builder().
                                                            url(BUILD_URL("BasketBallRoom/WethereHaveRoom?userCode=" + LoginInfo.user.getUserCode())).build();
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
                                                            if (null != success && success.equals("true")) {
                                                                userRoomInfoT = new ArrayList<>();
                                                                JSONArray userRoomInfo =
                                                                        ((JSONObject) ((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
                                                                for (int i = 0; i < userRoomInfo.size(); i++) {
                                                                    RoomDate roomDate = new RoomDate();
                                                                    String roomId =
                                                                            ((JSONObject) userRoomInfo.get(i)).getString("roomId");

                                                                    roomDate.setRoomId(roomId);
                                                                    userRoomInfoT.add(roomDate);

                                                                }
                                                                Message message = judgeUserHandler.obtainMessage(1, userRoomInfoT.get(0).getRoomId());
                                                                judgeUserHandler.sendMessage(message);
                                                            }
                                                        }
                                                    });

                                                    /**
                                                     * 判断用户向主线程发送消息
                                                     */
                                                    judgeUserHandler = new Handler() {
                                                        @Override
                                                        public void handleMessage(Message msg) {
                                                            super.handleMessage(msg);
                                                            switch (msg.what) {
                                                                case 1:

                                                                    if (roomId.toString().equals(msg.obj.toString())) {
                                                                        Toast.makeText(getApplicationContext(), "调用加入房间逻辑（已加入房间）", Toast.LENGTH_SHORT).show();


                                                                        Intent intent = new Intent();
                                                                        intent.setComponent(new ComponentName(SearchRoomActivity.this, ChartRoomActivity.class));
                                                                        intent.putExtra("roomId", Long.parseLong(roomId.toString()));
                                                                        intent.putExtra("roomName", roomName);
                                                                        startActivity(intent);

                                                                    } else if ("0".equals(msg.obj.toString())) {

                                                                        window = new PopupWindow(viewRoomView, 850, 1000, true);
                                                                        // 设置PopupWindow是否能响应外部点击事件
                                                                        window.setOutsideTouchable(false);
                                                                        // 设置PopupWindow是否能响应点击事件
                                                                        window.setTouchable(true);
                                                                        window.showAtLocation(view, Gravity.LEFT, 20, -200);
                                                                        setBackgroundAlpha(SearchRoomActivity.this,0.5f);
                                                                        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                                                            @Override
                                                                            public void onDismiss() {
                                                                                if (SearchRoomActivity.this != null) {
                                                                                    setBackgroundAlpha(SearchRoomActivity.this, 1f);
                                                                                }
                                                                            }
                                                                        });
                                                                    } else {
                                                                        Toast.makeText(getApplicationContext(), "您已经加入其它房间！", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                        }
                                                    };


                                                }
                                                /**
                                                 * 关闭房间信息
                                                 */
                                                Button join_close = viewRoomView.findViewById(R.id.join_close);
                                                join_close.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        if (null != window && window.isShowing()) {
                                                            window.dismiss();
                                                        }
                                                    }
                                                });


                                                /**
                                                 *  点击进入房间按钮
                                                 */

                                                Button joinRoom = viewRoomView.findViewById(R.id.join_room);
                                                joinRoom.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        flag = 1;
                                                        joinRoomPass = textJoinRoomPass.getText().toString();
                                                        if (roomProT >= roomNumT) {
                                                            Toast.makeText(getApplicationContext(), "房间已满", Toast.LENGTH_SHORT).show();
                                                            flag = 0;
                                                        }

                                                        //密码验证
                                                        if ("1".equals(joinRoomPassSet)) {
                                                            if (joinRoomPass.length() != 4 && flag == 1) {
                                                                Toast.makeText(getApplicationContext(), "请输入4位密码", Toast.LENGTH_SHORT).show();
                                                                flag = 0;
                                                            }
                                                            if (!roomPass.equals(joinRoomPass) && flag == 1) {
                                                                Toast.makeText(getApplicationContext(), "输入房间密码不正确！", Toast.LENGTH_SHORT).show();
                                                                flag = 0;
                                                            }
                                                        }


                                                        if (flag == 1) {
                                                            /**
                                                             *  该用户进入房间，填充三表
                                                             */

                                                            Request request = new Request.Builder().
                                                                    url(BUILD_URL("BasketBallRoom/JoinBallRoom?roomId=" + roomId + "&userCode=" + LoginInfo.user.getUserCode())).build();
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
                                                                    if (null != success && success.equals("true")) {
                                                                        System.out.println(44444444);
                                                                        Message message = joinRoomHandker.obtainMessage(1, "success");
                                                                        joinRoomHandker.sendMessage(message);
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }
                                                });


                                                /**
                                                 * 进入房间逻辑
                                                 */
                                                joinRoomHandker = new Handler() {
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        super.handleMessage(msg);
                                                        switch (msg.what) {
                                                            case 1:

                                                                Toast.makeText(getApplicationContext(), "加入房间成功（暂时不拥有房间）！", Toast.LENGTH_SHORT).show();

                                                                if (null != window && window.isShowing()) {
                                                                    System.out.println(2222);
                                                                    window.dismiss();
                                                                }

                                                                //申请加入讨论组
                                                                JMessageClient.applyJoinGroup(roomId, "", new BasicCallback() {
                                                                    @Override
                                                                    public void gotResult(int responseCode, String responseMessage) {
                                                                        if (responseCode == 0) {
                                                                            Toast.makeText(getApplicationContext(), "申请成功", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            Log.d(TAG, "apply failed. code :" + responseCode + " msg : " + responseMessage);
                                                                            Toast.makeText(getApplicationContext(), "申请失败", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                                Intent intent = new Intent();
                                                                intent.setComponent(new ComponentName(SearchRoomActivity.this, ChartRoomActivity.class));
                                                                intent.putExtra("roomId", Long.parseLong(roomId.toString()));
                                                                intent.putExtra("roomName", roomName);
                                                                startActivity(intent);

                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                    }
                                                };


                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                };


                            }
                        });
                }

                /**
                 * 关闭房间信息
                 */
                Button create_close = viewRoomView.findViewById(R.id.join_close);
                create_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (null != window && window.isShowing()) {
                            window.dismiss();
                        }
                    }
                });

                /**
                 *  点击进入房间按钮
                 */
                textJoinRoomPass = viewRoomView.findViewById(R.id.roomPassword);
                Button joinRoom = viewRoomView.findViewById(R.id.join_room);
                joinRoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flag = 1;
                        joinRoomPass = textJoinRoomPass.getText().toString();
                        //密码验证
                        if (joinRoomPass.length() != 4 && flag == 1) {
                            Toast.makeText(getApplicationContext(), "请输入4位密码", Toast.LENGTH_SHORT).show();
                            flag = 0;
                        }
                        if (!roomPass.equals(joinRoomPass) && flag == 1) {
                            Toast.makeText(getApplicationContext(), "输入房间密码不正确！", Toast.LENGTH_SHORT).show();
                            flag = 0;
                        }

                        if (flag == 1) {
                            Toast.makeText(getApplicationContext(), "登陆成功！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                        Toast.makeText(SearchRoomActivity.this, "查找的房间不在列表中", Toast.LENGTH_SHORT).show();
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
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }
}
