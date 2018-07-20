package com.football.fantasy.fragments.leagues.team_statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.TeamStatisticAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_squad.TeamSquadFragment;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.TeamStatisticResponse;
import com.football.utilities.AppUtilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamStatisticFragment extends BaseMainMvpFragment<ITeamStatisticView, ITeamStatisticPresenter<ITeamStatisticView>> implements ITeamStatisticView {

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";

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

    private String title;
    private TeamResponse team;
    private int leagueId;

    TeamStatisticAdapter teamStatisticAdapter;


    public static Bundle newBundle(String title, TeamResponse team, int leagueId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putSerializable(KEY_TEAM, team);
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_statistic_fragment;
    }

    @Override
    public String getTitleString() {
        return title;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        initData();

        getTeamStatistic();
    }

    private void getDataFromBundle() {
        title = getArguments().getString(KEY_TITLE);
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
        leagueId = getArguments().getInt(KEY_LEAGUE_ID);
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
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
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

    @OnClick(R.id.llPointPerPlayer)
    public void onPointPerPlayerClicked() {
        if (team.getCompleted()) {
            AloneFragmentActivity.with(this)
                    .parameters(TeamSquadFragment.newBundle(team, getString(R.string.statistics), leagueId))
                    .start(TeamSquadFragment.class);
        } else {
            showMessage(getString(R.string.message_team_lineup_is_not_completed_yet));
        }
    }
}
