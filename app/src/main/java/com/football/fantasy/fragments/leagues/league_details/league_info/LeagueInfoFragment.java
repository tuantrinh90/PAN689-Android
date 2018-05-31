package com.football.fantasy.fragments.leagues.league_details.league_info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;

import butterknife.BindView;
import butterknife.OnClick;

public class LeagueInfoFragment extends BaseMainMvpFragment<ILeagueInfoView, ILeagueInfoPresenter<ILeagueInfoView>> implements ILeagueInfoView {
    static final String TAG = LeagueInfoFragment.class.getSimpleName();

    private static final String KEY_LEAGUE = "LEAGUE";

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

    private LeagueResponse league;

    public static LeagueInfoFragment newInstance(LeagueResponse league) {
        LeagueInfoFragment fragment = new LeagueInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
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

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        league = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
    }

    private void displayViews() {
        tvLeagueType.setText(league.getLeagueTypeDisplay());
        tvMaxNumberOfTeam.setText(String.valueOf(league.getNumberOfUser()));
        tvGamePlayOptions.setText(league.getGameplayOptionDisplay());
        tvBudget.setText(getString(R.string.budget_value, league.getBudgetId() + "", league.getBudgetValue() + "")); // todo: chua co budget name
        tvScoringSystem.setText(league.getScoringSystemDisplay());
        tvDescription.setText(league.getDescription());
        ImageLoaderUtils.displayImage(league.getLogo(), ivLeague.getImageView());
    }

    @NonNull
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
