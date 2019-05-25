package com.example.wordhelper.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wordhelper.R;
import com.example.wordhelper.activity.LocalDictionaryActivity;
import com.example.wordhelper.activity.MyReviewPlanActivity;
import com.example.wordhelper.activity.MyVocabularyBookActivity;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyBookFragment extends Fragment {
    @BindView(R.id.topbar)
    QMUITopBarLayout mQmuiTopBarLayout;
    @OnClick(R.id.frag_my_danciben) void openIntentVocaBook(){
        startActivity(new Intent(getActivity(), MyVocabularyBookActivity.class));
        // Toast.makeText(getActivity(),"1",Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.frag_my_dict) void openIntentMyDic(){
        startActivity(new Intent(getActivity(), LocalDictionaryActivity.class));
    }
    @OnClick(R.id.frag_my_plan)void openIntentMyPlan(){
        startActivity(new Intent(getActivity(), MyReviewPlanActivity.class));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View rootView = inflater.inflate(R.layout.frag_wode,null);
        ButterKnife.bind(this,rootView);
        initTopBar();
        return rootView;
    }

    private void initTopBar(){
        mQmuiTopBarLayout.setTitle("复习计划").setGravity(Gravity.CENTER);
        mQmuiTopBarLayout.setTitle("复习计划").setTextColor(Color.parseColor("#FFFFFF"));
        mQmuiTopBarLayout.setBackgroundColor(Color.parseColor("#05B6F7"));
    }
}
