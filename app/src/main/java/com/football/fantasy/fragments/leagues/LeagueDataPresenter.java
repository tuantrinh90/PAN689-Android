package com.football.fantasy.fragments.leagues;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class LeagueDataPresenter extends BaseDataPresenter<ILeagueView> implements ILeaguePresenter<ILeagueView> {
    /**
     * @param appComponent
     */
    protected LeagueDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
