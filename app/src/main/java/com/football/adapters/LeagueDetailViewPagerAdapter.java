package com.football.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.football.common.fragments.BaseMvpFragment;

import java.util.List;

public class LeagueDetailViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseMvpFragment> fragments;

    public LeagueDetailViewPagerAdapter(FragmentManager fm, List<BaseMvpFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void remove(int index) {
        fragments.remove(index);
    }

    public void add(BaseMvpFragment fragment) {
        fragments.add(fragment);
    }
}
