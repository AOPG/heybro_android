package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.ui.adapter.SearchfriendAdapter;
import com.aopg.heybro.utils.HttpUtils;

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
 * Created by 壑过忘川 on 2018/6/5.
 * 添加好友界面
 */


/*
*
*报废的
*
*
*
* */







public class AddFriendActivity extends Activity{
    List<User> users;
    private ImageView btn_search;
    private EditText userCode;
    private OkHttpClient client;
    private String user_code_txt;
    private View friendView;
    private PopupWindow window;
    private MainHandler mainHandler = new MainHandler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_friend_addfriend);
        //返回按钮
        ImageView addFriend_back = findViewById(R.id.addfriend_back);
        addFriend_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_search =findViewById(R.id.search_ensure);
        userCode=findViewById(R.id.search_friend);
        user_code_txt=String.valueOf(userCode.getText());
//        ImageView concern_back = findViewById(R.id.concern_back);
//        concern_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendView = getLayoutInflater().inflate(R.layout.search_friend_msg, null);
                System.out.println("测试");
                if (v.getId() == R.id.search_ensure) {
                    user_code_txt = userCode.getText().toString();
                    sendRequest(user_code_txt);
                }
                if (null == window || !window.isShowing()) {
                    window = new PopupWindow(friendView, 850, 1000, true);
                    // 设置PopupWindow是否能响应外部点击事件
                    window.setOutsideTouchable(false);
                    // 设置PopupWindow是否能响应点击事件
                    window.setTouchable(true);
                    window.showAtLocation(v, Gravity.LEFT, 20, -200);
                }
            }
        });

    }
    private void sendRequest(final String user_code_txt){
        client = HttpUtils.init(client);
        Request request = new Request.Builder().
                url(BUILD_URL("averageUser/searchUserById?id="+user_code_txt)).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                users=new ArrayList<>();
                String result = response.body().string();
                System.out.println("测试"+result+"123");
                JSONArray UserInfo =
                        ((JSONObject)((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
                String success = (JSONObject.parseObject(result)).getString("success");
                if (null!=success&&success.equals("true")) {
                    for (int i = 0; i < UserInfo.size(); i++) {
                        User user = new User();
                        if(((JSONObject)UserInfo.get(i)).getString("userCode")==user_code_txt) {
                            String userName =
                                    ((JSONObject) UserInfo.get(i)).getString("userName");
                            user.setUsername(userName);
                            user.setUserCode(user_code_txt);
                            users.add(user);
                        }
                    }
                    Message message = mainHandler.obtainMessage(1,"formCallBack");
                    mainHandler.sendMessage(message);
                }
            }
        });
    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }
    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    SearchfriendAdapter adapter = new SearchfriendAdapter(AddFriendActivity.this,users);
                    ListView userlv=friendView.findViewById(R.id.friend_lv);
                    userlv.setAdapter(adapter);
            }
        }
    }
}
