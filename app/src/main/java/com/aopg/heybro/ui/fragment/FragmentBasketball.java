package com.aopg.heybro.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.activity.SearchRoomActivity;
import com.aopg.heybro.ui.adapter.BasketBallFragmentPagerAdapter;
import com.aopg.heybro.ui.room.CustomViewPager;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by 王伟健 on 2018-03-16.
 * 篮球页面
 */

public class FragmentBasketball extends Fragment{

    private View rootView;
    private Button btn_ball;
    private Button btn_game;
    private CustomViewPager myViewPager;
    private List<Fragment> list;
    private BasketBallFragmentPagerAdapter adapter;
    private View ball_selected;
    private View game_selected;
    private Button btn_searchRoom;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        Log.e("", "onCreateView");
        if (rootView == null) {
            Log.e("", "FragmentBasketball");
            rootView = inflater.inflate(R.layout.fragment_basketball,container,false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView();
        // 设置菜单栏的点击事件
        /**
         * 选择打球
         */
        btn_ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewPager.setCurrentItem(0);
                ball_selected.setVisibility(View.VISIBLE);
                game_selected.setVisibility(View.GONE);
            }
        });
        /**
         * 选择比赛
         */
        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myViewPager.setCurrentItem(1);
                game_selected.setVisibility(View.VISIBLE);
                ball_selected.setVisibility(View.GONE);
            }
        });
        /**
         * 搜索房间
         */
        btn_searchRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchRoomIntent = new Intent(getActivity(), SearchRoomActivity.class);
                startActivity(searchRoomIntent);
            }
        });
        myViewPager.setOnPageChangeListener(new MyPagerChangeListener());
        myViewPager.setScanScroll(false);
        //把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(new FragmentBall());
        list.add(new FragmentGame());
        adapter = new BasketBallFragmentPagerAdapter(getChildFragmentManager(), list);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面
        game_selected.setVisibility(View.GONE);
        return rootView;
    }
    /**
     * 初始化控件
     * */
    private void initView(){
        ball_selected = rootView.findViewById(R.id.ball_selected);
        game_selected = rootView.findViewById(R.id.game_selected);
        btn_ball = rootView.findViewById(R.id.date_ball);
        btn_game = rootView.findViewById(R.id.date_game);
        myViewPager = rootView.findViewById(R.id.basketball_viewPager);
        btn_searchRoom = rootView.findViewById(R.id.basket_search);
    }
    /**
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     *
     */
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    ball_selected = rootView.findViewById(R.id.ball_selected);
                    ball_selected.setVisibility(View.VISIBLE);
                    game_selected.setVisibility(View.GONE);
                    break;
                case 1:
                    game_selected = rootView.findViewById(R.id.game_selected);
                    game_selected.setVisibility(View.VISIBLE);
                    ball_selected.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
