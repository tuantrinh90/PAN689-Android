package com.football.fantasy.fragments.match_up.real;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class MatchupRealLeagueFragment extends BaseMainMvpFragment<IMatchupRealLeagueView, IMatchupRealLeaguePresenter<IMatchupRealLeagueView>> implements IMatchupRealLeagueView {
    public static MatchupRealLeagueFragment newInstance() {
        return new MatchupRealLeagueFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.match_up_real_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @NonNull
    @Override
    public IMatchupRealLeaguePresenter<IMatchupRealLeagueView> createPresenter() {
        return new MatchupRealDataLeaguePresenter(getAppComponent());
    }
}
