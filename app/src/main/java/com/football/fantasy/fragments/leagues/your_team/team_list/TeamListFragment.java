package com.football.fantasy.fragments.leagues.your_team.team_list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.TeamAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.TeamDetailFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TeamListFragment extends BaseMainMvpFragment<ITeamListView, ITeamListPresenter<ITeamListView>> implements ITeamListView {
    static final String KEY_LEAGUE = "LEAGUE";

    @BindView(R.id.tvNumber)
    ExtTextView tvNumber;
    @BindView(R.id.tvTotal)
    ExtTextView tvTotal;
    @BindView(R.id.rvTeam)
    ExtRecyclerView<TeamResponse> rvTeam;

    LeagueResponse league;
    TeamAdapter teamAdapter;

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
    }

    private void getTeams() {
        presenter.getTeams(league.getId());
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        league = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
    }

    void initView() {
        tvNumber.setText(String.valueOf(league.getCurrentNumberOfUser()));
        tvTotal.setText(String.valueOf(league.getNumberOfUser()));

        teamAdapter = new TeamAdapter(
                new ArrayList<>(),
                league.getOwner(),
                team -> { // handle click
                    AloneFragmentActivity.with(this)
                            .parameters(TeamDetailFragment.newBundle(team, league.getId()))
                            .start(TeamDetailFragment.class);
                },
                null);
        rvTeam.adapter(teamAdapter)
                .refreshListener(() -> {
                    Optional.from(rvTeam).doIfPresent(ExtRecyclerView::clear);
                    getTeams();
                })
                .build();
    }

    @NonNull
    @Override
    public ITeamListPresenter<ITeamListView> createPresenter() {
        return new TeamListPresenter(getAppComponent());
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        Optional.from(rvTeam).doIfPresent(rv -> rv.addItems(teams));
    }
}
