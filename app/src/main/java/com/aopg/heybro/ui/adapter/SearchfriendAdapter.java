package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.entity.User;

import java.util.List;

/**
 * Created by L on 2018/6/19.
 */

public class SearchfriendAdapter extends BaseAdapter {
    private Context context;
    private int item_layout_id;
    private List<User> users;
    public SearchfriendAdapter(Context context, int search_friend_msg_item, List<User> users){
        this.context = context;
        this.item_layout_id = search_friend_msg_item;
        this.users = users;
    }
    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
        }
        final TextView userName=convertView.findViewById(R.id.userName);
        final TextView userCode=convertView.findViewById(R.id.userId);
        userName.setText(users.get(position).getUsername());
        userCode.setText(users.get(position).getUserCode());
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        return convertView;
    }
}
