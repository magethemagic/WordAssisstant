package com.example.wordhelper.activity;


import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.example.wordhelper.R;
import com.example.wordhelper.adapter.CategoryAdapter;
import com.example.wordhelper.base.BaseActivity;
import com.example.wordhelper.util.FileUtil;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {
    private LinearLayout[] linearLayouts;
    @BindView(R.id.main_vp) ViewPager mViewPager;
    @BindView(R.id.main_tab) TabLayout mTabs;
    @BindView(R.id.act_1_layout) LinearLayout mMenu1;
    @BindView(R.id.act_2_layout) LinearLayout mMenu2;
    @BindView(R.id.act_3_layout) LinearLayout mMenu3;

    @OnClick(R.id.act_1_layout)
    public void setmActLayout1(){
        //Toast.makeText(this,"Recommend",Toast.LENGTH_SHORT).show();
        setItem(0);
        mViewPager.setCurrentItem(0,false);
    }
    @OnClick(R.id.act_2_layout)
    public void setmActLayout2(){
        //Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
        setItem(0);
        mViewPager.setCurrentItem(1,false);
    }
    @OnClick(R.id.act_3_layout)

    public void setmActLayout3(){
       // Toast.makeText(this,"My vocabulary book",Toast.LENGTH_SHORT).show();
        setItem(0);
        mViewPager.setCurrentItem(2,false);
    }

    @Override
    protected void initViews() {
        linearLayouts = new LinearLayout[]{mMenu1,mMenu2,mMenu3};
        CategoryAdapter myPagerAdapter = new CategoryAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myPagerAdapter);
        mTabs.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void setItem(int index) {
        for (int i = 0; i < 3; i++) {
            if (i == index) {
                linearLayouts[i].setBackgroundColor(getResources().getColor(R.color.lighter_grey));
            } else {
                linearLayouts[i].setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }

}
