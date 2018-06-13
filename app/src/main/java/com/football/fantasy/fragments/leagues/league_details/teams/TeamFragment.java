package com.football.fantasy.fragments.leagues.league_details.teams;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.bon.share_preferences.AppPreferences;
import com.bon.util.DateTimeUtils;
import com.bon.util.DialogUtils;
import com.football.adapters.TeamAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.requests.LeagueRequest;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import java8.util.stream.StreamSupport;

public class TeamFragment extends BaseMainMvpFragment<ITeamView, ITeamPresenter<ITeamView>> implements ITeamView {
    static final String TAG = TeamFragment.class.getSimpleName();
    static final String KEY_LEAGUE = "LEAGUE";
    static final String KEY_LEAGUE_TYPE = "league_type";

    public static TeamFragment newInstance(LeagueResponse leagueId, String leagueType) {
        TeamFragment fragment = new TeamFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, leagueId);
        bundle.putString(KEY_LEAGUE_TYPE, leagueType);
        fragment.setArguments(bundle);
        return fragment;
    }


    @BindView(R.id.tvTeamSetupLabel)
    ExtTextView tvTeamSetupLabel;
    @BindView(R.id.tvTeamSetupTime)
    ExtTextView tvTeamSetupTime;
    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    LeagueResponse leagueResponse;
    String leagueType;

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
        leagueResponse = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        leagueType = getArguments().getString(KEY_LEAGUE_TYPE);
    }

    void initView() {
        try {
            displayTime();
            teamAdapter = new TeamAdapter(mActivity, new ArrayList<>(), leagueResponse, teamResponseDetails -> {
            }, removeTeamResponse -> {
                DialogUtils.confirmBox(mActivity,
                        getString(R.string.app_name),
                        String.format(getString(R.string.remove_team_message), removeTeamResponse.getName()),
                        getString(R.string.yes),
                        getString(R.string.no), (dialogInterface, i) -> {
                            presenter.removeTeam(leagueResponse.getId(), removeTeamResponse.getId());
                        });
            });

            rvRecyclerView.init(mActivity, teamAdapter)
                    .setOnExtRefreshListener(() -> {
                        Optional.from(rvRecyclerView).doIfPresent(rv -> rv.clearItems());
                        getTeams();
                    });
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void displayTime() {
        tvTeamSetupLabel.setText(DateTimeUtils.convertCalendarToString(leagueResponse.getTeamSetUpCalendar(), Constant.FORMAT_DATE_TIME));

        if (leagueResponse.getGameplayOption().equalsIgnoreCase(LeagueRequest.GAMEPLAY_OPTION_TRANSFER)) {
            tvTeamSetupLabel.setText(R.string.team_setup_time);
            tvTeamSetupTime.setText(DateTimeUtils.convertCalendarToString(leagueResponse.getTeamSetUpCalendar(), Constant.FORMAT_DATE_TIME));
        } else {
            tvTeamSetupTime.setText(R.string.start_time);
            tvTeamSetupTime.setText(DateTimeUtils.convertCalendarToString(leagueResponse.getStartAtCalendar(), Constant.FORMAT_DATE_TIME));
        }
    }

    void getTeams() {
        presenter.getTeams(leagueResponse.getId());
    }

    @NonNull
    @Override
    public ITeamPresenter<ITeamView> createPresenter() {
        return new TeamDataPresenter(getAppComponent());
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            rv.clearItems();
            rv.addNewItems(teams);

            UserResponse userResponse = AppPreferences.getInstance(mActivity).getObject(Constant.KEY_USER, UserResponse.class);
            if (userResponse == null) return;

            // auto create team if owner or joined
            if (leagueResponse.getOwner() || leagueResponse.getIsJoined()) {
                if (teams == null || teams.size() <= 0) {
                    createTeam();
                } else {
                    if (StreamSupport
                            .stream(teams)
                            .filter(n ->
                                    n.getUser() != null && n.getUser().getId().equals(userResponse.getId()))
                            .count() <= 0) {
                        createTeam();
                    }
                }
            }
        });
    }

    void createTeam() {
        AloneFragmentActivity.with(mActivity)
                .parameters(SetupTeamFragment.newBundle(
                        leagueResponse,
                        null,
                        mActivity.getTitleToolBar().getText().toString(),
                        LeagueDetailFragment.MY_LEAGUES))
                .start(SetupTeamFragment.class);
        mActivity.finish();
    }

    @Override
    public void removeSuccess(int teamId) {
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
