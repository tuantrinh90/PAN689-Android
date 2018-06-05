package com.football.fantasy.fragments.leagues.player_pool.display;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class PlayerPoolDisplayFragment extends BaseMainMvpFragment<IPlayerPoolDisplayView, IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView>> implements IPlayerPoolDisplayView {
    @Override
    public int getResourceId() {
        return R.layout.player_pool_display_fragment;
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
