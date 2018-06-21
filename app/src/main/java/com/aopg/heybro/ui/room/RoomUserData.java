package com.aopg.heybro.ui.room;

import com.aopg.heybro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王攀 on 2018/6/20.
 */

public class RoomUserData {
    private List<Map<String,Object>> list;

    //进入房间模块
    public List<Map<String,Object>> dataResource(){

        list = new ArrayList<>();

        Map<String,Object> map = new HashMap<>();
        map.put("img", R.drawable.head_portrait_one);
        map.put("user_name",R.drawable.dis_ball);
        map.put("user_Intro","一起来看流星雨");



        Map<String,Object> map1 = new HashMap<>();
        map1.put("img",R.drawable.head_portrait_two);
        map1.put("user_name",R.drawable.dis_ball_two);
        map1.put("user_Intro","一起来看流星雨");


        Map<String,Object> map2 = new HashMap<>();
        map2.put("img",R.drawable.head_portrait_three);
        map2.put("user_name",R.drawable.dis_ball_three);
        map2.put("user_Intro","篮球火");



        list.add(map);
        list.add(map1);
        list.add(map2);

        return list;
    }

}
