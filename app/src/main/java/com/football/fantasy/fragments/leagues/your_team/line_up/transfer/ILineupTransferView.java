package com.football.fantasy.fragments.leagues.your_team.line_up.transfer;

import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpView;
import com.football.models.responses.TeamResponse;

public interface ILineupTransferView extends ILineUpView {

    void displayTeamState(TeamResponse team);

    void enableCompleteButton(boolean enable);

    void checkCompleted(boolean firstCheck);

    void onCompleteLineup();


}
