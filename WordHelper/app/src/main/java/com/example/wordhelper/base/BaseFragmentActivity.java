package com.example.wordhelper.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import butterknife.ButterKnife;

public abstract class BaseFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView();
    }
    abstract protected void initView();
    abstract protected int getLayoutId();
    public void showToast(String string){
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
    }
}
