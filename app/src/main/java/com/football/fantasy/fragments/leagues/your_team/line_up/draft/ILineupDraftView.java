package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpView;

public interface ILineupDraftView extends ILineUpView {
    void setCountdown(int draftTimeLeft);
}
