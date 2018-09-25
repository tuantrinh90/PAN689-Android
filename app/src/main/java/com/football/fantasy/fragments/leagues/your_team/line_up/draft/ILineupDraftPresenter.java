package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpPresenter;
import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpView;
import com.football.models.responses.PlayerResponse;

public interface ILineupDraftPresenter<V extends ILineUpView> extends ILineUpPresenter<V> {

    void joinRoom(int leagueId);

    void leaveLeague(int leagueId);

    void joinDraftPick(int leagueId);

    void endCountdown(int leagueId);

    void endTurn(int teamId, int pickRound, int pickOrder);
}
