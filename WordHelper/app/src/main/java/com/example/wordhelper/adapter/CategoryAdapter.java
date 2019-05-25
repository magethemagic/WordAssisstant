package com.example.wordhelper.adapter;

import com.example.wordhelper.activity.MainActivity;
import com.example.wordhelper.fragment.MyBookFragment;
import com.example.wordhelper.fragment.RecommendFragment;
import com.example.wordhelper.fragment.SearchFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CategoryAdapter extends FragmentPagerAdapter {

    public CategoryAdapter(FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position ==1){
            return  new SearchFragment();
        }else if(position ==0){
            return new RecommendFragment();
        }else{
            return new MyBookFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
