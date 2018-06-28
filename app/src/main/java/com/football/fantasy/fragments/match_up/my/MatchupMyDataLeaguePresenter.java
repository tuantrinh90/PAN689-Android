package com.football.fantasy.fragments.match_up.my;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class MatchupMyDataLeaguePresenter extends BaseDataPresenter<IMatchupMyLeagueView> implements IMatchupMyLeaguePresenter<IMatchupMyLeagueView> {
    /**
     * @param appComponent
     */
    protected MatchupMyDataLeaguePresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
