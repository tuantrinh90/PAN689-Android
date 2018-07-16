package com.football.fantasy.fragments.leagues.league_details.league_info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.logger.Logger;
import com.bon.util.DateTimeUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.events.StartLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
import com.football.fantasy.fragments.leagues.team_lineup.TeamLineUpFragment;
import com.football.fantasy.fragments.leagues.your_team.YourTeamFragment;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class LeagueInfoFragment extends BaseMainMvpFragment<ILeagueInfoView, ILeagueInfoPresenter<ILeagueInfoView>> implements ILeagueInfoView {
    static final String TAG = LeagueInfoFragment.class.getSimpleName();
    static final String KEY_LEAGUE = "LEAGUE";
    static final String KEY_LEAGUE_TYPE = "league_type";
    private static final String KEY_INVITATION_ID = "INVITATION_ID";

    @BindView(R.id.tvTimeLabel)
    ExtTextView tvTimeLabel;
    @BindView(R.id.tvTime)
    ExtTextView tvTime;
    @BindView(R.id.ivLeague)
    CircleImageViewApp ivLeague;
    @BindView(R.id.tvSetupTeam)
    ExtTextView tvSetupTeam;
    @BindView(R.id.tvLeagueType)
    ExtTextView tvLeagueType;
    @BindView(R.id.tvMaxNumberOfTeam)
    ExtTextView tvMaxNumberOfTeam;
    @BindView(R.id.tvGamePlayOptions)
    ExtTextView tvGamePlayOptions;
    @BindView(R.id.tvBudget)
    ExtTextView tvBudget;
    @BindView(R.id.tvScoringSystem)
    ExtTextView tvScoringSystem;
    @BindView(R.id.tvDescription)
    ExtTextView tvDescription;
    @BindView(R.id.tvStartLeague)
    ExtTextView tvStartLeague;
    @BindView(R.id.tvJoinLeague)
    ExtTextView tvJoinLeague;

    String leagueType;
    LeagueResponse league;
    private Integer invitationId;

    public static LeagueInfoFragment newInstance(LeagueResponse league, String leagueType, int invitationId) {
        LeagueInfoFragment fragment = new LeagueInfoFragment();

        Bundle bundle = new Bundle();
        if (league != null) bundle.putSerializable(KEY_LEAGUE, league);
        bundle.putString(KEY_LEAGUE_TYPE, leagueType);
        bundle.putInt(KEY_INVITATION_ID, invitationId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.league_info_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        displayLeague(league);
    }

    void getDataFromBundle() {
        if (getArguments().containsKey(KEY_LEAGUE)) {
            league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        }
        leagueType = getArguments().getString(KEY_LEAGUE_TYPE);
        invitationId = getArguments().getInt(KEY_INVITATION_ID);
    }

    @NonNull
    @Override
    public ILeagueInfoPresenter<ILeagueInfoView> createPresenter() {
        return new LeagueInfoDataPresenter(getAppComponent());
    }

    @OnClick(R.id.tvSetupTeam)
    void onClickSetupTeam() {
        if (league.getTeam() == null) {
            AloneFragmentActivity.with(this)
                    .parameters(SetupTeamFragment.newBundle(
                            null,
                            league.getId(),
                            getString(R.string.league_information), leagueType))
                    .start(SetupTeamFragment.class);
        } else {
            if (league.getStatus().equals(LeagueResponse.WAITING_FOR_START)) {
                AloneFragmentActivity.with(this)
                        .parameters(YourTeamFragment.newBundle(league))
                        .start(YourTeamFragment.class);
            } else {
                league.getTeam().setUserId(league.getUserId());
                AloneFragmentActivity.with(this)
                        .parameters(TeamLineUpFragment.newBundle(getString(R.string.league_information), league.getTeam()))
                        .start(TeamLineUpFragment.class);
            }
        }
    }

    @OnClick(R.id.tvStartLeague)
    void onClickStartLeague() {
        showMessage(R.string.message_confirm_start_league,
                R.string.ok,
                R.string.cancel,
                aVoid -> {
                    presenter.startLeague(league.getId());
                }, null);
    }

    @OnClick(R.id.tvJoinLeague)
    void onClickJoinLeague() {
        if (league.getTeam() == null) {
            if (league.getCurrentNumberOfUser() < league.getNumberOfUser()) {
                presenter.acceptInvite(invitationId);
            } else {
                showMessage(getString(R.string.message_league_full));
            }
        } else {
            showMessage(getString(R.string.message_joined_this_league), R.string.ok, null);
        }
    }

    @Override
    public void displayLeague(LeagueResponse league) {
        if (!this.league.equals(league)) {
            this.league = league;
        }

        try {
            tvTime.setText(DateTimeUtils.convertCalendarToString(league.getTeamSetUpCalendar(), Constant.FORMAT_DATE_TIME));
            ivLeague.setImageUri(league.getLogo());
            tvLeagueType.setText(league.getLeagueTypeDisplay());
            tvMaxNumberOfTeam.setText(String.valueOf(league.getNumberOfUser()));
            tvGamePlayOptions.setText(league.getGameplayOptionDisplay());

            tvBudget.setText(getString(R.string.budget_value,
                    (league.getBudgetOption() != null ? league.getBudgetOption().getName() : "") + "",
                    (league.getBudgetOption() != null ? league.getBudgetOption().getValueDisplay() + "" : "")));
            tvScoringSystem.setText(league.getScoringSystemDisplay());
            tvDescription.setText(league.getDescription());

            tvSetupTeam.setVisibility(View.GONE);
            tvJoinLeague.setVisibility(View.GONE);
            tvStartLeague.setVisibility(View.GONE);

            // show button setup team
            if (league.getOwner() || league.getIsJoined()) {
                tvSetupTeam.setVisibility(View.VISIBLE);
            }

            // show button join leagues
            if (!league.getOwner() && !league.getIsJoined() && AppUtilities.isSetupTime(league.getTeamSetup())) {
                tvJoinLeague.setVisibility(View.VISIBLE);
            }

            // show button start league
            // conditions: owner + after setup time and before start time. + !isSetupTime + The number of missing user in the league does not exceed 1 user
            if (AppUtilities.isStartLeagueEnable(league)) {
                tvStartLeague.setVisibility(View.VISIBLE);
            }

            // line up my team
            if (league.getStatus() == LeagueResponse.WAITING_FOR_START) {
                if (AppUtilities.isSetupTime(league.getTeamSetup())) {
                    tvTimeLabel.setText(R.string.team_setup_time);
                    tvTime.setText(DateTimeUtils.convertCalendarToString(league.getTeamSetUpCalendar(), Constant.FORMAT_DATE_TIME));
                } else {
                    tvTimeLabel.setText(R.string.start_time);
                    tvTime.setText(DateTimeUtils.convertCalendarToString(league.getStartAtCalendar(), Constant.FORMAT_DATE_TIME));
                }

            } else if (league.getStatus() == LeagueResponse.ON_GOING) {
                tvSetupTeam.setText(R.string.lineup_my_team);
                tvTimeLabel.setText(R.string.transfer_deadline);
                tvTime.setText(DateTimeUtils.convertCalendarToString(league.getStartAtCalendar(), Constant.FORMAT_DATE_TIME));

            } else if (league.getStatus() == LeagueResponse.FINISHED) {
                tvSetupTeam.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void onAcceptSuccess() {
        AloneFragmentActivity.with(this)
                .parameters(SetupTeamFragment.newBundle(
                        null,
                        league.getId(),
                        mActivity.getTitleToolBar().getText().toString(),
                        leagueType))
                .start(SetupTeamFragment.class);
        mActivity.finish();
    }

    @Override
    public void onStartSuccess(LeagueResponse league) {
        this.league = league;
        displayLeague(league);

        bus.send(new StartLeagueEvent(league));
    }
}
