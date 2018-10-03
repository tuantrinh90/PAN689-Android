package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.jackson.JacksonUtils;
import com.bon.share_preferences.AppPreferences;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.customizes.lineup.PlayerView;
import com.football.customizes.progress.ExtProgress;
import com.football.customizes.textview.ExtTextViewCountdown;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailForLineupFragment;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpFragment;
import com.football.fantasy.fragments.leagues.your_team.players_popup.PlayerPopupFragment;
import com.football.models.responses.PickTurnResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TurnReceiveResponse;
import com.football.models.responses.TurnResponse;
import com.football.utilities.Constant;
import com.football.utilities.SocketEventKey;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_NONE_INFO;

public class LineupDraftFragment extends LineUpFragment<ILineupDraftView, ILineupDraftPresenter<ILineupDraftView>> implements ILineupDraftView {

    private static final String TAG = "LineupDraftFragment";

    @BindView(R.id.draft_countdown)
    View draftCountdown;
    @BindView(R.id.draft_loading)
    View draftLoading;
    @BindView(R.id.draft_turn)
    View draftTurn;
    @BindView(R.id.draft_team)
    View draftTeam;
    @BindView(R.id.text_countdown)
    ExtTextViewCountdown textCountdown;
    @BindView(R.id.tvDraftYourTurnTimeLeft)
    ExtTextViewCountdown tvDraftYourTurnTimeLeft;
    @BindView(R.id.tvDraftYourTurnTimeUnit)
    ExtTextView tvDraftYourTurnTimeUnit;
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

    private int userId;

