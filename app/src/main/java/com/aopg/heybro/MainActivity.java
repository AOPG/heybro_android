package com.aopg.heybro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.aopg.heybro.entity.User;
import com.aopg.heybro.service.TimerTaskService;
import com.aopg.heybro.ui.Common.ActivitiesManager;
import com.aopg.heybro.ui.activity.ActivityexciActivity;
import com.aopg.heybro.ui.activity.LoginActivty;
import com.aopg.heybro.ui.activity.Mysex;
import com.aopg.heybro.ui.fragment.FragmentActivity;
import com.aopg.heybro.ui.fragment.FragmentBasketball;
import com.aopg.heybro.ui.fragment.FragmentDiscovery;
import com.aopg.heybro.ui.fragment.FragmentFriend;
import com.aopg.heybro.ui.fragment.FragmentMy;
import com.aopg.heybro.utils.ActivityUtils;
import com.aopg.heybro.utils.BaiduMapLocationUtil;
import com.aopg.heybro.utils.LoginInfo;
import com.baidu.mapapi.SDKInitializer;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.GroupApprovalEvent;
import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

import static com.aopg.heybro.utils.ActivityUtils.getStatusBarHeight;
import static com.aopg.heybro.utils.ThreadUtils.findAllThreads;

public class MainActivity extends AppCompatActivity {

    private ContentType contentType;
    private cn.jpush.im.android.api.model.Message message;
    private Handler mHandler;
    private Handler mFridendHandler;
    private FragmentTabHost myTabHost;
    private Map<String,Map<String,Object>> map;
    private static String LAST_SELECT = "basketball";
    MyBroadcastReceiver mbcr;
    private BaiduMapLocationUtil baiduMapLocationUtil;
    public static Integer CMD_STOP_USER_INFO_SERVICE = 0;
    private Thread userInfoThread;
    private ImageView iv;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fullScreen(this);
//        ImageView exci=findViewById(R.id.exci);
//        exci.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), ActivityexciActivity.class);
//                startActivity(intent);
//
//            }
//        });
        LoginInfo.user =  DataSupport.where("isLogin = ?", "1").findFirst(User.class);
        startTimerTaskService();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        //注册jMessage
        JMessageClient.registerEventReceiver(this);

        setContentView(R.layout.activity_main);
        map = new HashMap<>();
        initTabHost();
        ActivitiesManager.getInstance().addActivity(this);

        mbcr = new MyBroadcastReceiver();
        //动态注册一个广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("FragmentMy");
        registerReceiver(mbcr, filter);// 注册
        userInfoThread = new UserInfoThread();
        userInfoThread.start();

