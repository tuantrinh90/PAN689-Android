package com.football.fantasy.fragments.leagues.team_statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class TeamStatisticFragment extends BaseMainMvpFragment<ITeamStatisticView, ITeamStatisticPresenter<ITeamStatisticView>> implements ITeamStatisticView {
    @Override
    public int getResourceId() {
        return R.layout.team_statistic_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public ITeamStatisticPresenter<ITeamStatisticView> createPresenter() {
        return new TeamStatisticPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.league_details;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }
}
