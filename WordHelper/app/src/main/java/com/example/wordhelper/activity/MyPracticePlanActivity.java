package com.example.wordhelper.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;

import androidx.viewpager.widget.ViewPager;

import com.example.wordhelper.R;
import com.example.wordhelper.adapter.CardPagerAdapter;
import com.example.wordhelper.app.MyApp;
import com.example.wordhelper.base.BaseActivity;
import com.example.wordhelper.bean.WordBean;
import com.example.wordhelper.dao.WordBeanDao;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyPracticePlanActivity extends BaseActivity {
    @BindView(R.id.act_prac_vp) ViewPager mViewPager;
    @BindView(R.id.act_prac_remember) Button mRemember;
    @BindView(R.id.act_prac_unremember) Button mUnremember;
    @BindView(R.id.topbar) QMUITopBarLayout mTopBar;

    private CardPagerAdapter mAdapter;
    private List<WordBean> mList;
    private int position;
    private int review_mode;
    private WordBeanDao mWordBeanDao;

    @SuppressLint("CheckResult")
    @Override
    protected void initViews() {
        initTopBar();
        String groupName = getIntent().getStringExtra("groupName");
        review_mode = getIntent().getIntExtra("review_mode",0);
        mWordBeanDao = ((MyApp)getApplication()).getDaoSession().getWordBeanDao();
        //noinspection ResultOfMethodCallIgnored
        getWordList(groupName).subscribe(wordBeans -> {
            mList =wordBeans;
            mAdapter = new CardPagerAdapter(mList,review_mode);
            mViewPager.setAdapter(mAdapter);
        }, Throwable::printStackTrace);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MyPracticePlanActivity.this.position =position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRemember.setOnClickListener(v -> {
            WordBean bean =mList.get(position);
            bean.setDone(true);
            mWordBeanDao.update(bean);
            if(position != mList.size() -1){
                mViewPager.setCurrentItem(position +1);
            }else {
                showToast("本次复习结束");
                finish();
            }
        });

        mUnremember.setOnClickListener(v -> {
            if(position != mList.size() -1){
                mViewPager.setCurrentItem(position +1);
            }else {
                showToast("本次复习结束");
                finish();
            }
        });
    }

    private void initTopBar(){
        mTopBar.setTitle("单词复习").setGravity(Gravity.CENTER);
        mTopBar.setTitle("单词复习").setTextColor(Color.parseColor("#FFFFFF"));
        mTopBar.setBackgroundColor(Color.parseColor("#05B6F7"));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }
     private Observable<List<WordBean>> getWordList(String groupName){
      return   Observable.create((ObservableOnSubscribe<List<WordBean>>) emitter -> {
          emitter.onNext(mWordBeanDao.queryBuilder()
                  .where(WordBeanDao.Properties.GroupName.eq(groupName))
                  .where(WordBeanDao.Properties.Done.eq(false))
                  .list());
          emitter.onComplete();
      }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plan_practice;
    }

    @Override
    public void showToast(String string) {
        super.showToast(string);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.releaseMediaPlayer();
    }
}
