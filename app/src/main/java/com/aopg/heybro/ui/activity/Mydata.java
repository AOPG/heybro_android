package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.data.DataAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈燕博 on 2018/6/24.
 */

public class Mydata extends Activity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_data);

        //返回按钮
        ImageView info_back = findViewById(R.id.info_back);
        info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //传入我的数据

        // 准备数据源
        List<Map<String, Object>> dataSource = new ArrayList<>();
        Map<String, Object> itemData1 = new HashMap<>();
    // 定义第1个数据项
        itemData1.put("star", R.drawable.star2);
        itemData1.put("result", "胜");
        itemData1.put("gaimao", "盖帽:0");
        itemData1.put("defen","得分:7");
        itemData1.put("lanban","篮板:1");
        itemData1.put("qiangduan","抢断:2");
        itemData1.put("zhugong","助攻:4");
        itemData1.put("shiwu","失误:0");
        dataSource.add(itemData1);
     // 第2个数据项
        Map<String, Object> itemData2 = new HashMap<>();
        itemData2.put("star", R.drawable.star2);
        itemData2.put("result", "胜");
        itemData2.put("gaimao", "盖帽:0");
        itemData2.put("defen","得分:7");
        itemData2.put("lanban","篮板:1");
        itemData2.put("qiangduan","抢断:2");
        itemData2.put("zhugong","助攻:4");
        itemData2.put("shiwu","失误:0");
        dataSource.add(itemData2);
        // 第3个数据项
        Map<String, Object> itemData3 = new HashMap<>();
        itemData3.put("star", R.drawable.star1);
        itemData3.put("result", "负");
        itemData3.put("gaimao", "盖帽:1");
        itemData3.put("defen","得分:5");
        itemData3.put("lanban","篮板:1");
        itemData3.put("qiangduan","抢断:3");
        itemData3.put("zhugong","助攻:3");
        itemData3.put("shiwu","失误:0");
        dataSource.add(itemData3);
        // 第4个数据项
        Map<String, Object> itemData4 = new HashMap<>();
        itemData4.put("star", R.drawable.star1);
        itemData4.put("result", "负");
        itemData4.put("gaimao", "盖帽:0");
        itemData4.put("defen","得分:3");
        itemData4.put("lanban","篮板:1");
        itemData4.put("qiangduan","抢断:2");
        itemData4.put("zhugong","助攻:3");
        itemData4.put("shiwu","失误:0");
        dataSource.add(itemData4);
        // 5个数据项
        Map<String, Object> itemData5 = new HashMap<>();
        itemData5.put("star", R.drawable.star2);
        itemData5.put("result", "胜");
        itemData5.put("gaimao", "盖帽:2");
        itemData5.put("defen","得分:15");
        itemData5.put("lanban","篮板:0");
        itemData5.put("qiangduan","抢断:3");
        itemData5.put("zhugong","助攻:4");
        itemData5.put("shiwu","失误:4");
        dataSource.add(itemData5);
        //获得listView控件
        ListView listView=findViewById(R.id.mydata);
         DataAdapter dataAdapter = new DataAdapter(
                   this,       // 上下文环境
                  dataSource, // 数据源
                   R.layout.my_item // 列表项布局文件
            );
         listView.setAdapter(dataAdapter);
        // 给ListView的列表项注册点击事件监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });

    }
}
