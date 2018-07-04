package com.aopg.heybro.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.aopg.heybro.R;
import com.aopg.heybro.entity.ChatRecord;
import com.aopg.heybro.entity.UserConversation;
import com.aopg.heybro.ui.adapter.PrivateLetterAdapter;
import com.aopg.heybro.utils.LoginInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 王伟健 on 2018-06-30.
 */

public class PrivateLetterActivity extends AppCompatActivity {

    ListView messageLv;
    private PrivateLetterAdapter privateLetterAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_letter_activity);
        IntentFilter filter = new IntentFilter("updatePrivateActivity");
        registerReceiver(broadcastReceiver, filter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //接收到更新聊天列表的信息
            privateLetterAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onResume() {
        messageLv = findViewById(R.id.messageLv);
        List<UserConversation> userConversationList =  DataSupport
                .where("userCode = ?", LoginInfo.user.getUserCode())
                .order("date desc")
                .find(UserConversation.class);
        privateLetterAdapter =
                new PrivateLetterAdapter(this,R.layout.private_letter_item,userConversationList);
        messageLv.setAdapter(privateLetterAdapter);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
