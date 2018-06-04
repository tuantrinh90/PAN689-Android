package com.football.fantasy.fragments.leagues.player_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class PlayerDetailFragment extends BaseMainMvpFragment<IPlayerDetailView, IPlayerDetailPresenter<IPlayerDetailView>> implements IPlayerDetailView {
    @Override
    public int getResourceId() {
        return R.layout.player_detail_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public IPlayerDetailPresenter<IPlayerDetailView> createPresenter() {
        return new PlayerDetailPresenter(getAppComponent());
    }
}
