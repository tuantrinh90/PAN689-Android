package com.football.fantasy.fragments.leagues.your_team.line_up;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;

import com.bon.customview.textview.ExtTextView;
import com.bon.logger.Logger;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.customizes.lineup.StatisticView;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.your_team.players_popup.PlayerPopupFragment;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class LineUpFragment extends BaseMainMvpFragment<ILineUpView, ILineUpPresenter<ILineUpView>> implements ILineUpView {

    private static final String TAG = "LineUpFragment";

    static final String KEY_TEAM_ID = "TEAM_ID";
    private static final String KEY_TEAM_SETUP_TIME = "TEAM_SETUP_TIME";
    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";


    public static LineUpFragment newInstance(Integer leagueId, Integer teamId, String teamSetupTime) {
        LineUpFragment fragment = new LineUpFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        bundle.putInt(KEY_TEAM_ID, teamId);
        bundle.putString(KEY_TEAM_SETUP_TIME, teamSetupTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.tvSetupTime)
    ExtTextView tvSetupTime;
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
    @BindView(R.id.tvComplete)
    ExtTextView tvComplete;

    private int leagueId;
    private int teamId;

    @Override
    public int getResourceId() {
        return R.layout.lineup_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        initView();
        registerEvent();

        presenter.getLineup(teamId);
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            leagueId = bundle.getInt(KEY_LEAGUE_ID);
            teamId = bundle.getInt(KEY_TEAM_ID);
            tvSetupTime.setText(bundle.getString(KEY_TEAM_SETUP_TIME));
        }
    }

    void initView() {
        enableCompleteButton(false);

        // setup lineupView
        lineupView.setupLineup(new PlayerResponse[18], new int[]{4, 6, 6, 2});
        lineupView.setClickConsumer((position, index) -> {
            AloneFragmentActivity.with(this)
                    .parameters(PlayerPopupFragment.newBundle(3 - position, index, leagueId))
                    .start(PlayerPopupFragment.class);
        });
        lineupView.setRemoveConsumer((player, position) -> {
            DialogUtils.messageBox(mActivity,
                    0,
                    getString(R.string.app_name),
                    getString(R.string.message_confirm_remove_lineup_player),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    (dialog, which) -> presenter.removePlayer(player, teamId),
                    (dialog, which) -> {
                    });
        });
    }

    private void enableCompleteButton(boolean enable) {
        // disable button complete
        tvComplete.setEnabled(enable);
        tvComplete.setBackgroundResource(enable ? R.drawable.bg_button_yellow : R.drawable.bg_button_gray);
    }

    void registerEvent() {
        try {
            // action add click on PlayerList
            mCompositeDisposable.add(bus.ofType(PlayerEvent.class)
                    .subscribeWith(new DisposableObserver<PlayerEvent>() {
                        @Override
                        public void onNext(PlayerEvent event) {
                            switch (event.getAction()) {
                                case PlayerEvent.ACTION_ADD_CLICK:
                                    insertToLineUpView(event.getData(), event.getPosition());
                                    break;
                            }
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

    private void insertToLineUpView(PlayerResponse player, int position) {
        presenter.addPlayer(player, teamId);
    }

    @Override
    public ILineUpPresenter<ILineUpView> createPresenter() {
        return new LineUpPresenter(getAppComponent());
    }

    @OnClick(R.id.tvComplete)
    public void onCompleteClicked() {
        getActivity().finish();
    }

    @OnCheckedChanged({R.id.switch_display})
    public void onCheckedChanged(CompoundButton button, boolean checked) {
        lineupView.displayByName(!checked);
    }

    @Override
    public void displayBudget(TeamResponse team) {
        tvBudget.setText(getString(R.string.team_budget_value, team.getCurrentBudgetValue()));
    }

    @Override
    public void displayLineupPlayers(List<PlayerResponse> players) {
        lineupView.notifyDataSetChanged();
        for (PlayerResponse player : players) {
            lineupView.addPlayer(player, 3 - player.getMainPosition());
        }
    }

    @Override
    public void displayStatistic(StatisticResponse statistic) {
        svGoalkeeper.setCount(statistic.getGoalkeeper());
        svDefender.setCount(statistic.getDefender());
        svMidfielder.setCount(statistic.getMidfielder());
        svAttacker.setCount(statistic.getAttacker());
    }

    @Override
    public void onAddPlayer(TeamResponse team, PlayerResponse player) {
        lineupView.addPlayer(player, 3 - player.getMainPosition());

        if (lineupView.isSetupComplete()) {
            enableCompleteButton(true);
        }
    }

    @Override
    public void onRemovePlayer(TeamResponse team, PlayerResponse player) {
        lineupView.removePlayer(player, 3 - player.getMainPosition());
        enableCompleteButton(false);
    }

}
