package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.jackson.JacksonUtils;
import com.football.customizes.progress.ExtProgress;
import com.football.customizes.textview.ExtTextViewCountdown;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpFragment;
import com.football.models.responses.ChangeTurnResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.TurnResponse;
import com.football.utilities.SocketEventKey;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;
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
    @BindView(R.id.tvDraftYourTurnTimeUnit)
    ExtTextView tvDraftYourTurnTimeUnit;
    @BindView(R.id.progress_draft)
    ExtProgress progressDraft;

    private boolean pickEnable = false;

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
            mActivity.runOnUiThread(() -> {
                textCountdown.stop();
                draftCountdown.setVisibility(View.GONE);
                draftTurn.setVisibility(View.VISIBLE);
                draftLoading.setVisibility(View.GONE);

                if (args != null && args.length > 0 && args[0] != null) {
                    JSONObject jsonObject = (JSONObject) args[0];
                    Log.i(TAG, "EVENT_CHANGE_TURN: " + jsonObject.toString());
                    ChangeTurnResponse response = JacksonUtils.convertJsonToObject(jsonObject.toString(), ChangeTurnResponse.class);
                    if (response != null) {
                        TurnResponse current = response.getCurrent();
                        TurnResponse next = response.getNext();
                        setTurn(current, next, current.getDraftTimeLeft());
                    }
                } else {
                    Log.e(TAG, "SocketEventKey.EVENT_CHANGE_TURN: args are null");
                }
            });
        });

        getAppContext().getSocket().on(SocketEventKey.EVENT_CHANGE_LINEUP, args -> {
            Log.i(SocketEventKey.EVENT_CHANGE_LINEUP, "");
            if (pickEnable && args != null && args.length > 0 && args[0] != null) {
                Log.i(TAG, "EVENT_CHANGE_LINEUP: " + args[0].toString());
                mActivity.runOnUiThread(() -> {
                    textCountdown.stop();

                    PlayerResponse player = JacksonUtils.convertJsonToObject(args[0].toString(), PlayerResponse.class);
                    lineupView.addPlayer(player, player.getMainPosition(), player.getOrder() == null ? NONE_ORDER : player.getOrder());
                    updateStatistic(player.getMainPosition(), 1);
                });
            } else {
                Log.e(TAG, "SocketEventKey.EVENT_CHANGE_LINEUP: not your turn");
            }
        });
    }

    private void setYourTurn(int timeLeft) {
        // countdown YOUR TURN _s
        tvDraftYourTurnTimeLeft.setTime(timeLeft);
        tvDraftYourTurnTimeLeft.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_SECONDS_ONLY);
        tvDraftYourTurnTimeLeft.start();
        tvDraftYourTurnTimeLeft.setTimeoutCallback(aVoid -> {
//            setYourTurn();
        });

        // progress
        progressDraft.setProgress(timeLeft);
        progressDraft.setMax(MAX_SECONDS_CHANGE_TURN);
        progressDraft.start();

    }

    private void setTurn(TurnResponse current, TurnResponse next, int timeLeft) {
        draftTeam.setVisibility(View.VISIBLE);
        // if your turn, visible view
        if (current != null && current.getTeam().getId() == teamId) {
            visibleYourTurn();

            // enable lineupView
            enableLineupView(true);

            setYourTurn(timeLeft);
        } else {
            goneYourTurn();

            // disable lineupView
            enableLineupView(false);
        }

        // countdown Current 00:00:00
        tvDraftCurrentTimeLeft.setTime(timeLeft);
        tvDraftCurrentTimeLeft.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_HOURS);
        tvDraftCurrentTimeLeft.start();
        tvDraftCurrentTimeLeft.setTimeoutCallback(aVoid -> {
            setTurn(next, null, MAX_SECONDS_CHANGE_TURN);
        });

        // set current team's Name
        tvDraftCurrentTeam.setText(current != null ? current.getTeam().getName() : "null");

        // set next team's name
        tvDraftNextTeam.setText(next != null ? next.getTeam().getName() : "null");
    }

    private void visibleYourTurn() {
        tvDraftEndTurn.setVisibility(View.VISIBLE);
        progressDraft.setVisibility(View.GONE);
        tvDraftYourTurnTimeUnit.setVisibility(View.GONE);
    }

    private void goneYourTurn() {
        tvDraftEndTurn.setVisibility(View.GONE);
        progressDraft.setVisibility(View.VISIBLE);
        tvDraftYourTurnTimeUnit.setVisibility(View.VISIBLE);
    }

    private void enableLineupView(boolean enable) {
        lineupView.setAddable(enable);
        lineupView.setPickDraftMode(enable);
        pickEnable = enable;
    }

    @Override
    public void onAddPlayer(TeamResponse team, PlayerResponse player, int order) {
        lineupView.setPlayerFocus(player);
        super.onAddPlayer(team, player, order);
    }

    @OnClick({R.id.tvDraftEndTurn})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.tvDraftEndTurn:
                lineupView.getPlayerFocus();
                break;
        }
    }

    @Override
    public void setCountdown(int draftTimeLeft) {
        if (draftTimeLeft > 0) {
            initHeader(draftTimeLeft);
        } else {
            presenter.joinDraftPick(league.getId());
        }
    }
}
