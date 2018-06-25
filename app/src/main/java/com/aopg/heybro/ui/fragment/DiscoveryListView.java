package com.aopg.heybro.ui.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by 王伟健 on 2018-03-19.
 */

public class DiscoveryListView extends ListView {
    public DiscoveryListView(Context context) {
        super(context);
    }

    public DiscoveryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiscoveryListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
