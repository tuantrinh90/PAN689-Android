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

    }

    @Override
    protected void onAddPlaySuccess(PropsPlayerResponse response, PlayerResponse player, int position, int order) {

    }

    @Override
    protected void onRemovePlayerSuccess(PropsPlayerResponse response, PlayerResponse player, int position) {

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
            room.put("room", leagueId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getOptView().get().getAppActivity().getAppContext().getSocket().emit(SocketEventKey.EVENT_JOIN_ROOM, room);

    }
}
