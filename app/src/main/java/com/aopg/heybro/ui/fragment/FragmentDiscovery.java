package com.aopg.heybro.ui.fragment;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.Common.MyViewPageLB;
import com.aopg.heybro.ui.discover.CustomerAdapter;
import com.aopg.heybro.ui.discover.HorizontalListView;
import com.aopg.heybro.ui.discover.HorizontalListViewAdapter;
import com.aopg.heybro.ui.discover.Virtualdiscoverdata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentDiscovery extends Fragment {

    private  List<Map<String,Object>> list;
    HorizontalListView hListView;
    HorizontalListViewAdapter hListViewAdapter;
    ImageView previewImg;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e("", "onCreateView");
        if (rootView == null) {
            Log.e("", "FragmentDiscovery");
            rootView = inflater.inflate(R.layout.fragment_discovery,container,false);
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();


        LinearLayout llPoints = (LinearLayout) rootView.findViewById(R.id.ll_points);
        ViewPager mVp = (ViewPager) rootView.findViewById(R.id.vp);
        MyViewPageLB myViewPageLB = new MyViewPageLB(rootView.getContext(),mVp,llPoints);

        initUI();

        list = new Virtualdiscoverdata().dataResource();
        ListView lv = rootView.findViewById(R.id.lv);
        lv.setAdapter(new CustomerAdapter(rootView.getContext(),list,R.layout.discover_list_item));

        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    public void initUI(){
        hListView = (HorizontalListView)rootView.findViewById(R.id.horizon_listview);
        previewImg = (ImageView)rootView.findViewById(R.id.dis_pic);
        String[] titles = {"推荐", "篮球动态", "篮球装备", "达人动态","附近动态","热门话题"};
        final int[] ids = {R.drawable.discover_search, R.drawable.discover_ball,
                R.drawable.discover_equipment, R.drawable.discover_tj,R.drawable.discover_tj
        ,R.drawable.discover_topic};
        final int[] idss = {R.drawable.discover_coins, R.drawable.discover_coinf,
                R.drawable.discover_coint, R.drawable.discover_coinfo,R.drawable.discover_coinbl
                ,R.drawable.discover_coinlibl};
//        Resources resources = getContext().getResources();
//        List<Drawable> list = new ArrayList<>();
//        list.add(resources.getDrawable(R.drawable.discover_coins));
//        list.add(resources.getDrawable(R.drawable.discover_coinf));
//        list.add(resources.getDrawable(R.drawable.discover_coint));
//        list.add(resources.getDrawable(R.drawable.discover_coinfo));
//        list.add(resources.getDrawable(R.drawable.discover_coinbl));
//        list.add(resources.getDrawable(R.drawable.discover_coinlibl));


        hListViewAdapter = new HorizontalListViewAdapter(rootView.getContext(),titles,ids,idss);
        hListView.setAdapter(hListViewAdapter);
        //      hListView.setOnItemSelectedListener(new OnItemSelectedListener() {
        //
        //          @Override
        //          public void onItemSelected(AdapterView<?> parent, View view,
        //                  int position, long id) {
        //              // TODO Auto-generated method stub
        //              if(olderSelected != null){
        //                  olderSelected.setSelected(false); //上一个选中的View恢复原背景
        //              }
        //              olderSelected = view;
        //              view.setSelected(true);
        //          }
        //
        //          @Override
        //          public void onNothingSelected(AdapterView<?> parent) {
        //              // TODO Auto-generated method stub
        //
        //          }
        //      });
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
//              if(olderSelectView == null){
//                  olderSelectView = view;
//              }else{
//                  olderSelectView.setSelected(false);
//                  olderSelectView = null;
//              }
//              olderSelectView = view;
//              view.setSelected(true);
//                previewImg.setImageResource(ids[position]);
                hListViewAdapter.setSelectIndex(position);
                hListViewAdapter.notifyDataSetChanged();

            }
        });

    }
}
