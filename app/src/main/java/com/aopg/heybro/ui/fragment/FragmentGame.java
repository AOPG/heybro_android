package com.aopg.heybro.ui.fragment;

import android.content.Intent;
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
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.ui.activity.ChartRoomActivity;
import com.aopg.heybro.ui.room.HorizontaRoomlListViewAdapter;
import com.aopg.heybro.ui.room.HorizontalRoomListView;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;

/**
 * Created by 壑过忘川 on 2018/6/7.
 * 比赛界面
 */


public class FragmentGame extends Fragment {
    private View rootView;
    private View createRoomView;
    private View matchRoomView;
    private PopupWindow window;
    HorizontalRoomListView hListView;
    HorizontaRoomlListViewAdapter hListViewAdapter;

    private BasketRoomInfo basketRoomInfo;
    private OkHttpClient client;
    private String password;//房间密码（可选）
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e("", "onCreateView");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_basketball_game, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        TextView myRate = rootView.findViewById(R.id.my_rate_game);
        myRate.setText("当前等级:    "+ LoginInfo.user.getUserGrade());
        //房间部分
        initUI();
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
                        basketRoomInfo.setRoomName( roomName.getText().toString());
                        if(passwordSet.getText().toString() != null){
                            password = passwordSet.getText().toString();
                        }
                        basketRoomInfo.setType(1);
                        basketRoomInfo.setMaster(LoginInfo.user.getUserCode());
                        httpInsertRoom(basketRoomInfo,password);
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
                    url(BUILD_URL("basketRoom/createRoom?roomName="
                            + basketRoomInfo.getRoomName() + "&type=" + basketRoomInfo.getType()
                            + "&mode=" + basketRoomInfo.getMode() + "&rateLow=" + basketRoomInfo.getRateLow()
                            +"&rateHigh="+basketRoomInfo.getRateHigh()
                            + "&num=" + basketRoomInfo.getNum() + "&password=" + basketRoomInfo.getPassword()
                            + "&userCode=" + basketRoomInfo.getMaster())).build();
        }else{
            request = new Request.Builder().
                    url(BUILD_URL("basketRoom/createRoom?roomName="
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
                    TextView tvRoom = createRoomView.findViewById(R.id.roomId);
                    String roomId = tvRoom.getText().toString();
                    String roomName = roomInfo.getString("roomName");
                    Intent roomIntent = new Intent(getActivity(), ChartRoomActivity.class);
                    roomIntent.putExtra("roomId",roomId);
                    roomIntent.putExtra("roomName",roomName);
                    startActivity(roomIntent);
                }
            }
        });
    }
    //房间部分
    public void initUI(){
        hListView = (HorizontalRoomListView)rootView.findViewById(R.id.ball_horizon_listview);
        String[] roomCode = {"20180613001", "20180613002", "20180613003", "20180613004",
                "20180613005","20180613006"};
        String[] roomTitle = {"快来打球吧！", "快来打球吧！", "快来打球吧！",
                "快来打球吧！","快来打球吧！","快来打球吧！"};
        String[] roomNum = {"1/10", "2/10", "3/10", "4/10","5/10","6/10"};
        final int[] idss = {R.drawable.room_corner, R.drawable.room_corner,
                R.drawable.room_corner, R.drawable.room_corner,R.drawable.room_corner
                ,R.drawable.room_corner};

        hListViewAdapter = new HorizontaRoomlListViewAdapter(rootView.getContext(),
                roomCode,roomTitle,roomNum);
        hListView.setAdapter(hListViewAdapter);
        //      hListView.setOnItemSelectedListener(new OnItemSelectedListener() {
        //
        //          @Override
        //          public void onItemSelected(AdapterView<?> parent, View view,
        //                  int position, long id) {
        //              // TODO Auto-generated method stub
        //              if(olderSelected != null){
        //                  olderSelected.setSelected(false); //上一个选中的View恢复原背景
        //              }
        //              olderSelected = view;
        //              view.setSelected(true);
        //          }
        //
        //          @Override
        //          public void onNothingSelected(AdapterView<?> parent) {
        //              // TODO Auto-generated method stub
        //
        //          }
        //      });
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
//              if(olderSelectView == null){
//                  olderSelectView = view;
//              }else{
//                  olderSelectView.setSelected(false);
//                  olderSelectView = null;
//              }
//              olderSelectView = view;
//              view.setSelected(true);
//                previewImg.setImageResource(ids[position]);
                hListViewAdapter.setSelectIndex(position);
                hListViewAdapter.notifyDataSetChanged();

            }
        });

    }
}
