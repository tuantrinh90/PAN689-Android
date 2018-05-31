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

import java.util.List;

import butterknife.BindView;

public class TeamFragment extends BaseMainMvpFragment<ITeamView, ITeamPresenter<ITeamView>> implements ITeamView {
    public static TeamFragment newInstance() {
        return new TeamFragment();
    }

    @BindView(R.id.tvTeamSetupTime)
    ExtTextView tvTeamSetupTime;
    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    List<TeamResponse> teamResponses;
    TeamAdapter teamAdapter;

    @Override
    public int getResourceId() {
        return R.layout.team_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
        teamAdapter = new TeamAdapter(mActivity, teamResponses, teamResponseDetails -> {
        }, removeTeamResponse -> {
            DialogUtils.confirmBox(mActivity, getString(R.string.app_name), String.format(getString(R.string.remove_team_message), removeTeamResponse.getName()),
                    getString(R.string.yes), getString(R.string.no), (dialogInterface, i) -> {

                    });
        });

        rvRecyclerView.init(mActivity, teamAdapter);
    }

    @NonNull
    @Override
    public ITeamPresenter<ITeamView> createPresenter() {
        return new TeamDataPresenter(getAppComponent());
    }
}
