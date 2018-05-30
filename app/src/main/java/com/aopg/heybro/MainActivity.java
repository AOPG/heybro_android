package com.aopg.heybro;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.aopg.heybro.ui.fragment.FragmentActivity;
import com.aopg.heybro.ui.fragment.FragmentBasketball;
import com.aopg.heybro.ui.fragment.FragmentDiscovery;
import com.aopg.heybro.ui.fragment.FragmentFriend;
import com.aopg.heybro.ui.fragment.FragmentMy;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FragmentTabHost myTabHost;
    private Map<String,Map<String,Object>> map;
    private static String LAST_SELECT = "basketball";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = new HashMap<>();
        initTabHost();
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

    /**
     * 初始化tabhost
     * */
    private void initTabHost(){
        myTabHost = this.findViewById(android.R.id.tabhost);
        myTabHost.setup(this, getSupportFragmentManager(),
                android.R.id.tabcontent);
        addTabSpec("discovery","发现",R.drawable.discovery, FragmentDiscovery.class);
        addTabSpec("activity","活动",R.drawable.activity, FragmentBasketball.class);
        addTabSpec("basketball","篮球",R.drawable.basketball, FragmentFriend.class);
        addTabSpec("friend","好友",R.drawable.friend, FragmentActivity.class);
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
}
