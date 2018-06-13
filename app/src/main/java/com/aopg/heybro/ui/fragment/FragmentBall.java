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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import com.aopg.heybro.R;
import com.aopg.heybro.ui.room.HorizontaRoomlListViewAdapter;
import com.aopg.heybro.ui.room.HorizontalRoomListView;


/**
 * Created by 壑过忘川 on 2018/6/7.
 * 打球界面
 */

public class FragmentBall extends Fragment {
    private View rootView;
    private View createRoomView;
    private View joinRoomView;
    private PopupWindow window;
    HorizontalRoomListView hListView;
    HorizontaRoomlListViewAdapter hListViewAdapter;


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

        //房间部分
        initUI();

        /**
         * 创建房间
         */
        final Button btn_create = rootView.findViewById(R.id.btn_create);
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


    //房间部分
    public void initUI(){
        hListView = (HorizontalRoomListView)rootView.findViewById(R.id.ball_horizon_listview);
        String[] roomCode = {"20180613001", "20180613002", "20180613003", "20180613004",
                "20180613005","20180613006"};
        String[] roomTitle = {"快来打球吧！", "快来打球吧！", "快来打球吧！",
                "快来打球吧！","快来打球吧！","快来打球吧！"};
        String[] roomNum = {"1/10", "2/10", "3/10", "4/10","5/10","6/10"};
        final int[] idss = {R.drawable.room_corner, R.drawable.room_corner,
                R.drawable.room_corner, R.drawable.room_corner,R.drawable.room_corner
                ,R.drawable.room_corner};

        hListViewAdapter = new HorizontaRoomlListViewAdapter(rootView.getContext(),idss,
                roomCode,roomTitle,roomNum);
        hListView.setAdapter(hListViewAdapter);
        //      hListView.setOnItemSelectedListener(new OnItemSelectedListener() {
        //
        //          @Override
        //          public void onItemSelected(AdapterView<?> parent, View view,
        //                  int position, long id) {
        //              // TODO Auto-generated method stub
        //              if(olderSelected != null){
        //                  olderSelected.setSelected(false); //上一个选中的View恢复原背景
        //              }
        //              olderSelected = view;
        //              view.setSelected(true);
        //          }
        //
        //          @Override
        //          public void onNothingSelected(AdapterView<?> parent) {
        //              // TODO Auto-generated method stub
        //
        //          }
        //      });
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
//              if(olderSelectView == null){
//                  olderSelectView = view;
//              }else{
//                  olderSelectView.setSelected(false);
//                  olderSelectView = null;
//              }
//              olderSelectView = view;
//              view.setSelected(true);
//                previewImg.setImageResource(ids[position]);
                hListViewAdapter.setSelectIndex(position);
                hListViewAdapter.notifyDataSetChanged();

            }
        });

    }
}
