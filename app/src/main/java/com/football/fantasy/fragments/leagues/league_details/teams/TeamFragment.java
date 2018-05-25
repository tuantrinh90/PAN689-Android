package com.football.fantasy.fragments.leagues.league_details.teams;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.DialogUtils;
import com.football.adapters.TeamAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.models.Team;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TeamFragment extends BaseMainMvpFragment<ITeamView, ITeamPresenter<ITeamView>> implements ITeamView {
    public static TeamFragment newInstance() {
        return new TeamFragment();
    }

    @BindView(R.id.tvTeamSetupTime)
    ExtTextView tvTeamSetupTime;
    @BindView(R.id.rvRecyclerView)
    RecyclerView rvRecyclerView;

    List<Team> teams;
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
        teams = new ArrayList<Team>() {{
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team", "Clark Kent", true));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team", "Clark Kent", false));
        }};

        teamAdapter = new TeamAdapter(mActivity, teams, teamDetails -> {

        }, removeTeam -> {
            DialogUtils.confirmBox(mActivity, getString(R.string.app_name), String.format(getString(R.string.remove_team_message), removeTeam.getTeamName()),
                    getString(R.string.yes), getString(R.string.no), (dialogInterface, i) -> {

                    });
        });
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvRecyclerView.setAdapter(teamAdapter);
    }

    @NonNull
    @Override
    public ITeamPresenter<ITeamView> createPresenter() {
        return new TeamDataPresenter(getAppComponent());
    }
}
