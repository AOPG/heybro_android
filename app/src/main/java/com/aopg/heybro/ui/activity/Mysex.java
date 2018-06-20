package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aopg.heybro.R;
import com.aopg.heybro.utils.LoginInfo;

/**
 * Created by 陈燕博 on 2018/6/19.
 */

public class Mysex extends Activity {
    public static int FLAG=0;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RadioGroup sexs=findViewById(R.id.sexs);
        RadioButton sex0=findViewById(R.id.male);
        RadioButton sex1=findViewById(R.id.female);
        Button sex=findViewById(R.id.sexsure);
        setContentView(R.layout.fragment_my_ziliao_sex);
        if (LoginInfo.user.getUserSex().equals("男")){
            sex0.setChecked(true);
        }else {
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
