package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpPresenter;
import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpView;
import com.football.models.responses.PlayerResponse;

import org.json.JSONObject;

public interface ILineupDraftPresenter<V extends ILineUpView> extends ILineUpPresenter<V> {

    /**
     * register socket
     */
    void joinRoom(int leagueId);

    void leaveLeague(int leagueId);

    void addPlayer(JSONObject turn, int teamId, int playerId, int pickRound, int pickOrder);

    void removePlayer(JSONObject turn, int teamId, int playerId, int pickRound, int pickOrder);

    /**
     * End count down a League.
     */
    void endCountdown(int leagueId);

    /**
     * End turn when draft in the Team.
     */
    void endTurnNew(JSONObject turn);
}
