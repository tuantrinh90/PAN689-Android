package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import com.bon.share_preferences.AppPreferences;
import com.football.application.AppContext;
import com.football.di.AppComponent;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpPresenter;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.Constant;

public class LineupDraftDataPresenter extends LineUpPresenter<ILineupDraftView> implements ILineupDraftPresenter<ILineupDraftView> {
    /**
     * @param appComponent
     */
    protected LineupDraftDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getLineup(int teamId) {

    }

    @Override
    public void addPlayer(PlayerResponse player, int teamId, int position, int order) {

    }

    @Override
    public void removePlayer(PlayerResponse player, int position, int teamId) {

    }
}
