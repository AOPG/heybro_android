package com.aopg.heybro;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.aopg.heybro.entity.ChatRecord;
import com.aopg.heybro.entity.ChatRoomRecord;
import com.aopg.heybro.entity.UserConversation;
import com.aopg.heybro.ui.activity.ChartRoomActivity;
import com.aopg.heybro.ui.activity.SingleChartActivity;
import com.aopg.heybro.utils.LoginInfo;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;


/**
 * 在demo中对于通知栏点击事件和在线消息接收事件，我们都直接在全局监听
 */
public class GlobalEventListener {
    private Context appContext;

    public GlobalEventListener(Context context) {
        appContext = context;
        JMessageClient.registerEventReceiver(this);
    }

    public void onEvent(NotificationClickEvent event) {
        Message msg = event.getMessage();
        UserInfo fromUser = msg.getFromUser();
        if (msg.getTargetType() == ConversationType.group){
            Intent intent = new Intent(appContext, ChartRoomActivity.class);
            GroupInfo groupInfo = (GroupInfo) msg.getTargetInfo();
            intent.putExtra("roomName",groupInfo.getGroupName());
            intent.putExtra("roomId",groupInfo.getGroupID());
            appContext.startActivity(intent);
        }else {
            Intent intent = new Intent(appContext, SingleChartActivity.class);
            intent.putExtra("userConcernCode",fromUser.getUserName());
            appContext.startActivity(intent);
        }
    }

    public void onEvent(MessageEvent event) {
        saveConversationDate(event.getMessage());
    }

    private void saveConversationDate(Message msg) {
        UserInfo fromUser = msg.getFromUser();
        Intent notificationIntent;
        Conversation conversation;
        if (msg.getTargetType() == ConversationType.group) {
            notificationIntent = new Intent("group_message");
            GroupInfo groupInfo = (GroupInfo) msg.getTargetInfo();

            conversation = JMessageClient.getGroupConversation(groupInfo.getGroupID());
            Log.e("","收到来自群聊：" + groupInfo.getGroupID() + ",用户" + fromUser.getUserName() + "的消息\n");
            if (conversation == null) {
                Toast.makeText(getApplicationContext(), "会话对象为null", Toast.LENGTH_SHORT).show();
                return;
            }
            Message message = conversation.getMessage(msg.getId());

            TextContent textContent = (TextContent) message.getContent();

            //存储Group信息
            ChatRoomRecord chatRoomRecord = new ChatRoomRecord();
            chatRoomRecord.setUserCode(fromUser.getUserName());
            chatRoomRecord.setMessage(textContent.getText());
            chatRoomRecord.setRoomId(groupInfo.getGroupID()+"");
            chatRoomRecord.setRoomName(groupInfo.getGroupName());
            chatRoomRecord.setDate(new Date().getTime());
            chatRoomRecord.save();

            notificationIntent.putExtra("chatRoomRecord",chatRoomRecord);


        } else {
            notificationIntent = new Intent("single_message");

            conversation = JMessageClient.getSingleConversation(fromUser.getUserName());

            Log.e("", "initData: 收到来自用户：" + fromUser.getUserName() + "的消息\n" );
            if (conversation == null) {
                Toast.makeText(getApplicationContext(), "会话对象为null", Toast.LENGTH_SHORT).show();
                return;
            }
            Message message = conversation.getMessage(msg.getId());
            TextContent textContent = (TextContent) message.getContent();
            ChatRecord chatRecord = new ChatRecord();
            chatRecord.setUserCode(LoginInfo.user.getUserCode());
            chatRecord.setTheOrtherCode(fromUser.getUserName());
            chatRecord.setMessage(textContent.getText());
            chatRecord.setDate(new Date().getTime());
            chatRecord.setToMe(true);
            chatRecord.save();

            UserConversation userConversation = new UserConversation();
            List<UserConversation> userConversationList =  DataSupport
                    .where("userCode = ? and userConversationCode = ?", LoginInfo.user.getUserCode(),fromUser.getUserName())
                    .find(UserConversation.class);
            if (null!=userConversationList&&userConversationList.size()>0){
                userConversation.setUnReadNum(userConversationList.get(0).getUnReadNum()+1);
                userConversation.setDate(new Date().getTime());
                userConversation.updateAll("userCode = ? and userConversationCode = ?", LoginInfo.user.getUserCode(),fromUser.getUserName());
            }else {
                userConversation.setUnReadNum(0);
                userConversation.setUserCode(LoginInfo.user.getUserCode());
                userConversation.setUserConversationCode(fromUser.getUserName());
                userConversation.setDate(new Date().getTime());
                userConversation.save();
            }
            Intent updatePrivateActivity = new Intent("updatePrivateActivity");
            appContext.sendBroadcast(updatePrivateActivity);

            notificationIntent.putExtra("chatRecord",chatRecord);
        }
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.sendBroadcast(notificationIntent);
    }
}
