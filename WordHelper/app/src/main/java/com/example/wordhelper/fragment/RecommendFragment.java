package com.example.wordhelper.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wordhelper.R;
import com.example.wordhelper.base.BaseFragment;
import com.example.wordhelper.http.HttpMethods;
import com.example.wordhelper.util.NetWorkUtil;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RecommendFragment extends BaseFragment {
    private MediaPlayer mMediaPlayer;
    @BindView(R.id.frag_tui_img) ImageView mImage;
    @BindView(R.id.frag_tui_voice) ImageView mVoice;
    @BindView(R.id.frag_tui_content1) TextView mContent1;
    @BindView(R.id.frag_tui_content2) TextView mContent2;
    @BindView(R.id.frag_tui_des) TextView mDes;
    @BindView(R.id.topbar) QMUITopBarLayout mQmuiTopBarLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_tuijian;
    }

    protected void initView(){
        initTopBar();
         mMediaPlayer = new MediaPlayer();
         mMediaPlayer.setOnPreparedListener(MediaPlayer::start);
         if(NetWorkUtil.isNetWorkAvailable(Objects.requireNonNull(getActivity()))){
             getDailySentence();
         }else {
             Toast.makeText(getActivity(),"请检查您的网络连接",Toast.LENGTH_SHORT).show();
         }

    }

    private void initTopBar(){
        mQmuiTopBarLayout.setTitle("每日推荐").setGravity(Gravity.CENTER);
        mQmuiTopBarLayout.setTitle("每日推荐").setTextColor(Color.parseColor("#FFFFFF"));
        mQmuiTopBarLayout.setBackgroundColor(Color.parseColor("#05B6F7"));
    }


    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void getDailySentence(){
        //noinspection ResultOfMethodCallIgnored
        HttpMethods.getInstance().getEveryDayWords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(everyDayWords -> {
                    mContent1.setText(everyDayWords.getContent());
                    mContent2.setText(everyDayWords.getNote());
                    mDes.setText(everyDayWords.getTranslation()+"   ");
                    Glide.with(Objects.requireNonNull(getActivity())).load(everyDayWords.getPicture2()).into(mImage);
                    mVoice.setOnClickListener((v) -> {
                            mMediaPlayer.reset();
                            try{
                                mMediaPlayer.setDataSource(everyDayWords.getTts());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            mMediaPlayer.prepareAsync();
                    });

                }, Throwable::printStackTrace);

    }



    @Override
    public void onDestroy() {
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        super.onDestroy();
    }

}
