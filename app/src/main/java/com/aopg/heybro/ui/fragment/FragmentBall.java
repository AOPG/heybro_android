package com.aopg.heybro.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aopg.heybro.R;

/**
 * Created by 壑过忘川 on 2018/6/7.
 * 打球界面
 */

public class FragmentBall extends Fragment {
    private View rootView;
    private boolean isVisible = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e("", "onCreateView");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_basketball_ball, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
         // relativeLayout = rootView.findViewById(R.id.create);
//        relativeLayout.setVisibility(View.GONE);
//        ballView = rootView.findViewById(R.id.ball_selected);
//        gameView = rootView.findViewById(R.id.game_selected);
//        gameView.setVisibility(View.GONE);
        /**
         * 创建房间
         */
        Button btn_create = rootView.findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    //isVisible = false;//设置其他模块不能打开
                    Log.e("msg","123");
                    //relativeLayout.setVisibility(View.VISIBLE);//这一句显示布局
                }
            }
        });
//        /**
//         * 关闭创建房间
//         */
//        Button create_close = rootView.findViewById(R.id.create_close);
//        create_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                relativeLayout.setVisibility(View.GONE);
//                isVisible = true;//设置其他模块可以打开
//            }
//        });
//        /**
//         * 约球
//         */
//        Button btn_ball = rootView.findViewById(R.id.date_ball);
//        btn_ball.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isVisible) {
//                    ballView.setVisibility(View.VISIBLE);//这一句显示布局
//                    gameView.setVisibility(View.GONE);
//               }
//            }
//        });
//        /**
//         * 约赛
//         */
//        Button btn_game = rootView.findViewById(R.id.date_game);
//        btn_game.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isVisible) {
//                    gameView.setVisibility(View.VISIBLE);//这一句显示布局
//                    ballView.setVisibility(View.GONE);
//                }
//            }
//        });
        return rootView;
    }
}
