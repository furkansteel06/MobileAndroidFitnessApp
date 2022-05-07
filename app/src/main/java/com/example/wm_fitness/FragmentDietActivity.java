package com.example.wm_fitness;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.wm_fitness.Utils.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class FragmentDietActivity extends Fragment {

    ViewPager vp_pages;
    TabLayout tbl_pages;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diet,container,false);

        bindViews(view );

        PagerAdapter pagerAdapter=new FragmentAdapter(getFragmentManager());
        vp_pages.setAdapter(pagerAdapter);
        tbl_pages.setupWithViewPager(vp_pages);

        return view;
    }

    public void bindViews(View v){
        vp_pages = (ViewPager) view.findViewById(R.id.vp_pages);
        tbl_pages = (TabLayout) view.findViewById(R.id.tbl_pages);
    }
}
