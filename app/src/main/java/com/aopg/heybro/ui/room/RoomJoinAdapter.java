package com.aopg.heybro.ui.room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Map;

import static com.aopg.heybro.utils.HttpUtils.BASE_URL;

/**
 * Created by 王攀 on 2018/6/20.
 */

public class RoomJoinAdapter extends BaseAdapter {
    private List<Map<String,Object>> data;
    private Context context;
    private int item_layout_id;

    public RoomJoinAdapter(Context context,
                           List<Map<String,Object>> data,
                           int item_layout_id){
        this.context = context;
        this.item_layout_id = item_layout_id;
        this.data = data;
    }

    //子项数量
    @Override
    public int getCount() {
        return data.size();
    }

    //获取每一项的数据
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater minflater = LayoutInflater.from(context);
            convertView = minflater.inflate(item_layout_id,null);
        }

        ImageView iv = convertView.findViewById(R.id.user_img);
        TextView text1 = convertView.findViewById(R.id.user_name);
        TextView text2 = convertView.findViewById(R.id.user_Intro);


        Map<String,Object> map = data.get(position);

//      iv.setImageResource((int)map.get("img"));

        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();

        Glide.with(context)
                .load(BASE_URL+ map.get("img"))
                .apply(options)
                .into(iv);

        text1.setText(map.get("user_name").toString());
        text2.setText(map.get("user_Intro").toString());

        return convertView;
    }

}
