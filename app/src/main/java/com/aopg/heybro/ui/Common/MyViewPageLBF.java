package com.aopg.heybro.ui.Common;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;


/**
 * Created by 王攀 on 2018-06-05.
 * 轮播图的实现
 */

public class MyViewPageLBF extends PagerAdapter {

    private List<ImageView> mList;

    public MyViewPageLBF(List<ImageView> mList){

        this.mList=mList;

    }
//当要显示的图片进行缓存时，会调用这个方法进行显示图片的初始化
//我们将要显示的ImageView加入到ViewGroup中

    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(mList.get(position));

        return mList.get(position);

    }

    @Override
//PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView(mList.get(position));
    }
    //获取要滑动的控件的数量，
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    //来判断显示的是否是同一张照片，这个我们将两个图片对比 再返回
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0==arg1;
    }

}
