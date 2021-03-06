package com.aopg.heybro.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.Common.MyViewPageLB;
import com.aopg.heybro.ui.activity.ActivityexciActivity;
import com.aopg.heybro.ui.activity.ActivityzhiboActivity;
import com.aopg.heybro.ui.adapter.CustomerAdapter;
import com.aopg.heybro.ui.discover.HorizontalListView;
import com.aopg.heybro.ui.discover.HorizontalListViewAdapter;
import com.aopg.heybro.ui.discover.Virtualdiscoverdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.aopg.heybro.utils.ActivityUtils.addStatusViewWithColor;


/**
 * Created by 王伟健 on 2018-03-16.
 * 活动界面
 */

public class FragmentActivity extends Fragment {


    //下拉列表
    private List<Map<String,Object>> list3;
    HorizontalListView hListView;
    HorizontalListViewAdapter hListViewAdapter;
    ImageView previewImg;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.e("", "onCreateView");
        if (rootView == null) {
            Log.e("", "FragmentActivity");
            rootView = inflater.inflate(R.layout.fragment_activity,container,false);
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();

//        if (Virtualdiscoverdata.flag1 != 1) {
//            Virtualdiscoverdata.flag1 = 1;
//            LinearLayout llPoints = (LinearLayout) rootView.findViewById(R.id.ll_points);
//            ViewPager mVp = (ViewPager) rootView.findViewById(R.id.vp);
//            MyViewPageLB myViewPageLB = new MyViewPageLB(rootView.getContext(), mVp, llPoints);
//        }

        //下拉列表
        list3 = new Virtualdiscoverdata().dataResource2();
        final CustomerAdapter adapter = new CustomerAdapter(rootView.getContext(), list3,
                R.layout.fragment_activity_item);
        ListView lv = rootView.findViewById(R.id.lvActivity);
        lv.setAdapter(adapter);




        //图片点击事件
        ImageView exci = rootView.findViewById(R.id.exci);
        exci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), ActivityexciActivity.class);
                startActivity(intent);
            }
        });

        //图片点击事件
        ImageView zhibo = rootView.findViewById(R.id.zhibo);
        zhibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), ActivityzhiboActivity.class);
                startActivity(intent);
            }
        });

        //图片点击事件
        ImageView fujin = rootView.findViewById(R.id.fujin);
        fujin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), ActivityexciActivity.class);
                startActivity(intent);
            }
        });

        if (parent != null) {
            parent.removeView(rootView);
        }
        addStatusViewWithColor(rootView, Color.parseColor("#1A1B1F"));
        return rootView;
    }



}
