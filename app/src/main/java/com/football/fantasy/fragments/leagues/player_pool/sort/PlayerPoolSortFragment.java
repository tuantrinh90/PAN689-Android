package com.football.fantasy.fragments.leagues.player_pool.sort;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class PlayerPoolSortFragment extends BaseMainMvpFragment<IPlayerPoolSortView, IPlayerPoolSortPresenter<IPlayerPoolSortView>> implements IPlayerPoolSortView {
    @Override
    public int getResourceId() {
        return R.layout.player_pool_sort_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public IPlayerPoolSortPresenter<IPlayerPoolSortView> createPresenter() {
        return new PlayerPoolSortPresenter(getAppComponent());
    }
}
