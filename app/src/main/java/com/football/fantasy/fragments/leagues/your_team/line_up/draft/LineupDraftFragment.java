package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.jackson.JacksonUtils;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.customizes.lineup.PlayerView;
import com.football.customizes.progress.ExtProgress;
import com.football.customizes.textview.ExtTextViewCountdown;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailForLineupFragment;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpFragment;
import com.football.fantasy.fragments.leagues.your_team.players_popup.PlayerPopupFragment;
import com.football.models.responses.ChangeTurnResponse;
import com.football.models.responses.PickTurnResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TurnResponse;
import com.football.utilities.SocketEventKey;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_NONE;
import static com.football.utilities.Constant.MAX_SECONDS_CHANGE_TURN;

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

    private PlayerView playerViewSelected;
    private boolean pickEnable = false;
    private int currentRequestId;
    private TurnResponse currentTurn;

    @NonNull
    @Override
    public ILineupDraftPresenter<ILineupDraftView> createPresenter() {
        return new LineupDraftPresenter(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerSocket();
        presenter.endCountdown(league.getId());

        // join league
        presenter.joinRoom(league.getId());
    }

    @Override
    public void onDestroyView() {
        getAppContext().off(SocketEventKey.EVENT_CHANGE_LINEUP);
        getAppContext().off(SocketEventKey.EVENT_CHANGE_TURN);
        presenter.leaveLeague(league.getId());
        textCountdown.onDestroyView();
        tvDraftYourTurnTimeLeft.onDestroyView();
        tvDraftCurrentTimeLeft.onDestroyView();
        progressDraft.onDestroyView();
        super.onDestroyView();
    }

    private void initHeader(int draftTimeLeft) {
        if (league.getDraftTimeCalendar().after(Calendar.getInstance())) {
            draftCountdown.setVisibility(View.VISIBLE);
            draftTurn.setVisibility(View.GONE);
            textCountdown.setTimeoutCallback(aVoid -> {
                draftCountdown.setVisibility(View.GONE);
                draftLoading.setVisibility(View.VISIBLE);
                presenter.joinDraftPick(league.getId());
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

    private void registerSocket() {
        getAppContext().getSocket().on(SocketEventKey.EVENT_CHANGE_TURN, args -> {
            Log.i(SocketEventKey.EVENT_CHANGE_TURN, "");
            if (args != null && args.length > 0 && args[0] != null) {
                JSONObject jsonObject = (JSONObject) args[0];
                ChangeTurnResponse response = JacksonUtils.convertJsonToObject(jsonObject.toString(), ChangeTurnResponse.class);
                Log.i(TAG, "EVENT_CHANGE_TURN: " + jsonObject.toString());
                if (response != null && currentRequestId != response.getCurrent().getId()) {
                    currentRequestId = response.getCurrent().getId();
                    TurnResponse current = response.getCurrent();
                    TurnResponse next = response.getNext();
                    TurnResponse next2 = response.getNext2();
                    mActivity.runOnUiThread(() -> {
                        textCountdown.stop();
                        draftCountdown.setVisibility(View.GONE);
                        draftTurn.setVisibility(View.VISIBLE);
                        draftLoading.setVisibility(View.GONE);

                        // remove old callback
                        tvDraftCurrentTimeLeft.setTimeoutCallback(null);

                        setTurn(current, next, next2, current.getDraftTimeLeft(), false);

                    });
                }
            } else {
                Log.e(TAG, "SocketEventKey.EVENT_CHANGE_TURN: args are null");
            }
        });

        getAppContext().getSocket().on(SocketEventKey.EVENT_CHANGE_LINEUP, args -> {
            Log.i(SocketEventKey.EVENT_CHANGE_LINEUP, "");
            if (pickEnable && args != null && args.length > 0 && args[0] != null) {
                Log.i(TAG, "EVENT_CHANGE_LINEUP: " + args[0].toString());
                mActivity.runOnUiThread(() -> {
                    textCountdown.stop();
                    presenter.getLineup(teamId);
                });
            } else {
                Log.e(TAG, "SocketEventKey.EVENT_CHANGE_LINEUP: not your turn");
            }
        });

        getAppContext().getSocket().on(SocketEventKey.EVENT_END_TURN, args -> {
            Log.i(SocketEventKey.EVENT_END_TURN, "");
            if (mActivity != null) mActivity.runOnUiThread(() -> {
                draftHeader.setVisibility(View.GONE);
                pickEnable = false;
//                showMessage("Xong roi nha", R.string.ok, avoid -> {
//                    getActivity().finish();
//                });
            });
        });
    }

    private void setTurn(TurnResponse current, TurnResponse next, TurnResponse next2, int timeLeft, boolean recursion) {
        if (current == null) return;

        currentTurn = current;
        draftTeam.setVisibility(View.VISIBLE);
        // if your turn, visible view
        if (current.getTeam().getId() == teamId) {
            setYourTurn(true);

            // enable lineupView
            enableLineupView(true);

            // set currentTurn team's Name
            tvDraftCurrentTeam.setText("YOUR TURN");
        } else {
            setYourTurn(false);

            // disable lineupView
            enableLineupView(false);

            // set currentTurn team's Name
            tvDraftCurrentTeam.setText(current.getTeam().getName());
        }

        // countdown Current 00:00:00
        tvDraftCurrentTimeLeft.setTime(timeLeft);
        tvDraftCurrentTimeLeft.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_HOURS);
        tvDraftCurrentTimeLeft.start();
        if (!recursion) {
            tvDraftCurrentTimeLeft.setTimeoutCallback(aVoid -> {
                setTurn(next, next2, null, MAX_SECONDS_CHANGE_TURN, true);

                // call End turn
                endTurn();
                presenter.joinDraftPick(league.getId());
            });
        }

        // set next team's name
        if (next != null) {
            if (next.getTeam().getId() == teamId) {
                tvDraftNextTeam.setText("YOUR TURN");
            } else {
                tvDraftNextTeam.setText(next.getTeam().getName());
            }
        } else {
            tvDraftNextTeam.setText("null");
        }
    }

    private void setYourTurn(boolean yourTurn) {
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

    private void enableLineupView(boolean enable) {
        lineupView.setAddable(enable);
        pickEnable = enable;
    }

    private void endTurn() {
        if (currentTurn != null) {
            presenter.endTurn(teamId, currentTurn.getRound(), currentTurn.getOrder());
        }
    }

    @OnClick({R.id.tvDraftEndTurn})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.tvDraftEndTurn:
                endTurn();
                break;
        }
    }

    @Override
    public void setCountdown(int draftTimeLeft) {
        if (draftTimeLeft > 0) {
            initHeader(draftTimeLeft);
        } else {
            presenter.joinDraftPick(league.getId());

            draftCountdown.setVisibility(View.GONE);
            draftTurn.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayYourTurn(PickTurnResponse yourTurn) {
        tvDraftYourTurnTimeLeft.setTime(yourTurn.getYourTurnIn());
        tvDraftYourTurnTimeLeft.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_SECONDS_ONLY);
        tvDraftYourTurnTimeLeft.start();
        progressDraft.setMax(yourTurn.getYourTurnIn());
        progressDraft.setProgress(yourTurn.getYourTurnIn());
        progressDraft.start();
    }

    @Override
    protected void onLineupViewAddClicked(PlayerView playerView, int position, int order) {
        if (playerViewSelected == null) {
            AloneFragmentActivity.with(this)
                    .parameters(PlayerPopupFragment.newBundle(position, order, league))
                    .start(PlayerPopupFragment.class);
        }
    }

    @Override
    protected void onAddClickedFromPopup(PlayerResponse player, int position, int order) {
        playerViewSelected = lineupView.addPlayer(player, player.getMainPosition(), order);
        playerViewSelected.setRemovable(true);
        playerViewSelected.setAddable(false);
        callback.accept(true, "");
        callback = null;
        presenter.addPlayer(player, teamId, position, order);
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
                        presenter.removePlayer(player, position, index);
                    }
                },
                (dialog, which) -> {
                });
    }

    @Override
    protected void onLineupViewInfoClicked(PlayerResponse player, int position, int order) {
        PlayerDetailForLineupFragment.start(
                this,
                player,
                -1,
                getString(R.string.lineup),
                league.getGameplayOption(),
                PICK_NONE,
                position,
                order);
    }
}
