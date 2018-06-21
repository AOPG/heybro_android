package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aopg.heybro.R;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static cn.jpush.im.android.api.jmrtc.JMRTCInternalUse.getApplicationContext;
import static com.aopg.heybro.ui.activity.Myposition.FLAG0;
import static com.aopg.heybro.ui.activity.Mysex.FLAG;
import static com.aopg.heybro.utils.HttpUtils.BASE_URL;

/**
 * Created by 壑过忘川 on 2018/6/6.
 * 我的资料界面
 */

public class MyInfoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_ziliao);
        //返回按钮
        ImageView info_back = findViewById(R.id.info_back);
        info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //头像显示
        ImageView image=findViewById(R.id.image);
        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();

        Glide.with(this)
                .load(BASE_URL+LoginInfo.user.getUserPortrait())
                .apply(options)
                .into(image);
        //头像上传
        LinearLayout ima=findViewById(R.id.image1);
        ima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //昵称
        final EditText nicheng=findViewById(R.id.user_name);
        nicheng.setHint(LoginInfo.user.getNickName());
        final String[] name = {LoginInfo.user.getNickName()};
        nicheng.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                name[0] = String.valueOf(nicheng.getText());
            }
        });

        //ID(不可修改)
        TextView ID=findViewById(R.id.user_code);
        ID.setText(LoginInfo.user.getUserCode());
        String id= String.valueOf(ID.getText());
        //地区
        TextView userpo=findViewById(R.id.user_location);
        if(FLAG0==0){
            userpo.setText(LoginInfo.user.getUserProvince()+"  "+LoginInfo.user.getUserCity());
        }else {
            Intent intent1 =  getIntent();;       //获取已有的intent对象
            Bundle bundle1 = intent1.getExtras();//获取intent里面的bundle对象
            String provice= bundle1.getString("provice");
            String city=bundle1.getString("city");
            userpo.setText(provice+"  "+city);
            FLAG0=0;
        }
        userpo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent poIntent = new Intent(getApplicationContext(), Myposition.class);
                startActivity(poIntent);
                  }
               });

        //简介
        final EditText userintro=findViewById(R.id.user_intro);
       userintro.setHint(LoginInfo.user.getUserIntro());
        final String[] intro = {LoginInfo.user.getUserIntro()};
        userintro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                intro[0] = String.valueOf(userintro.getText());
            }
        });

        //性别
        TextView sex=findViewById(R.id.user_sex);
        System.out.print(LoginInfo.user.getUserSex());//测试打印性别
        String s=null;
        if (FLAG==0){
            sex.setText(LoginInfo.user.getUserSex());
        }else{
            Intent intent = getIntent();//获取已有的intent对象
            Bundle bundle = intent.getExtras();    //获取intent里面的bundle对象
            s= bundle.getString("sex");    //获取Bundle里面的字符串
            sex.setText(s);
            FLAG=0;
        }

        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent sexIntent = new Intent(getApplicationContext(), Mysex.class);
                startActivity(sexIntent);
            }
        });
        //二维码
        LinearLayout erweima=findViewById(R.id.erweima);
        erweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toastTip
                        = Toast.makeText(getApplicationContext(),
                        "该模块待开发",
                        Toast.LENGTH_LONG);
                toastTip.setGravity(Gravity.CENTER, 0, 0);
                toastTip.show();
            }
        });
        //生日
//       final TextView mybirtnday=findViewById(R.id.birthday);
//       mybirtnday.setText(String.valueOf(LoginInfo.user.getBirthday()));
//       mybirtnday.setOnClickListener(new View.OnClickListener() {
 //           @Override
 //           public void onClick(View v) {
//                DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(MyInfoActivity.this,
 //                      new DateChooseWheelViewDialog.DateChooseInterface() {
//                           @Override
//                           public void getDateTime(String time, boolean longTimeChecked) {
//                               mybirtnday.setText(time);
  //                          }
//    });
//               endDateChooseDialog.setTimePickerGone(true);
 //              endDateChooseDialog.setDateDialogTitle("选择生日");
 //              endDateChooseDialog.showDateChooseDialog();
 //           }
//        });

//        String birth= String.valueOf(mybirtnday.getText());
//        System.out.print(birth);
 //       long b=new java.util.Date(birth).getTime();
        //修改
        Button xiugai=findViewById(R.id.xiugai);
        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }
}
