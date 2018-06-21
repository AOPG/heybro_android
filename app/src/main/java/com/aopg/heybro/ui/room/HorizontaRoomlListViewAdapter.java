package com.aopg.heybro.ui.room;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.discover.BitmapUtil;

/**
 *  Created by 王攀 on 2018-06-13.
 *  房间模块左右滑动
 */


public class HorizontaRoomlListViewAdapter extends BaseAdapter {
//    private int[] mIconIDs;
    private String[] roomCode;
    private String[] roomTitle;
    private String[] roomNum;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;

    public HorizontaRoomlListViewAdapter(Context context,String[] roomCode, String[] roomTitle, String[] roomNum) {
        this.mContext = context;
        this.roomTitle = roomTitle;
        this.roomCode = roomCode;
        this.roomNum = roomNum;
//        this.mIconIDs = idss;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return roomCode.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
//        LinearLayout disLayout;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.basketball_list_item, null);
            holder.room_code = (TextView) convertView.findViewById(R.id.ball_room_code);
            holder.room_name = (TextView) convertView.findViewById(R.id.ball_room_name);
            holder.room_num = (TextView) convertView.findViewById(R.id.ball_room_num);
//            holder.disLayout = (LinearLayout)convertView.findViewById(R.id.room_Layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == selectIndex) {
            convertView.setSelected(true);
        } else {
            convertView.setSelected(false);
        }

        holder.room_code.setText(roomCode[position]);
        holder.room_name.setText(roomTitle[position]);
        holder.room_num.setText(roomNum[position]);
//        holder.disLayout.setBackgroundResource(mIconIDs[position]);


        return convertView;
    }

    private static class ViewHolder {
        private TextView room_code;
        private TextView room_name;
        private TextView room_num;
//        private LinearLayout disLayout;
    }

    private Bitmap getPropThumnail(int id) {
        Drawable d = mContext.getResources().getDrawable(id);
        Bitmap b = BitmapUtil.drawable2Bitmap(d);
//      Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
//        int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
//        int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);

        Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, 36, 36);

        return thumBitmap;
    }

    public void setSelectIndex(int i) {
        selectIndex = i;
    }
}