package com.example.wordhelper.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.wordhelper.R;
import com.example.wordhelper.app.MyApp;
import com.example.wordhelper.base.BaseActivity;
import com.example.wordhelper.bean.PlanCount;
import com.example.wordhelper.bean.WordBean;
import com.example.wordhelper.dao.WordBeanDao;
import com.example.wordhelper.util.SharedPrefUtil;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.zhl.cbdialog.CBDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class MyReviewPlanActivity extends BaseActivity {
    @BindView(R.id.act_plan_lv) ListView mListView;
    @BindView(R.id.topbar) QMUITopBarLayout mTopBar;
    @BindView(R.id.topbar_rightbutton) QMUIAlphaImageButton mTopbarRightButton;
    private List<String> plans;
    private List<PlanCount> planCounts;
    private Random random;
    private PlanAdapter adapter;
    private Dialog mDialog;
    private Dialog mReviewModeDialog;
    private Dialog mDeleteDialog;
    private int review_mode;
    private WordBeanDao mWordBeanDao;
    private Intent mIntent;
    private int position;
    @Override
    protected void initViews() {
        initTopBar();
        random = new Random();
        mWordBeanDao = ((MyApp)getApplication()).getDaoSession().getWordBeanDao();
        addEmptyText();
        mTopbarRightButton.setOnClickListener(v -> {
            if(mDialog != null){
                mDialog.show();
            }
        });
        setDialog();
        switchReviewMode();
        showDeleteDialog();
        mIntent =new Intent(MyReviewPlanActivity.this,MyPracticePlanActivity.class);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            if(planCounts.get(position).getTotal() == 0){
                showToast("快去单词本添加单词吧~");
            }else if(planCounts.get(position).getTotal() == planCounts.get(position).getDone()){
                mDeleteDialog.show();
                this.position =position;
                return;
            }
            mIntent.putExtra("groupName",plans.get(position));
            if(mReviewModeDialog != null && planCounts.get(position).getTotal() != 0){
                mReviewModeDialog.show();
            }

        });
        setUpData();
    }

    private void initTopBar(){
        mTopBar.setTitle("复习计划").setGravity(Gravity.CENTER);
        mTopBar.setTitle("复习计划").setTextColor(Color.parseColor("#FFFFFF"));
        mTopBar.addLeftBackImageButton().setOnClickListener(v -> finish());
        mTopBar.setBackgroundColor(Color.parseColor("#05B6F7"));
    }

    private void deletePlan(){
        List<WordBean> beans = mWordBeanDao.queryBuilder()
                .where(WordBeanDao.Properties.GroupName.eq(plans.get(position)))
                .build()
                .list();
        for(WordBean bean:beans){
            bean.setDone(false);
        }
        mWordBeanDao.updateInTx(beans);
        plans.remove(position);
        planCounts.remove(position);
        SharedPrefUtil.saveGroup(MyReviewPlanActivity.this,"group",plans);
        adapter.notifyDataSetChanged();
        showToast("分级删除成功");
    }
    private void showDeleteDialog(){
        mDeleteDialog = new CBDialogBuilder(this)
                .setTouchOutSideCancelable(true)
                .setTitle("该分级已复习，是否删除？")
                .showCancelButton(true)
                .setCancelButtonText("取消")
                .showConfirmButton(true)
                .setConfirmButtonText("确定")
                .setMessage("")
                .setButtonClickListener(true, (context, dialog, whichBtn) -> {
                    switch (whichBtn){
                        case 0:
                            deletePlan();
                            break;
                        case 1:
                            break;
                            default:
                                break;
                    }
                })
                .create();
    }
    private void switchReviewMode(){
        final String[] itemOptions = new String[]{"中-英模式", "英-中模式"};
        mReviewModeDialog = new CBDialogBuilder(this)
                .setTouchOutSideCancelable(true)
                .showConfirmButton(false)
                .setTitle("选择复习模式")
                .setDialogAnimation(CBDialogBuilder.DIALOG_ANIM_SLID_BOTTOM)
                .setItems(itemOptions, (ItemAdapter, context, dialogbuilder, dialog, position) -> {
                    review_mode = position;
                    dialog.dismiss();
                    mIntent.putExtra("review_mode",review_mode);
                    startActivity(mIntent);
                })
                .create();
    }
    private void setDialog(){
        View mDialogView = View.inflate(this,R.layout.view_et,null);
        final EditText mEditText =  mDialogView.findViewById(R.id.et);
        mDialog = new CBDialogBuilder(this,CBDialogBuilder.DIALOG_STYLE_NORMAL)
                .setTitle("新建").showIcon(false)
                .setButtonClickListener(true,(context, dialog, whichBtn) -> {
                    String name = mEditText.getText().toString().trim();
                    if(name.equals("")){
                        showToast("请输入分级名称");
                        return;
                    }
                    if(plans.contains(name)){
                        showToast("该分级已存在");
                        return;
                    }
                    mEditText.setText("");
                    plans.add(name);
                    SharedPrefUtil.saveGroup(MyReviewPlanActivity.this,"group",plans);
                    planCounts.add(new PlanCount(0,0));
                    adapter.notifyDataSetChanged();
                }).setView(mDialogView).create();
    }
    private void addEmptyText(){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        textView.setText("右上角创建新分级");
        textView.setGravity(Gravity.CENTER);
        textView.setVisibility(View.GONE);
        ((ViewGroup)mListView.getParent()).addView(textView);
        mListView.setEmptyView(textView);
    }

    private void setUpData(){
        List<String> list = SharedPrefUtil.getGroup(this, "group");
        plans = new ArrayList<>();
        planCounts = new ArrayList<>();
        if(list != null){
            plans.addAll(list);
            for(String str:plans){
                long a = mWordBeanDao.queryBuilder()
                        .where(WordBeanDao.Properties.GroupName.eq(str))
                        .buildCount()
                        .count();
                long b = mWordBeanDao.queryBuilder()
                        .where(WordBeanDao.Properties.GroupName.eq(str))
                        .where(WordBeanDao.Properties.Done.eq(true))
                        .buildCount()
                        .count();
                planCounts.add(new PlanCount(a,b));
            }
        }
        adapter= new PlanAdapter();
        mListView.setAdapter(adapter);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_planlist;
    }

    class PlanAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return plans.size();
        }

        @Override
        public Object getItem(int position) {
            return plans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if(convertView == null){
                convertView = View.inflate(MyReviewPlanActivity.this,R.layout.item_plan,null);
                vh = new ViewHolder();
                vh.cv = convertView.findViewById(R.id.item_plan_cv);
                vh.name = convertView.findViewById(R.id.item_plan_name);
                vh.des = convertView.findViewById(R.id.item_plan_des);
                convertView.setTag(vh);
            }else {
                vh = (ViewHolder)convertView.getTag();
            }
            int color = 0xff000000 |random.nextInt(0x00ffffff);
            vh.cv.setBackgroundColor(color);
            vh.name.setText(plans.get(position));
            vh.des.setText("共" + planCounts.get(position).getTotal() + "个，已完成" + planCounts.get(position).getDone() + "个");
            return convertView;
        }

        class ViewHolder{
            TextView name,des;
            CardView cv;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpData();
    }
}
