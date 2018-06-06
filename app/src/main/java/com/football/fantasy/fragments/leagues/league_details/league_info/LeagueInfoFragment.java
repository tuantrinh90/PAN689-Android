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
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
import com.football.fantasy.fragments.leagues.your_team.YourTeamFragment;
import com.football.models.requests.LeagueRequest;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class LeagueInfoFragment extends BaseMainMvpFragment<ILeagueInfoView, ILeagueInfoPresenter<ILeagueInfoView>> implements ILeagueInfoView {
    static final String TAG = LeagueInfoFragment.class.getSimpleName();
    static final String KEY_LEAGUE = "LEAGUE";
    static final String KEY_LEAGUE_TYPE = "league_type";

    @BindView(R.id.tvTeamSetupTime)
    ExtTextView tvTeamSetupTime;
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

    public static LeagueInfoFragment newInstance(LeagueResponse league, String leagueType) {
        LeagueInfoFragment fragment = new LeagueInfoFragment();

        Bundle bundle = new Bundle();
        if (league != null) bundle.putSerializable(KEY_LEAGUE, league);
        bundle.putString(KEY_LEAGUE_TYPE, leagueType);
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
        displayViews();
    }

    void getDataFromBundle() {
        if (getArguments().containsKey(KEY_LEAGUE)) {
            league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        }
        leagueType = getArguments().getString(KEY_LEAGUE_TYPE);
    }

    void displayViews() {
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

            if (league.getGameplayOption().equalsIgnoreCase(LeagueRequest.GAMEPLAY_OPTION_TRANSFER)) {
                tvTeamSetupTime.setText(R.string.team_setup_time);
                tvTime.setText(DateTimeUtils.convertCalendarToString(league.getTeamSetUpCalendar(), Constant.FORMAT_DATE_TIME));
            } else {
                tvTeamSetupTime.setText(R.string.start_time);
                tvTime.setText(DateTimeUtils.convertCalendarToString(league.getStartAtCalendar(), Constant.FORMAT_DATE_TIME));
            }

            tvSetupTeam.setVisibility(View.GONE);
            tvJoinLeague.setVisibility(View.GONE);
            tvStartLeague.setVisibility(View.GONE);

            if (league.getOwner()) {
                tvSetupTeam.setVisibility(View.VISIBLE);
                tvStartLeague.setVisibility(View.VISIBLE);
            } else {
                tvJoinLeague.setVisibility(View.VISIBLE);
            }

            // line up my team
            if (league.getStatus() == LeagueResponse.WAITING_FOR_START) {
                tvSetupTeam.setText(R.string.team_setup_time);
            }

            if (league.getStatus() == LeagueResponse.ON_GOING) {
                tvSetupTeam.setText(R.string.lineup_my_team);
            }

            if (league.getStatus() == LeagueResponse.FINISHED) {
                tvSetupTeam.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @NonNull
    @Override
    public ILeagueInfoPresenter<ILeagueInfoView> createPresenter() {
        return new LeagueInfoDataPresenter(getAppComponent());
    }

    @OnClick(R.id.tvSetupTeam)
    void onClickSetupTeam() {
        AloneFragmentActivity.with(this)
                .parameters(YourTeamFragment.newBundle(league.getId(), league))
                .start(YourTeamFragment.class);
    }

    @OnClick(R.id.tvStartLeague)
    void onClickStartLeague() {
    }

    @OnClick(R.id.tvJoinLeague)
    void onClickJoinLeague() {
        AloneFragmentActivity.with(this).parameters(SetupTeamFragment.newBundle(league, null,
                mActivity.getTitleToolBar().getText().toString(), leagueType))
                .start(SetupTeamFragment.class);
    }
}
