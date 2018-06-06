package com.aopg.heybro.ui.discover;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aopg.heybro.R;

import static cn.jpush.android.api.JPushInterface.a.h;
import static cn.jpush.android.api.JPushInterface.a.w;
import static com.baidu.location.g.j.p;

public class HorizontalListViewAdapter extends BaseAdapter {
    private int[] mIconIDs;
    private String[] mTitles;
    private int[] bacIDs;
    private Context mContext;
    private LayoutInflater mInflater;
    Bitmap iconBitmap;
    private int selectIndex = -1;

    public HorizontalListViewAdapter(Context context, String[] titles, int[] ids,int[] idss) {
        this.mContext = context;
        this.mIconIDs = ids;
        this.mTitles = titles;
        this.bacIDs = idss;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mIconIDs.length;
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
        LinearLayout disLayout;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.discover_horizontal_list_item, null);
            holder.mImage = (ImageView) convertView.findViewById(R.id.dis_pic);
            holder.mTitle = (TextView) convertView.findViewById(R.id.dis_title);
            holder.disLayout = (LinearLayout)convertView.findViewById(R.id.dis_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == selectIndex) {
            convertView.setSelected(true);
        } else {
            convertView.setSelected(false);
        }

        holder.mTitle.setText(mTitles[position]);
        iconBitmap = getPropThumnail(mIconIDs[position]);
        holder.mImage.setImageBitmap(iconBitmap);
        holder.disLayout.setBackgroundResource(bacIDs[position]);

        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitle;
        private ImageView mImage;
        private LinearLayout disLayout;
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