package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import com.football.di.AppComponent;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpPresenter;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PropsPlayerResponse;
import com.football.utilities.SocketEventKey;

import org.json.JSONException;
import org.json.JSONObject;

public class LineupDraftPresenter extends LineUpPresenter<ILineupDraftView> implements ILineupDraftPresenter<ILineupDraftView> {

    @Override
    protected void onLineupSuccess(LineupResponse response) {
        getOptView().doIfPresent(v -> {
            v.displayLineupPlayers(response.getPlayers());
            v.displayStatistic(response.getStatistic());
        });
    }

    @Override
    protected void onAddPlaySuccess(PropsPlayerResponse response, PlayerResponse player, int position, int order) {
        getOptView().doIfPresent(v -> {
            v.updateStatistic(position == PlayerResponse.POSITION_NONE ? player.getMainPosition() : position, 1);
            v.handleCallback(true, "");
            v.onAddPlayer(response.getTeam(), player, order);
        });
    }

    @Override
    protected void onRemovePlayerSuccess(PropsPlayerResponse response, PlayerResponse player, int position) {
        getOptView().doIfPresent(v -> {
            v.updateStatistic(position, -1);
            v.onRemovePlayer(response.getTeam(), player);
        });
    }

    @Override
    protected void onCompleteLineup() {

    }

    /**
     * @param appComponent
     */
    protected LineupDraftPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void joinRoom(int leagueId) {
        JSONObject room = new JSONObject();
        try {
            room.put("room", "room_" + leagueId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getOptView().get().getAppActivity().getAppContext().getSocket().emit(SocketEventKey.EVENT_JOIN_ROOM, room);

    }
}
