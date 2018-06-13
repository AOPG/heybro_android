package com.aopg.heybro;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.aopg.heybro.im.InitIM;
import com.aopg.heybro.ui.Common.ActivitiesManager;
import com.aopg.heybro.ui.activity.LoginActivty;
import com.aopg.heybro.ui.fragment.FragmentActivity;
import com.aopg.heybro.ui.fragment.FragmentBasketball;
import com.aopg.heybro.ui.fragment.FragmentDiscovery;
import com.aopg.heybro.ui.fragment.FragmentFriend;
import com.aopg.heybro.ui.fragment.FragmentMy;
import com.aopg.heybro.utils.BaiduMapLocationUtil;
import com.baidu.mapapi.SDKInitializer;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.im.android.api.event.LoginStateChangeEvent;
import cn.jpush.im.android.api.model.UserInfo;

public class MainActivity extends AppCompatActivity {
    private Handler mHandler;
    private FragmentTabHost myTabHost;
    private Map<String,Map<String,Object>> map;
    private static String LAST_SELECT = "basketball";
    MyBroadcastReceiver mbcr;
    private BaiduMapLocationUtil baiduMapLocationUtil;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        baiduMapLocationUtil.init(this);
        map = new HashMap<>();
        initTabHost();
        ActivitiesManager.getInstance().addActivity(this);

        mbcr = new MyBroadcastReceiver();
        //动态注册一个广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("FragmentMy");
        registerReceiver(mbcr, filter);// 注册
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    Log.e("mainactivity",
                            "------------------------启动service--------------------------");
                    startService(new Intent(MainActivity.this, UserInfoService.class));
                    try {
                        sleep(300000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();

        //底部tabhost改变图标
        myTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //将上一次点击的图标改回
                Map attr = map.get(LAST_SELECT);
                View view = (View) attr.get("view");
                ImageView iv = view.findViewById(R.id.imageId);
                TextView tv = view.findViewById(R.id.textId);
                iv.setImageResource((int)attr.get("img"));
                tv.setTextColor(0xFF2C2C2C);
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
    @Override
    protected void onStop() {
        super.onStop();

        baiduMapLocationUtil.onStop();
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
        addTabSpec("basketball","篮球",R.drawable.basketball, FragmentBasketball.class);
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
        TextView textView = view.findViewById(R.id.textId);
        textView.setText(title);
        return view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            Message msg = mHandler.obtainMessage();
            msg.what =0;
            mHandler.sendMessage(msg);
        }
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }


    public void onEvent(LoginStateChangeEvent event) {
        LoginStateChangeEvent.Reason reason = event.getReason();
        UserInfo myInfo = event.getMyInfo();
        Intent intent = new Intent(getApplicationContext(), LoginActivty.class);
        Toast.makeText(getApplicationContext(),"您在其他设备登录!如不是您登录，请及时修改密码！",Toast.LENGTH_SHORT);
        startActivity(intent);
    }
}
