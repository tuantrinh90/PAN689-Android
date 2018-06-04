package com.football.fantasy.fragments.leagues.your_team.player_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class PlayerListFragment extends BaseMainMvpFragment<IPlayerListView, IPlayerListPresenter<IPlayerListView>> implements IPlayerListView {
    public static PlayerListFragment newInstance() {
        return new PlayerListFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.player_list_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {

    }

    @Override
    public IPlayerListPresenter<IPlayerListView> createPresenter() {
        return new PlayerListPresenter(getAppComponent());
    }
}
