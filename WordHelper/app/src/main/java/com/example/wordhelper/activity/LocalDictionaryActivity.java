package com.example.wordhelper.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wordhelper.R;
import com.example.wordhelper.base.BaseActivity;
import com.example.wordhelper.bean.DictionaryBean;
import com.example.wordhelper.util.FileUtil;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class LocalDictionaryActivity extends BaseActivity {
    @BindView(R.id.act_dict_lv) ListView mListView;
    @BindView(R.id.act_dict_et) EditText mEditText;
    @BindView(R.id.topbar) QMUITopBarLayout mTopBar;
    private SQLiteDatabase database;
    private List<DictionaryBean> list;
    private ArrayList<DictionaryBean> changable_list;
    private DictAdapter adapter;
    @Override
    protected void initViews() {
        changable_list = new ArrayList<>();
        initTopBar();
        database = FileUtil.openDatabase(this,getFilesDir().getAbsolutePath()+"/dictionary.db");
        setData();
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("CheckResult")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSearchBean(s.toString()).subscribe(dictionaryBeans -> {
                    changable_list.clear();
                    changable_list.addAll(dictionaryBeans);
                    adapter =new DictAdapter(changable_list);
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this,EnglishSearchActivity.class);
            intent.putExtra("searchStr",changable_list.get(position).getEnglish());
            startActivity(intent);
        });
    }

    Observable<List<DictionaryBean>> getSearchBean(String str){
        return Observable.create((ObservableOnSubscribe<List<DictionaryBean>>) emitter -> {
            emitter.onNext(search(str));
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<DictionaryBean> search(String str){
        List<DictionaryBean> result = new ArrayList<>();
        for (DictionaryBean bean:list){
            if(bean.getEnglish().contains(str) || bean.getChinese().contains(str)){
                result.add(bean);
            }
        }
        return result;
    }

    private void initTopBar(){
        mTopBar.setTitle("本地词典").setGravity(Gravity.CENTER);
        mTopBar.setTitle("本地词典").setTextColor(Color.parseColor("#FFFFFF"));
        mTopBar.setBackgroundColor(Color.parseColor("#05B6F7"));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }
    private void setData(){
        Cursor cursor = database.query("t_words",null,null,null,null,null,null,null);
        list = new ArrayList<>();
        while (cursor.moveToNext()){
            DictionaryBean bean = new DictionaryBean(cursor.getString(0),cursor.getString(1));
            list.add(bean);
        }
        changable_list.addAll(list);
        cursor.close();
        adapter = new DictAdapter(list);
        mListView.setAdapter(adapter);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_dictionary;
    }


    class DictAdapter extends BaseAdapter {
        List<DictionaryBean> list;
        DictAdapter(List<DictionaryBean> list) {
            this.list =list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder vh;
            if (v == null) {
                v = View.inflate(LocalDictionaryActivity.this, R.layout.item_dictionary, null);
                vh = new ViewHolder();
                vh.english =  v.findViewById(R.id.item_dict_english);
                vh.chinese =  v.findViewById(R.id.item_dict_chinese);
                v.setTag(vh);
            } else {
                vh = (ViewHolder) v.getTag();
            }
            vh.english.setText(list.get(position).getEnglish());
            vh.chinese.setText(list.get(position).getChinese());
            return v;
        }

        class ViewHolder {
            TextView english, chinese;
        }
    }
}
