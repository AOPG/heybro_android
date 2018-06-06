package com.aopg.heybro.ui.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.discover.Virtualdiscoverdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MyViewPageLB  extends AppCompatActivity {
    private ViewPager mVp;

    private LinearLayout llPoints;

    private int[] images;// 存放所有要显示的图片资源id


    private List<ImageView> lists = new ArrayList<>();


    private List<ImageView> list=new ArrayList<ImageView>();//存放要显示在ViewPager对象中的所有Imageview对象
    private int prevPosition = 0;

    public MyViewPageLB(Context ap, ViewPager mVp, final LinearLayout llPoints){
        this.mVp = mVp;
        this.llPoints = llPoints;
        images = getImages();

            for (int i = 0; i < images.length; i++) {
                ImageView iv = new ImageView(ap);
                iv.setBackgroundResource(images[i]);
                list.add(iv);
                final View view = new View(ap);
                view.setBackgroundResource(R.drawable.small_circle);
                DisplayMetrics metrics = new DisplayMetrics();
                float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 30, metrics);
                float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 30, metrics);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) width, (int) height);
                params.leftMargin = 5;
                view.setLayoutParams(params);
                llPoints.addView(view);
//            根据图片的数量生成相对应的数量的小圆点
            }

            //设置第一页显示的标题
            //tvTitle.setText(titles[0]);
            //设置第一页的时候，小圆点显示的背景图
            llPoints.getChildAt(0).setBackgroundResource(R.drawable.small_circle);
            //下面封装viewpager的适配器
            MyViewPageLBF adapter = new MyViewPageLBF(list);
            mVp.setAdapter(adapter);

            //设置ViewPager对象页面变化时的监听
            mVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                //当下一个页面被选择的时候
                public void onPageSelected(int arg0) {
                    // TODO Auto-generated method stub
                    //tvTitle.setText(titles[arg0%list.size()]);

                    llPoints.getChildAt(prevPosition).setBackgroundResource(R.drawable.small_circle);

                    llPoints.getChildAt(arg0).setBackgroundResource(R.drawable.small_circle);

                    //把当前点位置做为下一次变化的前一个点的位置
                    prevPosition = arg0;
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // TODO Auto-generated method stub

                }
            });

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    while (true) {

                        SystemClock.sleep(3000);

                        handler.sendEmptyMessage(0);

                    }
                }
            }).start();
        

    }





    private Handler handler = new Handler() {

        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0:
                    // 得到mvp当前页面的索引
                    int currentItem = mVp.getCurrentItem();
                    // 要显示的下一个页面的索引
                    currentItem++;
                    // 设置ViewPager显示的页面
                    mVp.setCurrentItem(currentItem % list.size());

                    break;

                default:
                    break;
            }
        };
    };

//    private void initView() {
//        // TODO Auto-generated method stub
//        mVp = (ViewPager) findViewById(R.id.vp);
//        llPoints = (LinearLayout) findViewById(R.id.ll_points);
//    }


    private int[] getImages(){

        return new int[]{R.drawable.demoa,R.drawable.demob,R.drawable.democ};


    }


}
