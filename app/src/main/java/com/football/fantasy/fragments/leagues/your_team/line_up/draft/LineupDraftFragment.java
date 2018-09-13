package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bon.jackson.JacksonUtils;
import com.football.customizes.textview.ExtTextViewCountdown;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpFragment;
import com.football.models.responses.ChangeTurnResponse;
import com.football.models.responses.PlayerResponse;
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
    @BindView(R.id.draft_your_turn)
    View draftYourTurn;
    @BindView(R.id.text_countdown)
    ExtTextViewCountdown textCountdown;

    @NonNull
    @Override
    public ILineupDraftPresenter<ILineupDraftView> createPresenter() {
        return new LineupDraftPresenter(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeader();
        registerSocket();

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
        super.onDestroyView();
    }

    private void initHeader() {
        if (league.getDraftTimeCalendar().after(Calendar.getInstance())) {
            draftCountdown.setVisibility(View.VISIBLE);
            draftTurn.setVisibility(View.GONE);
            textCountdown.setTimeoutCallback(aVoid -> {
                draftCountdown.setVisibility(View.GONE);
                draftLoading.setVisibility(View.VISIBLE);
            });
            textCountdown.setTime(league.getDraftTimeCalendar().getTimeInMillis() / 1000 - Calendar.getInstance().getTimeInMillis() / 1000);
            textCountdown.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_HOURS);
            textCountdown.start();
        } else {
            draftCountdown.setVisibility(View.GONE);
            draftTurn.setVisibility(View.VISIBLE);
        }
    }

    private void registerSocket() {
        getAppContext().getSocket().on(SocketEventKey.EVENT_CHANGE_TURN, args -> {
            Log.i(SocketEventKey.EVENT_CHANGE_TURN, "");
            mActivity.runOnUiThread(() -> {
                draftCountdown.setVisibility(View.GONE);
                draftTurn.setVisibility(View.VISIBLE);
                draftLoading.setVisibility(View.GONE);

                if (args != null && args.length > 0) {
                    JSONObject jsonObject = (JSONObject) args[0];
                    Log.i(TAG, "EVENT_CHANGE_TURN: " + jsonObject.toString());
                    ChangeTurnResponse response = JacksonUtils.convertJsonToObject(jsonObject.toString(), ChangeTurnResponse.class);
                    if (response != null) {
                        TurnResponse current = response.getCurrent();
                        TurnResponse next = response.getNext();
                        setTurn(current, next, current.getDraftTimeLeft());
                    }
                } else {
                    Log.e(TAG, "registerSocket: args are null");
                }
            });
        });

        getAppContext().getSocket().on(SocketEventKey.EVENT_CHANGE_LINEUP, args -> {
            Log.i(SocketEventKey.EVENT_CHANGE_LINEUP, "");
            if (args != null && args.length > 0) {
                Log.i(TAG, "EVENT_CHANGE_LINEUP: " + args[0].toString());
                mActivity.runOnUiThread(() -> {
                    PlayerResponse player = JacksonUtils.convertJsonToObject(args[0].toString(), PlayerResponse.class);
                    lineupView.addPlayer(player, player.getMainPosition(), player.getOrder() == null ? NONE_ORDER : player.getOrder());
                    updateStatistic(player.getMainPosition(), 1);
                });
            }
        });
    }


    private void setTurn(TurnResponse current, TurnResponse next, int timeLeft) {
        if (current != null) {
            // if your turn
            if (current.getTeam().getId() == teamId) {
                draftYourTurn.setVisibility(View.VISIBLE);
                draftTeam.setVisibility(View.GONE);

                // your turn view
                tvDraftYourTurnTimeLeft.setTime(timeLeft);
                tvDraftYourTurnTimeLeft.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_SECONDS_ONLY);
                tvDraftYourTurnTimeLeft.start();
                tvDraftYourTurnTimeLeft.setTimeoutCallback(aVoid -> {
                    setTurn(next, null, MAX_SECONDS_CHANGE_TURN);
                });

                progressDraft.setProgress(timeLeft);
                progressDraft.setMax(MAX_SECONDS_CHANGE_TURN);
                progressDraft.start();
            } else {
                draftYourTurn.setVisibility(View.GONE);
                draftTeam.setVisibility(View.VISIBLE);

                // current view
                tvDraftCurrentTimeLeft.setTime(timeLeft);
                tvDraftCurrentTimeLeft.setFormatType(ExtTextViewCountdown.FORMAT_NUMBER_HOURS);
                tvDraftCurrentTimeLeft.start();
                tvDraftCurrentTimeLeft.setTimeoutCallback(aVoid -> {
                    setTurn(next, null, MAX_SECONDS_CHANGE_TURN);
                });
                tvDraftCurrentTeam.setText(current.getTeam().getName());
            }
        }

        // next view
        tvDraftNextTeam.setText(next != null ? next.getTeam().getName() : "");
    }

    @OnClick({R.id.tvDraftEndTurn})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.tvDraftEndTurn:

                break;
        }
    }
}
