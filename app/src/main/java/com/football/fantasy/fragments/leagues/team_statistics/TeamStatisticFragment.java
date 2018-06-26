package com.football.fantasy.fragments.leagues.team_statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.TeamStatisticAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.TeamStatisticResponse;
import com.football.utilities.AppUtilities;

import java.util.ArrayList;

import butterknife.BindView;

public class TeamStatisticFragment extends BaseMainMvpFragment<ITeamStatisticView, ITeamStatisticPresenter<ITeamStatisticView>> implements ITeamStatisticView {

    private static final String KEY_TEAM = "TEAM";

    @BindView(R.id.llPointPerPlayer)
    LinearLayout llPointPerPlayer;
    @BindView(R.id.ivRank)
    ImageView ivRank;
    @BindView(R.id.tvRankValue)
    ExtTextView tvRankValue;
    @BindView(R.id.tvRankLabel)
    ExtTextView tvRankLabel;
    @BindView(R.id.tvPoints)
    ExtTextView tvPoints;
    @BindView(R.id.tvBudget)
    ExtTextView tvBudget;
    @BindView(R.id.lvData)
    ExtPagingListView lvData;

    private TeamResponse team;
    TeamStatisticAdapter teamStatisticAdapter;


    public static Bundle newBundle(TeamResponse team) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_statistic_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        initView();
        initData();

        getTeamStatistic();
    }

    private void getDataFromBundle() {
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));

    }

    void initData() {
        teamStatisticAdapter = new TeamStatisticAdapter(mActivity, new ArrayList<>());
        lvData.init(mActivity, teamStatisticAdapter);
    }

    private void getTeamStatistic() {
        lvData.startLoading();
        presenter.getTeamStatistic(team.getId());

    }

    @Override
    public ITeamStatisticPresenter<ITeamStatisticView> createPresenter() {
        return new TeamStatisticPresenter(getAppComponent());
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
    public void displayTeamStatistic(TeamStatisticResponse teamStatistic) {
        Optional.from(lvData).doIfPresent(rv -> rv.addNewItems(teamStatistic.getRounds()));
        lvData.stopLoading();

        tvPoints.setText(AppUtilities.convertNumber(Long.valueOf(teamStatistic.getTotalPoint())));
        tvBudget.setText(getString(R.string.money_prefix, AppUtilities.getMoney(teamStatistic.getCurrentBudget())));

        tvRankLabel.setVisibility(View.GONE);
        tvRankValue.setVisibility(View.GONE);
        switch (team.getRank()) {
            case 1:
                ivRank.setImageResource(R.drawable.ic_number_one);
                break;

            case 2:
                ivRank.setImageResource(R.drawable.ic_number_two);
                break;

            case 3:
                ivRank.setImageResource(R.drawable.ic_number_three);
                break;

            default:
                tvRankValue.setText(String.valueOf(team.getRank()));
                ivRank.setVisibility(View.GONE);
                tvRankLabel.setVisibility(View.VISIBLE);
                tvRankValue.setVisibility(View.VISIBLE);
        }
    }
}
