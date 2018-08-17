package com.football.fantasy.fragments.leagues.team_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.logger.Logger;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.events.TeamEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
import com.football.fantasy.fragments.leagues.team_details.gameplay_option.GameplayOptionFragment;
import com.football.fantasy.fragments.leagues.team_details.team_lineup.TeamLineUpFragment;
import com.football.fantasy.fragments.leagues.team_details.team_squad.TeamSquadFragment;
import com.football.fantasy.fragments.leagues.team_details.team_statistics.TeamStatisticFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;

public class TeamDetailFragment extends BaseMvpFragment<ITeamDetailView, ITeamDetailPresenter<ITeamDetailView>> implements ITeamDetailView {

    private static final String TAG = "TeamDetailFragment";

    private static final String KEY_TEAM_ID = "TEAM_ID";
    private static final String KEY_LEAGUE = "LEAGUE_ID";

    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.ivEdit)
    ImageView ivEdit;
    @BindView(R.id.tvName)
    ExtTextView tvName;
    @BindView(R.id.tvOwner)
    ExtTextView tvOwner;
    @BindView(R.id.ivAvatar)
    CircleImageViewApp ivAvatar;
    @BindView(R.id.tvRank)
    ExtTextView tvRank;
    @BindView(R.id.tvPoints)
    ExtTextView tvPoints;
    @BindView(R.id.tvBudget)
    ExtTextView tvBudget;
    @BindView(R.id.tvDescription)
    ExtTextView tvDescription;
    @BindView(R.id.llTeamLineUp)
    LinearLayout llTeamLineUp;
    @BindView(R.id.llTeamSquad)
    LinearLayout llTeamSquad;
    @BindView(R.id.llStatistics)
    LinearLayout llStatistics;
    @BindView(R.id.tvGamePlayOption)
    ExtTextView tvGamePlayOption;

    private int teamId;
    private LeagueResponse league;

    private TeamResponse team;

    public static Bundle newBundle(int teamId, LeagueResponse league) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TEAM_ID, teamId);
        bundle.putSerializable(KEY_LEAGUE, league);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_detail_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        registerEvent();

        initView();

        getTeam();
    }

    private void initView() {
        tvGamePlayOption.setText(getString(league.getGameplayOption().equals(GAMEPLAY_OPTION_TRANSFER) ? R.string.transfer : R.string.waving));
    }

    private void getTeam() {
        presenter.getTeam(teamId);
    }

    private void getDataFromBundle() {
        assert getArguments() != null;
        teamId = getArguments().getInt(KEY_TEAM_ID);
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    @NonNull
    @Override
    public ITeamDetailPresenter<ITeamDetailView> createPresenter() {
        return new TeamDetailPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.league_details;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    void registerEvent() {
        try {
            // action add click on PlayerList
            mCompositeDisposable.add(bus.ofType(TeamEvent.class)
                    .subscribeWith(new DisposableObserver<TeamEvent>() {
                        @Override
                        public void onNext(TeamEvent event) {
                            getTeam();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));

        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @OnClick({R.id.ivEdit})
    public void onEditClick() {
        AloneFragmentActivity.with(mActivity)
                .parameters(SetupTeamFragment.newBundle(
                        team,
                        league.getId(),
                        mActivity.getTitleToolBar().getText().toString()))
                .start(SetupTeamFragment.class);
    }

    @OnClick({R.id.llTeamLineUp, R.id.llGamePlayOption, R.id.llTeamSquad, R.id.llStatistics})
    public void onBlockClicked(View view) {
        if (league.getStatus().equals(LeagueResponse.WAITING_FOR_START)) {
            showMessage(R.string.league_is_not_started_yet, R.string.ok, null);
            return;
        }

        switch (view.getId()) {
            case R.id.llTeamLineUp:
                if (team.getCompleted()) {
                    AloneFragmentActivity.with(this)
                            .parameters(TeamLineUpFragment.newBundle(getString(R.string.team_details), team))
                            .start(TeamLineUpFragment.class);
                } else {
                    showMessage(getString(R.string.message_team_lineup_is_not_completed_yet));
                }
                break;
            case R.id.llGamePlayOption:
                if (AppUtilities.isOwner(getContext(), team.getUserId())) {
                    if (league.getStatus() == LeagueResponse.ON_GOING) {
                        GameplayOptionFragment.start(this, getString(R.string.team_details), team, league);
                    } else {
                        showMessage(R.string.start_league_before_team_setup_time, R.string.ok, null);
                    }
                } else {
                    showMessage(R.string.message_not_owner_the_team, R.string.ok, null);
                }
                break;
            case R.id.llTeamSquad:
                if (team.getCompleted()) {
                    AloneFragmentActivity.with(this)
                            .parameters(TeamSquadFragment.newBundle(team, team.getName(), league))
                            .start(TeamSquadFragment.class);
                } else {
                    showMessage(getString(R.string.message_team_lineup_is_not_completed_yet));
                }
                break;
            case R.id.llStatistics:
                AloneFragmentActivity.with(this)
                        .parameters(TeamStatisticFragment.newBundle(getString(R.string.team_details), team, league))
                        .start(TeamStatisticFragment.class);
                break;
        }
    }

    @Override
    public void displayTeam(TeamResponse team) {
        this.team = team;

        ivEdit.setVisibility(AppUtilities.isOwner(getAppContext(), team.getUserId()) ? View.VISIBLE : View.GONE);

        tvHeader.setText(team.getName());
        tvName.setText(team.getUser().getName());
        ImageLoaderUtils.displayImage(team.getLogo(), ivAvatar.getImageView());
        tvRank.setText(String.valueOf(team.getRank()));
        tvPoints.setText(AppUtilities.convertNumber(Long.valueOf(team.getTotalPoint())));
        tvBudget.setText(getString(R.string.money_prefix, AppUtilities.getMoney(team.getCurrentBudget())));
        tvDescription.setText(team.getDescription());
    }
}
