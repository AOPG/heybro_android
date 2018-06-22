package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.Concern;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.ui.adapter.MyConcernAdapter;
import com.aopg.heybro.ui.adapter.SearchfriendAdapter;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;

/**
 * Created by L on 2018/6/19.
 */

/*
*
*报废的
*
*
*
* */
public class SearchFriendActivity extends AppCompatActivity {
//    List<User> users;
//    private ImageView btn_search;
//    private EditText userCode;
//    private OkHttpClient client;
//    private String user_code_txt;
//    private View friendView;
//    private PopupWindow window;
//    private MainHandler mainHandler = new MainHandler();
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_friend_addfriend);
//        btn_search =findViewById(R.id.search_ensure);
//        userCode=findViewById(R.id.search_friend);
//
////        ImageView concern_back = findViewById(R.id.concern_back);
////        concern_back.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                onBackPressed();
////            }
////        });
//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                friendView = getLayoutInflater().inflate(R.layout.search_friend_msg, null);
//                System.out.println("测试");
//                if (v.getId() == R.id.search_ensure) {
//                    user_code_txt = userCode.getText().toString();
//                    sendRequest(user_code_txt);
//                }
//                if (null == window || !window.isShowing()) {
//                    window = new PopupWindow(friendView, 850, 1000, true);
//                    // 设置PopupWindow是否能响应外部点击事件
//                    window.setOutsideTouchable(false);
//                    // 设置PopupWindow是否能响应点击事件
//                    window.setTouchable(true);
//                    window.showAtLocation(v, Gravity.LEFT, 20, -200);
//                }
//            }
//        });
//
//        }
//        private void sendRequest(final String user_code_txt){
//            Map map =new HashMap();
//            map.put("userCode",user_code_txt);
//            JSONObject jsonObject =new JSONObject(map);
//            String jsonString = jsonObject.toString();
//            client = HttpUtils.init(client);
//            Request request = new Request.Builder().
//                url(BUILD_URL("averageUser/searchUserById?id="+jsonString )).build();
//            Call call = client.newCall(request);
//            call.enqueue(new Callback() {//4.回调方法
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                users=new ArrayList<>();
//                String result = response.body().string();
//                JSONArray UserInfo =
//                        ((JSONObject)((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
//                String success = (JSONObject.parseObject(result)).getString("success");
//                if (null!=success&&success.equals("true")) {
//                    for (int i = 0; i < UserInfo.size(); i++) {
//                        User user = new User();
//                        if(((JSONObject)UserInfo.get(i)).getString("userCode")==user_code_txt) {
//                            String userName =
//                                    ((JSONObject) UserInfo.get(i)).getString("userName");
//                            user.setUsername(userName);
//                            user.setUserCode(user_code_txt);
//                            users.add(user);
//                        }
//                    }
//                    Message message = mainHandler.obtainMessage(1,"formCallBack");
//                    mainHandler.sendMessage(message);
//                }
//            }
//        });
//    }
//    public void onBackPressed() {
//        //返回
//        super.onBackPressed();
//    }
//    private class MainHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 1:
//
//                    SearchfriendAdapter adapter = new SearchfriendAdapter(SearchFriendActivity.this,R.layout.search_friend_msg_item,users);
//                    ListView userlv=friendView.findViewById(R.id.friend_lv);
//                    userlv.setAdapter(adapter);
//            }
//        }
//    }
}
