package com.aopg.heybro.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.aopg.heybro.R;


/**
 * Created by 王伟健 on 2018-03-16.
 * 篮球页面
 */

public class FragmentBasketball extends Fragment {

    private View rootView;
    private boolean isVisible = true;
    private RelativeLayout relativeLayout;
    private View ballView;
    private View gameView;

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
        ballView = rootView.findViewById(R.id.ball_selected);
        gameView = rootView.findViewById(R.id.game_selected);
        gameView.setVisibility(View.GONE);
        /**
         * 创建房间
         */
        Button btn_create = rootView.findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    isVisible = false;//设置其他模块不能打开
                    relativeLayout.setVisibility(View.VISIBLE);//这一句显示布局
                }
            }
        });
        /**
         * 关闭创建房间
         */
        Button create_close = rootView.findViewById(R.id.create_close);
        create_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.GONE);
                isVisible = true;//设置其他模块可以打开
            }
        });
        /**
         * 约球
         */
        Button btn_ball = rootView.findViewById(R.id.date_ball);
        btn_ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    ballView.setVisibility(View.VISIBLE);//这一句显示布局
                    gameView.setVisibility(View.GONE);
               }
            }
        });
        /**
         * 约赛
         */
        Button btn_game = rootView.findViewById(R.id.date_game);
        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    gameView.setVisibility(View.VISIBLE);//这一句显示布局
                    ballView.setVisibility(View.GONE);
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
