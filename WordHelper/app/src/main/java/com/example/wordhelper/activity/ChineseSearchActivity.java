package com.example.wordhelper.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wordhelper.R;
import com.example.wordhelper.base.BaseActivity;
import com.example.wordhelper.bean.ChineseBean;
import com.example.wordhelper.http.HttpMethods;
import com.example.wordhelper.util.NetWorkUtil;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChineseSearchActivity extends BaseActivity {
    @BindView(R.id.act_chinese_duyin) TextView mPhoneticSymbol;
    @BindView(R.id.act_chinese_name) TextView mName;
    @BindView(R.id.act_chinese_voice) ImageView mImageView;
    @BindView(R.id.topbar) QMUITopBarLayout mQmuiTopBarLayout;
    @BindView(R.id.act_chinese_lv) ListView mListView;

    private ArrayList<String> meaning_list;
    private ArrayAdapter adapter;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void initViews() {
        String searchStr = getIntent().getStringExtra("searchStr");
        mMediaPlayer = new MediaPlayer();
        meaning_list = new ArrayList<>();
        initTopBar();
        adapter = new ArrayAdapter<>(this,R.layout.text_history,meaning_list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ChineseSearchActivity.this,EnglishSearchActivity.class);
            intent.putExtra("searchStr", meaning_list.get(position).replaceAll("[^A-Za-z ]",""));
            startActivity(intent);
        });
        if(NetWorkUtil.isNetWorkAvailable(this)){
            getData(searchStr);
        }else {
            showToast("请检查您的网络连接");
        }

    }

    private void initTopBar(){
        mQmuiTopBarLayout.setTitle("搜索结果").setGravity(Gravity.CENTER);
        mQmuiTopBarLayout.setTitle("搜索结果").setTextColor(Color.parseColor("#FFFFFF"));
        mQmuiTopBarLayout.setBackgroundColor(Color.parseColor("#05B6F7"));
        mQmuiTopBarLayout.addLeftBackImageButton().setOnClickListener(v -> finish());
    }
    @SuppressLint("CheckResult")
    private void getData(String searchStr){
        //noinspection ResultOfMethodCallIgnored
        HttpMethods.getInstance().getChineseSearch(searchStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setView, Throwable::printStackTrace);
    }

    private void setView(ChineseBean chineseBean){
        mName.setText(chineseBean.getWord_name());
        String pronun = chineseBean.getSymbols().get(0).getWord_symbol();
        if(pronun != null && !pronun.equals("")){
            mPhoneticSymbol.setText(pronun);
        }else {mPhoneticSymbol.setVisibility(View.GONE);
            mImageView.setVisibility(View.GONE);}
        if(chineseBean.getSymbols().get(0).getParts() != null){
            List<ChineseBean.Symbol.Parts.Means> means = chineseBean.getSymbols().get(0)
                    .getParts().get(0).getMeans();
            for(ChineseBean.Symbol.Parts.Means mean:means){
                meaning_list.add(mean.getWord_mean());
            }
        }else {
            showToast("没有搜索结果");
        }
        adapter.notifyDataSetChanged();
        mImageView.setOnClickListener(v -> {
            if(chineseBean.getSymbols().get(0).getSymbol_mp3() ==null ||chineseBean.getSymbols().get(0).getSymbol_mp3().equals("")){
                showToast("暂无匹配资源");
                return;
            }
            mMediaPlayer.reset();
            try{ mMediaPlayer.setDataSource(chineseBean.getSymbols().get(0).getSymbol_mp3());}catch (IOException e){e.printStackTrace();}
            mMediaPlayer.prepareAsync();
        });
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_chinese_search;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer.isPlaying()){mMediaPlayer.stop();}
        mMediaPlayer.release();
    }
}
