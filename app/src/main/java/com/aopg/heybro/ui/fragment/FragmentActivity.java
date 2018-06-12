package com.aopg.heybro.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.aopg.heybro.R;
import com.aopg.heybro.ui.Common.MyViewPageLB;
import com.aopg.heybro.ui.discover.HorizontalListView;
import com.aopg.heybro.ui.discover.HorizontalListViewAdapter;
import com.aopg.heybro.ui.discover.Virtualdiscoverdata;

import java.util.List;
import java.util.Map;


/**
 * Created by 王伟健 on 2018-03-16.
 * 活动界面
 */

public class FragmentActivity extends Fragment {

    private List<Map<String,Object>> list;
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

        if (Virtualdiscoverdata.flag1 != 1) {
            Virtualdiscoverdata.flag1 = 1;
            LinearLayout llPoints = (LinearLayout) rootView.findViewById(R.id.ll_points);
            ViewPager mVp = (ViewPager) rootView.findViewById(R.id.vp);
            MyViewPageLB myViewPageLB = new MyViewPageLB(rootView.getContext(), mVp, llPoints);
        }


        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }



}
