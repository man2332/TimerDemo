package com.example.timerdemo;

import android.util.Log;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TimerPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public TimerPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        Log.d("ttag", "getItem: CHANGED ITEM ADAPTER");
        return this.fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.fragmentList.size();
    }

    public void stopTimers(int position){
        Log.d("ttag", "stopTimers: TIMERPAGERADAPTER: timer stopping at pos: "+position);
        TimerFragment fragment = (TimerFragment) fragmentList.get(position);
        fragment.stopTimer();
    }
}
