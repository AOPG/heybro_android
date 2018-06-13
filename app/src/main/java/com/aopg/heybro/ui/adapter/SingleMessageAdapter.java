package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.entity.ChatRecord;
import com.aopg.heybro.entity.Concern;
import com.aopg.heybro.utils.LoginInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王伟健 on 2018-06-07.
 */

public class SingleMessageAdapter extends BaseAdapter {
    private Context context;
    private List<ChatRecord> messages;
    private ChatRecord chatRecord;
    private ListView messageLv;

    public SingleMessageAdapter(Context context, String userConcernCode, ListView messageLv) {
        this.context = context;

        messages = DataSupport.where("userCode = ? and theOrtherCode = ?",
                LoginInfo.user.getUserCode()+"",userConcernCode).limit(20).order("Date desc").find(ChatRecord.class);
        Collections.reverse(messages);
        this.messageLv = messageLv;

    }


    /** 添加item数据 */
    public void addMessage(ChatRecord chatRecord) {
        if (messages != null)
            messages.add(chatRecord);// 添加数据
        messageLv.smoothScrollToPosition(messageLv.getCount() - 1);
    }

    /** 移除item数据 */
    public void delMessage() {
        if (messages != null && messages.size() > 0)
            messages.remove(messages.size() - 1);// 移除最后一条数据
    }

    @Override
    public int getCount() {
        if (messages == null)
            return 0;
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        chatRecord = messages.get(position);
        String userCode =  chatRecord.getUserCode();
        if (!chatRecord.isToMe()){
            convertView = View.inflate(context, R.layout.room_chatitem_me, null);
        }else {
            convertView = View.inflate(context, R.layout.room_chatitem_others, null);
        }
        String text = chatRecord.getMessage();
        // 设置文本
        ((TextView) convertView.findViewById(R.id.msg)).setText(text);
        return convertView;
    }

}
