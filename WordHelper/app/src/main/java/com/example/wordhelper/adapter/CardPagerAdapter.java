package com.example.wordhelper.adapter;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.wordhelper.R;
import com.example.wordhelper.bean.WordBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {
    private List<CardView> mCardView;
    private List<WordBean> mWordBean;
    private float mBaseElevation;
    private MediaPlayer mMediaPlayer;
    private int REVIEW_MODE;
    private static final int CHINESE_VISIBLE = 0;
    private static final int ENGLISH_VISIBLE = 1;

    public CardPagerAdapter(List<WordBean> mWordBean,int review_mode) {
        this.mWordBean = mWordBean;
        this.REVIEW_MODE =review_mode;
        mCardView = new ArrayList<>();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(MediaPlayer::start);
        for(int i = 0;i<mWordBean.size();i++){
            mCardView.add(null);
        }
    }
    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mCardView.get(position);
    }

    @Override
    public int getCount() {
        return mWordBean.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.adapter,container,false);
        container.addView(view);
        CardView cardView = view.findViewById(R.id.cardView);
        if(mBaseElevation == 0){
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        initData(view,position,REVIEW_MODE);
        mCardView.set(position,cardView);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initData(View view, final int position,int mode) {
        TextView title = view.findViewById(R.id.item_prac_name);
        TextView ukduyin =  view.findViewById(R.id.item_prac_ukduyin);
        TextView usduyin = view.findViewById(R.id.item_prac_usduyin);
        Button ukvoice = view.findViewById(R.id.item_prac_ukvoice);
        Button usvoice = view.findViewById(R.id.item_prac_usvoice);

        final TextView but =  view.findViewById(R.id.item_prac_but);
        final TextView meaning = view.findViewById(R.id.item_prac_meaning);
        switch (mode){
            case ENGLISH_VISIBLE:
                meaning.setVisibility(View.GONE);
                break;
            case CHINESE_VISIBLE:
                title.setVisibility(View.GONE);
                ukduyin.setVisibility(View.GONE);
                ukvoice.setVisibility(View.GONE);
                usduyin.setVisibility(View.GONE);
                usvoice.setVisibility(View.GONE);
                but.setText("展开单词▲");
                break;
        }
        title.setText(mWordBean.get(position).getName());
        ukduyin.setText("[美]" + mWordBean.get(position).getEn_sympol());
        usduyin.setText("[英]" + mWordBean.get(position).getAm_symbol());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mWordBean.get(position).getParts().size(); i++) {
            builder.append(mWordBean.get(position).getParts().get(i).getPart()).append("  ").append(mWordBean.get(position).getParts().get(i).getMeans()).append("\r\n");
        }
        meaning.setText(builder.toString());

        but.setOnClickListener(v -> {
            but.setVisibility(View.INVISIBLE);
            switch (mode){
                case ENGLISH_VISIBLE:
                    meaning.setVisibility(View.VISIBLE);
                    break;
                case CHINESE_VISIBLE:
                    title.setVisibility(View.VISIBLE);
                    ukduyin.setVisibility(View.VISIBLE);
                    ukvoice.setVisibility(View.VISIBLE);
                    usduyin.setVisibility(View.VISIBLE);
                    usvoice.setVisibility(View.VISIBLE);
                    break;
            }

        });

        ukvoice.setOnClickListener(v -> {
            if (mWordBean.get(position).getEn_mp3() == null || mWordBean.get(position).getEn_mp3().equals("")) {
                return;
            }
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(mWordBean.get(position).getEn_mp3());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.prepareAsync();
        });
        usvoice.setOnClickListener(v -> {
            if (mWordBean.get(position).getAm_mp3() == null || mWordBean.get(position).getAm_mp3().equals("")) {
                return;
            }
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(mWordBean.get(position).getAm_mp3());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.prepareAsync();
        });
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
        mCardView.set(position,null);
    }
    public void releaseMediaPlayer(){
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
    }
}
