package com.football.fantasy.fragments.leagues.your_team.team_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.TeamListAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.events.TeamEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_preview.LineupPreviewFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;

public class TeamListFragment extends BaseMvpFragment<ITeamListView, ITeamListPresenter<ITeamListView>> implements ITeamListView {
    static final String KEY_LEAGUE = "LEAGUE";

    @BindView(R.id.tvNumber)
    ExtTextView tvNumber;
    @BindView(R.id.tvTotal)
    ExtTextView tvTotal;
    @BindView(R.id.rvTeam)
    ExtRecyclerView<TeamResponse> rvTeam;

    LeagueResponse league;
    TeamListAdapter teamAdapter;

    public static TeamListFragment newInstance(LeagueResponse leagueResponse) {
        TeamListFragment fragment = new TeamListFragment();
        Bundle bundle = new Bundle();
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

        registerEvent();
    }

    private void getTeams() {
        presenter.getTeams(league.getId());
    }

    private void refresh() {
        rvTeam.clear();
        rvTeam.startLoading();
        getTeams();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        league = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
    }

    void initView() {
        tvNumber.setText(String.valueOf(league.getCurrentNumberOfUser()));
        tvTotal.setText(String.valueOf(league.getNumberOfUser()));

        teamAdapter = new TeamListAdapter(
                getContext(),
                league.getOwner(),
                team -> { // handle click
                    LineupPreviewFragment.start(this, team, LeagueResponse.GAMEPLAY_OPTION_TRANSFER);
                },
                null);
        rvTeam.adapter(teamAdapter)
                .refreshListener(this::refresh)
                .build();
    }

    void registerEvent() {
        // action add click onEvent PlayerList
        onEvent(TeamEvent.class, event -> refresh());
    }

    @NonNull
    @Override
    public ITeamListPresenter<ITeamListView> createPresenter() {
        return new TeamListPresenter(getAppComponent());
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
            rvTeam.stopLoading();
        }
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        Optional.from(rvTeam).doIfPresent(rv -> rv.addItems(teams));
    }
}
