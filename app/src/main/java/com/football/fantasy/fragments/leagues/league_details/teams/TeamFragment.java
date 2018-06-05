package com.football.fantasy.fragments.leagues.league_details.teams;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.util.DialogUtils;
import com.football.adapters.TeamAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.TeamResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TeamFragment extends BaseMainMvpFragment<ITeamView, ITeamPresenter<ITeamView>> implements ITeamView {
    static final String KEY_LEAGUE_ID = "LEAGUE_ID";

    public static TeamFragment newInstance(int leagueId) {
        TeamFragment fragment = new TeamFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.tvTeamSetupTime)
    ExtTextView tvTeamSetupTime;
    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    int leagueId;

    TeamAdapter teamAdapter;

    @Override
    public int getResourceId() {
        return R.layout.team_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        getTeams();
    }

    void getDataFromBundle() {
        if (getArguments() == null) return;
        leagueId = getArguments().getInt(KEY_LEAGUE_ID);
    }

    void initView() {
        teamAdapter = new TeamAdapter(mActivity, new ArrayList<>(), teamResponseDetails -> {
        }, removeTeamResponse -> {
            DialogUtils.confirmBox(mActivity,
                    getString(R.string.app_name),
                    String.format(getString(R.string.remove_team_message), removeTeamResponse.getName()),
                    getString(R.string.yes),
                    getString(R.string.no), (dialogInterface, i) -> {
                        presenter.removeTeam(leagueId, removeTeamResponse.getId());
                    });
        });

        rvRecyclerView.init(mActivity, teamAdapter)
                .setOnExtRefreshListener(() -> {
                    Optional.from(rvRecyclerView).doIfPresent(rv -> rv.clearItems());
                    getTeams();
                });
    }

    void getTeams() {
        presenter.getTeams(leagueId);
    }

    @NonNull
    @Override
    public ITeamPresenter<ITeamView> createPresenter() {
        return new TeamDataPresenter(getAppComponent());
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> rv.addNewItems(teams));
    }

    @Override
    public void removeSuccess(int leagueId) {
        getTeams();
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            if (isLoading) {
                rvRecyclerView.startLoading(true);
            } else {
                rvRecyclerView.stopLoading(true);
            }
        });
    }
}
