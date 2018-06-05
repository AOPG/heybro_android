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

import com.aopg.heybro.R;
import com.aopg.heybro.ui.activity.AddFriendActivity;


/**
 * Created by 王伟健 on 2018-03-16.
 * 我的好友
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
        return rootView;
    }
}
