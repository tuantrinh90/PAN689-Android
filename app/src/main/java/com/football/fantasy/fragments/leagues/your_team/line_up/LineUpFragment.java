package com.football.fantasy.fragments.leagues.your_team.line_up;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.DateTimeUtils;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.customizes.lineup.StatisticView;
import com.football.events.PickEvent;
import com.football.events.PlayerEvent;
import com.football.events.TeamEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.your_team.players_popup.PlayerPopupFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;
import java8.util.function.BiConsumer;

public class LineUpFragment extends BaseMainMvpFragment<ILineUpView, ILineUpPresenter<ILineUpView>> implements ILineUpView {

    static final String KEY_TEAM_ID = "TEAM_ID";
    private static final String KEY_LEAGUE = "LEAGUE_ID";

    private BiConsumer<Boolean, String> callback;


    public static LineUpFragment newInstance(LeagueResponse league, Integer teamId) {
        LineUpFragment fragment = new LineUpFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
        bundle.putInt(KEY_TEAM_ID, teamId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.tvTimeLabel)
    ExtTextView tvTimeLabel;
    @BindView(R.id.tvTime)
    ExtTextView tvTime;
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

    private LeagueResponse league;
    private int teamId;

    private boolean isSetupTime;

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
            league = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
            teamId = bundle.getInt(KEY_TEAM_ID);
        }
    }

    void initView() {
        isSetupTime = AppUtilities.isSetupTime(league.getTeamSetup());
        if (isSetupTime) {
            lineupView.setEditable(true);
            lineupView.setAddable(true);
            lineupView.setRemovable(true);
            tvTimeLabel.setText(R.string.team_setup_time);
            tvTime.setText(DateTimeUtils.convertCalendarToString(league.getTeamSetUpCalendar(), Constant.FORMAT_DATE_TIME));

        } else {
            lineupView.setEditable(false);
            lineupView.setAddable(true);
            lineupView.setRemovable(false);
            tvTimeLabel.setText(R.string.start_time);
            tvTime.setText(DateTimeUtils.convertCalendarToString(league.getStartAtCalendar(), Constant.FORMAT_DATE_TIME));

            // hide complete button
            tvComplete.setVisibility(View.GONE);
        }

        // setup lineupView
        lineupView.setupLineup(new PlayerResponse[18], new int[]{4, 6, 6, 2});
        lineupView.setAddCallback((position, order) -> {
            if (isSetupTime) {
                AloneFragmentActivity.with(this)
                        .parameters(PlayerPopupFragment.newBundle(position, order, league.getId()))
                        .start(PlayerPopupFragment.class);
            } else {
                showMessage(getString(R.string.message_pick_after_team_setup_time));
            }
        });
        lineupView.setInfoCallback(player -> {
            AloneFragmentActivity.with(this)
                    .parameters(PlayerDetailFragment.newBundle(player, getString(R.string.line_up), false))
                    .start(PlayerDetailFragment.class);
        });
        lineupView.setRemoveCallback((player, position, index) -> {
            DialogUtils.messageBox(mActivity,
                    0,
                    getString(R.string.app_name),
                    getString(R.string.message_confirm_remove_lineup_player),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    (dialog, which) -> presenter.removePlayer(player, position, teamId),
                    (dialog, which) -> {
                    });
        });
    }

    private void enableCompleteButton(boolean enable) {
        // disable button complete
        boolean realEnable = AppUtilities.isSetupTime(league.getTeamSetup()) && enable;
        tvComplete.setEnabled(realEnable);
        tvComplete.setBackgroundResource(realEnable ? R.drawable.bg_button_yellow : R.drawable.bg_button_gray);
    }

    void registerEvent() {
        // action add click on PlayerList
        mCompositeDisposable.add(bus.ofType(PlayerEvent.class)
                .subscribeWith(new DisposableObserver<PlayerEvent>() {
                    @Override
                    public void onNext(PlayerEvent event) {
                        switch (event.getAction()) {
                            case PlayerEvent.ACTION_ADD_CLICK:
                                callback = event.getCallback();
                                insertToLineUpView(event.getData(), event.getPosition(), event.getOrder() == null ? LineupView.NONE_ORDER : event.getOrder());
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
    }

    private void insertToLineUpView(PlayerResponse player, int position, int index) {
        if (!lineupView.isFullPosition(position)) {
            presenter.addPlayer(player, teamId, position, index);
        } else {
            callback.accept(false, getString(R.string.message_full_position));
        }
    }

    @Override
    public ILineUpPresenter<ILineUpView> createPresenter() {
        return new LineUpPresenter(getAppComponent());
    }

    @OnClick(R.id.tvComplete)
    public void onCompleteClicked() {
        showMessage(R.string.message_confirm_complete, R.string.yes, R.string.no,
                aVoid -> presenter.completeLineup(teamId), null);
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
//        enableCompleteButton(lineupView.isSetupComplete());
    }

    @Override
    public void displayStatistic(StatisticResponse statistic) {
        svGoalkeeper.setCount(statistic.getGoalkeeper());
        svDefender.setCount(statistic.getDefender());
        svMidfielder.setCount(statistic.getMidfielder());
        svAttacker.setCount(statistic.getAttacker());
    }

    @Override
    public void onAddPlayer(TeamResponse team, PlayerResponse player, int order) {
        bus.send(new PickEvent(PickEvent.ACTION_PICK, player.getId()));

        lineupView.addPlayer(player, player.getMainPosition(), order);

        if (lineupView.isSetupComplete()) {
            enableCompleteButton(true);
        }
    }

    @Override
    public void onRemovePlayer(TeamResponse team, PlayerResponse player) {
        lineupView.removePlayer(player, player.getMainPosition());
        enableCompleteButton(false);
        bus.send(new PickEvent(PickEvent.ACTION_REMOVE, player.getId()));
    }

    @Override
    public void handleCallback(boolean success, String error) {
        if (callback != null) {
            callback.accept(success, error);
            callback = null;
        }
    }

    @Override
    public void updateStatistic(int position, int value) {
        switch (position) {
            case PlayerResponse.POSITION_ATTACKER:
                svAttacker.appendCount(value);
                break;
            case PlayerResponse.POSITION_MIDFIELDER:
                svMidfielder.appendCount(value);
                break;
            case PlayerResponse.POSITION_DEFENDER:
                svDefender.appendCount(value);
                break;
            case PlayerResponse.POSITION_GOALKEEPER:
                svGoalkeeper.appendCount(value);
                break;
        }
    }

    @Override
    public void onCompleteLineup() {
        bus.send(new TeamEvent(null));
        mActivity.finish();
    }

}
