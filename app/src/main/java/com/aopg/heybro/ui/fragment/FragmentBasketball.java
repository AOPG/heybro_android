package com.aopg.heybro.ui.fragment;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;


/**
 * Created by 王伟健 on 2018-03-16.
 * 篮球页面
 */

public class FragmentBasketball extends Fragment {

    private View rootView;
    private boolean isVisible = true;
    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        Log.e("", "onCreateView");
        if (rootView == null) {
            Log.e("", "FragmentBasketball");
            rootView = inflater.inflate(R.layout.basketball_establish,container,false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        relativeLayout = rootView.findViewById(R.id.create);
        relativeLayout.setVisibility(View.GONE);
        /**
         * 创建房间
         */
        Button btn_create = rootView.findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    isVisible = false;
                    relativeLayout.setVisibility(View.VISIBLE);//这一句显示布局
                } else {
                    relativeLayout.setVisibility(View.GONE);//这一句即隐藏布局
                    isVisible = true;
                }
            }
        });
        return rootView;
    }
    /**
     * 房间显示
     */
//    private void changeGridView() {
//        // item宽度
//        int itemWidth = DensityUtil.dip2px(this, 100);
//        // item之间的间隔
//        int itemPaddingH = DensityUtil.dip2px(this, 1);
//        int size = datas.size();
//        // 计算GridView宽度
//        int gridviewWidth = size * (itemWidth + itemPaddingH);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
//        mContentGv.setLayoutParams(params);
//        mContentGv.setColumnWidth(itemWidth);
//        mContentGv.setHorizontalSpacing(itemPaddingH);
//        mContentGv.setStretchMode(GridView.NO_STRETCH);
//        mContentGv.setNumColumns(size);
//    }
}
