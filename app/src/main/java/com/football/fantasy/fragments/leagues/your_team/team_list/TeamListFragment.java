package com.football.fantasy.fragments.leagues.your_team.team_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.TeamAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TeamListFragment extends BaseMainMvpFragment<ITeamListView, ITeamListPresenter<ITeamListView>> implements ITeamListView {

    static final String KEY_LEAGUE_ID = "LEAGUE_ID";
    static final String KEY_LEAGUE = "LEAGUE";

    @BindView(R.id.tvTeamSetupTime)
    ExtTextView tvTeamSetupTime;
    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    LeagueResponse leagueResponse;
    TeamAdapter teamAdapter;
    int leagueId;

    public static TeamListFragment newInstance(Integer leagueId, LeagueResponse leagueResponse) {
        TeamListFragment fragment = new TeamListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        bundle.putSerializable(KEY_LEAGUE, leagueResponse);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_list_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        getTeams();
    }

    private void getTeams() {
        presenter.getTeams(leagueId);
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        leagueId = bundle.getInt(KEY_LEAGUE_ID);
        leagueResponse = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
    }

    void initView() {
        teamAdapter = new TeamAdapter(mActivity, new ArrayList<>(), leagueResponse, null, null);
        rvRecyclerView.init(mActivity, teamAdapter)
                .setOnExtRefreshListener(() -> {
                    Optional.from(rvRecyclerView).doIfPresent(rv -> rv.clearItems());
                    getTeams();
                });
    }

    @NonNull
    @Override
    public ITeamListPresenter<ITeamListView> createPresenter() {
        return new TeamListPresenter(getAppComponent());
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> rv.addNewItems(teams));
    }
}