    private PlayerView playerViewSelected;
    private boolean pickEnable = false;
    private JSONObject currentTurn;
    private int pickRound;
    private int pickOrder;

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
        tvDraftYourTurnTimeLeft.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_SECONDS_ONLY);
        tvDraftCurrentTimeLeft.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_HOURS);
    }

    @Override
    public void onDestroyView() {
        getAppContext().off(SocketEventKey.EVENT_CHANGE_LINEUP);
        getAppContext().off(SocketEventKey.EVENT_END_TURN);
        getAppContext().off(SocketEventKey.EVENT_TURN_RECEIVE);
        getAppContext().off(SocketEventKey.EVENT_REFRESH_UI);
        getAppContext().off(SocketEventKey.EVENT_PICK_TURN_FINISH);
        presenter.leaveLeague(league.getId());
        textCountdown.onDestroyView();
        tvDraftYourTurnTimeLeft.onDestroyView();
        tvDraftCurrentTimeLeft.onDestroyView();
        progressDraft.onDestroyView();
        super.onDestroyView();
    }

    private void registerSocket() {
        getAppContext().getSocket().on(SocketEventKey.EVENT_CHANGE_LINEUP, args -> {
            Log.i(TAG, "=======================================================");
            Log.i(TAG, "EVENT_CHANGE_LINEUP");
            Log.i(TAG, args != null && args.length > 0 && args[0] != null ? args[0].toString() : "null");
            Log.i(TAG, "=======================================================");
        });

        getAppContext().getSocket().on(SocketEventKey.EVENT_END_TURN, args -> {
            Log.i(TAG, "=======================================================");
            Log.i(TAG, "EVENT_END_TURN");
            Log.i(TAG, "=======================================================");
            onEventEndTurn();
        });

        /*
         * trả về object
         */
        getAppContext().getSocket().on(SocketEventKey.EVENT_TURN_RECEIVE, args -> {
            Log.i(TAG, "=======================================================");
            Log.i(TAG, "EVENT_TURN_RECEIVE");
            Log.i(TAG, args != null && args.length > 0 && args[0] != null ? args[0].toString() : "null");
            Log.i(TAG, "=======================================================");
            if (args != null && args.length > 0 && args[0] != null) {
                TurnReceiveResponse response = JacksonUtils.convertJsonToObject(args[0].toString(), TurnReceiveResponse.class);
                if (response != null && mActivity != null) {
                    currentTurn = (JSONObject) args[0];
                    mActivity.runOnUiThread(() -> onEventTurnReceive(response));
                }
            }
        });

        /*
         * trả về object => refresh đội hình
         */
        getAppContext().getSocket().on(SocketEventKey.EVENT_REFRESH_UI, args -> {
            Log.i(TAG, "=======================================================");
            Log.i(TAG, "EVENT_REFRESH_UI");
            Log.i(TAG, args != null && args.length > 0 && args[0] != null ? args[0].toString() : "null");
            Log.i(TAG, "=======================================================");
            presenter.getLineup(teamId);
        });

        /*
          trả về object => kết thúc quá trình pick cầu thủ
         */
        getAppContext().getSocket().on(SocketEventKey.EVENT_PICK_TURN_FINISH, args -> {
            Log.i(TAG, "=======================================================");
            Log.i(TAG, "EVENT_PICK_TURN_FINISH");
            Log.i(TAG, args != null && args.length > 0 && args[0] != null ? args[0].toString() : "null");
            Log.i(TAG, "=======================================================");
            onEventEndTurn();
        });
    }

    private void onEventTurnReceive(TurnReceiveResponse response) {
        // stop countdown & visible/gone any views
        textCountdown.stop();
        draftHeader.setVisibility(View.VISIBLE);
        draftCountdown.setVisibility(View.GONE);
        draftTurn.setVisibility(View.VISIBLE);
        draftLoading.setVisibility(View.GONE);

        pickRound = response.getPickRound();
        tvDraftCurrentTimeLeft.setTime(response.getNumber());
        for (TurnResponse turn : response.getLeagues()) {
            if (turn.isCurrent()) {
                pickOrder = turn.getPickOrder();

                // cập nhật YOUR TURN or {name}
                if (turn.getUserId() == userId) {
                    tvDraftCurrentTeam.setText(getString(R.string.your_turn_cap));

                    setYourTurn(true);
                } else {
                    tvDraftCurrentTeam.setText(turn.getName());

                    setYourTurn(false);
                }

            } else if (turn.isNext()) {
                if (turn.getUserId() == userId) {
                    tvDraftNextTeam.setText(getString(R.string.your_turn_cap));
                } else {
                    tvDraftNextTeam.setText(turn.getName());
                }
            }
        }
    }

    private void onEventEndTurn() {
        if (mActivity != null) mActivity.runOnUiThread(() -> {
            draftHeader.setVisibility(View.GONE);
            pickEnable = false;
            tvDraftCurrentTimeLeft.stop();
        });
    }

    private void setYourTurn(boolean yourTurn) {
        lineupView.setAddable(yourTurn);
        pickEnable = yourTurn;
        if (yourTurn) {
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
            tvDraftYourTurnTimeUnit.setVisibility(View.VISIBLE);
        }
    }

    private void endTurnNew() {
        if (pickEnable) {
            presenter.endTurnNew(currentTurn);
        }
    }

    @OnClick({R.id.tvDraftEndTurn})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.tvDraftEndTurn:
                endTurnNew();
                break;
        }
    }

    @Override
    public void setCountdown(int draftTimeLeft) {
        draftHeader.setVisibility(View.VISIBLE);
        if (draftTimeLeft > 0) {
            initHeader(draftTimeLeft);
        } else {
            draftCountdown.setVisibility(View.GONE);
            draftTurn.setVisibility(View.GONE);
            draftLoading.setVisibility(View.VISIBLE);

            presenter.joinDraft(league.getId());
            presenter.getLineup(teamId);
        }
    }

    private void initHeader(int draftTimeLeft) {
        if (league.getDraftTimeCalendar().after(Calendar.getInstance())) {
            draftCountdown.setVisibility(View.VISIBLE);
            draftTurn.setVisibility(View.GONE);
            textCountdown.setTimeoutCallback(aVoid -> {
                draftCountdown.setVisibility(View.GONE);
                draftLoading.setVisibility(View.VISIBLE);

                presenter.joinDraft(league.getId());
            });
            textCountdown.setTime(draftTimeLeft);
            textCountdown.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_HOURS);
            textCountdown.start();
        } else {
            draftCountdown.setVisibility(View.GONE);
            draftTurn.setVisibility(View.GONE);
            draftLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayYourTurn(PickTurnResponse yourTurn) {
        tvDraftYourTurnTimeLeft.setTime(yourTurn.getYourTurnIn());
        tvDraftYourTurnTimeLeft.start();
        progressDraft.setMax(yourTurn.getYourTurnIn());
        progressDraft.setProgress(yourTurn.getYourTurnIn());
        progressDraft.start();
    }

    @Override
    protected void onLineupViewAddClicked(PlayerView playerView, int position, int order) {
        if (pickEnable && playerViewSelected == null) {
            AloneFragmentActivity.with(this)
                    .parameters(PlayerPopupFragment.newBundle(position, order, league))
                    .start(PlayerPopupFragment.class);
        }
    }

    @Override
    protected void onAddClickedFromPopup(PlayerResponse player, int position, int order) {
        if (pickEnable) {
            playerViewSelected = lineupView.addPlayer(player, player.getMainPosition(), order);
            playerViewSelected.setRemovable(true);
            playerViewSelected.setAddable(false);
            callback.accept(true, "");
            presenter.addPlayer(player, teamId, position, order, pickRound, pickOrder);
        }
        callback = null;
    }

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
                        presenter.removePlayer(player, teamId, position, pickRound, pickOrder);
                    }
                },
                null);
    }

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
