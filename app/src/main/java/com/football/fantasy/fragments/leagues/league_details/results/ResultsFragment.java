package com.football.fantasy.fragments.leagues.league_details.results;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;

public class ResultsFragment extends BaseMvpFragment<IResultsView, IResultsPresenter<IResultsView>> implements IResultsView {
    static final String TAG = ResultsFragment.class.getSimpleName();

    static final String KEY_LEAGUE = "key_leagues";

    public static ResultsFragment newInstance(LeagueResponse leagueResponse) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_LEAGUE, leagueResponse);

        ResultsFragment fragment = new ResultsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LeagueResponse league;

    @Override
    public int getResourceId() {
        return R.layout.league_detail_ranking_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void getDataFromBundle() {
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    void initView() {
    }

    @Override
    public IResultsPresenter<IResultsView> createPresenter() {
        return new ResultsPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }
}
