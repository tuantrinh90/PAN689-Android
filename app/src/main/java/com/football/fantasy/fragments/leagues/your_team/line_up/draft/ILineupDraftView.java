package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpView;
import com.football.models.responses.PickTurnResponse;

public interface ILineupDraftView extends ILineUpView {
    void setCountdown(int draftTimeLeft);

    void displayYourTurn(PickTurnResponse yourTurn);
}
