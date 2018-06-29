package com.aopg.heybro.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.ui.activity.ChartRoomActivity;
import com.aopg.heybro.ui.activity.LoginActivty;
import com.aopg.heybro.ui.room.HorizontaRoomlListViewAdapter;
import com.aopg.heybro.ui.room.HorizontalRoomListView;
import com.aopg.heybro.ui.room.RoomDate;
import com.aopg.heybro.ui.room.RoomJoinAdapter;
import com.aopg.heybro.ui.room.RoomUser;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;
import static cn.jpush.im.android.tasks.RegisterTask.TAG;
import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;


/**
 * Created by 壑过忘川 on 2018/6/7.
 * 打球界面
 */

public class FragmentBall extends Fragment{
    private View rootView;
    private View createRoomView;
    private View matchRoomView;
    private PopupWindow window;

    private CreateGroupCallback callback;
    private BasketRoomInfo basketRoomInfo;
    private OkHttpClient client;
    private String password;//房间密码（可选）

    private String modeSelect;//匹配选择模式

    //房间滑动字段
    List<RoomDate> roomDateList;
    List<Map<String,Object>> roomUserList;
    private  List<Map<String,Object>> list;
    List<RoomDate> userRoomInfoT;
    private Integer roomId = 0;
    private View joinRoomView;
    private View viewRoomView;
    HorizontalRoomListView hListView;
    HorizontaRoomlListViewAdapter hListViewAdapter;
    private Handler ReduceHandler;
    private Integer joinFlag = 1;
    private String joinRoomPass;
    private EditText textJoinRoomPass;
    private TextView textViewJoinRoomPass;
    private Integer flag = 1;
    private String roomProT;
    private String roomNumT;
    private String roomPass;
    private Handler handler;
    private Handler judgeUserHandler;
    private Handler joinRoomHandker;
    private static Button btn_create;
    private String joinRoomPassSet;
    private String roomName;

