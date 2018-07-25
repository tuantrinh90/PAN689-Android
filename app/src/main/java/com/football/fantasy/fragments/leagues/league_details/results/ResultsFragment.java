package com.football.fantasy.fragments.leagues.league_details.results;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.football.adapters.ResultsAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.MatchResponse;

import java.util.List;

import butterknife.BindView;

public class ResultsFragment extends BaseMvpFragment<IResultsView, IResultsPresenter<IResultsView>> implements IResultsView {
    static final String TAG = ResultsFragment.class.getSimpleName();

    static final String KEY_LEAGUE = "key_leagues";

    @BindView(R.id.rv_results)
    ExtRecyclerView<MatchResponse> rvResults;

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
        return R.layout.league_detail_results_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();

        getResults();
    }

    void getDataFromBundle() {
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    void initView() {
        ResultsAdapter adapter = new ResultsAdapter(
                getContext(),
                (team, league) -> {

                });
        rvResults
                .adapter(adapter)
                .refreshListener(() -> {
                    rvResults.clear();
                    getResults();
                })
                .build();
    }

    private void getResults() {
        presenter.getMatchResults(league.getId());
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

    @Override
    public void displayMatches(List<MatchResponse> matches) {
        rvResults.addItems(matches);
    }
}
