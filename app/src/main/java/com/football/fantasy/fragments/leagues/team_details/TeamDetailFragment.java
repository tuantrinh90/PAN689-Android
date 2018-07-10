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
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.events.TeamEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.fantasy.fragments.leagues.team_lineup.TeamLineUpFragment;
import com.football.fantasy.fragments.leagues.team_squad.TeamSquadFragment;
import com.football.fantasy.fragments.leagues.team_squad.trade.TradeFragment;
import com.football.fantasy.fragments.leagues.team_squad.trade.transferring.TransferringFragment;
import com.football.fantasy.fragments.leagues.team_statistics.TeamStatisticFragment;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class TeamDetailFragment extends BaseMainMvpFragment<ITeamDetailView, ITeamDetailPresenter<ITeamDetailView>> implements ITeamDetailView {

    private static final String TAG = "TeamDetailFragment";

    private static final String KEY_TEAM = "TEAM_ID";
    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";

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
    @BindView(R.id.llTransfer)
    LinearLayout llTransfer;
    @BindView(R.id.llTeamSquad)
    LinearLayout llTeamSquad;
    @BindView(R.id.llStatistics)
    LinearLayout llStatistics;

    private TeamResponse team;
    private int leagueId;

    public static Bundle newBundle(TeamResponse team, Integer leagueId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
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

        displayTeamDetails(team);
    }

    private void getDataFromBundle() {
        assert getArguments() != null;
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
        leagueId = getArguments().getInt(KEY_LEAGUE_ID);
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
                            displayTeamDetails(event.getTeam());
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

    private void displayTeamDetails(TeamResponse team) {
        ivEdit.setVisibility(AppUtilities.isOwner(getAppContext(), team.getUserId()) ? View.VISIBLE : View.GONE);

        tvHeader.setText(team.getName());
        tvName.setText(team.getUser().getName());
        ImageLoaderUtils.displayImage(team.getLogo(), ivAvatar.getImageView());
        tvRank.setText(String.valueOf(team.getRank()));
        tvPoints.setText(AppUtilities.convertNumber(Long.valueOf(team.getTotalPoint())));
        tvBudget.setText(getString(R.string.money_prefix, AppUtilities.getMoney(team.getCurrentBudget())));
        tvDescription.setText(team.getDescription());
    }

    @OnClick({R.id.ivEdit, R.id.llTeamLineUp, R.id.llTransfer, R.id.llTeamSquad, R.id.llStatistics})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivEdit:
                AloneFragmentActivity.with(mActivity)
                        .parameters(SetupTeamFragment.newBundle(
                                team,
                                leagueId,
                                mActivity.getTitleToolBar().getText().toString(),
                                LeagueDetailFragment.MY_LEAGUES))
                        .start(SetupTeamFragment.class);
                break;
            case R.id.llTeamLineUp:
                if (team.getCompleted()) {
                    AloneFragmentActivity.with(this)
                            .parameters(TeamLineUpFragment.newBundle(getString(R.string.team_details), team))
                            .start(TeamLineUpFragment.class);
                } else {
                    showMessage(getString(R.string.message_team_lineup_is_not_completed_yet));
                }
                break;
            case R.id.llTransfer:
                TradeFragment.start(this, team, getString(R.string.team_squad));
                break;
            case R.id.llTeamSquad:
                if (team.getCompleted()) {
                    AloneFragmentActivity.with(this)
                            .parameters(TeamSquadFragment.newBundle(team, team.getName()))
                            .start(TeamSquadFragment.class);
                } else {
                    showMessage(getString(R.string.message_team_lineup_is_not_completed_yet));
                }
                break;
            case R.id.llStatistics:
                AloneFragmentActivity.with(this)
                        .parameters(TeamStatisticFragment.newBundle(getString(R.string.team_details), team))
                        .start(TeamStatisticFragment.class);
                break;
        }
    }
}
