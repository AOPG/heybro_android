package com.aopg.heybro.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.adapter.SingleMessageAdapter;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.FileContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;
import cn.jpush.im.api.BasicCallback;

import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_FROM_APPKEY;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_FROM_USERNAME;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_GROUPID;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_IS_GROUP;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_MSGID;
import static com.aopg.heybro.im.createmessage.ShowMessageActivity.EXTRA_MSG_TYPE;

/**
 * Created by 王伟健 on 2018-06-07.
 */

public class SingleChartActivity extends AppCompatActivity {
    private static final String TAG = "CreateSigTextMessage";
    private String username;
    private EditText userMessage;
    private Button sendMessage;
    private ContentType contentType;
    private Message message;
    private ListView messageLv;
    private SingleMessageAdapter singleMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }


    private void initView() {
        setContentView(R.layout.single_chart_activity);
        messageLv = findViewById(R.id.msg_list_view);
        singleMessageAdapter = new SingleMessageAdapter(this);
        messageLv.setAdapter(singleMessageAdapter);
        userMessage = findViewById(R.id.input_msg);
        sendMessage = findViewById(R.id.send_msg);
        username = getIntent().getStringExtra("username");

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username;
                String text = userMessage.getText().toString();
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
                    Message message = mConversation.createSendMessage(textContent, username);
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
         //   textView.setText(intent.getExtras().getString("data"));
            String msgTypeString = intent.getStringExtra(EXTRA_MSG_TYPE);
            contentType = ContentType.valueOf(msgTypeString);
            boolean isGroup = intent.getBooleanExtra(EXTRA_IS_GROUP, false);
            long gid = intent.getLongExtra(EXTRA_GROUPID, 0);
            String user = intent.getStringExtra(EXTRA_FROM_USERNAME);
            String appkey = intent.getStringExtra(EXTRA_FROM_APPKEY);
            int msgid = intent.getIntExtra(EXTRA_MSGID, 0);
            Conversation conversation;

            conversation = JMessageClient.getSingleConversation(user, appkey);
            Log.e(TAG, "initData: 收到来自用户：" + user + "的消息\n" );
            if (conversation == null) {
                Toast.makeText(getApplicationContext(), "会话对象为null", Toast.LENGTH_SHORT).show();
                return;
            }
            message = conversation.getMessage(msgid);

            switch (contentType) {
                case text:
                    TextContent textContent = (TextContent) message.getContent();
                    singleMessageAdapter.addMessage(textContent.getText());
                    singleMessageAdapter.notifyDataSetChanged();
                    Log.e(TAG, "文本消息" + "\n消息内容 = " + textContent.getText());
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
