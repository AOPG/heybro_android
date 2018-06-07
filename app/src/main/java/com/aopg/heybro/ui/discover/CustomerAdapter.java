package com.aopg.heybro.ui.discover;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aopg.heybro.R;


import java.util.List;
import java.util.Map;

/**
 * Created by 王攀 on 2018-06-06.
 * 发现模块的下滑listView
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

        ImageView iv = convertView.findViewById(R.id.distrend_picfir);
        ImageView iv2 = convertView.findViewById(R.id.distrend_picsec);
        TextView text1 = convertView.findViewById(R.id.dictrend_textfir);
        TextView text2 = convertView.findViewById(R.id.distrend_textsec);
        TextView text3 = convertView.findViewById(R.id.distrend_textthir);
        TextView text4 = convertView.findViewById(R.id.distrend_textfor);
        TextView text5 = convertView.findViewById(R.id.distrend_textfif);


        Map<String,Object> map = data.get(position);

        iv.setImageResource((int)map.get("src"));
        iv2.setImageResource((int)map.get("src_another"));
        text1.setText(map.get("textfir").toString());
        text2.setText(map.get("textsec").toString());
        text3.setText(map.get("textthir").toString());
        text4.setText(map.get("textfor").toString());
        text5.setText(map.get("textfif").toString());

        return convertView;
    }

}
