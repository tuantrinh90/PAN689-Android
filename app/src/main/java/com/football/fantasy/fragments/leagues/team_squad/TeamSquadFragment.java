package com.football.fantasy.fragments.leagues.team_squad;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class TeamSquadFragment extends BaseMainMvpFragment<ITeamSquadView, ITeamSquadPresenter<ITeamSquadView>> implements ITeamSquadView {
    @Override
    public int getResourceId() {
        return R.layout.team_squad_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public ITeamSquadPresenter<ITeamSquadView> createPresenter() {
        return new TeamSquadPresenter(getAppComponent());
    }
}
