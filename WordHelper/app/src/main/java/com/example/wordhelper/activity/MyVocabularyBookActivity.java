package com.example.wordhelper.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wordhelper.R;
import com.example.wordhelper.app.MyApp;
import com.example.wordhelper.base.BaseActivity;
import com.example.wordhelper.bean.WordBean;
import com.example.wordhelper.util.SharedPrefUtil;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.zhl.cbdialog.CBDialogBuilder;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class MyVocabularyBookActivity extends BaseActivity {

    @BindView(R.id.topbar) QMUITopBarLayout mTopBar;
    @BindView(R.id.topbar_rightbutton) QMUIAlphaImageButton mTopBarRightButton;

    @BindView(R.id.act_danciben_lv) ListView mListView;
    @BindView(R.id.act_danciben_but) Button mAddButton;
    @BindView(R.id.act_danciben_select_all) CheckBox mSelectAllCheckBox;
    @BindView(R.id.act_danciben_edit_linearlayout) LinearLayout mLinearLayout;

    private List<WordBean> list;
    private List<String> planList;
    private Query<WordBean> mQuery;
    private MyVocaBookAdapter adapter;
    private boolean editMode;
    private int position = -1;

    @OnClick(R.id.topbar_rightbutton)
    void editVocabularyBook(){
        if(!editMode){
            //mLinearLayout.setVisibility(View.VISIBLE);
            setAnimation(true);
            for(WordBean bean:list){
                bean.setChecked(false);
            }
            editMode = true;
            adapter.notifyDataSetChanged();
        }else {
            //mLinearLayout.setVisibility(View.GONE);
            setAnimation(false);
            for(WordBean bean:list){
                bean.setChecked(true);
            }
            editMode = false;
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.act_danciben_but)
    void addPlan(){
        mLinearLayout.setVisibility(View.GONE);
        editMode =false;
        adapter.notifyDataSetChanged();
        addToPlan();
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.act_danciben_removeall)
    void removeAll(){
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            for(WordBean bean : list){
                ((MyApp)getApplication()).getDaoSession().getWordBeanDao().delete(bean);
            }
            emitter.onNext(true);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    showToast("移除成功");
                    list.clear() ;
                    mLinearLayout.setVisibility(View.GONE);
                    editMode =false;
                    adapter.notifyDataSetChanged();
                });
    }


    @Override
    protected void initViews() {
        initTopBar();
        adapter = new MyVocaBookAdapter();
        list =new ArrayList<>();
        setEmptyText();
        mListView.setAdapter(adapter);
        mSelectAllCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                for(WordBean bean:list){
                    bean.setChecked(true);
                }
            }else {
                for(WordBean bean:list){
                    bean.setChecked(false);
                }
            }
            adapter.notifyDataSetChanged();
        });
        getData();
    }

    private void initTopBar(){
        mTopBar.setTitle("我的单词本").setGravity(Gravity.CENTER);
        mTopBar.setTitle("我的单词本").setTextColor(Color.parseColor("#FFFFFF"));
        mTopBar.setBackgroundColor(Color.parseColor("#05B6F7"));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
    }
    private void setAnimation(boolean isShow){
        if (!isShow) {
            final TranslateAnimation animation = new TranslateAnimation(
                    TranslateAnimation.RELATIVE_TO_SELF,0,TranslateAnimation.RELATIVE_TO_SELF,0,
                    TranslateAnimation.RELATIVE_TO_SELF,0,TranslateAnimation.RELATIVE_TO_SELF,1);
            animation.setDuration(300L);
            mLinearLayout.postDelayed(() -> {
                mLinearLayout.setVisibility(View.GONE);
                mLinearLayout.startAnimation(animation);
            },300);
        }else {
            final TranslateAnimation animation = new TranslateAnimation(
                    TranslateAnimation.RELATIVE_TO_SELF,0,TranslateAnimation.RELATIVE_TO_SELF,0,
                    TranslateAnimation.RELATIVE_TO_SELF,1,TranslateAnimation.RELATIVE_TO_SELF,0);
            animation.setDuration(300L);
            mLinearLayout.postDelayed(() -> {
            mLinearLayout.setVisibility(View.VISIBLE);
            mLinearLayout.startAnimation(animation);
            },300);
        }
    }
    @SuppressLint("CheckResult")
    private void getData(){
        Observable.create((ObservableOnSubscribe<List<WordBean>>) emitter -> {
            mQuery = ((MyApp)getApplication()).getDaoSession().getWordBeanDao().queryBuilder().build();
            emitter.onNext(mQuery.list());
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wordBeans -> {
                    list.addAll(wordBeans);
                    adapter.notifyDataSetChanged();
                });
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_danciben;
    }


    private void addToPlan() {
        planList = SharedPrefUtil.getGroup(this,"group");
        if(planList == null){
            showToast("暂无分级，去添加吧");
            return;
        }
        String[] items =new String[planList.size()];
        //选择item的事件
        //确认按钮的事件
        Dialog dialog1 = new CBDialogBuilder(this, CBDialogBuilder.DIALOG_STYLE_NORMAL)
                .showConfirmButton(true)
                .setTitle("选择")
                .showIcon(false)
                .setItems(planList.toArray(items), (ItemAdapter, context, dialogbuilder, dialog, position) -> {
                    //选择item的事件
                    MyVocabularyBookActivity.this.position = position;
                }).setButtonClickListener(true, (context, dialog, whichBtn) -> {
                    //确认按钮的事件
                    if (position == -1) {
                        showToast("请选择计划~");
                        return;
                    }
                    insertDataBase();
                }).create();
        dialog1.show();
    }

    @SuppressLint("CheckResult")
    private void insertDataBase() {
        for(WordBean bean :list){
            if(bean.isChecked()){
                bean.setGroupName(planList.get(position));
            }
        }
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
          ((MyApp)getApplication()).getDaoSession().getWordBeanDao().updateInTx(list);
          emitter.onNext(true);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if(aBoolean){
                        showToast("添加到该分级成功");
                        position = -1;
                    }
                });
    }

    private void setEmptyText(){
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        emptyView.setText("单词本空空如也，快去搜索添加吧~");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) mListView.getParent()).addView(emptyView);
        mListView.setEmptyView(emptyView);
    }

    class MyVocaBookAdapter extends BaseAdapter {
        @Override
        public int getCount() { return list.size(); }
        @Override
        public Object getItem(int position) { return list.get(position); }
        @Override
        public long getItemId(int position) { return position; }

        @SuppressLint({"SetTextI18n", "CheckResult"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder viewHolder;
            if(convertView == null){
                convertView = View.inflate(MyVocabularyBookActivity.this, R.layout.item_danci,null);
                viewHolder = new MyViewHolder();
                viewHolder.name = convertView.findViewById(R.id.item_danci_name);
                viewHolder.symbol =convertView.findViewById(R.id.item_danci_symbol);
                viewHolder.checkBox = convertView.findViewById(R.id.item_danci_check);
                viewHolder.del = convertView.findViewById(R.id.item_danci_del);
                viewHolder.meaning = convertView.findViewById(R.id.item_danci_meaning);
                viewHolder.item_layout = convertView.findViewById(R.id.item_danci_item);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            final WordBean bean = list.get(position);
            viewHolder.name.setText(bean.getName());
            viewHolder.symbol.setText("[英]" + bean.getEn_sympol() + "/[美]" + bean.getAm_symbol());
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < bean.getParts().size(); i++) {
                builder.append(bean.getParts().get(i).getPart()).append("  ").append(bean.getParts().get(i).getMeans()).append("\r\n");
            }
            viewHolder.meaning.setText(builder.toString());
            viewHolder.checkBox.setChecked(bean.isChecked());
            if(editMode){
                viewHolder.checkBox.setVisibility(View.VISIBLE);
            }else {
                viewHolder.checkBox.setVisibility(View.GONE);
            }
            viewHolder.item_layout.setOnClickListener(v -> {
                if(bean.isChecked()){
                    bean.setChecked(false);
                   // viewHolder.item_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    viewHolder.checkBox.setChecked(false);
                }else {
                    bean.setChecked(true);
                    //viewHolder.item_layout.setBackgroundColor(Color.parseColor("#CCCCCCCC"));
                    viewHolder.checkBox.setChecked(true);
                }
                notifyDataSetChanged();
            });
            viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->{
                bean.setChecked(isChecked);
                notifyDataSetChanged();
//                if(isChecked){
//                    viewHolder.item_layout.setBackgroundColor(Color.parseColor("#CCCCCCCC"));
//                }else {
//                    viewHolder.item_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                }
            });
            viewHolder.del.setOnClickListener(v -> Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                ((MyApp)getApplication()).getDaoSession().getWordBeanDao().delete(bean);
                emitter.onNext(true);
                emitter.onComplete();
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        showToast("移除成功");
                        list.remove(bean);
                        notifyDataSetChanged();
                    }));

            if(bean.isChecked()){
                viewHolder.item_layout.setBackgroundColor(Color.parseColor("#CCCCCCCC"));
            }else {
                viewHolder.item_layout.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            return convertView;
        }

        class MyViewHolder{
            LinearLayout item_layout;
            TextView name,symbol,meaning,del;
            CheckBox checkBox;
        }

    }


    @Override
    public void onBackPressed() {
        if(editMode){
            mLinearLayout.setVisibility(View.GONE);
            editMode =false;
            adapter.notifyDataSetChanged();
        }else{
            super.onBackPressed();
        }
    }
}
