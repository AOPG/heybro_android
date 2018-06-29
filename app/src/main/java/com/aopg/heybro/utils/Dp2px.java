package com.aopg.heybro.utils;

import android.content.Context;

/**
 * Created by 王伟健 on 2018-03-16.
 */

public class Dp2px {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources()
                .getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
}
