package com.football.fantasy.fragments.match_up.my;

import android.support.annotation.NonNull;

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
    protected void initialized() {
        super.initialized();
        initView();
        getMatchResults();
        rvMyLeague.startLoading();
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

    @Override
    public void stopLoading() {
        rvMyLeague.stopLoading();
    }
}
