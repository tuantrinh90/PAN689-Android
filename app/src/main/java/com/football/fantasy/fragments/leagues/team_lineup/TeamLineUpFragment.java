package com.football.fantasy.fragments.leagues.team_lineup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class TeamLineUpFragment extends BaseMainMvpFragment<ITeamLineUpView, ITeamLineUpPresenter<ITeamLineUpView>> implements ITeamLineUpView {
    @Override
    public int getResourceId() {
        return R.layout.team_lineup_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public ITeamLineUpPresenter<ITeamLineUpView> createPresenter() {
        return new TeamLineUpPresenter(getAppComponent());
    }
}
