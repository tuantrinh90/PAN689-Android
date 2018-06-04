package com.football.fantasy.fragments.leagues.my_supper_team;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class MySupperTeamFragment extends BaseMainMvpFragment<IMySupperTeamView, IMySupperTeamPresenter<IMySupperTeamView>> implements IMySupperTeamView {
    @Override
    public int getResourceId() {
        return R.layout.my_supper_team_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public IMySupperTeamPresenter<IMySupperTeamView> createPresenter() {
        return new MySupperTeamPresenter(getAppComponent());
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
