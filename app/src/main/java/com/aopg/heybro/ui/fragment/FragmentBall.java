package com.aopg.heybro.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.ui.activity.ChartRoomActivity;
import com.aopg.heybro.ui.room.HorizontaRoomlListViewAdapter;
import com.aopg.heybro.ui.room.HorizontalRoomListView;
import com.aopg.heybro.ui.room.RoomDate;
import com.aopg.heybro.ui.room.RoomJoinAdapter;
import com.aopg.heybro.ui.room.RoomUser;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;
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

    //房间滑动字段
    List<RoomDate> roomDateList;
    List<Map<String,Object>> roomUserList;
    private  List<Map<String,Object>> list;
    private Integer roomId = 0;
    private Integer ballRoomPeo = 0;
    private Integer ballRoomNum = 0;
    private String ballRoomName = "";
    private View viewRoomView;
    private HorizontalRoomListView hListView;
    private HorizontaRoomlListViewAdapter hListViewAdapter;
    private Handler ReduceHandler;
    private Integer joinFlag = 1;
    private String joinRoomPass;
    private EditText textJoinRoomPass;
    private Integer flag = 1;
    private String roomPass;
    private Handler handler;


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
        final Button btn_create = rootView.findViewById(R.id.btn_create);
        basketRoomInfo = new BasketRoomInfo();
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
                            basketRoomInfo.setRateLow(0);
                            basketRoomInfo.setRateHigh(0);
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
                            basketRoomInfo.setRateLow(0);
                            basketRoomInfo.setRateHigh(0);
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
                            if(LoginInfo.user.getRoomId() == 0||LoginInfo.user.getRoomId() == null) {
                                basketRoomInfo.setRoomName(roomName.getText().toString());
                                if (passwordSet.getText().toString() != null) {
                                    password = passwordSet.getText().toString();
                                }
                                basketRoomInfo.setType(0);
                                basketRoomInfo.setMaster(LoginInfo.user.getUserCode());
                                basketRoomInfo.setRoomId(-1L);
                                //设置房间Id
                                createImRoom(basketRoomInfo.getRoomName(), "");
                            }else{
                                Toast.makeText(getApplicationContext(), "您已经创建或者加入一个房间，无需再创建", Toast.LENGTH_SHORT).show();
                            }
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
                if (null!=success&&success.equals("true")) {

                    JSONArray concernInfo =
                            ((JSONObject)((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
                    roomDateList = new ArrayList<>();
                    for (int i = 0; i < concernInfo.size(); i++) {
                        RoomDate roomDate = new RoomDate();
                        String roomId =
                                ((JSONObject)concernInfo.get(i)).getString("roomId");
                        String roomNum =
                                ((JSONObject)concernInfo.get(i)).getString("roomNum");
                        String roomPeo =
                                ((JSONObject)concernInfo.get(i)).getString("roomPeo");
                        String roomName =
                                ((JSONObject)concernInfo.get(i)).getString("roomName");
                        String roomPass =
                                ((JSONObject)concernInfo.get(i)).getString("roomPass");

                        roomDate.setRoomId(roomId);
                        roomDate.setRoomName(roomName);
                        roomDate.setRoomNum(roomNum);
                        roomDate.setRoomPro(roomPeo);
                        roomDate.setRoomPass(roomPass);

                        roomDateList.add(roomDate);

                    }

                    Message message = handler.obtainMessage(0,"formCallBackk");
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
                    window.showAtLocation(view, Gravity.LEFT, 20,-200);
                }
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
                Log.e("msg",result);
                JSONObject roomInfo =
                        (JSONObject)((JSONObject)((JSONObject.parseObject(result)).get("data"))).get("room");
                String success = (JSONObject.parseObject(result)).getString("success");
                if(null!=success&&success.equals("true")) {
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


    private void createImRoom(String roomName,String roomDesc){
        callback = new CreateGroupCallback() {
            @Override
            public void gotResult(int responseCode, String responseMsg, long groupId) {
                if (responseCode == 0) {
                    //创建成功
                    basketRoomInfo.setRoomId(groupId);
                    httpInsertRoom(basketRoomInfo,password);
                }
            }
        };
        JMessageClient.createGroup(roomName, roomDesc, callback);
    }

    /**
     * Created by 王攀 .
     * 房间问题
     */
    public void initUI(){
        hListView = (HorizontalRoomListView)rootView.findViewById(R.id.ball_horizon_listview);
        String[] roomCode = new String[roomDateList.size()];
        final String[] roomTitle = new String[roomDateList.size()];
        String[] roomNum = new String[roomDateList.size()];
        //获取日期
        long time=System.currentTimeMillis();
        Date date=new Date(time);
        String ma="yyyyMMdd";
        SimpleDateFormat forma=new SimpleDateFormat(ma);
        String nwdate=forma.format(date);

        if (roomDateList.size()>0){
            for (int i = 0;i<roomDateList.size();i++){
                System.out.println(roomDateList.size());
                System.out.println(roomDateList.get(i).getRoomId());
                roomCode[i] = roomDateList.get(i).getRoomId();
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

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                hListViewAdapter.setSelectIndex(position);
                hListViewAdapter.notifyDataSetChanged();
                System.out.println(roomDateList.get(position).getRoomId()+222);


                roomId = Integer.parseInt(roomDateList.get(position).getRoomId());
                System.out.println(roomDateList.get(position).getRoomPro().length());
                if (roomDateList.get(position).getRoomPro().length()>0 ) {
                    ballRoomPeo = Integer.parseInt(roomDateList.get(position).getRoomPro());
                }
                ballRoomNum = Integer.parseInt(roomDateList.get(position).getRoomNum());
                roomPass = roomDateList.get(position).getRoomPass();
                ballRoomName = roomDateList.get(position).getRoomName();

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
                            JSONArray concernInfo =
                                    ((JSONObject)((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
                            for (int i = 0; i < concernInfo.size(); i++) {
                                RoomUser roomUser = new RoomUser();
                                String userName =
                                        ((JSONObject)concernInfo.get(i)).getString("userName");
                                String userIntro =
                                        ((JSONObject)concernInfo.get(i)).getString("userIntro");
                                String userPortrait =
                                        ((JSONObject)concernInfo.get(i)).getString("userPortrait");


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
                                if (null == window || !window.isShowing()) {
                                    //加入listView
                                    if (joinFlag == 1) {
                                        ListView lv = viewRoomView.findViewById(R.id.roomDetail_list);
                                        lv.setAdapter(new RoomJoinAdapter(viewRoomView.getContext(), roomUserList, R.layout.basketball_joinroom_item));
                                    }

                                    window = new PopupWindow(viewRoomView, 850, 1000, true);
                                    // 设置PopupWindow是否能响应外部点击事件
                                    window.setOutsideTouchable(false);
                                    // 设置PopupWindow是否能响应点击事件
                                    window.setTouchable(true);
                                    window.showAtLocation(view, Gravity.LEFT, 20, -200);
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
                                textJoinRoomPass = viewRoomView.findViewById(R.id.spinner4);
                                Button joinRoom = viewRoomView.findViewById(R.id.join_room);
                                joinRoom.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        flag = 1;
                                        joinRoomPass = textJoinRoomPass.getText().toString();
                                        //密码验证
                                        if (joinRoomPass.length()!=4 && flag == 1){
                                            Toast.makeText(getApplicationContext(), "请输入4位密码", Toast.LENGTH_SHORT).show();
                                            flag = 0;
                                        }
                                        if (!roomPass.equals(joinRoomPass) && flag == 1){
                                            Toast.makeText(getApplicationContext(), "输入房间密码不正确！", Toast.LENGTH_SHORT).show();
                                            flag = 0;
                                        }

                                        if (ballRoomPeo >= ballRoomNum && flag == 1){
                                            Toast.makeText(getApplicationContext(), "房间已满！", Toast.LENGTH_SHORT).show();
                                            flag = 0;
                                        }

                                        if (flag == 1){
                                            System.out.println(LoginInfo.user.getUserCode());
                                            Toast.makeText(getApplicationContext(), "登陆成功！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                break;
                            default:
                                break;
                        }
                    }
                };
            }
        });

    }


}