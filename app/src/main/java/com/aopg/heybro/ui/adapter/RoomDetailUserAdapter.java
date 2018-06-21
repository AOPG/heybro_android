package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.entity.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static com.aopg.heybro.utils.HttpUtils.BASE_URL;

/**
 * Created by 王伟健 on 2018-06-20.
 */

public class RoomDetailUserAdapter extends BaseAdapter {
    private Context context;
    private List<User> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView;
    private TextView  userNickNameTv;

    public RoomDetailUserAdapter(Context context,List<User> list){
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.room_user_grid_item, null);
        mImageView = convertView.findViewById(R.id.roomMeber);
        userNickNameTv = convertView.findViewById(R.id.user_name);
        if (position < list.size()) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.image)
                    .fallback(R.drawable.image).centerCrop();

            Glide.with(context)
                    .load(BASE_URL+ list.get(position).getUserPortrait())
                    .apply(options)
                    .into(mImageView);
            userNickNameTv.setText(list.get(position).getNickName());
        }else{
            mImageView.setImageResource(R.drawable.add);//最后一个显示加号图片
            userNickNameTv.setText("");
        }
        return convertView;
    }
}
