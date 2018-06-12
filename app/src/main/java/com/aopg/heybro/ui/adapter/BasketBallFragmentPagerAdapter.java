package com.aopg.heybro.ui.adapter;

import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by 壑过忘川 on 2018/6/12.
 * viewPager 适配器
 */

public class BasketBallFragmentPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mfragmentManager;
    private List<Fragment> mlist;

    public BasketBallFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mlist = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mlist.get(arg0);//显示第几个页面
    }

    @Override
    public int getCount() {
        return mlist.size();//有几个页面
    }
}
