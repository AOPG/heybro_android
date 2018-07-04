package com.aopg.heybro.ui.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.UserInfoService;
import com.aopg.heybro.codescan.ScanCodeActivity;
import com.aopg.heybro.ui.activity.MyInfoActivity;
import com.aopg.heybro.ui.activity.Mydata;
import com.aopg.heybro.ui.activity.Myrili;
import com.aopg.heybro.ui.activity.SaoyisaoActivity;
import com.aopg.heybro.ui.activity.SettingActivity;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;
import static com.aopg.heybro.utils.HttpUtils.BASE_URL;



/**
 * Created by 王伟健 on 2018-03-16.
 * 我的界面
 */

public class FragmentMy extends Fragment {
    public final static int SCANNIN_GREQUEST_CODE = 1;

    private static View rootView;
    private boolean isVisible = true;

    private View myRateView;
    private View myPositionView;
    private PopupWindow window;

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
        final RelativeLayout myRating = rootView.findViewById(R.id.rating_show);

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
                myPositionView = LayoutInflater.from(getContext()).inflate(R.layout.my_position,null,false);
                if(null == window || !window.isShowing()) {
                    window = new PopupWindow(myPositionView, 850, 1000, true);
                    // 设置PopupWindow是否能响应外部点击事件
                    window.setOutsideTouchable(false);
                    // 设置PopupWindow是否能响应点击事件
                    window.setTouchable(true);
                    window.showAtLocation(view, Gravity.LEFT, 120,0);
                }
                /**
                 * 关闭我的位置
                 */
                ImageView position_close = myPositionView.findViewById(R.id.position_close);
                position_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        window.dismiss();
                    }
                });
            }
        });
        /**
         * 我的等级的显示
         */
        Button my_rating = rootView.findViewById(R.id.rating);
        my_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRateView = LayoutInflater.from(getContext()).inflate(R.layout.my_rate,null,false);
                if(null == window || !window.isShowing()) {
                    window = new PopupWindow(myRateView, 850, 1000, true);
                    // 设置PopupWindow是否能响应外部点击事件
                    window.setOutsideTouchable(false);
                    // 设置PopupWindow是否能响应点击事件
                    window.setTouchable(true);
                    window.showAtLocation(view, Gravity.LEFT, 120,0);

                    int grade=LoginInfo.user.getUserGrade();
                    if (grade==1){
                        RadioButton rd=myRateView.findViewById(R.id.rd11);
                        TextView tv=myRateView.findViewById(R.id.rd12);
                        rd.setChecked(true);
                        tv.setTextColor(Color.WHITE);
                    }else if (grade==2){
                        RadioButton rd=myRateView.findViewById(R.id.rd21);
                        TextView tv=myRateView.findViewById(R.id.rd22);
                        rd.setChecked(true);
                        tv.setTextColor(Color.WHITE);
                    }else if (grade==3){
                        RadioButton rd=myRateView.findViewById(R.id.rd31);
                        TextView tv=myRateView.findViewById(R.id.rd32);
                        rd.setChecked(true);
                        tv.setTextColor(Color.WHITE);
                    }else if (grade==4){
                        RadioButton rd=myRateView.findViewById(R.id.rd41);
                        TextView tv=myRateView.findViewById(R.id.rd42);
                        rd.setChecked(true);
                        tv.setTextColor(Color.WHITE);
                    }else if (grade==5){
                        RadioButton rd=myRateView.findViewById(R.id.rd51);
                        TextView tv=myRateView.findViewById(R.id.rd52);
                        rd.setChecked(true);
                        tv.setTextColor(Color.WHITE);
                    }else if (grade==6){
                        RadioButton rd=myRateView.findViewById(R.id.rd61);
                        TextView tv=myRateView.findViewById(R.id.rd62);
                        rd.setChecked(true);
                        tv.setTextColor(Color.WHITE);
                    }else if (grade==7){
                        RadioButton rd=myRateView.findViewById(R.id.rd71);
                        TextView tv=myRateView.findViewById(R.id.rd72);
                        rd.setChecked(true);
                        tv.setTextColor(Color.WHITE);
                    }else if (grade==8){
                        RadioButton rd=myRateView.findViewById(R.id.rd81);
                        TextView tv=myRateView.findViewById(R.id.rd82);
                        rd.setChecked(true);
                        tv.setTextColor(Color.WHITE);
                    }else if (grade==9){
                        RadioButton rd=myRateView.findViewById(R.id.rd91);
                        TextView tv=myRateView.findViewById(R.id.rd92);
                        rd.setChecked(true);
                        tv.setTextColor(Color.WHITE);
                    }

                }
                /**
                 * 关闭我的等级
                 */
                ImageView rating_close = myRateView.findViewById(R.id.rating_close);
                rating_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        window.dismiss();
                    }
                });
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
//                Intent saoyisaoIntent = new Intent(getActivity(), SaoyisaoActivity.class);
//                startActivity(saoyisaoIntent);
                Intent intent = new Intent();
                intent.setClass(getActivity(), ScanCodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
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
         * 数据
         * */
        ImageView shuju=rootView.findViewById(R.id.data);
        shuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shujuIntent = new Intent(getActivity(), Mydata.class);
                startActivity(shujuIntent);
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

    public void loadInfo(){
        System.out.println("正在从内存中读取数据--------------------------------");
        TextView userIntroTv = rootView.findViewById(R.id.user_intro);
        userIntroTv.setText(LoginInfo.user.getUserIntro());
        TextView userNickNameTv = rootView.findViewById(R.id.myName);
        userNickNameTv.setText(LoginInfo.user.getNickName());
        TextView userIdTv = rootView.findViewById(R.id.user_code);
        userIdTv.setText(LoginInfo.user.getUserCode());
        Button ratingTv = rootView.findViewById(R.id.rating);
        ratingTv.setText("等级："+LoginInfo.user.getUserGrade()+"级");
        ImageView image = rootView.findViewById(R.id.image);

        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();

        Glide.with(this)
                .load(BASE_URL+LoginInfo.user.getUserPortrait())
                .apply(options)
                .into(image);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("收到更新用户信息的消息！--------------------------------");
                    loadInfo();
                    break;
                case 403:
                    System.out.println("收到更新房间信息的消息！--------------------------------");
            }
        }
    };

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
        mActivity.setHandler(handler);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == -1) {
                    Bundle bundle = data.getExtras();
                    //取出扫描到的内容
                    String str = bundle.getString("result");
                    Log.e("onActivityResult",str);
                    Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT);
                    Uri uri=Uri.parse(str);
                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                    //调用外部浏览器
//                    String regEx = "^http\\://.*$";
//                    Pattern pattern = Pattern.compile(regEx);
//                    Matcher matcher = pattern.matcher(str);
//                    boolean b = matcher.matches();
//                    if (b) {
//                        try {
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse(str));
//                            startActivity(intent);
//                        } catch (Exception e) {
//                            Log.v("liulan", e.getMessage());
//                        }
//                    }
                    //显示图片数据到img标签中
                    //mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }
}
