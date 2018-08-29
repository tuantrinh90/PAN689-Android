package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import com.football.application.AppContext;
import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpPresenter;
import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpView;

public interface ILineupDraftPresenter<V extends ILineUpView> extends ILineUpPresenter<V> {
    String getToken(AppContext appContext);
}
