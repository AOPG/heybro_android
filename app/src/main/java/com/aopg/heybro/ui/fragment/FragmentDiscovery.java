package com.aopg.heybro.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.Common.MyViewPageLB;


/**
 * Created by 王伟健 on 2018-03-16.
 * 发现界面
 */

public class FragmentDiscovery extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e("", "onCreateView");
        if (rootView == null) {
            Log.e("", "FragmentDiscovery");
            rootView = inflater.inflate(R.layout.fragment_discovery,container,false);
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();


        LinearLayout llPoints = (LinearLayout) rootView.findViewById(R.id.ll_points);
        ViewPager mVp = (ViewPager) rootView.findViewById(R.id.vp);
        MyViewPageLB myViewPageLB = new MyViewPageLB(rootView.getContext(),mVp,llPoints);
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }
}
