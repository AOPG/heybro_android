package com.aopg.heybro.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.activity.AddFriendActivity;
import com.aopg.heybro.ui.activity.ChartRoomActivity;
import com.aopg.heybro.ui.activity.MyConcernActivity;
import com.aopg.heybro.ui.activity.MyMessageActivity;
import com.aopg.heybro.utils.LoginInfo;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;


/**
 * Created by 王伟健 on 2018-03-16.
 * 好友界面
 */

public class FragmentFriend extends Fragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e("", "onCreateView");
        if (rootView == null) {
            Log.e("", "FragmentFriend");
            rootView = inflater.inflate(R.layout.fragment_friend,container,false);
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        /**
         * 跳转添加好友界面
         */
        ImageView addFriend = rootView.findViewById(R.id.add);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(addIntent);
            }
        });
        /**
         * 跳转消息界面
         */
        ImageView myMessage = rootView.findViewById(R.id.xinxi);
        myMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messageIntent = new Intent(getActivity(), MyMessageActivity.class);
                startActivity(messageIntent);
            }
        });
        /**
         * 跳转关注列表界面
         */
        LinearLayout concern = rootView.findViewById(R.id.guanzhuliebiao);
        concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent concernIntent = new Intent(getActivity(), MyConcernActivity.class);
                startActivity(concernIntent);
            }
        });

        /**
         * 跳转我的房间界面
         * */
        LinearLayout wodefangjian = rootView.findViewById(R.id.wodefangjian);
        wodefangjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wodefangjianIntent = new Intent(getActivity(), ChartRoomActivity.class);
                if (LoginInfo.user.getRoomId()==0){
                    Toast.makeText(getApplicationContext(),"您当前没有任何房间!",Toast.LENGTH_SHORT);
                }else {
                    wodefangjianIntent.putExtra("roomId",LoginInfo.user.getRoomId());
                    wodefangjianIntent.putExtra("roomName","AOPG约起来");
                    startActivity(wodefangjianIntent);
                }

            }
        });
        return rootView;
    }
}
