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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;

import com.aopg.heybro.entity.ChatRoomRecord;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.ui.adapter.ChartRoomMessageAdapter;

import com.aopg.heybro.ui.adapter.SingleMessageAdapter;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;


import java.io.IOException;
import java.util.Date;


import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;


/**
 * Created by 王伟健 on 2018-06-15.
 */

public class ChartRoomActivity extends AppCompatActivity {

    private static final String TAG = "ChartRoomActivity";
    private String roomName;
    private EditText userMessage;
    private Button sendMessage;
    private ContentType contentType;
    private Message message;
    private ListView messageLv;
    private ChartRoomMessageAdapter chartRoomMessageAdapter;
    private TextView roomNameTv;
    private ImageView back;
    private Long roomId;
    private Button roomDetail;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        roomName = getIntent().getStringExtra("roomName");
        roomId = getIntent().getLongExtra("roomId",0L);
        roomNameTv.setText(roomName);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.chart_room);
        initView();
        initData();
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        roomId = getIntent().getLongExtra("roomId",0L);
        setContentView(R.layout.chart_room);
        back = findViewById(R.id.room_back);
        messageLv = findViewById(R.id.msg_list_view);
        chartRoomMessageAdapter = new ChartRoomMessageAdapter(ChartRoomActivity.this,roomId,messageLv);
        messageLv.setAdapter(chartRoomMessageAdapter);
        userMessage = findViewById(R.id.input_msg);
        sendMessage = findViewById(R.id.send_msg);
        roomNameTv = findViewById(R.id.roomName);
        roomDetail = findViewById(R.id.room_setting);
        roomName = getIntent().getStringExtra("roomName");
        roomNameTv.setText(roomName);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChartRoomActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        roomDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChartRoomActivity.this,RoomDetailActivity.class);
                intent.putExtra("roomName",roomName);
                intent.putExtra("roomId",roomId);
                startActivity(intent);
            }
        });
        sendMessage.setClickable(false);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long groupId = -1L;
                try {
                    groupId = roomId;
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "群组id解析失败", Toast.LENGTH_SHORT).show();
                }
                String text = userMessage.getText().toString();
                if (-1 != groupId && !TextUtils.isEmpty(text)) {
                    String customFromName = LoginInfo.user.getNickName();



                    //通过groupid拿到会话对象
                    Conversation mConversation = JMessageClient.getGroupConversation(groupId);
                    if (mConversation == null) {
                        mConversation = Conversation.createGroupConversation(groupId);
                    }

                    //构造message content对象
                    TextContent textContent = new TextContent(text);

                    ChatRoomRecord chatRoomRecord = new ChatRoomRecord();
                    chatRoomRecord.setUserCode(LoginInfo.user.getUserCode());
                    chatRoomRecord.setMessage(text);
                    chatRoomRecord.setRoomId(roomId+"");
                    chatRoomRecord.setRoomName(roomName);
                    chatRoomRecord.setDate(new Date().getTime());
                    chatRoomRecord.save();
                    Message message;
                        //创建一条没有@任何群成员的消息
                    message = mConversation.createSendMessage(textContent, customFromName);

                    //设置消息发送回调。
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Log.i(TAG, "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(TAG, "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                Toast.makeText(getApplicationContext(), "该群组已解散,请尽快填写比赛信息!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //设置消息发送时的一些控制参数
                    MessageSendingOptions options = new MessageSendingOptions();
                    options.setNeedReadReceipt(true);//是否需要对方用户发送消息已读回执
                    options.setRetainOffline(true);//是否当对方用户不在线时让后台服务区保存这条消息的离线消息
                    options.setShowNotification(true);//是否让对方展示sdk默认的通知栏通知
                    options.setCustomNotificationEnabled(true);//是否需要自定义对方收到这条消息时sdk默认展示的通知栏中的文字
                    options.setNotificationTitle(LoginInfo.user.getNickName());//自定义对方收到消息时通知栏展示的title
             //       options.setNotificationAtPrefix(mEt_customNotifyAtPrefix.getText().toString());//自定义对方收到消息时通知栏展示的@信息的前缀
                    options.setNotificationText(LoginInfo.user.getNickName()+":"+userMessage.getText());//自定义对方收到消息时通知栏展示的text


                    //发送消息
                    chartRoomMessageAdapter.addMessage(chatRoomRecord);
                    chartRoomMessageAdapter.notifyDataSetChanged();
                    userMessage.setText("");
                    JMessageClient.sendMessage(message, options);
                } else {
                    Toast.makeText(getApplicationContext(), "必填字段不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initData() {
        IntentFilter filter = new IntentFilter("group_message");
        registerReceiver(broadcastReceiver, filter);

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ChatRoomRecord chatRoomRecord = (ChatRoomRecord) intent.getSerializableExtra("chatRoomRecord");
            chartRoomMessageAdapter.addMessage(chatRoomRecord);
            chartRoomMessageAdapter.notifyDataSetChanged();
            Log.e(TAG, "文本消息" + "\n消息内容 = " + chatRoomRecord.getMessage());
        }
    };






    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
