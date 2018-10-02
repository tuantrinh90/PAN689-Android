package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpPresenter;
import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpView;
import com.football.models.responses.PlayerResponse;

public interface ILineupDraftPresenter<V extends ILineUpView> extends ILineUpPresenter<V> {

    /**
     * register socket
     */
    void joinRoom(int leagueId);

    void leaveLeague(int leagueId);

    void addPlayer(PlayerResponse player, int teamId, int position, int order, int pickRound, int pickOrder);

    void removePlayer(PlayerResponse player, int position, int teamId, int pickRound, int pickOrder);

    /**
     * Join to draft pick turn in a League.
     */
    void joinDraft(int leagueId);

    /**
     * End count down a League.
     */
    void endCountdown(int leagueId);

    /**
     * End turn when draft in the Team.
     */
    void endTurn(int teamId, int pickRound, int pickOrder);

    void endTurnNew();
}
