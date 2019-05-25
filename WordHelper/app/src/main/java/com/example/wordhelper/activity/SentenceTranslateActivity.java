package com.example.wordhelper.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wordhelper.R;
import com.example.wordhelper.base.BaseActivity;
import com.example.wordhelper.http.HttpMethods;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SentenceTranslateActivity extends BaseActivity {
    @BindView(R.id.act_sentence_but) Button mButton;
    @BindView(R.id.act_sentence_et) EditText mEditText;
    @BindView(R.id.act_sentence_tv) TextView mTextView;
    @BindView(R.id.topbar) QMUITopBarLayout mTopBar;
    @SuppressLint("CheckResult")
    @Override
    protected void initViews() {
        initTopBar();
        mButton.setOnClickListener(v -> {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(mEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            String str = mEditText.getText().toString().trim();
            if(str.equals("")){
                showToast("请输入内容");
                return;
            }
            //noinspection ResultOfMethodCallIgnored
            HttpMethods.getInstance().getLongSentenceTranslation(str)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(sentenceBean -> mTextView.setText(sentenceBean.getTranslation().get(0)),
                            Throwable::printStackTrace);
        });
    }
    private void initTopBar(){
        mTopBar.setTitle("搜索结果").setGravity(Gravity.CENTER);
        mTopBar.setTitle("搜索结果").setTextColor(Color.parseColor("#FFFFFF"));
        mTopBar.setBackgroundColor(Color.parseColor("#05B6F7"));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sentence;
    }
}
