package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.utils.LoginInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王伟健 on 2018-06-07.
 */

public class SingleMessageAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,String>> messages = new ArrayList<>();
    private Map<String,String> message;

    public SingleMessageAdapter(Context context) {
        this.context = context;
    }


    /** 添加item数据 */
    public void addMessage(Map<String,String> message) {
        if (messages != null)
            messages.add(message);// 添加数据
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
        message = messages.get(position);
        String userCode =  message.get("userCode");
        if (userCode== LoginInfo.user.getUserCode()){
            convertView = View.inflate(context, R.layout.room_chatitem_me, null);
        }else if (userCode!= LoginInfo.user.getUserCode()){
            convertView = View.inflate(context, R.layout.room_chatitem_others, null);
        }
        String text = message.get("text");
        // 设置文本
        ((TextView) convertView.findViewById(R.id.msg)).setText(text);
        return convertView;
    }

}
