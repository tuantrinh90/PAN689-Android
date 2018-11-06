package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import android.util.Log;

import com.bon.share_preferences.AppPreferences;
import com.football.application.AppContext;
import com.football.di.AppComponent;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpPresenter;
import com.football.listeners.ApiCallback;
import com.football.models.responses.DraftCountdownResponse;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PropsPlayerResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;
import com.football.utilities.SocketEventKey;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class LineupDraftPresenter extends LineUpPresenter<ILineupDraftView> implements ILineupDraftPresenter<ILineupDraftView> {

    private static final String TAG = "LineupDraftPresenter";

    @Override
    protected void setLineup(LineupResponse response) {
        getOptView().doIfPresent(v -> {
            v.displayLineupPlayers(response.getPlayers());
            v.displayStatistic(response.getStatistic());
            v.displayDraftStatus(response.getLeague().getDraftRunning());
        });

        // Log players
//        if (BuildConfig.DEBUG) {
//            mCompositeDisposable.add(
//                    Completable.create(emitter -> {
//                        JSONArray playersJSON = new JSONArray();
//                        for (PlayerResponse player : response.getPlayers()) {
//                            JSONObject json = new JSONObject();
//                            json.put("mainPosition", player.getMainPosition());
//                            json.put("order", player.getOrder());
//                            json.put("name", player.getName());
//                            playersJSON.put(json);
//                        }
//                        Log.e(TAG, "players: " + playersJSON);
//                        emitter.onComplete();
//                    })
//                            .subscribeOn(Schedulers.newThread())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(() -> {
//
//                            }, Throwable::printStackTrace)
//            );
//        }
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
        Completable.create(e -> {
            turn.put("endturn", true);
            getOptView().get().getAppActivity().getAppContext().getSocket().emit(SocketEventKey.EVENT_END_TURN_NEW, turn);
            e.onComplete();
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void addPlayer(JSONObject turn, int teamId, int playerId, int order, int pickRound, int pickOrder) {
        Completable.create(e -> {
            AppContext context = getOptView().get().getAppActivity().getAppContext();
            String token = AppPreferences.getInstance(context).getString(Constant.KEY_TOKEN);

            JSONObject player = new JSONObject();
            player.put("action", "add_player");
            player.put("token", token);
            player.put("team_id", teamId);
            player.put("player_id", playerId);
            player.put("order", order);
            player.put("pick_order", pickOrder);
            player.put("pick_round", pickRound);

            turn.put("playerData", player);

            Log.e(TAG, "addPlayer: " + turn);
            context.getSocket().emit(SocketEventKey.EVENT_ADD_REMOVE_PLAYER, turn);

            e.onComplete();
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void removePlayer(JSONObject turn, int teamId, int playerId, int pickRound, int pickOrder) {
        Completable.create(e -> {
            AppContext context = getOptView().get().getAppActivity().getAppContext();
            String token = AppPreferences.getInstance(context).getString(Constant.KEY_TOKEN);

            JSONObject player = new JSONObject();
            player.put("action", "remove_player");
            player.put("token", token);
            player.put("team_id", teamId);
            player.put("player_id", playerId);
            player.put("pick_order", pickOrder);
            player.put("pick_round", pickRound);

            turn.put("playerData", player);

            Log.e(TAG, "removePlayer: " + turn);
            context.getSocket().emit(SocketEventKey.EVENT_ADD_REMOVE_PLAYER, turn);

            e.onComplete();
        }).subscribeOn(Schedulers.io()).subscribe();
    }

}
