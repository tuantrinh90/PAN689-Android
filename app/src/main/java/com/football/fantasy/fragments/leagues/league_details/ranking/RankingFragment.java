package com.football.fantasy.fragments.leagues.league_details.ranking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.football.adapters.RankingAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.RankingResponse;

import java.util.List;

import butterknife.BindView;

public class RankingFragment extends BaseMvpFragment<IRankingView, IRankingPresenter<IRankingView>> implements IRankingView {
    static final String TAG = RankingFragment.class.getSimpleName();

    static final String KEY_LEAGUE = "key_leagues";

    @BindView(R.id.rv_ranking)
    ExtRecyclerView<RankingResponse> rvRanking;

    private LeagueResponse league;

    public static RankingFragment newInstance(LeagueResponse leagueResponse) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_LEAGUE, leagueResponse);

        RankingFragment fragment = new RankingFragment();
        fragment.setArguments(args);
        return fragment;
    }

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

        getRankings();
    }

    void getDataFromBundle() {
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    void initView() {

        //recyclerView
        RankingAdapter adapter = new RankingAdapter(getContext());
        rvRanking
                .adapter(adapter)
                .refreshListener(() -> {
                    rvRanking.clear();
                    rvRanking.startLoading();
                    getRankings();
                })
                .build();
    }

    private void getRankings() {
        presenter.getRanking(league.getId());
    }

    @Override
    public IRankingPresenter<IRankingView> createPresenter() {
        return new RankingPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (isLoading) {
            rvRanking.startLoading();
        } else {
            rvRanking.stopLoading();
        }
    }

    @Override
    public void displayRanking(List<RankingResponse> rankingList) {
        rvRanking.addItems(rankingList);
    }
}
