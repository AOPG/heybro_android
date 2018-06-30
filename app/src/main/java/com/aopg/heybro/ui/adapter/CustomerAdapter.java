package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.Lunbo.GlideRoundTransform;
import com.aopg.heybro.ui.Lunbo.ShareCardView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.Set;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/3/9.
 */

public class CustomerAdapter extends BaseAdapter{
    private List<Map<String,Object>> data;
    private Context context;
    private int item_layout_id;

    public CustomerAdapter(Context context,
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

        ImageView iv = convertView.findViewById(R.id.ball_image);


        Map<String,Object> map = data.get(position);

        iv.setImageResource((int)map.get("src"));


        RequestOptions myOptions = new RequestOptions()
                .transform(new GlideRoundTransform(context,12));
        Glide.with(context)
                .load((int)data.get(position).get("src"))
                .apply(myOptions)
                .into(iv);

        return convertView;
    }


}