    //判断用户是否已经拥有房间
    private Integer whetherHaveRoom = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e("", "onCreateView");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_basketball_ball, container, false);
            ball();
        }

        rootView = inflater.inflate(R.layout.fragment_basketball_ball, container, false);
        ball();
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        TextView myRate = rootView.findViewById(R.id.my_rate_ball);
        myRate.setText("当前等级:    "+LoginInfo.user.getUserGrade());

        //创建房间
        createRoom();
        //快速匹配
        matchRoom();
        return rootView;
    }
    /**
     * 创建房间
     */
    public void createRoom(){
        btn_create = rootView.findViewById(R.id.btn_create);
        basketRoomInfo = new BasketRoomInfo();
        btn_create.setClickable(false);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoomView = LayoutInflater.from(getContext()).inflate(R.layout.create_room,null,false);
                if(null == window || !window.isShowing()) {
                    window = new PopupWindow(createRoomView, 850, 1000, true);
                    // 设置PopupWindow是否能响应外部点击事件
                    window.setOutsideTouchable(false);
                    // 设置PopupWindow是否能响应点击事件
                    window.setTouchable(true);
                    window.showAtLocation(view, Gravity.LEFT, 20,-200);
                }
                /**
                 * 获取选择的数据
                 */
                final EditText roomName = createRoomView.findViewById(R.id.set_name);
                final Spinner modeSpinner = createRoomView.findViewById(R.id.spinner_mode);
                final Spinner rateSpinner = createRoomView.findViewById(R.id.spinner_rate);
                final Spinner numSpinner = createRoomView.findViewById(R.id.spinner_num);
                final EditText passwordSet = createRoomView.findViewById(R.id.set_roomPassword);
                modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        basketRoomInfo.setMode(getActivity().getResources().getStringArray(R.array.spinner_mode)[i]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                rateSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(getActivity().getResources().getStringArray(R.array.spinner_rate)[i].equals("无限制")){
                            basketRoomInfo.setRateLow(1);
                            basketRoomInfo.setRateHigh(9);
                        }else if(getActivity().getResources().getStringArray(R.array.spinner_rate)[i].equals("Ⅰ-Ⅲ")){
                            basketRoomInfo.setRateLow(1);
                            basketRoomInfo.setRateHigh(3);
                        }else if(getActivity().getResources().getStringArray(R.array.spinner_rate)[i].equals("Ⅳ-Ⅵ")){
                            basketRoomInfo.setRateLow(4);
                            basketRoomInfo.setRateHigh(6);
                        }else if(getActivity().getResources().getStringArray(R.array.spinner_rate)[i].equals("Ⅶ-Ⅸ")){
                            basketRoomInfo.setRateLow(7);
                            basketRoomInfo.setRateHigh(9);
                        }else{
                            basketRoomInfo.setRateLow(1);
                            basketRoomInfo.setRateHigh(9);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                numSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(getActivity().getResources().getStringArray(R.array.spinner_num)[i].equals("10人以下")){
                            basketRoomInfo.setNum(10);
                        }else {
                            basketRoomInfo.setNum(Integer.parseInt(getActivity().getResources().getStringArray(R.array.spinner_num)[i]));
                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                /**
                 * 完成创建
                 */
                Button btn_create_finish = createRoomView.findViewById(R.id.btn_create_finish);
                btn_create_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        basketRoomInfo.setRoomName(roomName.getText().toString());
                        if (passwordSet.getText().toString() != null) {
                            password = passwordSet.getText().toString();
                        }
                        basketRoomInfo.setType(0);
                        basketRoomInfo.setMaster(LoginInfo.user.getUserCode());
                        basketRoomInfo.setRoomId(0L);
                        //设置房间Id
                        createImRoom(basketRoomInfo.getRoomName(), "");
                    }
                });
                /**
                 * 关闭创建房间
                 */
                Button create_close = createRoomView.findViewById(R.id.create_close);
                create_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(null != window && window.isShowing()){
                            window.dismiss();
                        }
                    }
                });
            }
        });
    }

    /**
     *  Created by 王攀 on 2018/6/20
     *  主线程房间滑动信息
     */
    public void ball(){

        /**
         *  查询房间列表
         */

        client = HttpUtils.init(client);

        Request request = new Request.Builder().
                url(BUILD_URL("BasketBallRoom/BallRoomInfo?userCode=" + LoginInfo.user.getUserCode())).build();
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

                if (null != success&&success.equals("true")) {

                    JSONArray roomListInfo =
                            ((JSONObject)((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
                    roomDateList = new ArrayList<>();
                    for (int i = 0; i < roomListInfo.size(); i++) {
                        RoomDate roomDate = new RoomDate();
                        String roomId =
                                ((JSONObject)roomListInfo.get(i)).getString("roomId");
                        String roomNum =
                                ((JSONObject)roomListInfo.get(i)).getString("roomNum");
                        String roomPeo =
                                ((JSONObject)roomListInfo.get(i)).getString("roomPeo");
                        String roomName =
                                ((JSONObject)roomListInfo.get(i)).getString("roomName");
                        String roomPass =
                                ((JSONObject)roomListInfo.get(i)).getString("roomPass");
                        String roomPassSet =
                                ((JSONObject)roomListInfo.get(i)).getString("roomPassSet");

                        roomDate.setRoomId(roomId);
                        roomDate.setRoomName(roomName);
                        roomDate.setRoomNum(roomNum);
                        roomDate.setRoomPro(roomPeo);
                        roomDate.setRoomPass(roomPass);
                        roomDate.setRoomPassSet(roomPassSet);

                        roomDateList.add(roomDate);

                    }

                    Message message = handler.obtainMessage(0,"formCallBack");
                    handler.sendMessage(message);

                }
            }
        });


        /**
         *  向主线程发送消息
         */
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        //房间部分
                        initUI();
                        break;
                    default:
                        break;
                }
            }
        };

    }


    /**
     * 快速匹配
     */
    public void matchRoom(){
        final Button btn_join = rootView.findViewById(R.id.btn_match);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                matchRoomView = LayoutInflater.from(getContext()).inflate(R.layout.match_room,null,false);
                if(null == window || !window.isShowing()) {
                    window = new PopupWindow(matchRoomView, 850, 1000, true);
                    // 设置PopupWindow是否能响应外部点击事件
                    window.setOutsideTouchable(false);
                    // 设置PopupWindow是否能响应点击事件
                    window.setTouchable(true);
                    window.showAtLocation(view, Gravity.LEFT, 20, -200);
                }
                /**
                 * 获取选择的数据
                 */
                final Spinner modeChose = matchRoomView.findViewById(R.id.spinner_mode);
                modeChose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        modeSelect = getActivity().getResources().getStringArray(R.array.spinner_mode)[i];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                /**
                 * 开始匹配
                 */
                Button btn_match_finish = matchRoomView.findViewById(R.id.btn_match_finish);
                btn_match_finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        httpMatchRoom(modeSelect,LoginInfo.user.getUserGrade(),LoginInfo.user.getUserCode(),0);
                    }
                });
                /**
                 * 关闭快速匹配
                 */
                Button match_close = matchRoomView.findViewById(R.id.match_close);
                match_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(null != window && window.isShowing()){
                            window.dismiss();
                        }
                    }
                });
            }
        });
    }


    /**
     * 向远程数据库添加房间信息
     */
    public void httpInsertRoom(final BasketRoomInfo basketRoomInfo, String password){
        client = HttpUtils.init(client);
        Request request;
        if(password != null&&!password.equals("")) {
            basketRoomInfo.setPassword(password);
            request = new Request.Builder().
                    url(BUILD_URL("basketRoom/createRoom?roomId="+basketRoomInfo.getRoomId()+"&roomName="
                            + basketRoomInfo.getRoomName() + "&type=" + basketRoomInfo.getType()
                            + "&mode=" + basketRoomInfo.getMode() + "&rateLow=" + basketRoomInfo.getRateLow()
                            +"&rateHigh="+basketRoomInfo.getRateHigh()
                            + "&num=" + basketRoomInfo.getNum() + "&password=" + basketRoomInfo.getPassword()
                            + "&userCode=" + basketRoomInfo.getMaster())).build();
        }else{
            request = new Request.Builder().
                    url(BUILD_URL("basketRoom/createRoom?roomId="+basketRoomInfo.getRoomId()+"&roomName="
                            + basketRoomInfo.getRoomName() + "&type=" + basketRoomInfo.getType()
                            + "&mode=" + basketRoomInfo.getMode() + "&rateLow=" + basketRoomInfo.getRateLow()
                            +"&rateHigh="+basketRoomInfo.getRateHigh()
                            + "&num=" + basketRoomInfo.getNum() + "&userCode=" + basketRoomInfo.getMaster())).build();
        }
        Call call = client.newCall(request);
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                String haveRoom = (JSONObject.parseObject(result)).getString("msg");
                if(null != haveRoom&&haveRoom.equals("已经加入或创建一个房间，无需创建")){
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "您已经创建或者加入一个房间，无需再创建", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                Log.e("result",result);
                String success = (JSONObject.parseObject(result)).getString("success");
                if(null!=success&&success.equals("true")) {
                    JSONObject roomInfo =
                            (JSONObject)((JSONObject)((JSONObject.parseObject(result)).get("data"))).get("room");
                    Long roomId = Long.parseLong(roomInfo.getString("roomId"));
                    String roomName = roomInfo.getString("roomName");
                    Intent roomIntent = new Intent(getActivity(), ChartRoomActivity.class);
                    roomIntent.putExtra("roomId",roomId);
                    roomIntent.putExtra("roomName",roomName);
                    startActivity(roomIntent);
                }
            }
        });
    }
    /**
     * 向远程数据库匹配房间信息
     */
    public void httpMatchRoom(String mode, int userRate, final String userCode, int type){
        client = HttpUtils.init(client);
        Request request = new Request.Builder().
                url(BUILD_URL("basketRoom/matchRoom?mode="+ mode +"&userRate="
                        + userRate + "&type=" + type
                        + "&userCode=" + userCode)).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                String info = (JSONObject.parseObject(result)).getString("msg");
                if(null != info&&info.equals("已经加入或创建一个房间，无需匹配")){
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "您已经创建或者加入一个房间，请先退出房间", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if(null != info&&info.equals("没有符合条件的房间！")){
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "没有符合条件的房间，请稍后再试", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                Log.e("result",result);
                String success = (JSONObject.parseObject(result)).getString("success");
                if(null!=success&&success.equals("true")) {
                    JSONObject matchRoomInfo =
                            (JSONObject)((JSONObject)((JSONObject.parseObject(result)).get("data"))).get("matchRoom");
                    Long roomId = Long.parseLong(matchRoomInfo.getString("roomId"));
                    String roomName = matchRoomInfo.getString("roomName");
                    Log.e("room",roomName);
                    joinRoom(Integer.parseInt(matchRoomInfo.getString("roomId")),roomName,userCode);
                }
            }
        });
    }


    private void createImRoom(String roomName,String roomDesc){
        callback = new CreateGroupCallback() {
            @Override
            public void gotResult(int responseCode, String responseMsg, long groupId) {
                if (responseCode == 0) {
                    btn_create.setClickable(false);
                    //创建成功
                    basketRoomInfo.setRoomId(groupId);
                    LoginInfo.user.setRoomId(groupId);
                    httpInsertRoom(basketRoomInfo,password);
                }
            }
        };
        JMessageClient.createPublicGroup(roomName, roomDesc, callback);
    }

    /**
     * Created by 王攀 .
     * 房间问题
     */
    public void initUI(){
        hListView = (HorizontalRoomListView)rootView.findViewById(R.id.ball_horizon_listview);
        String[] roomCode = new String[roomDateList.size()];
        final String[] roomTitle = new String[roomDateList.size()];
        final String[] roomNum = new String[roomDateList.size()];
        //获取日期
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        String ma="yyyyMMdd";
        SimpleDateFormat forma=new SimpleDateFormat(ma);
        String nwdate=forma.format(date);

        if (roomDateList.size()>0 && roomDateList != null){
            for (int i = 0;i<roomDateList.size();i++){
                roomCode[i] = ""+nwdate+roomDateList.get(i).getRoomId();
                roomTitle[i] = roomDateList.get(i).getRoomName();
                roomNum[i] = ""+roomDateList.get(i).getRoomPro()+"/"+roomDateList.get(i).getRoomNum();
            }
        }

        hListViewAdapter = new HorizontaRoomlListViewAdapter(rootView.getContext(),
                roomCode,roomTitle,roomNum);
        hListView.setAdapter(hListViewAdapter);

        /**
         * 房间点击事件
         */

        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("HandlerLeak")
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                hListViewAdapter.setSelectIndex(position);
                hListViewAdapter.notifyDataSetChanged();
                roomId = Integer.parseInt(roomDateList.get(position).getRoomId());
                roomPass = roomDateList.get(position).getRoomPass();
                joinRoomPassSet = roomDateList.get(position).getRoomPassSet();
                roomName = roomDateList.get(position).getRoomName();
                roomProT = roomDateList.get(position).getRoomPro();
                roomNumT = roomDateList.get(position).getRoomNum();

                /**
                 * 查询房间详情信息
                 */
                Request request = new Request.Builder().
                        url(BUILD_URL("BasketBallRoom/BallRoomDetailedInfo?roomId="+roomId+"&userCode=" + LoginInfo.user.getUserCode())).build();
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
                            roomUserList = new ArrayList<>();
                            JSONArray userInfo =
                                    ((JSONObject)((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
                            for (int i = 0; i < userInfo.size(); i++) {
                                RoomUser roomUser = new RoomUser();
                                String userName =
                                        ((JSONObject)userInfo.get(i)).getString("userName");
                                String userIntro =
                                        ((JSONObject)userInfo.get(i)).getString("userIntro");
                                String userPortrait =
                                        ((JSONObject)userInfo.get(i)).getString("userPortrait");


                                Map<String,Object> map = new HashMap<>();
                                map.put("img", userPortrait);
                                map.put("user_name",userName);
                                map.put("user_Intro",userIntro);

                                System.out.println(userIntro);
                                System.out.println(userPortrait);

                                roomUserList.add(map);

                            }
                            joinFlag = 1;
                            Message message = ReduceHandler.obtainMessage(1,"formCallBack");
                            ReduceHandler.sendMessage(message);
                        }else{
                            roomUserList = new ArrayList<>();
                            joinFlag = 0;
                            Message message = ReduceHandler.obtainMessage(1,"formCallBackT");
                            ReduceHandler.sendMessage(message);
                        }
                    }
                });


                /**
                 *  房间详情向主线程发送信息
                 */
                ReduceHandler= new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case 1:
                                viewRoomView = LayoutInflater.from(getContext()).inflate(R.layout.join_room, null, false);

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
                                    if ("1".equals(joinRoomPassSet)){


                                    } else if("0".equals(joinRoomPassSet)){
                                        textJoinRoomPass.setVisibility(View.GONE);
                                        textViewJoinRoomPass.setVisibility(View.GONE);
                                    }


                                    /**
                                     *  查询该用户拥有的房间信息
                                     *  该用户只能进入自己的房间
                                     */

                                    Request request = new Request.Builder().
                                            url(BUILD_URL("BasketBallRoom/WethereHaveRoom?userCode="+LoginInfo.user.getUserCode())).build();
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
                                                userRoomInfoT = new ArrayList<>();
                                                JSONArray userRoomInfo =
                                                        ((JSONObject)((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
                                                for (int i = 0; i < userRoomInfo.size(); i++) {
                                                    RoomDate roomDate = new RoomDate();
                                                    String roomId =
                                                            ((JSONObject)userRoomInfo.get(i)).getString("roomId");

                                                    roomDate.setRoomId(roomId);

                                                    userRoomInfoT.add(roomDate);

                                                }
                                                Message message = judgeUserHandler.obtainMessage(1,userRoomInfoT.get(0).getRoomId());
                                                judgeUserHandler.sendMessage(message);
                                            }
                                        }
                                    });

                                    /**
                                     * 判断用户向主线程发送消息
                                     */
                                    judgeUserHandler = new Handler(){
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                            switch (msg.what) {
                                                case 1:

                                                    if (roomId.toString().equals(msg.obj.toString())){
                                                        Toast.makeText(getApplicationContext(), "调用加入房间逻辑（已加入房间）", Toast.LENGTH_SHORT).show();


                                                        Intent intent = new Intent();
                                                        intent.setComponent(new ComponentName(rootView.getContext(), ChartRoomActivity.class));
                                                        intent.putExtra("roomId",Long.parseLong(roomId.toString()));
                                                        intent.putExtra("roomName",roomName);
                                                        startActivity(intent);

                                                    }else if ("0".equals(msg.obj.toString())){

                                                        window = new PopupWindow(viewRoomView, 850, 1000, true);
                                                        // 设置PopupWindow是否能响应外部点击事件
                                                        window.setOutsideTouchable(false);
                                                        // 设置PopupWindow是否能响应点击事件
                                                        window.setTouchable(true);
                                                        window.showAtLocation(view, Gravity.LEFT, 20, -200);
                                                    }else{
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

                                Button joinRoom = viewRoomView.findViewById(R.id.join_room);
                                joinRoom.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        flag = 1;
                                        joinRoomPass = textJoinRoomPass.getText().toString();
                                        if (Integer.parseInt(roomProT) >= Integer.parseInt(roomNumT)){
                                            Toast.makeText(getApplicationContext(), "房间已满", Toast.LENGTH_SHORT).show();
                                            flag = 0;
                                        }

                                        //密码验证
                                        if ("1".equals(joinRoomPassSet)){
                                            if (joinRoomPass.length()!=4 && flag == 1){
                                                Toast.makeText(getApplicationContext(), "请输入4位密码", Toast.LENGTH_SHORT).show();
                                                flag = 0;
                                            }
                                            if (!roomPass.equals(joinRoomPass) && flag == 1){
                                                Toast.makeText(getApplicationContext(), "输入房间密码不正确！", Toast.LENGTH_SHORT).show();
                                                flag = 0;
                                            }
                                        }


                                        if (flag == 1){

                                            System.out.println(3333);
                                            System.out.println(roomId);
                                            System.out.println(LoginInfo.user.getUserCode());
                                            /**
                                             *  该用户进入房间，填充三表
                                             */

                                            Request request = new Request.Builder().
                                                    url(BUILD_URL("BasketBallRoom/JoinBallRoom?roomId="+ roomId+"&userCode="+LoginInfo.user.getUserCode())).build();
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
                                                        System.out.println(44444444);
                                                        Message message = joinRoomHandker.obtainMessage(1,"success");
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
                                joinRoomHandker = new Handler(){
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
                                                JMessageClient.applyJoinGroup(roomId,"", new BasicCallback() {
                                                    @Override
                                                    public void gotResult(int responseCode, String responseMessage) {
                                                        if (responseCode == 0) {
                                                            Toast.makeText(rootView.getContext(), "申请成功", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Log.d(TAG, "apply failed. code :" + responseCode + " msg : " + responseMessage);
                                                            Toast.makeText(rootView.getContext(), "申请失败", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                Intent intent = new Intent();
                                                intent.setComponent(new ComponentName(rootView.getContext(), ChartRoomActivity.class));
                                                intent.putExtra("roomId",Long.parseLong(roomId.toString()));
                                                intent.putExtra("roomName",roomName);
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

    public void joinRoom(final Integer roomId, final String roomName, String userCode){
        /**
         *  该用户进入房间，填充三表
         */

        Request request = new Request.Builder().
                url(BUILD_URL("BasketBallRoom/JoinBallRoom?roomId="+ roomId+"&userCode="+userCode)).build();
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
                    //申请加入讨论组
                    JMessageClient.applyJoinGroup(roomId,"", new BasicCallback() {
                        @Override
                        public void gotResult(int responseCode, String responseMessage) {
                            if (responseCode == 0) {
                                Toast.makeText(rootView.getContext(), "申请成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "apply failed. code :" + responseCode + " msg : " + responseMessage);
                                Toast.makeText(rootView.getContext(), "申请失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(rootView.getContext(), ChartRoomActivity.class));
                    intent.putExtra("roomId",Long.parseLong(roomId.toString()));
                    intent.putExtra("roomName",roomName);
                    startActivity(intent);

                }
            }
        });

    }
    public static void setCreateRoomStates(boolean flag){
        btn_create.setClickable(flag);
    }
}