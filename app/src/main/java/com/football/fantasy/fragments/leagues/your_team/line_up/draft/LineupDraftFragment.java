package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bon.customview.textview.ExtTextView;
import com.bon.jackson.JacksonUtils;
import com.bon.logger.Logger;
import com.bon.share_preferences.AppPreferences;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.customizes.lineup.PlayerView;
import com.football.customizes.progress.ExtProgress;
import com.football.customizes.textview.ExtTextViewCountdown;
import com.football.events.GeneralEvent;
import com.football.fantasy.BuildConfig;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailForLineupFragment;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpFragment;
import com.football.fantasy.fragments.leagues.your_team.players_popup.PlayerPopupFragment;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TurnReceiveResponse;
import com.football.models.responses.TurnResponse;
import com.football.utilities.Constant;
import com.football.utilities.SocketEventKey;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_NONE_INFO;
import static com.football.models.responses.LeagueResponse.DRAFT_FINISHED;
import static com.github.nkzawa.socketio.client.Socket.EVENT_DISCONNECT;

public class LineupDraftFragment extends LineUpFragment<ILineupDraftView, ILineupDraftPresenter<ILineupDraftView>> implements ILineupDraftView {

    private static final String TAG = "LineupDraftFragment";

    @BindView(R.id.draft_countdown)
    View draftCountdown;
    @BindView(R.id.draft_loading)
    View draftLoading;
    @BindView(R.id.text_lineup_completed)
    View textLineupCompleted;
    @BindView(R.id.draft_your_turn)
    View draftYourTurn;
    @BindView(R.id.draft_team)
    View draftTeam;
    @BindView(R.id.text_countdown)
    ExtTextViewCountdown textCountdown;
    @BindView(R.id.tvDraftYourTurnTimeLeft)
    ExtTextViewCountdown tvDraftYourTurnTimeLeft;
    @BindView(R.id.tvDraftYourTurnTimeUnit)
    ExtTextView tvDraftYourTurnTimeUnit;
    @BindView(R.id.text_your_turn)
    ExtTextView textYourTurn;
    @BindView(R.id.progress_draft)
    ExtProgress progressDraft;
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

    private Handler mHandler = new Handler();
    private Runnable mRunnableReconnect;
    private int userId;

    private boolean offSocket = false;

    private PlayerView playerViewSelected;
    private boolean pickEnable = false;
    private TurnReceiveResponse currentTurn;
    private int currentNumberTimeLeft; // số giây countdown trả về từ onEventTurnReceive, lưu lại để xử lý endTurn
    private int pickRound = -1;
    private int pickOrder = -1;

    @NonNull
    @Override
    public ILineupDraftPresenter<ILineupDraftView> createPresenter() {
        return new LineupDraftPresenter(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerSocket();

        // join league
        presenter.joinRoom(league.getId());

        presenter.endCountdown(league.getId());

        userId = AppPreferences.getInstance(getAppContext()).getInt(Constant.KEY_USER_ID);
        tvDraftYourTurnTimeLeft.setFormatType(ExtTextViewCountdown.FORMAT_TEXT_HOURS_2);
        tvDraftCurrentTimeLeft.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_HOURS);
    }

    @Override
    public void onDestroyView() {
        offSocket = true;

        getAppContext().off(SocketEventKey.EVENT_CHANGE_LINEUP);
        getAppContext().off(SocketEventKey.EVENT_END_TURN);
        getAppContext().off(SocketEventKey.EVENT_TURN_RECEIVE);
        getAppContext().off(SocketEventKey.EVENT_REFRESH_UI);
        getAppContext().off(SocketEventKey.EVENT_PICK_TURN_FINISH);

        mHandler.removeCallbacks(mRunnableReconnect);
        playerViewSelected = null;

        presenter.leaveLeague(league.getId());
        textCountdown.onDestroyView();
        tvDraftYourTurnTimeLeft.onDestroyView();
        tvDraftCurrentTimeLeft.onDestroyView();
        progressDraft.onDestroyView();
        super.onDestroyView();
    }

    private void log(String tag, Object... args) {
        Logger.e(tag, args[0].toString());
    }

