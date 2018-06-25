package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aopg.heybro.R;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;

import static com.aopg.heybro.utils.HttpUtils.BASE_URL;

/**
 * Created by 陈燕博 on 2018/6/19.
 */

public class Mysex extends Activity {
    public static int FLAG=0;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_ziliao_sex);
        //头像显示
        ImageView image=findViewById(R.id.image);
        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();

        Glide.with(this)
                .load(BASE_URL+LoginInfo.user.getUserPortrait())
                .apply(options)
                .into(image);
       //昵称
        final TextView nicheng=findViewById(R.id.user_name);
        nicheng.setText(LoginInfo.user.getNickName());
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
        //性别
        TextView sexx=findViewById(R.id.user_sex);
        sexx.setText(LoginInfo.user.getUserSex());
        //生日
        TextView mybirtnday=findViewById(R.id.birthday);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String bir=sf.format(LoginInfo.user.getBirthday());
        mybirtnday.setText(bir);
        //关闭该页面
       // Button circle_close=findViewById(R.id.circle_close);
       // circle_close.setOnClickListener(new View.OnClickListener() {
     //       @Override
     //       public void onClick(View v) {
    //            Intent circleIntent=new Intent(getApplicationContext(),MyInfoActivity.class);
    //            startActivity(circleIntent);
    //        }
   //     });
        //选择男女
        final RadioGroup sexs=findViewById(R.id.sexs);
        RadioButton sex0=findViewById(R.id.male);
        RadioButton sex1=findViewById(R.id.female);
        Button sex=findViewById(R.id.sexsure);
        System.out.print(LoginInfo.user.getUserSex());//测试性别
        if ("男".equals(LoginInfo.user.getUserSex())){
           sex0.setChecked(true);
           sex1.setChecked(false);
        }else {
            sex0.setChecked(false);
            sex1.setChecked(true);
        }
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FLAG=1;
               String sexid= String.valueOf(sexs.getCheckedRadioButtonId());
               String sexw;
                if (sexid.equals("male")){
                    sexw="男" ;
                }
                else {
                    sexw="女" ;
                }
                Intent sex = new Intent(getApplicationContext(), MyInfoActivity.class);
                Bundle bundle = new Bundle();                           //创建Bundle对象
                bundle.putString("sex", sexw);     //装入数据
                sex.putExtras(bundle);
                startActivity(sex);
            }
        });
    }
}
