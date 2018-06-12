package com.football.fantasy.fragments.leagues.your_team.line_up;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.bon.logger.Logger;
import com.bon.util.DialogUtils;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.customizes.lineup.StatisticView;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import io.reactivex.observers.DisposableObserver;

public class LineUpFragment extends BaseMainMvpFragment<ILineUpView, ILineUpPresenter<ILineUpView>> implements ILineUpView {

    private static final String TAG = "LineUpFragment";

    static final String KEY_TEAM_ID = "TEAM_ID";


    public static LineUpFragment newInstance(Integer teamId) {
        LineUpFragment fragment = new LineUpFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TEAM_ID, teamId);
        fragment.setArguments(bundle);
        return fragment;
    }

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

    private int teamId;

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
        registerEvent();

        presenter.getLineup(teamId);
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            teamId = bundle.getInt(KEY_TEAM_ID);
        }
    }

    void initView() {
        lineupView.setupLineup(new PlayerResponse[18], new int[]{4, 6, 6, 2});
        lineupView.setPlayerBiConsumer((player, position) -> {
            Log.d("dd", "setPlayerBiConsumer: " + position);
        });
        lineupView.setRemoveConsumer(position -> {
            DialogUtils.messageBox(mActivity,
                    0,
                    getString(R.string.app_name),
                    getString(R.string.message_confirm_remove_lineup_player),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    (dialog, which) -> lineupView.removePlayerView(position),
                    (dialog, which) -> lineupView.removePlayerView(position));
        });
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
//        lineupView.addPlayer(player, position == PlayerResponse.POSITION_NONE ? player.getMainPosition() : position);
        presenter.addPlayer(player, teamId);
    }

    @Override
    public ILineUpPresenter<ILineUpView> createPresenter() {
        return new LineUpPresenter(getAppComponent());
    }

    @OnCheckedChanged({R.id.switch_display})
    public void onCheckedChanged(CompoundButton button, boolean checked) {
        lineupView.displayByName(!checked);
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
    public void onAddPlayer(PlayerResponse player) {
        lineupView.addPlayer(player, 3 - player.getMainPosition());
    }
}
