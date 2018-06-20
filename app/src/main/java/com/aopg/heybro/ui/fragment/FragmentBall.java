package com.aopg.heybro.ui.fragment;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.entity.Concern;
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
 * 打球界面
 */

public class FragmentBall extends Fragment{
    private View rootView;
    private View createRoomView;
    private View joinRoomView;
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
            rootView = inflater.inflate(R.layout.fragment_basketball_ball, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        //房间部分
        initUI();
        //创建房间
        createRoom();
        //快速匹配
        joinRoom();
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
                        basketRoomInfo.setRate(getActivity().getResources().getStringArray(R.array.spinner_rate)[i]);
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
                        basketRoomInfo.setType(0);
                        basketRoomInfo.setMaster(LoginInfo.user);
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
    public void joinRoom(){
        final Button btn_join = rootView.findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinRoomView = LayoutInflater.from(getContext()).inflate(R.layout.join_room,null,false);
                if(null == window || !window.isShowing()) {
                    window = new PopupWindow(joinRoomView, 850, 1000, true);
                    // 设置PopupWindow是否能响应外部点击事件
                    window.setOutsideTouchable(false);
                    // 设置PopupWindow是否能响应点击事件
                    window.setTouchable(true);
                    window.showAtLocation(view, Gravity.LEFT, 20,-200);
                }
                /**
                 * 关闭快速匹配
                 */
                Button join_close = joinRoomView.findViewById(R.id.join_close);
                join_close.setOnClickListener(new View.OnClickListener() {
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
    public void httpInsertRoom(BasketRoomInfo basketRoomInfo,String password){
        client = HttpUtils.init(client);
        Request request;
        if(password != null&&!password.equals("")) {
            basketRoomInfo.setPassword(password);
            request = new Request.Builder().
                    url(BUILD_URL("basketRoom/createRoom?roomName="
                            + basketRoomInfo.getRoomName() + "&type=" + basketRoomInfo.getType()
                            + "&mode=" + basketRoomInfo.getMode() + "&rate=" + basketRoomInfo.getRate()
                            + "&num=" + basketRoomInfo.getNum() + "&password=" + basketRoomInfo.getPassword()
                            + "&userCode=" + basketRoomInfo.getMaster().getUserCode())).build();
        }else{
            request = new Request.Builder().
                    url(BUILD_URL("basketRoom/createRoom?roomName="
                            + basketRoomInfo.getRoomName() + "&type=" + basketRoomInfo.getType()
                            + "&mode=" + basketRoomInfo.getMode() + "&rate=" + basketRoomInfo.getRate()
                            + "&num=" + basketRoomInfo.getNum() + "&userCode=" + basketRoomInfo.getMaster().getUserCode())).build();
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

        hListViewAdapter = new HorizontaRoomlListViewAdapter(rootView.getContext(),idss,
                roomCode,roomTitle,roomNum);
        hListView.setAdapter(hListViewAdapter);

        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                hListViewAdapter.setSelectIndex(position);
                hListViewAdapter.notifyDataSetChanged();
            }
        });

    }

}
