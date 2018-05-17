package com.football.fantasy.fragments.leagues.action;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class ActionLeaguePresenter extends BaseDataPresenter<IActionLeagueView> implements IActionLeaguePresenter<IActionLeagueView> {
    /**
     * @param appComponent
     */
    public ActionLeaguePresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
