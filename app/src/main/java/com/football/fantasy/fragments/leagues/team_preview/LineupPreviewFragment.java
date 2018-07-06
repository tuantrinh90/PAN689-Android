package com.football.fantasy.fragments.leagues.team_preview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.CompoundButton;

import com.bon.customview.textview.ExtTextView;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.customizes.lineup.StatisticView;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class LineupPreviewFragment extends BaseMainMvpFragment<ILineupPreviewView, ITeamPreviewPresenter<ILineupPreviewView>> implements ILineupPreviewView {

    private static final String KEY_TEAM_ID = "TEAM_ID";

    @BindView(R.id.lineupView)
    LineupView lineupView;
    @BindView(R.id.svGoalkeeper)
    StatisticView svGoalkeeper;
    @BindView(R.id.svDefender)
    StatisticView svDefender;
    @BindView(R.id.svMidfielder)
    StatisticView svMidfielder;
    @BindView(R.id.svAttacker)
    StatisticView svAttacker;
    @BindView(R.id.tvBudget)
    ExtTextView tvBudget;

    private int teamId;

    public static void start(Fragment fragment, int teamId) {
        AloneFragmentActivity.with(fragment)
                .parameters(LineupPreviewFragment.newBundle(teamId))
                .start(LineupPreviewFragment.class);
    }

    public static Bundle newBundle(int teamId) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TEAM_ID, teamId);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.lineup_preview_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
        presenter.getLineup(teamId);
    }

    private void getDataFromBundle() {
        teamId = getArguments().getInt(KEY_TEAM_ID);
    }

    @NonNull
    @Override
    public ITeamPreviewPresenter<ILineupPreviewView> createPresenter() {
        return new LineupPreviewDataPresenter(getAppComponent());
    }

    @Override
    public String getTitleString() {
        return getString(R.string.team_list);
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    private void initView() {
        lineupView.setEditable(false);
        lineupView.setAddable(true);
        lineupView.setRemovable(false);
        lineupView.setupLineup(new PlayerResponse[18], new int[]{4, 6, 6, 2});
    }

    @OnCheckedChanged({R.id.switch_display})
    public void onCheckedChanged(CompoundButton button, boolean checked) {
        lineupView.displayByName(!checked);
    }

    @Override
    public void displayTeamState(TeamResponse team) {
        // display budget
        tvBudget.setText(getString(R.string.money_prefix, team.getCurrentBudgetValue()));
    }

    @Override
    public void displayLineupPlayers(List<PlayerResponse> players) {
        lineupView.notifyDataSetChanged();
        for (PlayerResponse player : players) {
            lineupView.addPlayer(player, player.getMainPosition(), player.getOrder() == null ? LineupView.NONE_ORDER : player.getOrder());
        }
    }

    @Override
    public void displayStatistic(StatisticResponse statistic) {
        svGoalkeeper.setCount(statistic.getGoalkeeper());
        svDefender.setCount(statistic.getDefender());
        svMidfielder.setCount(statistic.getMidfielder());
        svAttacker.setCount(statistic.getAttacker());
    }

}