        //底部tabhost改变图标
        myTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //将上一次点击的图标改回
                Map attr = map.get(LAST_SELECT);
                View view = (View) attr.get("view");
                iv = view.findViewById(R.id.imageId);
                TextView tv = view.findViewById(R.id.textId);
                iv.setImageResource((int)attr.get("img"));
                tv.setTextColor(0xFFFFFFFF);
                LAST_SELECT = tabId;
                //改此次点击的图标
                if(tabId.equals("discovery")){
                    view = (((View)(map.get(tabId).get("view"))).findViewById(R.id.imageId));
                    iv = view.findViewById(R.id.imageId);
                    view = (((View)(map.get(tabId).get("view"))).findViewById(R.id.textId));
                    tv = view.findViewById(R.id.textId);
                    iv.setImageResource(R.drawable.discovery_selected);
                    tv.setTextColor(0xFFFFB90F);
                }
                if(tabId.equals("activity")){
                    view = (((View)(map.get(tabId).get("view"))).findViewById(R.id.imageId));
                    iv = view.findViewById(R.id.imageId);
                    view = (((View)(map.get(tabId).get("view"))).findViewById(R.id.textId));
                    tv = view.findViewById(R.id.textId);
                    iv.setImageResource(R.drawable.activity_selected);
                    tv.setTextColor(0xFFFFB90F);
                }
                if(tabId.equals("basketball")){
                    view = (((View)(map.get(tabId).get("view"))).findViewById(R.id.imageId));
                    iv = view.findViewById(R.id.imageId);
                    view = (((View)(map.get(tabId).get("view"))).findViewById(R.id.textId));
                    tv = view.findViewById(R.id.textId);
//                    ViewGroup.LayoutParams params = iv.getLayoutParams();
//                    params.height=150;
//                    params.width =150;
//                    iv.setLayoutParams(params);

                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//            ViewGroup.LayoutParams params = iv.getLayoutParams();
                    param.height=140;
                    param.width =140;
                    iv.setLayoutParams(param);
                    iv.setPadding(0,0,0,30);

                    iv.setImageResource(R.drawable.basketball_selected);
                    tv.setTextColor(0xFFFFB90F);
                }
                if(tabId.equals("friend")){
                    view = (((View)(map.get(tabId).get("view"))).findViewById(R.id.imageId));
                    iv = view.findViewById(R.id.imageId);
                    view = (((View)(map.get(tabId).get("view"))).findViewById(R.id.textId));
                    tv = view.findViewById(R.id.textId);
                    iv.setImageResource(R.drawable.friend_selected);
                    tv.setTextColor(0xFFFFB90F);
                }
                if(tabId.equals("my")){
                    view = (((View)(map.get(tabId).get("view"))).findViewById(R.id.imageId));
                    iv = view.findViewById(R.id.imageId);
                    view = (((View)(map.get(tabId).get("view"))).findViewById(R.id.textId));
                    tv = view.findViewById(R.id.textId);
                    iv.setImageResource(R.drawable.my_selected);
                    tv.setTextColor(0xFFFFB90F);
                }
            }
        });
    }


    public class UserInfoThread extends Thread{
        // 用于停止线程
        private boolean stopMe = true;

        public void stopMe() {
            Log.e("userInfoThread","我被终止了！");
            stopMe = false;
        }

        @Override
        public void run() {
            while (stopMe) {
                this.setName("userInfoThread");
                Log.e("mainactivity",
                        "------------------------启动service--------------------------");
                MainActivity.this.startService(new Intent(MainActivity.this, UserInfoService.class));
                try {
                    sleep(300000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化tabhost
     * */
    private void initTabHost(){
        myTabHost = this.findViewById(android.R.id.tabhost);
        myTabHost.setup(this, getSupportFragmentManager(),
                android.R.id.tabcontent);
        addTabSpec("discovery","发现",R.drawable.discovery, FragmentDiscovery.class);
        addTabSpec("activity","活动",R.drawable.activity, FragmentActivity.class);
        addTabSpec("basketball",null,R.drawable.basketball, FragmentBasketball.class);
        addTabSpec("friend","好友",R.drawable.friend, FragmentFriend.class);
        addTabSpec("my","我的",R.drawable.my, FragmentMy.class);
        ImageView iv =  (myTabHost.getTabWidget().getChildTabViewAt(2).findViewById(R.id.imageId));
        TextView tv = (myTabHost.getTabWidget().getChildTabViewAt(2).findViewById(R.id.textId));
        iv.setImageResource(R.drawable.basketball_selected);
        tv.setTextColor(0xFFFFB90F);

        myTabHost.setCurrentTab(2);
    }

    /**
     * @param drawable 图片资源id
     * @param fragment
     * @param id tabid
     * */
    private void addTabSpec(String id,String title,int drawable,Class<?> fragment){
        Map<String,Object> attr = new HashMap<>();
        View viewTab = getTabView(drawable,title);
        TabHost.TabSpec tabSpec = myTabHost.newTabSpec(id)
                .setIndicator(viewTab);
        myTabHost.addTab(tabSpec,fragment,null);
        attr.put("view",viewTab);
        attr.put("img",drawable);
        map.put(id,attr);
    }

    /**
     * 创建tabview
     * */
    private View getTabView(int drawable,String title) {
        View view = getLayoutInflater().inflate(R.layout.fragment_tab, null);
        ImageView imageView = view.findViewById(R.id.imageId);
        imageView.setImageResource(drawable);
        if(title == null){
            iv = view.findViewById(R.id.imageId);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            param.height=140;
            param.width =140;
            iv.setPadding(0,0,0,30);
            iv.setLayoutParams(param);
        }
        TextView textView = view.findViewById(R.id.textId);
        textView.setText(title);
        return view;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    ActivitiesManager.getInstance().exit();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    //动态创建广播接收者
    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //对接收到的广播进行处理，intent里面包含数据
            System.out.println("收到广播！----------------------------");
            Message msg = mHandler.obtainMessage();
            msg.what =0;
            mHandler.sendMessage(msg);

        }
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public void setFragmentFriendHandler(Handler handler) {
        mFridendHandler = handler;
    }


    public void onEvent(LoginStateChangeEvent event) {
        System.out.println("------------------监听到事件-------------------");
        Looper.prepare();
        forceLogoutDig();
        Looper.loop();
        System.out.println("------------------已跳转-------------------");
    }

    public void forceLogoutDig(){
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("强制下线提醒");
        alertDialog.setMessage("您的账号在其他地方登录!如果不是您本人操作，" +
                "请尽快更改密码，以免造成财产损失。");
        alertDialog.setButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                        Thread[] threads = findAllThreads();
                        for (int i = 0; i < threads.length; i++) {
                            if (threads[i].getName().equals("userInfoThread")){
                                ((UserInfoThread)threads[i]).stopMe();
                            }
                        }

                        LoginInfo.ISLOGINIM=0;
                        LoginInfo.user.setIsLogin(0);
                        LoginInfo.user.updateAll("userName = ?",LoginInfo.user.getUsername());

                        LoginInfo.user = new User();
                        //刷新用户信息为空,向MainActivity发送信息刷新请求
                        Intent intentRefresh = new Intent("FragmentMy");
                        sendBroadcast(intentRefresh);

                        Intent intent = new Intent(MainActivity.this, LoginActivty.class);
                        startActivity(intent);
                        break;
                }
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void startTimerTaskService(){
        Intent it=new Intent(this, TimerTaskService.class);
        startService(it);
    }

    public void onEvent(final GroupApprovalEvent event) {
        event.getFromUserInfo(new GetUserInfoCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, UserInfo info) {
                if (0 == responseCode) {
                    final String fromUsername = info.getUserName();
                    final String fromUserAppKey = info.getAppKey();
                    if (event.getType() == GroupApprovalEvent.Type.apply_join_group) {
                        event.acceptGroupApproval(fromUsername, fromUserAppKey, new BasicCallback() {
                            @Override
                            public void gotResult(int responseCode, String responseMessage) {
                                if (0 == responseCode) {
                                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i("GroupApprovalEvent", "acceptApplyJoinGroup failed," + " code = " + responseCode + ";msg = " + responseMessage);
                                    Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if (event.getType() == GroupApprovalEvent.Type.invited_into_group) {
                            event.getApprovalUserInfoList(new GetUserInfoListCallback() {
                            @Override
                            public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                                if (0 == responseCode) {
                                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "被邀请人userInfo未找到", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 刷新会话信息
     * */

    @Override
    protected void onDestroy() {
        unregisterReceiver(mbcr);
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        startService(new Intent(MainActivity.this, UserInfoService.class));
        super.onRestart();
    }

    @Override
    protected void onResume() {
        if (ActivityUtils.STATUSHEIGHT==0){
            ActivityUtils.STATUSHEIGHT = getStatusBarHeight(this);
        }

        Log.e("mianActivity","onResume被调用！");
        if (LoginInfo.FragmentFriendISCREATE==1){
            Log.e("mianActivity","onResume被调用！");
            Message msg = mFridendHandler.obtainMessage();
            msg.what =403;
            mFridendHandler.sendMessage(msg);
        }
        int id = getIntent().getIntExtra("userloginflag", 0);
        int id0 = getIntent().getIntExtra("flagggg", 0);
        if (id == 1 ) {
            myTabHost.setCurrentTab(4);
        }
        if (id == 2 ) {
            myTabHost.setCurrentTab(4);
        }
        super.onResume();
    }

    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
