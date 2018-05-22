package com.football.fantasy.fragments.leagues.league_details.league_info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LeagueInfoFragment extends BaseMvpFragment<ILeagueInfoView, ILeagueInfoPresenter<ILeagueInfoView>> implements ILeagueInfoView {
    static final String TAG = LeagueInfoFragment.class.getSimpleName();

    public static LeagueInfoFragment newInstance() {
        return new LeagueInfoFragment();
    }

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

    @Override
    public int getResourceId() {
        return R.layout.league_info_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public ILeagueInfoPresenter<ILeagueInfoView> createPresenter() {
        return new LeagueInfoDataPresenter(getAppComponent());
    }

    @OnClick(R.id.tvSetupTeam)
    void onClickSetupTeam() {

    }

    @OnClick(R.id.tvStartLeague)
    void onClickStartLeague() {
    }
}
