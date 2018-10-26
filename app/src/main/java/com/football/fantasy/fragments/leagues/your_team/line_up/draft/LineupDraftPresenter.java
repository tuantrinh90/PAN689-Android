package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import android.util.Log;

import com.football.di.AppComponent;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpPresenter;
import com.football.listeners.ApiCallback;
import com.football.models.responses.DraftCountdownResponse;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PropsPlayerResponse;
import com.football.models.responses.TurnReceiveResponse;
import com.football.models.responses.TurnResponse;
import com.football.utilities.RxUtilities;
import com.football.utilities.SocketEventKey;
import com.github.nkzawa.socketio.client.Ack;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;

public class LineupDraftPresenter extends LineUpPresenter<ILineupDraftView> implements ILineupDraftPresenter<ILineupDraftView> {

    private static final String TAG = "LineupDraftPresenter";

    @Override
    protected void setLineup(LineupResponse response) {
        getOptView().doIfPresent(v -> {
            v.displayLineupPlayers(response.getPlayers());
            v.displayStatistic(response.getStatistic());
        });
    }

    @Override
    protected void addPlaySuccess(PropsPlayerResponse response, PlayerResponse player, int position, int order) {
        getOptView().doIfPresent(v -> {
            v.updateStatistic(position == PlayerResponse.POSITION_NONE ? player.getMainPosition() : position, 1);
            v.handleCallback(true, "");
        });
    }

    @Override
    protected void removePlayerSuccess(PropsPlayerResponse response, PlayerResponse player, int position) {
        getOptView().doIfPresent(v -> {
            v.updateStatistic(position, -1);
            v.removePlayerSuccess(response.getTeam(), player);
        });
    }

    @Override
    protected void completeLineupSuccess() {

    }

    /**
     * @param appComponent
     */
    protected LineupDraftPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void joinRoom(int leagueId) {
        Log.i(TAG, "joinRoom: ");
        JSONObject room = new JSONObject();
        try {
            room.put("room", "room_" + leagueId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getOptView().get().getAppActivity().getAppContext().getSocket().emit(SocketEventKey.EVENT_JOIN_ROOM, room);

    }

    @Override
    public void leaveLeague(int leagueId) {
        Log.i(TAG, "leaveLeague: ");
        JSONObject room = new JSONObject();
        try {
            room.put("room", "room_" + leagueId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getOptView().get().getAppActivity().getAppContext().getSocket().emit(SocketEventKey.EVENT_LEAVE_ROOM, room);
    }

    @Override
    public void endCountdown(int leagueId) {
        Log.i(TAG, "endCountdown: ");
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().endCountdown(leagueId),
                    new ApiCallback<DraftCountdownResponse>() {
                        @Override
                        public void onSuccess(DraftCountdownResponse response) {
                            v.setCountdown(response.getDraftTimeLeft());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void endTurnNew(JSONObject turn) {
        try {
            turn.put("endturn", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getOptView().get().getAppActivity().getAppContext().getSocket().emit(SocketEventKey.EVENT_END_TURN_NEW, turn, new Ack() {
            @Override
            public void call(Object... args) {

            }
        });
    }

    @Override
    public void addPlayer(PlayerResponse player, int teamId, int position, int order, int pickRound, int pickOrder) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().addPlayer(
                            teamId,
                            new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("pick_round", String.valueOf(pickRound))
                                    .addFormDataPart("pick_order", String.valueOf(pickOrder))
                                    .addFormDataPart("player_id", String.valueOf(player.getId()))
                                    .addFormDataPart("order", String.valueOf(order))
                                    .build()),
                    null));
        });
    }

    @Override
    public void removePlayer(PlayerResponse player, int teamId, int pickRound, int pickOrder) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().removePlayer(
                            teamId,
                            new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("pick_round", String.valueOf(pickRound))
                                    .addFormDataPart("pick_order", String.valueOf(pickOrder))
                                    .addFormDataPart("player_id", String.valueOf(player.getId()))
                                    .build()),
                    null));
        });
    }

}
