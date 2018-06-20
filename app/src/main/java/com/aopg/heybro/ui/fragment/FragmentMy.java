package com.aopg.heybro.ui.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.UserInfoService;
import com.aopg.heybro.ui.activity.MyInfoActivity;
import com.aopg.heybro.ui.activity.Myrili;
import com.aopg.heybro.ui.activity.SaoyisaoActivity;
import com.aopg.heybro.ui.activity.SettingActivity;
import com.aopg.heybro.utils.LoginInfo;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;


/**
 * Created by 王伟健 on 2018-03-16.
 * 我的界面
 */

public class FragmentMy extends Fragment {

    private static View rootView;
    private boolean isVisible = true;
    private MainActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        LoginInfo.FragmentMYISCREATE=1;
        Log.e("", "onCreateView");
        if (rootView == null) {
            Log.e("", "FragmentMy");
            rootView = inflater.inflate(R.layout.fragment_my,container,false);

        }
        loadInfo();
        this.getActivity().startService(new Intent(this.getActivity(), UserInfoService.class));
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        final RelativeLayout myPosition = rootView.findViewById(R.id.position_show);
        myPosition.setVisibility(View.GONE);
        final RelativeLayout myRating = rootView.findViewById(R.id.rating_show);
        myRating.setVisibility(View.GONE);

        /**
         * 我的资料界面
         */
        ImageView myImage = rootView.findViewById(R.id.image);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myInfoIntent = new Intent(getActivity(), MyInfoActivity.class);
                startActivity(myInfoIntent);
            }
        });
        /**
         * 擅长位置的显示
         */
        Button my_position = rootView.findViewById(R.id.position);
        my_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    isVisible = false;//设置其他模块不能打开
                    myPosition.setVisibility(View.VISIBLE);//这一句显示布局
                }
            }
        });
        /**
         * 我的等级的显示
         */
        Button my_rating = rootView.findViewById(R.id.rating);
        my_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisible) {
                    isVisible = false;//设置其他模块不能打开
                    myRating.setVisibility(View.VISIBLE);//这一句显示布局
                }
            }
        });
        /**
         * 关闭我的位置
         */
        ImageView position_close = rootView.findViewById(R.id.position_close);
        position_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPosition.setVisibility(View.GONE);
                isVisible = true;//设置其他模块可以打开
            }
        });
        /**
         * 关闭我的等级
         */
        ImageView rating_close = rootView.findViewById(R.id.rating_close);
        rating_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRating.setVisibility(View.GONE);
                isVisible = true;//设置其他模块可以打开
            }
        });
        /**
         * 跳转设置界面
         */
        ImageView setting = rootView.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingIntent = new Intent(getActivity(), SettingActivity.class);
                startActivity(settingIntent);
            }
        });
        /**
         * 扫一扫
         */
        ImageView saoyisao = rootView.findViewById(R.id.saoyisao);
        saoyisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent saoyisaoIntent = new Intent(getActivity(), SaoyisaoActivity.class);
                startActivity(saoyisaoIntent);
            }
        });
        /**
         * 竞猜
         * */
        ImageView jingcai=rootView.findViewById(R.id.jingcai);
        jingcai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toastTip
                        = Toast.makeText(getApplicationContext(),
                        "该模块待开发",
                        Toast.LENGTH_LONG);
                toastTip.setGravity(Gravity.CENTER, 0, 0);
                toastTip.show();

            }
        });
        /**
         * 装备
         * */
        ImageView zhuangbei=rootView.findViewById(R.id.zhuangbei);
        zhuangbei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toastTip
                        = Toast.makeText(getApplicationContext(),
                        "该模块待开发",
                        Toast.LENGTH_LONG);
                toastTip.setGravity(Gravity.CENTER, 0, 0);
                toastTip.show();

            }
        });
        /**
         * 打球日历
         * */
        ImageView rili=rootView.findViewById(R.id.rili);
        rili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rili = new Intent(getActivity(), Myrili.class);
                startActivity(rili);
            }
        });
        /**
         * 商城
         * */
        ImageView shopping=rootView.findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toastTip
                        = Toast.makeText(getApplicationContext(),
                        "该模块待开发",
                        Toast.LENGTH_LONG);
                toastTip.setGravity(Gravity.CENTER, 0, 0);
                toastTip.show();

            }
        });
        /**
         * 订单
         * */
        ImageView dingdan=rootView.findViewById(R.id.dingdan);
        dingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toastTip
                        = Toast.makeText(getApplicationContext(),
                        "该模块待开发",
                        Toast.LENGTH_LONG);
                toastTip.setGravity(Gravity.CENTER, 0, 0);
                toastTip.show();

            }
        });
        /**
         * 钱包
         * */
        ImageView qianbao=rootView.findViewById(R.id.qianbao);
        qianbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toastTip
                        = Toast.makeText(getApplicationContext(),
                        "该模块待开发",
                        Toast.LENGTH_LONG);
                toastTip.setGravity(Gravity.CENTER, 0, 0);
                toastTip.show();

            }
        });
        /**
         * 客服
         * */
        ImageView kefu=rootView.findViewById(R.id.kefu);
        kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toastTip
                        = Toast.makeText(getApplicationContext(),
                        "该模块待开发",
                        Toast.LENGTH_LONG);
                toastTip.setGravity(Gravity.CENTER, 0, 0);
                toastTip.show();

            }
        });
        return rootView;
    }

    public static void loadInfo(){
        System.out.println("正在从内存中读取数据--------------------------------");
        TextView userIntroTv = rootView.findViewById(R.id.user_intro);
        userIntroTv.setText(LoginInfo.user.getUserIntro());
        TextView userNickNameTv = rootView.findViewById(R.id.myName);
        userNickNameTv.setText(LoginInfo.user.getNickName());
        TextView userIdTv = rootView.findViewById(R.id.user_code);
        userIdTv.setText(LoginInfo.user.getUserCode());
        Button ratingTv = rootView.findViewById(R.id.rating);
        ratingTv.setText("等级："+LoginInfo.user.getUserGrade()+"级");
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("收到mainactivity的消息！--------------------------------");
                    loadInfo();
                    break;
            }
        }
    };

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
        mActivity.setHandler(handler);
    }
}
