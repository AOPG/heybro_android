package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.ui.activity.AddFriendActivity;
import com.aopg.heybro.ui.activity.FriendInfoActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static com.aopg.heybro.utils.HttpUtils.BASE_URL;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
        }
        final ImageView userPortrait=convertView.findViewById(R.id.user_img);
        final TextView userName=convertView.findViewById(R.id.userName);
        final TextView userCode=convertView.findViewById(R.id.userId);
        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();
        Glide.with(convertView)
                .load(BASE_URL+ users.get(position).getUserPortrait())
                .apply(options)
                .into(userPortrait);
        userName.setText(users.get(position).getUsername());
        userCode.setText(users.get(position).getUserCode());
        final String username=users.get(position).getUsername();
        final String usercode=users.get(position).getUserCode();
        final String userprovince=users.get(position).getUserProvince();
        final String usercity=users.get(position).getUserCity();
        final int usergrade=users.get(position).getUserGrade();
        final String userportrait=users.get(position).getUserPortrait();
        final String userintro=users.get(position).getUserIntro();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=users.get(position);
                user.setUsername(username);
                user.setUserCode(usercode);
                user.setUserProvince(userprovince);
                user.setUserCity(usercity);
                user.setUserGrade(usergrade);
                user.setUserPortrait(userportrait);
                user.setUserIntro(userintro);
                Intent intent = new Intent();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("user",user);
                intent.setClass(context,FriendInfoActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("usercode",usercode);
                intent.putExtra("userprovince",userprovince);
                intent.putExtra("usercity",usercity);
                intent.putExtra("usergrade",usergrade);
                intent.putExtra("userportrait",userportrait);
                intent.putExtra("userintro",userintro);
                intent.putExtras(mBundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
