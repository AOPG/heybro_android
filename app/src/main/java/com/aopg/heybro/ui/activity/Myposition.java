package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aopg.heybro.R;

import com.aopg.heybro.ui.position.BaseActivity;
import com.aopg.heybro.ui.position.OnWheelChangedListener;
import com.aopg.heybro.ui.position.ArrayWheelAdapter;
import com.aopg.heybro.ui.position.WheelView;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static com.aopg.heybro.utils.HttpUtils.BASE_URL;

/**
 * Created by 陈燕博 on 2018/6/19.
 */

public class Myposition extends BaseActivity implements View.OnClickListener, OnWheelChangedListener {
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirm;
    public static int FLAG0=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_ziliao_position);
        //头像显示
        ImageView image=findViewById(R.id.image);
        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();

        Glide.with(this)
                .load(BASE_URL+ LoginInfo.user.getUserPortrait())
                .apply(options)
                .into(image);
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
        setUpViews();
        setUpListener();
        setUpData();
    }

    private void setUpViews() {
        mViewProvince =  findViewById(R.id.id_province);
        mViewCity =  findViewById(R.id.id_city);
        mViewDistrict = findViewById(R.id.id_district);
        mBtnConfirm =  findViewById(R.id.btn_confirm);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
    }

    public void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(getApplicationContext(), getmProvinceDatas()));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(6);
        mViewCity.setVisibleItems(6);
        mViewDistrict.setVisibleItems(6);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
// //TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            setmCurrentDistrictName( getmCitisDatasMap().get(getmCurrentCityName())[newValue]);
            setmCurrentZipCode( getmZipcodeDatasMap().get(getmCurrentDistrictName()));
        }
    }
    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        setmCurrentCityName( getmCitisDatasMap().get(getmCurrentProviceName())[pCurrent]);
        String[] areas = getmDistrictDatasMap().get(getmCurrentCityName());

        if (areas == null) {
            areas = new String[] { "" };
        }

        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        setmCurrentProviceName(getmProvinceDatas()[pCurrent]);
        String[] cities = getmCitisDatasMap().get(getmCurrentProviceName());
        if (cities == null) {
            cities = new String[] { "" };
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                showSelectedResult();
                break;
            default:
                break;
        }
    }
    private void showSelectedResult() {
        FLAG0=1;
        Intent pos = new Intent(getApplicationContext(), MyInfoActivity.class);
        Bundle bundle = new Bundle();                           //创建Bundle对象
        bundle.putString("province",getmCurrentProviceName()); //装入数据
        bundle.putString("city",getmCurrentCityName());
        bundle.putString("district",getmCurrentDistrictName());
        pos.putExtras(bundle);
        startActivity(pos);
    }
}
