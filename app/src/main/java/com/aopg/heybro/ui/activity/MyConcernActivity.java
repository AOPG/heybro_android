package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.Concern;
import com.aopg.heybro.ui.adapter.MyConcernAdapter;
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
 * Created by 壑过忘川 on 2018/6/6.
 * 我的关注界面
 */

public class MyConcernActivity extends Activity {
    private OkHttpClient client;
    List<Concern> concerns;
    private MainHandler mainHandler = new MainHandler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_friend_guanzhu);
        //返回按钮
        ImageView concern_back = findViewById(R.id.concern_back);
        concern_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        client = HttpUtils.init(client);

        Request request = new Request.Builder().
                url(BUILD_URL("concern/getConcernIndex?userCode=" + LoginInfo.user.getUserCode())).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                concerns = new ArrayList<>();
                String result = response.body().string();

                String success = (JSONObject.parseObject(result)).getString("success");

                if (null!=success&&success.equals("true")) {
                    JSONArray concernInfo =
                            ((JSONObject)((JSONObject.parseObject(result)).get("data"))).getJSONArray("list");
                    for (int i = 0; i < concernInfo.size(); i++) {
                        Concern concern = new Concern();
                        String userConcernCode =
                                ((JSONObject)concernInfo.get(i)).getString("userConcernCode");
                        String userNote =
                                ((JSONObject)concernInfo.get(i)).getString("userNote");
                        String userPortrait =
                                ((JSONObject)concernInfo.get(i)).getString("userPortrait");
                        concern.setUserConcernCode(userConcernCode);
                        concern.setUserNote(userNote);
                        concern.setUserCode(LoginInfo.user.getUserCode());
                        concern.setUserPortrait(userPortrait);
                        concern.save();
                        concerns.add(concern);
                    }
                    Message message = mainHandler.obtainMessage(0,"formCallBack");
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
                case 0:
                    MyConcernAdapter adapter = new MyConcernAdapter(MyConcernActivity.this,concerns);
                    ListView concernsLv = findViewById(R.id.concerns);
                    concernsLv.setAdapter(adapter);
            }
        }
    }
}
