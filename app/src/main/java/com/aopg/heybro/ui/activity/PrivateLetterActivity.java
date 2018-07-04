package com.aopg.heybro.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.aopg.heybro.R;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_letter_activity);
        messageLv = findViewById(R.id.messageLv);
        List<UserConversation> userConversationList =  DataSupport
                .where("userCode = ?", LoginInfo.user.getUserCode())
                .find(UserConversation.class);
        PrivateLetterAdapter privateLetterAdapter =
                new PrivateLetterAdapter(this,R.layout.private_letter_item,userConversationList);
        messageLv.setAdapter(privateLetterAdapter);

    }



}
