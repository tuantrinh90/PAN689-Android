package com.football.fantasy.fragments.leagues.league_details.teams;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.logger.Logger;
import com.bon.util.DateTimeUtils;
import com.bon.util.DialogUtils;
import com.football.adapters.TeamAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.events.TeamEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.fantasy.fragments.leagues.team_details.TeamDetailFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import java.util.List;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

public class TeamFragment extends BaseMvpFragment<ITeamView, ITeamPresenter<ITeamView>> implements ITeamView {
    private static final String TAG = TeamFragment.class.getSimpleName();
    private static final String KEY_LEAGUE = "LEAGUE";
    private static final String KEY_LEAGUE_TYPE = "league_type";

    public static TeamFragment newInstance(LeagueResponse leagueId, String leagueType) {
        TeamFragment fragment = new TeamFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, leagueId);
        bundle.putString(KEY_LEAGUE_TYPE, leagueType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.tvTimeLabel)
    ExtTextView tvTimeLabel;
    @BindView(R.id.tvTime)
    ExtTextView tvTime;
    @BindView(R.id.rvTeam)
    ExtRecyclerView<TeamResponse> rvTeam;

    LeagueResponse league;
    String leagueType;

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
        registerEvent();
    }

    void getDataFromBundle() {
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        leagueType = getArguments().getString(KEY_LEAGUE_TYPE);
    }

    void registerEvent() {
        // action add click on PlayerList
        mCompositeDisposable.add(bus.ofType(TeamEvent.class)
                .subscribeWith(new DisposableObserver<TeamEvent>() {
                    @Override
                    public void onNext(TeamEvent event) {
                        // ở LineupFragment bắn về null
                        getTeams();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    void initView() {
        try {
            displayTime();
            TeamAdapter teamAdapter = new TeamAdapter(
                    getContext(),
                    league.getOwner(),
                    team -> { // click detail
                        if (!leagueType.equals(LeagueDetailFragment.OPEN_LEAGUES)) {
                            AloneFragmentActivity.with(this)
                                    .parameters(TeamDetailFragment.newBundle(getString(R.string.team_list), team.getId(), league))
                                    .start(TeamDetailFragment.class);
                        }
                    },
                    team -> { // click remove
                        DialogUtils.confirmBox(mActivity,
                                getString(R.string.app_name),
                                String.format(getString(R.string.remove_team_message), team.getName()),
                                getString(R.string.yes),
                                getString(R.string.no), (dialogInterface, i) -> {
                                    if (league.getStatus() == LeagueResponse.WAITING_FOR_START) {
                                        presenter.removeTeam(league.getId(), team.getId());
                                    }
                                });
                    });
            teamAdapter.removeVisible(league.getStatus() == LeagueResponse.WAITING_FOR_START);
            rvTeam.
                    adapter(teamAdapter)
                    .refreshListener(this::refresh)
                    .build();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void displayTime() {
        // line up my team
        boolean isTransfer = league.getGameplayOption().equals(LeagueResponse.GAMEPLAY_OPTION_TRANSFER);
        if (league.getStatus() == LeagueResponse.WAITING_FOR_START) {
            if (AppUtilities.isSetupTime(isTransfer ? league.getTeamSetup() : league.getDraftTime())) {
                tvTimeLabel.setText(R.string.team_setup_time);
                tvTime.setText(DateTimeUtils.convertCalendarToString(isTransfer ? league.getTeamSetUpCalendar() : league.getDraftTimeCalendar(), Constant.FORMAT_DATE_TIME));
            } else {
                tvTimeLabel.setText(R.string.start_time);
                tvTime.setText(DateTimeUtils.convertCalendarToString(league.getStartAtCalendar(), Constant.FORMAT_DATE_TIME));
            }

        } else if (league.getStatus() == LeagueResponse.ON_GOING) {
            tvTimeLabel.setText(isTransfer ? R.string.transfer_deadline : R.string.waiving_deadline);
            tvTime.setText(DateTimeUtils.convertCalendarToString(league.getTransferDeadlineCalendar(), Constant.FORMAT_DATE_TIME));
        }
    }

    void getTeams() {
        presenter.getTeams(league.getId());
    }

    private void refresh() {
        rvTeam.clear();
        rvTeam.startLoading();
        getTeams();
    }

    @NonNull
    @Override
    public ITeamPresenter<ITeamView> createPresenter() {
        return new TeamDataPresenter(getAppComponent());
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        rvTeam.addItems(teams);
    }

    @Override
    public void hideLoading() {
        rvTeam.stopLoading();
    }

    @Override
    public void removeSuccess(int teamId) {
        refresh();
    }
}
