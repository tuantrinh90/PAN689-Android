package com.football.fantasy.fragments.leagues.your_team.line_up;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.bon.customview.textview.ExtTextView;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.customizes.lineup.PlayerView;
import com.football.customizes.lineup.StatisticView;
import com.football.customizes.textview.ExtTextViewCountdown;
import com.football.events.PickEvent;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import io.reactivex.observers.DisposableObserver;
import java8.util.function.BiConsumer;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;

public abstract class LineUpFragment<V extends ILineUpView, P extends ILineUpPresenter<V>> extends BaseMvpFragment<V, P> implements ILineUpView {

    protected static final String KEY_TEAM_ID = "TEAM_ID";
    protected static final String KEY_LEAGUE = "LEAGUE_ID";
    protected BiConsumer<Boolean, String> callback;


    public static LineUpFragment newInstance(LineUpFragment fragment, LeagueResponse league, Integer teamId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
        bundle.putInt(KEY_TEAM_ID, teamId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.tvTimeLabel)
    protected ExtTextView tvTimeLabel;
    @BindView(R.id.tvTime)
    protected ExtTextView tvTime;
    @BindView(R.id.lineupView)
    protected LineupView lineupView;
    @BindView(R.id.svGoalkeeper)
    protected StatisticView svGoalkeeper;
    @BindView(R.id.svDefender)
    protected StatisticView svDefender;
    @BindView(R.id.svMidfielder)
    protected StatisticView svMidfielder;
    @BindView(R.id.svAttacker)
    protected StatisticView svAttacker;
    @BindView(R.id.tvBudget)
    protected ExtTextView tvBudget;
    @BindView(R.id.tvComplete)
    protected ExtTextView tvComplete;

    @BindView(R.id.transfer_header)
    protected TableRow transferHeader;
    @BindView(R.id.tvDraftCurrentTimeLeft)
    protected ExtTextViewCountdown tvDraftCurrentTimeLeft;
    @BindView(R.id.tvDraftCurrentTeam)
    protected ExtTextView tvDraftCurrentTeam;
    @BindView(R.id.tvDraftNextTeam)
    protected ExtTextView tvDraftNextTeam;
    @BindView(R.id.tvDraftEndTurn)
    protected ExtTextView tvDraftEndTurn;
    @BindView(R.id.draft_header)
    protected LinearLayout draftHeader;
    @BindView(R.id.transfer_bottom)
    protected LinearLayout transferBottom;


    protected LeagueResponse league;
    protected int teamId;

    @Override
    public int getResourceId() {
        return R.layout.lineup_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        setupLineupView();
        registerEvent();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            league = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
            teamId = bundle.getInt(KEY_TEAM_ID);
        }
    }

    protected void initView() {
        if (league.getGameplayOption().equals(LeagueResponse.GAMEPLAY_OPTION_TRANSFER)) {
            transferHeader.setVisibility(View.VISIBLE);
            transferBottom.setVisibility(View.VISIBLE);
            draftHeader.setVisibility(View.GONE);
        } else {
            transferHeader.setVisibility(View.GONE);
            transferBottom.setVisibility(View.GONE);
            draftHeader.setVisibility(View.VISIBLE);
        }

    }

    private void setupLineupView() {
        // setup lineupView
        lineupView.setupLineup(new PlayerResponse[18], new int[]{4, 6, 6, 2});
        lineupView.setAddCallback(this::onLineupViewAddClicked);
        lineupView.setInfoCallback(this::onLineupViewInfoClicked);
        lineupView.setRemoveCallback(this::onLineupViewRemoveClicked);
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
                                onAddClickedFromPopup(event.getData(), event.getPosition(), event.getOrder() == null ? NONE_ORDER : event.getOrder());
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

    @OnCheckedChanged({R.id.switch_display})
    public void onCheckedChanged(CompoundButton button, boolean checked) {
        lineupView.displayByName(!checked);
    }

    @Override
    public void displayLineupPlayers(List<PlayerResponse> players) {
        lineupView.notifyDataSetChanged();
        for (PlayerResponse player : players) {
            lineupView.addPlayer(player, player.getMainPosition(), player.getOrder() == null ? NONE_ORDER : player.getOrder());
        }
//        enableCompleteButton(league.getTeam() != null && !league.getTeam().getCompleted() && lineupView.isSetupComplete());
    }

    @Override
    public void displayStatistic(StatisticResponse statistic) {
        svGoalkeeper.setCount(statistic.getGoalkeeper());
        svDefender.setCount(statistic.getDefender());
        svMidfielder.setCount(statistic.getMidfielder());
        svAttacker.setCount(statistic.getAttacker());
    }

    @Override
    public void addPlayerSuccess(TeamResponse team, PlayerResponse player, int order) {
        lineupView.addPlayer(player, player.getMainPosition(), order);
        bus.send(new PickEvent(PickEvent.ACTION_PICK, player.getId()));
    }

    @Override
    public void removePlayerSuccess(TeamResponse team, PlayerResponse player) {
        lineupView.removePlayer(player, player.getMainPosition());
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

    protected abstract void onLineupViewAddClicked(PlayerView playerView, int position, int order);

    // player được bắn về từ popup && tab playerList
    protected abstract void onAddClickedFromPopup(PlayerResponse player, int position, int order);

    protected abstract void onLineupViewRemoveClicked(PlayerResponse player, int position, int index);

    protected abstract void onLineupViewInfoClicked(PlayerResponse player, int position, int order);


}
