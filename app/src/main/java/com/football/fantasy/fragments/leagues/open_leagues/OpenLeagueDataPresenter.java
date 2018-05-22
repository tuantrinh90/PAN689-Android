package com.football.fantasy.fragments.leagues.open_leagues;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class OpenLeagueDataPresenter extends BaseDataPresenter<IOpenLeagueView> implements IOpenLeaguePresenter<IOpenLeagueView> {
    /**
     * @param appComponent
     */
    public OpenLeagueDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
