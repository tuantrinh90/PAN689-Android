package com.football.fantasy.fragments.leagues;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class LeaguePresenter extends BaseDataPresenter<ILeagueView> implements ILeaguePresenter<ILeagueView> {
    /**
     * @param appComponent
     */
    protected LeaguePresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
