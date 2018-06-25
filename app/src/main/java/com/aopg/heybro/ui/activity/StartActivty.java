package com.aopg.heybro.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.im.InitIM;
import com.aopg.heybro.ui.Common.ActivitiesManager;
import com.aopg.heybro.utils.ActivityUtils;
import com.aopg.heybro.utils.LoginInfo;

import org.litepal.crud.DataSupport;

import static com.aopg.heybro.utils.ActivityUtils.getStatusBarHeight;


/**
 *  Created by  王攀 on 2018/5/31.
 *  开始动画
 */

public class StartActivty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.STATUSHEIGHT = getStatusBarHeight(this);
        setContentView(R.layout.start_activity);
        ActivitiesManager.getInstance().addActivity(this);
        MyAsyncTask asyncTask = new MyAsyncTask();

        asyncTask.execute();
    }

    private class MyAsyncTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            User user;
            user = DataSupport.where("isLogin = ?","1").findFirst(User.class);
            Intent intent;
            if (null!=user){
                LoginInfo.user = user;
                intent = new Intent(StartActivty.this, MainActivity.class);
            }else {
                intent = new Intent(StartActivty.this, LoginActivty.class);
            }

            startActivity(intent);
            overridePendingTransition(R.anim.in_anim,R.anim.init_anim);
        }
    }

}
