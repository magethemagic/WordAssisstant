package com.example.wordhelper.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordhelper.R;
import com.example.wordhelper.app.MyApp;
import com.example.wordhelper.base.BaseActivity;
import com.example.wordhelper.bean.EnglishBean;
import com.example.wordhelper.bean.Part;
import com.example.wordhelper.bean.WordBean;
import com.example.wordhelper.dao.WordBeanDao;
import com.example.wordhelper.http.HttpMethods;
import com.example.wordhelper.util.NetWorkUtil;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class EnglishSearchActivity extends BaseActivity {
    @BindView(R.id.act_english_name) TextView mName;
    @BindView(R.id.act_english_ukduyin) TextView mUKPhSymbol;
    @BindView(R.id.act_english_usduyin)  TextView mUSPhSymbol;
    @BindView(R.id.act_english_ukvoice) ImageView mUKPron;
    @BindView(R.id.act_english_usvoice) ImageView mUSPron;
    @BindView(R.id.act_english_star) ImageView mStar;
    @BindView(R.id.act_english_lin) LinearLayout mPart;
    @BindView(R.id.topbar) QMUITopBarLayout mQmuiTopBarLayout;
    private MediaPlayer mMediaPlayer;
    private  EnglishBean mEnglishBean;
    private String mWordName;
    private boolean isCollected;
    public WordBean mWordBean;
    private WordBeanDao mWordBeanDao;

    @SuppressLint("CheckResult")
    @Override
    protected void initViews() {
        mWordBean =new WordBean();
        mMediaPlayer = new MediaPlayer();
        initTopBar();
        mMediaPlayer.setOnPreparedListener(MediaPlayer::start);
        mStar.setOnClickListener(v -> {
            if(isCollected){
                    Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                        mWordBeanDao.deleteByKey(mWordBean.getId());
                        emitter.onNext(true);
                        emitter.onComplete();
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aBoolean -> {
                        if(aBoolean){
                            showToast("移除成功");
                            isCollected = false;
                            mStar.setImageResource(R.drawable.staroff);
                        }else {
                            showToast("Error");
                        }
                    });

            }else {
                final WordBean word = new WordBean();
                word.setName(mEnglishBean.getWord_name());
                word.setAm_symbol(mEnglishBean.getSymbols().get(0).getPh_am());
                word.setEn_sympol(mEnglishBean.getSymbols().get(0).getPh_en());
                word.setAm_mp3(mEnglishBean.getSymbols().get(0).getPh_am_mp3());
                word.setEn_mp3(mEnglishBean.getSymbols().get(0).getPh_en_mp3());
                Observable.create((ObservableOnSubscribe<WordBean>) emitter -> {
                   ((MyApp)getApplication()).getDaoSession().getWordBeanDao().insert(word);
                    emitter.onNext(word);

                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(wordBean -> {
                            mWordBean = wordBean;
                            List<EnglishBean.Symbols.Parts> parts = mEnglishBean.getSymbols().get(0).getParts();
                            final List<Part> addParts = word.getParts();
                            for (int i = 0; i < parts.size(); i++) {
                                Part part = new Part(null, wordBean.getId(), parts.get(i).getPart(), parts.get(i).getMeans().toString());
                                addParts.add(part);
                            }
                            Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                                ((MyApp)getApplication()).getDaoSession().getPartDao().insertInTx(addParts);
                                emitter.onNext(true);
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(aBoolean -> {
                                        showToast("添加成功");
                                        isCollected = true;
                                        mStar.setImageResource(R.drawable.staron);
                                    });
                        });
            }
        });
        if(NetWorkUtil.isNetWorkAvailable(this)){
            String searchStr = getIntent().getStringExtra("searchStr");
            mWordBeanDao = ((MyApp) getApplication()).getDaoSession().getWordBeanDao();
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
        HttpMethods.getInstance().getEnglishSearch(searchStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(englishBean ->{
                    dataToView(englishBean);
                    mEnglishBean = englishBean;
                    }
                    , Throwable::printStackTrace);
    }


    @SuppressLint("SetTextI18n")
    private void dataToView(EnglishBean englishBean){
        if(englishBean.getWord_name() == null || englishBean.getWord_name().equals("")){
            Toast.makeText(this,"没有你想要的结果",Toast.LENGTH_SHORT).show();
            return;
        }
        mWordName = englishBean.getWord_name();
        queryResult();
        mName.setText(englishBean.getWord_name());
        final EnglishBean.Symbols symbols = englishBean.getSymbols().get(0);
        mUKPhSymbol.setText("[英] /"+(symbols.getPh_en() == null?"NULL":symbols.getPh_en())+"/");
        mUSPhSymbol.setText("[美] /"+(symbols.getPh_am()== null?"NULL":symbols.getPh_am())+"/");
        mUKPron.setOnClickListener(v -> {
            if(symbols.getPh_en_mp3() == null || symbols.getPh_en_mp3().equals("")){
                showToast("暂无匹配资源");
                return;
            }
            mMediaPlayer.reset();
            try { mMediaPlayer.setDataSource(symbols.getPh_en_mp3()); }
            catch (IOException e){ e.printStackTrace(); }
            mMediaPlayer.prepareAsync();
        });
        mUSPron.setOnClickListener(v -> {
            if (symbols.getPh_am_mp3() == null || symbols.getPh_am_mp3().equals("")) {
                showToast("暂无匹配资源");
                return;
            }
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(symbols.getPh_am_mp3());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.prepareAsync();
        });
        for (int i = 0; i < symbols.getParts().size(); i++) {
            View v = View.inflate(this, R.layout.text_means, null);
            TextView part =  v.findViewById(R.id.part);
            TextView means =  v.findViewById(R.id.means);

            part.setText(symbols.getParts().get(i).getPart());
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < symbols.getParts().get(i).getMeans().size(); j++) {
                if (j < symbols.getParts().get(i).getMeans().size() - 1) {
                    sb.append(symbols.getParts().get(i).getMeans().get(j)).append(";").append("\r\n");
                } else {
                    sb.append(symbols.getParts().get(i).getMeans().get(j));
                }
            }
            means.setText(sb.toString());
            mPart.addView(v);
        }
    }

    @SuppressLint("CheckResult")
    private void queryResult(){
        Observable.create((ObservableOnSubscribe<List<WordBean>>) emitter -> {
            emitter.onNext(((MyApp) getApplication()).getDaoSession()
                    .getWordBeanDao()
                    .queryBuilder()
                    .where(WordBeanDao.Properties.Name.eq(mWordName)).build().list());
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordBeans -> {
                    if(wordBeans.size() == 0){
                        isCollected =false;
                        mStar.setImageResource(R.drawable.staroff);
                    }else {
                        isCollected =true;
                        mWordBean = wordBeans.get(0);
                        mStar.setImageResource(R.drawable.staron);
                    }
                }, Throwable::printStackTrace);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_english_search;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
    }
}
