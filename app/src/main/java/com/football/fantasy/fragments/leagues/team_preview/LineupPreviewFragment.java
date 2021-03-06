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
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.customizes.lineup.StatisticView;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;

public class LineupPreviewFragment extends BaseMvpFragment<ILineupPreviewView, ITeamPreviewPresenter<ILineupPreviewView>> implements ILineupPreviewView {

    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_GAMEPLAY = "GAMEPLAY";

    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
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

    @BindView(R.id.bottom)
    View bottom;

    private TeamResponse team;
    private String gameplay;

    public static void start(Fragment fragment, TeamResponse team, String gameplay) {
        AloneFragmentActivity.with(fragment)
                .parameters(LineupPreviewFragment.newBundle(team, gameplay))
                .start(LineupPreviewFragment.class);
    }

    private static Bundle newBundle(TeamResponse team, String gameplay) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);
        bundle.putString(KEY_GAMEPLAY, gameplay);
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
        presenter.getLineup(team.getId());
    }

    private void getDataFromBundle() {
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
        gameplay = getArguments().getString(KEY_GAMEPLAY);
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
        bottom.setVisibility(gameplay.equals(LeagueResponse.GAMEPLAY_OPTION_TRANSFER) ? View.VISIBLE : View.GONE);

        tvTitle.setText(team.getName());

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
            lineupView.addPlayer(player, player.getMainPosition(), player.getOrder() == null ? NONE_ORDER : player.getOrder());
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
