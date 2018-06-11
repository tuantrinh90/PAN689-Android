package com.football.fantasy.fragments.leagues.your_team.line_up;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.PlayerResponse;

public interface ILineUpView extends IBaseMvpView {

    void displayLineup(LineupResponse response);

    void onAddPlayer(PlayerResponse response);
}
