package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;

import java.util.List;

/**
 * Created by 壑过忘川 on 2018/6/21.
 * 显示所有房间的适配器
 */

public class SearchRoomAdapter extends BaseAdapter {
    private Context context;    // 上下文环境
    private int item_layout_id;// item视图布局文件
    private List<BasketRoomInfo> roomList; // 所有房间
    private BasketRoomInfo basketRoomInfo;

    private TextView roomName;
    private TextView roomRealNum;
    private TextView roomFullNum;
    public SearchRoomAdapter(Context context,
                       int item_layout_id,
                       List<BasketRoomInfo> roomList) {
        this.context = context;
        this.item_layout_id = item_layout_id;
        this.roomList = roomList;
    }
    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView =  mInflater.inflate(item_layout_id,null);
        }
        roomName = convertView.findViewById(R.id.roomName);
        roomRealNum = convertView.findViewById(R.id.roomRealNum);
        roomFullNum = convertView.findViewById(R.id.roomFullNum);
        basketRoomInfo = roomList.get(position);
        if(basketRoomInfo.getType() == 0) {
            roomName.setText(basketRoomInfo.getRoomName()+"(打球)");
        }else if(basketRoomInfo.getType() == 1){
            roomName.setText(basketRoomInfo.getRoomName()+"(比赛)");
        }else{
            roomName.setText(basketRoomInfo.getRoomName()+"(已解散)");
        }
        roomRealNum.setText(basketRoomInfo.getRoomRealNum()+"");
        roomFullNum.setText(basketRoomInfo.getNum()+"");
        return convertView;
    }
}
