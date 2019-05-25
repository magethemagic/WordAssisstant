package com.example.wordhelper.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wordhelper.R;
import com.example.wordhelper.activity.ChineseSearchActivity;
import com.example.wordhelper.activity.EnglishSearchActivity;
import com.example.wordhelper.activity.SentenceTranslateActivity;
import com.example.wordhelper.util.SharedPrefUtil;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment {
    @BindView(R.id.topbar) QMUITopBarLayout mTopBar;
    @OnClick(R.id.frag_fa_clear)
    void onClickClearButton(){
        if(histories.size() == 0){
            showInfo("暂无搜索历史记录");
            return;
        }
        SharedPrefUtil.clearHistory(Objects.requireNonNull(getActivity()));
        histories.clear();
        adapter.notifyDataSetChanged();
    }

    @BindView(R.id.frag_fa_et)
    EditText mEditText;

    @BindView(R.id.frag_fa_gv)
    GridView mGridView;

    @OnClick(R.id.topbar_textview)
    void onLongSentenceTranslate(){
            startActivity(new Intent(getActivity(), SentenceTranslateActivity.class));
    }

    private List<String> histories;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View rootView = inflater.inflate(R.layout.frag_faxian,null);
        ButterKnife.bind(this,rootView);
        initView();
        return rootView;
    }

    private void initView(){
        initTopBar();
        histories = new ArrayList<>();
        loadHistoryRecordFromLocal();
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),R.layout.text_his, histories);
        mGridView.setAdapter(adapter);

        mEditText.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    search(mEditText.getText().toString().trim());
            }
            return  false;
        });

        mGridView.setOnItemClickListener((parent, view, position, id) -> search(histories.get(position)));
    }
    private void initTopBar(){
        mTopBar.setBackgroundColor(Color.parseColor("#05B6F7"));
    }
    private void search(String word){
        if(word.equals("")){showInfo("请输入单词"); return; }
        if(histories.contains(word)){histories.remove(word);histories.add(word);}
        else {histories.add(word);}
        adapter.notifyDataSetChanged();
        SharedPrefUtil.saveHistory(Objects.requireNonNull(getActivity()),"history",histories);
        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(mEditText.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        mEditText.setText("");
        Intent intent = new Intent();
        intent.putExtra("searchStr",word);
        int length = (word.charAt(0)+"").getBytes().length;
        if(length == 3){intent.setClass(getActivity(), ChineseSearchActivity.class);}
        else if(length == 1){intent.setClass(getActivity(), EnglishSearchActivity.class);}
        startActivity(intent);

    }
    private void showInfo(String info){
        Toast.makeText(getActivity(),info,Toast.LENGTH_SHORT).show();
    }

    private void loadHistoryRecordFromLocal(){
        List<String> data = SharedPrefUtil.getList(Objects.requireNonNull(getActivity()), "history");
        if (data != null) {
            histories.addAll(data);
        }
    }

}
