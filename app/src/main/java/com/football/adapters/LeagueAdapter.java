package com.football.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.football.common.fragments.BaseMvpFragment;

import java.util.List;

public class LeagueAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = LeagueAdapter.class.getSimpleName();
    private final List<BaseMvpFragment> carousels;

    public LeagueAdapter(FragmentManager fm, List<BaseMvpFragment> carousels) {
        super(fm);
        this.carousels = carousels;
    }

    @Override
    public Fragment getItem(int position) {
        return carousels.get(position);
    }

    @Override
    public int getCount() {
        return carousels.size();
    }
}
