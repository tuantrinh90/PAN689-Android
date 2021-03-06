package com.football.fantasy.fragments.match_up.my;

import android.support.annotation.NonNull;
import android.util.Log;

import com.football.adapters.MatchupLeagueAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.fantasy.fragments.leagues.team_details.TeamDetailFragment;
import com.football.models.responses.MatchResponse;
import com.football.utilities.SocketEventKey;

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
        registerSocket();
    }

    @Override
    public void onDestroyView() {
        getAppContext().off(SocketEventKey.EVENT_MY_MATCH_RESULTS);
        super.onDestroyView();
    }

    private void initView() {
        MatchupLeagueAdapter adapter = new MatchupLeagueAdapter(
                getContext(),
                match -> {
                    AloneFragmentActivity.with(this)
                            .parameters(LeagueDetailFragment.newBundle(getString(R.string.match_up), match.getLeague().getId(), match.getRound()))
                            .start(LeagueDetailFragment.class);
                },
                (team, league) -> {
                    AloneFragmentActivity.with(this)
                            .parameters(TeamDetailFragment.newBundle(getString(R.string.match_up), team.getId(), league))
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

    private void registerSocket() {
        getAppContext().getSocket().on(SocketEventKey.EVENT_MY_MATCH_RESULTS, args -> {
            Log.d(SocketEventKey.EVENT_MY_MATCH_RESULTS, "registerSocket: ");
            mActivity.runOnUiThread(this::refresh);
        });
    }

    @NonNull
    @Override
    public IMatchupMyLeaguePresenter<IMatchupMyLeagueView> createPresenter() {
        return new MatchupMyDataLeaguePresenter(getAppComponent());
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
            rvMyLeague.stopLoading();
        }
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
