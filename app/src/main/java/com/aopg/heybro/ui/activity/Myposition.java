package com.aopg.heybro.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aopg.heybro.R;

import com.aopg.heybro.ui.position.BaseActivity;
import com.aopg.heybro.ui.position.OnWheelChangedListener;
import com.aopg.heybro.ui.position.ArrayWheelAdapter;
import com.aopg.heybro.ui.position.WheelView;

/**
 * Created by 陈燕博 on 2018/6/19.
 */

public class Myposition extends Activity implements View.OnClickListener, OnWheelChangedListener {
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirm;
    private BaseActivity baseActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_ziliao_position);
        setUpViews();
        setUpListener();
        setUpData();
    }

    private void setUpViews() {
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
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
        baseActivity.initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(getApplicationContext(), baseActivity.getmProvinceDatas()));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(com.aopg.heybro.ui.position.WheelView wheel, int oldValue, int newValue) {
// TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            baseActivity.setmCurrentDistrictName( baseActivity.getmCitisDatasMap().get(baseActivity.getmCurrentCityName())[newValue]);
            baseActivity.setmCurrentZipCode( baseActivity.getmZipcodeDatasMap().get(baseActivity.getmCurrentDistrictName()));
        }
    }
    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        baseActivity.setmCurrentCityName( baseActivity.getmCitisDatasMap().get(baseActivity.getmCurrentProviceName())[pCurrent]);
        String[] areas = baseActivity.getmDistrictDatasMap().get(baseActivity.getmCurrentCityName());

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
        baseActivity.setmCurrentProviceName( baseActivity.getmProvinceDatas()[pCurrent]);
        String[] cities = baseActivity.getmCitisDatasMap().get(baseActivity.getmCurrentProviceName());
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
        Toast.makeText(getApplicationContext(), "当前选中:"+baseActivity.getmCurrentProviceName()+","+baseActivity.getmCurrentCityName()+","
                +baseActivity.getmCurrentDistrictName()+","+baseActivity.getmCurrentZipCode(), Toast.LENGTH_SHORT).show();
    }
}
