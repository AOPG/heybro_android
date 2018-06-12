package com.aopg.heybro.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.aopg.heybro.R;

/**
 * Created by 壑过忘川 on 2018/6/7.
 * 比赛界面
 */

public class FragmentGame extends Fragment {
    private View rootView;
    private View createRoomView;
    private View joinRoomView;
    private PopupWindow window;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.e("", "onCreateView");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_basketball_game, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        /**
         * 创建房间
         */
        Button btn_create = rootView.findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRoomView = LayoutInflater.from(getContext()).inflate(R.layout.create_room,null,false);
                if(null == window || !window.isShowing()) {
                    window = new PopupWindow(createRoomView, 850, 1000, true);
                    // 设置PopupWindow是否能响应外部点击事件
                    window.setOutsideTouchable(false);
                    // 设置PopupWindow是否能响应点击事件
                    window.setTouchable(true);
                    window.showAtLocation(view, Gravity.LEFT, 20,-200);
                }
                /**
                 * 关闭创建房间
                 */
                Button create_close = createRoomView.findViewById(R.id.create_close);
                create_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(null != window && window.isShowing()){
                            window.dismiss();
                        }
                    }
                });
            }
        });
        /**
         * 快速匹配
         */
        final Button btn_join = rootView.findViewById(R.id.btn_join);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinRoomView = LayoutInflater.from(getContext()).inflate(R.layout.join_room,null,false);
                if(null == window || !window.isShowing()) {
                    window = new PopupWindow(joinRoomView, 850, 1000, true);
                    // 设置PopupWindow是否能响应外部点击事件
                    window.setOutsideTouchable(false);
                    // 设置PopupWindow是否能响应点击事件
                    window.setTouchable(true);
                    window.showAtLocation(view, Gravity.LEFT, 20,-200);
                }
                /**
                 * 关闭快速匹配
                 */
                Button join_close = joinRoomView.findViewById(R.id.join_close);
                join_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(null != window && window.isShowing()){
                            window.dismiss();
                        }
                    }
                });
            }
        });
        return rootView;
    }
}
