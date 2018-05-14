package com.football.fantasy.fragments.leagues;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;

public class LeagueFragment extends BaseMvpFragment<ILeagueView, ILeaguePresenter<ILeagueView>> implements ILeagueView {
    public static LeagueFragment newInstance() {
        return new LeagueFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public ILeaguePresenter<ILeagueView> createPresenter() {
        return new LeaguePresenter(getAppComponent());
    }
}
