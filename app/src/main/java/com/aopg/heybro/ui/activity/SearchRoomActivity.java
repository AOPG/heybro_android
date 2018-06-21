package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.ui.adapter.SearchRoomAdapter;

/**
 * Created by 壑过忘川 on 2018/6/21.
 * 搜索房间界面
 */

public class SearchRoomActivity extends Activity {
    private SearchRoomAdapter searchRoomAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basketball_searchroom);
        //取消
        TextView searchroom_cancel = findViewById(R.id.searchroom_cancel);
        searchroom_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }
}
