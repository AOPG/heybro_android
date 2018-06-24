package com.aopg.heybro.ui.data;

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
 * Created by 陈燕博 on 2018/6/24.
 */

public class DataAdapter extends BaseAdapter {
    private Context context;    // 上下文环境
    private List<Map<String, Object>> dataSource; // 声明数据源
    private int item_layout_id; // 声明列表项的布局

    // 声明列表项中的控件
    public DataAdapter(Context context, List<Map<String, Object>> dataSource,
                       int item_layout_id) {
        this.context = context;       // 上下文环境
        this.dataSource = dataSource; // 数据源
        this.item_layout_id = item_layout_id; // 列表项布局文件ID
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {  // 加载列表项布局文件
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(item_layout_id, null);

        }
        ImageView star = convertView.findViewById(R.id.star);
        TextView result = convertView.findViewById(R.id.result);
        TextView gaimao = convertView.findViewById(R.id.gaimao);
        TextView defen = convertView.findViewById(R.id.defen);
        TextView lanban = convertView.findViewById(R.id.lanban);
        TextView qiangduan = convertView.findViewById(R.id.qiangduan);
        TextView zhugong = convertView.findViewById(R.id.zhugong);
        TextView shiwu=convertView.findViewById(R.id.shiwu);
        // 给数据项填充数据
        final Map<String, Object> mItemData = dataSource.get(position);
        star.setImageResource((int)mItemData.get("star"));
        result.setText(mItemData.get("result").toString());
        gaimao.setText(mItemData.get("gaimao").toString());
        defen.setText(mItemData.get("defen").toString());
        lanban.setText(mItemData.get("lanban").toString());
        qiangduan.setText(mItemData.get("qiangduan").toString());
        zhugong.setText(mItemData.get("zhugong").toString());
        shiwu.setText(mItemData.get("shiwu").toString());



        // 给列表项中的控件注册事件监听器

        return convertView; // 返回列表项
    }
}