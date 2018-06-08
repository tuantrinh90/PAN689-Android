package com.football.fantasy.fragments.leagues.your_team.line_up;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bon.logger.Logger;
import com.bon.util.DialogUtils;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

public class LineUpFragment extends BaseMainMvpFragment<ILineUpView, ILineUpPresenter<ILineUpView>> implements ILineUpView {

    private static final String TAG = "LineUpFragment";

    public static LineUpFragment newInstance() {
        return new LineUpFragment();
    }

    @BindView(R.id.lineupView)
    LineupView lineupView;

    @Override
    public int getResourceId() {
        return R.layout.lineup_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        registerEvent();
    }

    void initView() {
        lineupView.setupLineup(new PlayerResponse[18], new int[] {2, 6, 6, 4});
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
                                    PlayerResponse player = event.getData();
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
        lineupView.addPlayer(player, position == PlayerResponse.POSITION_NONE ? player.getMainPosition() : position);
    }

    @Override
    public ILineUpPresenter<ILineUpView> createPresenter() {
        return new LineUpPresenter(getAppComponent());
    }
}
