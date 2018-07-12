package com.football.fantasy.fragments.match_up.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.MatchupLeagueAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.MyMatchResponse;

import java.util.List;

import butterknife.BindView;

public class MatchupMyLeagueFragment extends BaseMainMvpFragment<IMatchupMyLeagueView, IMatchupMyLeaguePresenter<IMatchupMyLeagueView>> implements IMatchupMyLeagueView {
    public static MatchupMyLeagueFragment newInstance() {
        return new MatchupMyLeagueFragment();
    }

    @BindView(R.id.rvMyLeague)
    ExtRecyclerView<MyMatchResponse> rvMyLeague;

    private int page = 1;

    @Override
    public int getResourceId() {
        return R.layout.match_up_my_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
        getMatchResults();
    }

    private void initView() {
        MatchupLeagueAdapter adapter = new MatchupLeagueAdapter();
        // init recyclerView
        rvMyLeague
                .adapter(adapter)
                .refreshListener(this::refresh)
                .loadMoreListener(() -> {
                    page++;
                    getMatchResults();
                })
                .build();
    }

    private void refresh() {
        page = 1;
        rvMyLeague.clear();
        getMatchResults();
    }

    private void getMatchResults() {
        presenter.getMatchResults(page);
    }


    @NonNull
    @Override
    public IMatchupMyLeaguePresenter<IMatchupMyLeagueView> createPresenter() {
        return new MatchupMyDataLeaguePresenter(getAppComponent());
    }

    @Override
    public void displayMatches(List<MyMatchResponse> matches) {
        rvMyLeague.addItems(matches);
    }
}
