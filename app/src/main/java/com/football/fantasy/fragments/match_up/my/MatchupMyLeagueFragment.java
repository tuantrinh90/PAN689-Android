package com.football.fantasy.fragments.match_up.my;

import android.support.annotation.NonNull;

import com.football.adapters.MatchupLeagueAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.TeamDetailFragment;
import com.football.models.responses.MatchResponse;

import java.util.List;

import butterknife.BindView;

public class MatchupMyLeagueFragment extends BaseMainMvpFragment<IMatchupMyLeagueView, IMatchupMyLeaguePresenter<IMatchupMyLeagueView>> implements IMatchupMyLeagueView {
    public static MatchupMyLeagueFragment newInstance() {
        return new MatchupMyLeagueFragment();
    }

    @BindView(R.id.rvMyLeague)
    ExtRecyclerView<MatchResponse> rvMyLeague;

    private int page;

    @Override
    public int getResourceId() {
        return R.layout.match_up_my_league_fragment;
    }

    @Override
    protected void initialized() {
        super.initialized();
        initView();
        page = 1;
        getMatchResults();
        rvMyLeague.startLoading();
    }

    private void initView() {
        MatchupLeagueAdapter adapter = new MatchupLeagueAdapter(
                getContext(),
                (team, league) -> {
                    AloneFragmentActivity.with(this)
                            .parameters(TeamDetailFragment.newBundle(team, league))
                            .start(TeamDetailFragment.class);
                });
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
    public void displayMatches(List<MatchResponse> matches) {
        rvMyLeague.addItems(matches);
    }

    @Override
    public void stopLoading() {
        rvMyLeague.stopLoading();
    }
}
