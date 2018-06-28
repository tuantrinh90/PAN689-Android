package com.football.fantasy.fragments.match_up.real;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class MatchupRealDataLeaguePresenter extends BaseDataPresenter<IMatchupRealLeagueView> implements IMatchupRealLeaguePresenter<IMatchupRealLeagueView> {
    /**
     * @param appComponent
     */
    protected MatchupRealDataLeaguePresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
