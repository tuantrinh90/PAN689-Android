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
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_squad.TeamSquadFragment;
import com.football.fantasy.fragments.leagues.team_statistics.TeamStatisticFragment;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamDetailFragment extends BaseMainMvpFragment<ITeamDetailView, ITeamDetailPresenter<ITeamDetailView>> implements ITeamDetailView {

    private static final String KEY_TEAM_ID = "TEAM_ID";

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

    private int teamId;
    private TeamResponse team;

    public static Bundle newBundle(int teamId) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TEAM_ID, teamId);
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

        presenter.getTeamDetails(teamId);
    }

    private void getDataFromBundle() {
        assert getArguments() != null;
        teamId = getArguments().getInt(KEY_TEAM_ID);
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

    @Override
    public void displayTeamDetails(TeamResponse team) {
        this.team = team;

        tvHeader.setText(team.getName());
        tvName.setText(team.getUser().getName());
        ImageLoaderUtils.displayImage(team.getLogo(), ivAvatar.getImageView());
        tvRank.setText(String.valueOf(team.getRank()));
        tvPoints.setText(AppUtilities.convertNumber(Long.valueOf(team.getTotalPoint())));
        tvBudget.setText(getString(R.string.money_prefix, AppUtilities.getMoney(team.getCurrentBudget())));
        tvDescription.setText(team.getDescription());
    }

    @OnClick({R.id.llTeamLineUp, R.id.llTransfer, R.id.llTeamSquad, R.id.llStatistics})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llTeamLineUp:
                break;
            case R.id.llTransfer:
                break;
            case R.id.llTeamSquad:
                AloneFragmentActivity.with(this)
                        .parameters(TeamSquadFragment.newBundle(team, team.getName()))
                        .start(TeamSquadFragment.class);
                break;
            case R.id.llStatistics:
                AloneFragmentActivity.with(this)
                        .parameters(TeamStatisticFragment.newBundle(team))
                        .start(TeamStatisticFragment.class);
                break;
        }
    }
}
