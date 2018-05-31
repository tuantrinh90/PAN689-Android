package com.football.fantasy.fragments.leagues.my_leagues;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.football.adapters.LeaguesAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;

import java.util.List;

import butterknife.BindView;

public class MyLeagueFragment extends BaseMainMvpFragment<IMyLeagueView, IMyLeaguePresenter<IMyLeagueView>> implements IMyLeagueView {
    public static MyLeagueFragment newInstance() {
        return new MyLeagueFragment();
    }

    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    List<LeagueResponse> leagueResponses;
    LeaguesAdapter leaguesAdapter;

    @Override
    public int getResourceId() {
        return R.layout.my_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
        // leagueResponses
        leaguesAdapter = new LeaguesAdapter(mActivity, leagueResponses, details -> {

        }, approve -> {

        }, reject -> {

        }, join -> {

        });
        rvRecyclerView.init(mActivity, leaguesAdapter);
    }

    @NonNull
    @Override
    public IMyLeaguePresenter<IMyLeagueView> createPresenter() {
        return new MyLeagueDataPresenter(getAppComponent());
    }
}
