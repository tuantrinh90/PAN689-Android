package com.football.fantasy.fragments.your_team.team_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class TeamListFragment extends BaseMainMvpFragment<ITeamListView, ITeamListPresenter<ITeamListView>> implements ITeamListView {
    public static TeamListFragment newInstance(){
        return new TeamListFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.team_list_fragment;
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
    public ITeamListPresenter<ITeamListView> createPresenter() {
        return new TeamListPresenter(getAppComponent());
    }
}
