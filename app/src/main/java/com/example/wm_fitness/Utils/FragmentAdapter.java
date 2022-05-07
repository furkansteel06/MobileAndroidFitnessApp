package com.example.wm_fitness.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.wm_fitness.FragmentAboutActivity;
import com.example.wm_fitness.FragmentAlarmActivity;
import com.example.wm_fitness.FragmentNoSporDiet;
import com.example.wm_fitness.FragmentNutritionActivity;
import com.example.wm_fitness.FragmentSporDiet;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    public FragmentAdapter(FragmentManager fm) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentSporDiet();
            case 1:
                return new FragmentNoSporDiet();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            //
            //Your tab titles
            //
            case 0:
                return "Sporlu";
            case 1:
                return "Sporsuz";
            default:
                return null;
        }
    }
}
