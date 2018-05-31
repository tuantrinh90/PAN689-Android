package com.football.fantasy.fragments.leagues.league_details.teams;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.util.DialogUtils;
import com.football.adapters.TeamAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.TeamResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TeamFragment extends BaseMainMvpFragment<ITeamView, ITeamPresenter<ITeamView>> implements ITeamView {

    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";

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

    private int leagueId;

    List<TeamResponse> teamResponses = new ArrayList<>();
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

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        leagueId = bundle.getInt(KEY_LEAGUE_ID);
    }

    void initView() {
        teamAdapter = new TeamAdapter(mActivity, teamResponses, teamResponseDetails -> {
        }, removeTeamResponse -> {
            DialogUtils.confirmBox(mActivity, getString(R.string.app_name), String.format(getString(R.string.remove_team_message), removeTeamResponse.getName()),
                    getString(R.string.yes), getString(R.string.no), (dialogInterface, i) -> {
                        showMessage("Chưa làm");
                    });
        });

        rvRecyclerView.init(mActivity, teamAdapter)
                .setOnExtRefreshListener(() -> {
                    getTeams();
                });
    }

    private void getTeams() {
        presenter.getTeams(leagueId);
    }

    @NonNull
    @Override
    public ITeamPresenter<ITeamView> createPresenter() {
        return new TeamDataPresenter(getAppComponent());
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        teamResponses.clear();
        teamResponses.addAll(teams);
        teamAdapter.notifyDataSetChanged();
    }
}
