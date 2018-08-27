package com.football.fantasy.fragments.leagues.your_team.draft_teamlist.draft_teams;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.DraftTeamAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;

public class DraftTeamsFragment extends BaseMvpFragment<IDraftTeamsView, IDraftTeamsPresenter<IDraftTeamsView>> implements IDraftTeamsView {
    private static final String TAG = DraftTeamsFragment.class.getSimpleName();

    static final String KEY_LEAGUE = "LEAGUE";

    @BindView(R.id.rv_team)
    ExtRecyclerView<TeamResponse> rvTeam;

    private LeagueResponse league;

    public static DraftTeamsFragment newInstance(LeagueResponse league) {
        DraftTeamsFragment fragment = new DraftTeamsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.draft_teams_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        initView();
        getTeams();
        registerBus();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        }
    }

    void initView() {
        DraftTeamAdapter adapter = new DraftTeamAdapter(
                getContext(),
                team -> {

                },
                team -> {

                });
        rvTeam.adapter(adapter)
                .refreshListener(this::refresh)
                .build();
    }

    private void refresh() {
        rvTeam.clear();
        rvTeam.startLoading();
        getTeams();
    }

    private void getTeams() {
        presenter.getTeams(league.getId());
    }

    @NonNull
    @Override
    public IDraftTeamsPresenter<IDraftTeamsView> createPresenter() {
        return new DraftTeamsPresenter(getAppComponent());
    }

    private void registerBus() {
    }


    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
        }
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        rvTeam.addItems(teams);
    }
}