    private void registerSocket() {
        /*
         * check connect
         */
        getAppContext().getSocket().on(Socket.EVENT_CONNECT, args -> {
            Log.e(TAG, "\n====================== EVENT_CONNECT ======================");
        });

        /*
         * check disconnect
         */
        getAppContext().getSocket().on(Socket.EVENT_DISCONNECT, args -> {
            Log.e(TAG, "\n====================== EVENT_DISCONNECT ======================");
        });

        /*
         * trả về object theo mỗi giây
         */
        getAppContext().getSocket().on(SocketEventKey.EVENT_TURN_RECEIVE, args -> {
            Log.i(TAG, "\n====================== EVENT_TURN_RECEIVE ======================");
            Log.i(TAG, args != null && args.length > 0 && args[0] != null ? args[0].toString() : "null");
            if (args != null && args.length > 0 && args[0] != null) {
                log("EVENT_TURN_RECEIVE", args);
                TurnReceiveResponse response = JacksonUtils.convertJsonToObject(args[0].toString(), TurnReceiveResponse.class);
                if (response != null && mActivity != null && response.getLeagueId().equals(league.getId())) {
                    currentTurn = response;
                    if (mActivity != null) mActivity.runOnUiThread(() -> {
                        try {
                            onEventTurnReceive(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

        /*
         * trả về object => refresh đội hình
         */
        getAppContext().getSocket().on(SocketEventKey.EVENT_REFRESH_UI, args -> {
            Log.i(TAG, "\n====================== EVENT_REFRESH_UI ======================");
            TurnReceiveResponse response = JacksonUtils.convertJsonToObject(args[0].toString(), TurnReceiveResponse.class);
            if (response != null && mActivity != null && response.getLeagueId().equals(league.getId())) {
                log("EVENT_REFRESH_UI", args);
                updatePickRound(response.getShowPickRound());

                onEventRefreshUI();

                if (BuildConfig.DEBUG) {
                    // log pickRound
                    String currentName = "null";

                    for (TurnResponse turn : response.getLeagues()) {
                        if (turn.isCurrent()) {
                            // log
                            currentName = turn.getName();
                            break;

                        }
                    }
                    Log.e(TAG, "EVENT_REFRESH_UI #pickRound: " + pickRound + " - CurrentTeam: " + currentName);
                }
            }
        });

        /*
          trả về object => kết thúc quá trình pick cầu thủ của all mems
         */
        getAppContext().getSocket().on(SocketEventKey.EVENT_PICK_TURN_FINISH, args -> {
            Log.i(TAG, "\n====================== EVENT_PICK_TURN_FINISH ======================");
            TurnReceiveResponse response = JacksonUtils.convertJsonToObject(args[0].toString(), TurnReceiveResponse.class);
            if (response != null && mActivity != null && response.getLeagueId().equals(league.getId())) {
                log("EVENT_PICK_TURN_FINISH", args);
                onEventPickTurnFinish();
            }
        });

        // onDisconnect
        getAppContext().getSocket().on(EVENT_DISCONNECT, args -> {
            if (!offSocket) reconnect();
        });

    }

    private void reconnect() {
        if (mRunnableReconnect == null) {
            mRunnableReconnect = () -> {
                if (getAppContext() != null && !getAppContext().getSocket().connected()) {
                    // reconnect
                    if (league != null) presenter.leaveLeague(league.getId());
                    if (league != null) presenter.joinRoom(league.getId());

                    // re-handle after 1s
                    mHandler.postDelayed(mRunnableReconnect, 1000);
                }
            };
        }
        mHandler.postDelayed(mRunnableReconnect, 1000);
    }

    private void updatePickRound(int pickRound) {
        if (pickRound > this.pickRound) {
            this.pickRound = pickRound;
        } else if (pickRound < this.pickRound && pickEnable) {
            Log.e(TAG, "updatePickRound. PickRound bị giảm: " + pickRound);
        }
    }

    private void onEventTurnReceive(TurnReceiveResponse response) {
        // stop countdown & visible/gone any views
        textCountdown.stop();
        draftHeader.setVisibility(View.VISIBLE);
        draftCountdown.setVisibility(View.GONE);
        draftTeam.setVisibility(View.VISIBLE);
        draftYourTurn.setVisibility(View.VISIBLE);
        draftLoading.setVisibility(View.GONE);

        updatePickRound(response.getShowPickRound());
        tvDraftCurrentTimeLeft.setTime(response.getNumber());
        currentNumberTimeLeft = response.getNumber();

        boolean finish = true;
        for (TurnResponse turn : response.getLeagues()) {
            if (turn.getUserId() == userId) {
                displayTimerYourTurn(turn.getDueNextTimeMax(), turn.getDueNextTime());
            }
            if (turn.isCurrent()) {
                pickOrder = turn.getPickOrder();

                // cập nhật YOUR TURN or {name}
                boolean isYourTurn = turn.getUserId() == userId;
                tvDraftCurrentTeam.setText(isYourTurn ? getString(R.string.your_turn_cap) : turn.getName());
                setYourTurn(isYourTurn);
            }
            if (turn.isNext()) {
                finish = false;
                if (turn.getUserId() == userId) {
                    tvDraftNextTeam.setText(getString(R.string.your_turn_cap));
                } else {
                    tvDraftNextTeam.setText(turn.getName());
                }
            }
        }
        // nếu finish rồi thì ẩn draftYourTurn và hiện textCompleted
        if (finish) {
            tvDraftNextTeam.setText(getString(R.string.finish).toUpperCase());
        }

        // nếu đã full 18 cầu thủ và ko phải đủ lượt pick thì ẩn draftYourTurn
        if (lineupView.isSetupComplete() && !pickEnable) {
            draftYourTurn.setVisibility(View.GONE);
            textLineupCompleted.setVisibility(View.VISIBLE);
        }
    }

    private void onEventPickTurnFinish() {
        if (mActivity != null) mActivity.runOnUiThread(() -> {
            try {
                showLoading(false);
                pickEnable = false;
                playerViewSelected = null;
                tvDraftCurrentTimeLeft.stop();

                draftLoading.setVisibility(View.GONE);
                draftYourTurn.setVisibility(View.GONE);
                textLineupCompleted.setVisibility(View.VISIBLE);
                draftTeam.setVisibility(View.GONE);

                presenter.getLineup(teamId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void onEventRefreshUI() {
        if (mActivity != null) mActivity.runOnUiThread(() -> {
            try {
                showLoading(false);
                playerViewSelected = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        presenter.getLineup(teamId);
    }

    private void setYourTurn(boolean yourTurn) {
        textYourTurn.setText(getString(yourTurn ? R.string.your_turn : R.string.your_turn_in));

        // send event to show button Add to playerListDraftFragment
        bus.send(new GeneralEvent<>(GeneralEvent.SOURCE.LINEUP_DRAFT, yourTurn));

        lineupView.setAddable(yourTurn);
        pickEnable = yourTurn;
        if (yourTurn) {
            tvDraftEndTurn.setEnabled(true);
            tvDraftEndTurn.setVisibility(View.VISIBLE);
            progressDraft.setVisibility(View.GONE);
            tvDraftYourTurnTimeLeft.setVisibility(View.GONE);
            tvDraftYourTurnTimeUnit.setVisibility(View.GONE);
        } else {
            if (playerViewSelected != null) {
                playerViewSelected.setRemovable(false);
                playerViewSelected = null;
            }
            tvDraftEndTurn.setVisibility(View.GONE);
            progressDraft.setVisibility(View.VISIBLE);
            tvDraftYourTurnTimeLeft.setVisibility(View.VISIBLE);
            tvDraftYourTurnTimeUnit.setVisibility(View.GONE);
        }
    }

    private void displayTimerYourTurn(int max, int progress) {
        tvDraftYourTurnTimeLeft.setTime(progress);
        progressDraft.setMax(max);
        progressDraft.setProgress(progress);
    }

    @OnClick({R.id.tvDraftEndTurn})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.tvDraftEndTurn:
                if (pickEnable) {
                    if (playerViewSelected != null) {
                        handleEndTurn(view);
                    } else {
                        showMessage(
                                R.string.message_non_pick_player_lineup,
                                R.string.ok,
                                R.string.cancel,
                                aVoid -> handleEndTurn(view), null);
                    }
                }
                break;
        }
    }

    private void handleEndTurn(View view) {
        if (pickEnable) {
            showLoading(true);
            view.setEnabled(false);

            pickRound++; // xử lý cho việc UI refresh chưa kịp tăng showPickRound

            Completable.create(e -> {
                // cập nhật lại time các turn khác
                for (TurnResponse turn : currentTurn.getLeagues()) {
                    if (turn.getUserId() != userId) {
                        turn.setDueNextTime(turn.getDueNextTime() - currentNumberTimeLeft);
                    } else {

                        // log currentTeam
                        Log.e(TAG, "Sau khi endTurn #pickRound: " + pickRound);
                    }
                }

                presenter.endTurnNew(getTurn());

                e.onComplete();
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    private JSONObject getTurn() throws JSONException {
        return new JSONObject(JacksonUtils.writeValueToString(currentTurn));
    }

    @Override
    public void displayLineupPlayers(List<PlayerResponse> players) {
        lineupView.notifyDataSetChanged();
        for (PlayerResponse player : players) {
            PlayerView playerView = lineupView.addPlayer(player,
                    player.getMainPosition(),
                    player.getOrder() == null ? NONE_ORDER : player.getOrder());
            if (player.getLastPickTurn() != null && player.getLastPickTurn().getRound() == pickRound
                    && pickEnable) {
                Log.e(TAG, "displayLineupPlayers: lastPickTurn " + player.getLastPickTurn().getRound() + " pickRound: " + pickRound);
                playerViewSelected = playerView;
                playerView.setRemovable(true);
            }
        }
    }

    @Override
    public void setCountdown(int draftTimeLeft) {
        draftHeader.setVisibility(View.VISIBLE);
        if (draftTimeLeft > 0) {
            initHeader(draftTimeLeft);
        } else {
            draftCountdown.setVisibility(View.GONE);
            draftTeam.setVisibility(View.GONE);
            draftYourTurn.setVisibility(View.GONE);
            draftLoading.setVisibility(View.VISIBLE);

            presenter.getLineup(teamId);
        }
    }

    @Override
    public void displayDraftStatus(int draftStatus) {
        if (draftStatus == DRAFT_FINISHED) {
            draftHeader.setVisibility(View.VISIBLE);
            draftLoading.setVisibility(View.GONE);
            textLineupCompleted.setVisibility(View.VISIBLE);
        }
    }

    private void initHeader(int draftTimeLeft) {
        if (league.getDraftTimeCalendar().after(Calendar.getInstance())) {
            draftCountdown.setVisibility(View.VISIBLE);
            draftTeam.setVisibility(View.GONE);
            draftYourTurn.setVisibility(View.GONE);
            textCountdown.setTimeoutCallback(aVoid -> {
                draftCountdown.setVisibility(View.GONE);
                draftLoading.setVisibility(View.VISIBLE);
            });
            textCountdown.setTime(draftTimeLeft);
            textCountdown.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_HOURS);
            textCountdown.start();
        } else {
            draftCountdown.setVisibility(View.GONE);
            draftTeam.setVisibility(View.GONE);
            draftYourTurn.setVisibility(View.GONE);
            draftLoading.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Click to add player onEvent LineupView
     */
    @Override
    protected void onLineupViewAddClicked(PlayerView playerView, int position, int order) {
        if (pickEnable && playerViewSelected == null) {
            AloneFragmentActivity.with(this)
                    .parameters(PlayerPopupFragment.newBundle(position, order, league))
                    .start(PlayerPopupFragment.class);
        }
    }

    /**
     * onAdd player
     */
    @Override
    protected void onAddClickedFromPopup(PlayerResponse player, int position, int order) {
        if (pickEnable && playerViewSelected == null) {
            if (order == NONE_ORDER) {
                order = lineupView.getOrder(position);
            }
            if (order != -1) {
                playerViewSelected = lineupView.addPlayer(player, position, order);
                if (playerViewSelected != null) {
                    playerViewSelected.setRemovable(true);
                    playerViewSelected.setAddable(false);
                    callback.accept(true, "");
                    try {
                        presenter.addPlayer(getTurn(), teamId, player.getId(), order, pickRound, pickOrder);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updateStatistic(player.getMainPosition(), 1);

                    // log currentTeam
                    Log.e(TAG, "addPlayer: #pickRound: " + pickRound);
                } else {
                    callback.accept(false, getString(R.string.message_full_position));
                }
            }

            if (BuildConfig.DEBUG) {
                if (order == -1) {
                    Toast.makeText(mActivity, "Order = -1 nè", Toast.LENGTH_LONG).show();
                }
                if (position == -1) {
                    Toast.makeText(mActivity, "Position = -1 nè", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            callback.accept(false, getString(R.string.cannot_pick_more));
        }
        callback = null;
    }

    /**
     * Click to Remove Player onEvent LineupView
     */
    @Override
    protected void onLineupViewRemoveClicked(PlayerResponse player, int position, int index) {
        DialogUtils.messageBox(mActivity,
                0,
                getString(R.string.app_name),
                getString(R.string.message_confirm_remove_lineup_player),
                getString(R.string.ok),
                getString(R.string.cancel),
                (dialog, which) -> {
                    if (playerViewSelected != null) {
                        playerViewSelected.setPlayer(null);
                        playerViewSelected = null;
                        try {
                            presenter.removePlayer(getTurn(), teamId, player.getId(), pickRound, pickOrder);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bus.send(new GeneralEvent<>(GeneralEvent.SOURCE.LINEUP_REMOVE_PLAYER));

                        updateStatistic(player.getMainPosition(), -1);

                        // log currentTeam
                        Log.e(TAG, "removePlayer #pickRound: " + pickRound);
                    }
                }, null);
    }

    /**
     * Click to view detail player from LineupView
     */
    @Override
    protected void onLineupViewInfoClicked(PlayerResponse player, int position, int order) {
        PlayerDetailForLineupFragment.start(
                this,
                player,
                -1,
                getString(R.string.lineup),
                league.getGameplayOption(),
                PICK_NONE_INFO,
                position,
                order);
    }
}
