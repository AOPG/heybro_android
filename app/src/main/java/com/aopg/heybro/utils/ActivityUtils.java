package com.aopg.heybro.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aopg.heybro.R;

/**
 * Created by 王伟健 on 2018-06-25.
 */

public class ActivityUtils {
    /**
     * 添加状态栏占位视图
     *
     * @param
     */
    public static void  addStatusViewWithColor(View view, int color) {
        LinearLayout contentView = view.findViewById(R.id.statusbar);
        LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)contentView.getLayoutParams();
        lp.height=STATUSHEIGHT;
        contentView.setLayoutParams(lp);
        contentView.requestLayout();
    }

    /**
     * 利用反射获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int STATUSHEIGHT = 0;
}
