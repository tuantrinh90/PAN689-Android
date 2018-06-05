package com.football.fantasy.fragments.leagues.player_pool.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_pool.display.IPlayerPoolDisplayPresenter;
import com.football.fantasy.fragments.leagues.player_pool.display.IPlayerPoolDisplayView;
import com.football.fantasy.fragments.leagues.player_pool.display.PlayerPoolDisplayPresenter;

public class PlayerPoolFilterFragment extends BaseMainMvpFragment<IPlayerPoolDisplayView, IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView>> implements IPlayerPoolDisplayView {
    @Override
    public int getResourceId() {
        return R.layout.player_pool_filter_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView> createPresenter() {
        return new PlayerPoolDisplayPresenter(getAppComponent());
    }
}
