package com.aopg.heybro.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.entity.ChatRecord;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.ui.adapter.SingleMessageAdapter;
import com.aopg.heybro.ui.fragment.FragmentFriend;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;
import com.aopg.heybro.utils.teamhead.utils.DensityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_FROM_APPKEY;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_FROM_USERNAME;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_GROUPID;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_IS_GROUP;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_MSGID;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_MSG_TYPE;
import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;

/**
 * Created by 王伟健 on 2018-06-07.
 */

public class SingleChartActivity extends AppCompatActivity {
    private static final String TAG = "SingleChartActivity";
    private String note;
    private EditText userMessage;
    private Button sendMessage;
    private ContentType contentType;
    private Message message;
    private ListView messageLv;
    private SingleMessageAdapter singleMessageAdapter;
    private TextView noteTv;
    private ImageView back;
    private String userConcernCode;
    private OkHttpClient client;
    private User concernUser;
    private MainHandler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        note = getIntent().getStringExtra("note");
        noteTv.setText(note);
    }

    private void initView() {
        mainHandler = new MainHandler();
        concernUser = new User();
        userConcernCode = getIntent().getStringExtra("userConcernCode");
        concernUser.setUserCode(userConcernCode);
        loadUserInfoByUserCode(userConcernCode);
        setContentView(R.layout.single_chart_activity);
        back = findViewById(R.id.back);
        messageLv = findViewById(R.id.msg_list_view);

        userMessage = findViewById(R.id.input_msg);
        sendMessage = findViewById(R.id.send_msg);
        noteTv = findViewById(R.id.user_name);

        note = getIntent().getStringExtra("note");
        noteTv.setText(note);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleChartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        sendMessage.setClickable(false);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sendMessage.isClickable()){
                    Toast.makeText(SingleChartActivity.this,"正在加载用户信息,请稍后",Toast.LENGTH_SHORT);
                }
                final String name = userConcernCode;
                final String text = userMessage.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(text)) {

                    //通过username和appkey拿到会话对象，通过指定appkey可以创建一个和跨应用用户的会话对象，从而实现跨应用的消息发送
                    Conversation mConversation = JMessageClient.getSingleConversation(name);
                    if (mConversation == null) {
                        mConversation = Conversation.createSingleConversation(name);
                    }

                    //构造message content对象
                    TextContent textContent = new TextContent(text);
                    //设置自定义的extra参数
                   // textContent.setStringExtra(extraKey, extraValue);

                    //创建message实体，设置消息发送回调。
                    ChatRecord chatRecord = new ChatRecord();
                    chatRecord.setUserCode(LoginInfo.user.getUserCode());
                    chatRecord.setTheOrtherCode(name);
                    chatRecord.setDate(new Date().getTime());
                    chatRecord.setToMe(false);
                    chatRecord.setMessage(text);
                    chatRecord.save();
                    Message message = mConversation.createSendMessage(textContent, userConcernCode);
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Log.i(TAG, "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(TAG, "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //设置消息发送时的一些控制参数
                    MessageSendingOptions options = new MessageSendingOptions();
                    options.setNeedReadReceipt(true);//是否需要对方用户发送消息已读回执
                    options.setRetainOffline(true);//是否当对方用户不在线时让后台服务区保存这条消息的离线消息
                    options.setShowNotification(true);//是否让对方展示sdk默认的通知栏通知
                    options.setCustomNotificationEnabled(true);//是否需要自定义对方收到这条消息时sdk默认展示的通知栏中的文字
                    options.setNotificationTitle(name);//自定义对方收到消息时通知栏展示的title
                    //options.setNotificationAtPrefix(mEt_customNotifyAtPrefix.getText().toString());//自定义对方收到消息时通知栏展示的@信息的前缀
                    options.setNotificationText(text);//自定义对方收到消息时通知栏展示的text

                    //发送消息
                    JMessageClient.sendMessage(message, options);

                    //在聊天界面上添加信息
                    singleMessageAdapter.addMessage(chatRecord);
                    singleMessageAdapter.notifyDataSetChanged();
                    userMessage.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "消息不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initData() {
        IntentFilter filter = new IntentFilter("single_message");
        registerReceiver(broadcastReceiver, filter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msgTypeString = intent.getStringExtra(EXTRA_MSG_TYPE);
            contentType = ContentType.valueOf(msgTypeString);
            String user = intent.getStringExtra(EXTRA_FROM_USERNAME);
            int msgid = intent.getIntExtra(EXTRA_MSGID, 0);
            Conversation conversation;

            conversation = JMessageClient.getSingleConversation(user);
            Log.e(TAG, "initData: 收到来自用户：" + user + "的消息\n" );
            if (conversation == null) {
                Toast.makeText(getApplicationContext(), "会话对象为null", Toast.LENGTH_SHORT).show();
                return;
            }
            message = conversation.getMessage(msgid);

            switch (contentType) {
                case text:
                    TextContent textContent = (TextContent) message.getContent();
                    ChatRecord chatRecord = new ChatRecord();
                    chatRecord.setUserCode(LoginInfo.user.getUserCode());
                    chatRecord.setTheOrtherCode(user);
                    chatRecord.setMessage(textContent.getText());
                    chatRecord.setDate(new Date().getTime());
                    chatRecord.setToMe(true);
                    chatRecord.save();
                    singleMessageAdapter.addMessage(chatRecord);
                    singleMessageAdapter.notifyDataSetChanged();
                    Log.e(TAG, "文本消息" + "\n消息内容 = " + textContent.getText());
                    break;
            }
        }
    };


    private void loadUserInfoByUserCode(String userCode){
        client = HttpUtils.init(client);
        Request request = new Request.Builder().
                url(BUILD_URL("averageUser/userInfoByCode?userCode=" + userCode)).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                android.os.Message message = mainHandler.obtainMessage(501,"");
                mainHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                String success = (JSONObject.parseObject(result)).getString("success");
                if (null!=success&&success.equals("true")) {
                    JSONObject userInfo = JSONObject.
                            parseObject((JSONObject.parseObject(result)).getString("data"));
                    JSONObject userDetailInfo = userInfo.getJSONObject("userInfo");
                    concernUser.setNickName(userInfo.getString("userNickname"));
                    concernUser.setUserSignature(userInfo.getString("userSignature"));
                    concernUser.setUserGrade(userInfo.getInteger("userGrade"));
                    concernUser.setUserPhone(userInfo.getString("userPhone"));
                    concernUser.setUserMail(userInfo.getString("userMail"));
                    concernUser.setUserCode(userInfo.getString("userCode"));
                    concernUser.setUserIntro(userInfo.getString("userIntro"));
                    concernUser.setUserCode(userInfo.getString("userCode"));
                    concernUser.setUserPortrait(userInfo.getString("userPortrait"));
                    concernUser.setHomepageBack(userInfo.getString("homepageBack"));
                    concernUser.setRoomId(Long.parseLong(userDetailInfo.getString("roomId")));
                    concernUser.setBirthday(Long.parseLong(userInfo.getString("birthday")));
                    concernUser.setUserProvince(userInfo.getString("userProvince"));
                    concernUser.setUserCity(userInfo.getString("userCity"));
                    concernUser.setUserSex(userInfo.getString("userSex"));
                    android.os.Message message = mainHandler.obtainMessage(500,"");
                    mainHandler.sendMessage(message);
                }
            }
        });
    }


    private class MainHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 500:
                    singleMessageAdapter = new SingleMessageAdapter(SingleChartActivity.this,userConcernCode,messageLv,concernUser);
                    messageLv.setAdapter(singleMessageAdapter);
                    sendMessage.setClickable(true);
                    break;
                case 501:
                    singleMessageAdapter = new SingleMessageAdapter(SingleChartActivity.this,userConcernCode,messageLv,concernUser);
                    messageLv.setAdapter(singleMessageAdapter);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
