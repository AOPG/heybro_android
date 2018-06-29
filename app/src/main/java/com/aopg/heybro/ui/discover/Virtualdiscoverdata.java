package com.aopg.heybro.ui.discover;
import com.aopg.heybro.R;
import com.aopg.heybro.ui.Lunbo.GlideRoundTransform;
import com.aopg.heybro.ui.Lunbo.ShareCardView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 王攀 on 2018-06-06.
 * 虚拟数据
 */


public class Virtualdiscoverdata {

    //活动模块
    private List<Map<String,Object>> list2;

    //发现
    private List<Map<String,Object>> list;

    //轮播图控制
    public static int flag1 = 0;
    public static int flag2 = 0;

    //活动模块
    public List<Map<String,Object>> dataResource2(){
        list2 = new ArrayList<>();



        Map<String,Object> map = new HashMap<>();
        map.put("src",R.drawable.basketc);

        Map<String,Object> map1 = new HashMap<>();
        map1.put("src",R.drawable.baskete);

        Map<String,Object> map2 = new HashMap<>();
        map2.put("src",R.drawable.basketf);

//        Map<String,Object> map3 = new HashMap<>();
//        map3.put("src",R.drawable.basketd);
//
//        Map<String,Object> map4 = new HashMap<>();
//        map4.put("src",R.drawable.basketf);
//
//        Map<String,Object> map5 = new HashMap<>();
//        map5.put("src",R.drawable.basketh);

        list2.add(map);
//        list2.add(map4);
//        list2.add(map5);
        list2.add(map1);
        list2.add(map2);


        return list2;
    }


    //发现模块
    public List<Map<String,Object>> dataResource(){

        list = new ArrayList<>();

        Map<String,Object> map = new HashMap<>();
        map.put("src", R.drawable.head_portrait_one);
        map.put("src_another",R.drawable.dis_ball);
        map.put("textfir","一起来看流星雨");
        map.put("textsec","1分钟前");
        map.put("textthir","打球受伤了哎，不过很开心！");
        map.put("textfor","风雨无情，球手，篮球王");
        map.put("textfif","查看更多评论");


        Map<String,Object> map1 = new HashMap<>();
        map1.put("src",R.drawable.head_portrait_two);
        map1.put("src_another",R.drawable.dis_ball_two);
        map1.put("textfir","爱你么么哒");
        map1.put("textsec","两小时前");
        map1.put("textthir","今天陪我打球的小哥哥真帅，下次再约！");
        map1.put("textfor","风雨无情，球手，篮球王");
        map1.put("textfif","查看更多评论");


        Map<String,Object> map2 = new HashMap<>();
        map2.put("src",R.drawable.head_portrait_three);
        map2.put("src_another",R.drawable.dis_ball_three);
        map2.put("textfir","篮球火");
        map2.put("textsec","一天前");
        map2.put("textthir","今天天好热哇，不过打球打的好开心！");
        map2.put("textfor","风雨无情，球手，篮球王");
        map2.put("textfif","查看更多评论");


        list.add(map);
        list.add(map1);
        list.add(map2);

        return list;
    }

}
