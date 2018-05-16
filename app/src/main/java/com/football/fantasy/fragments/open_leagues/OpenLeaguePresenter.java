package com.football.fantasy.fragments.open_leagues;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class OpenLeaguePresenter extends BaseDataPresenter<IOpenLeagueView> implements IOpenLeaguePresenter<IOpenLeagueView> {
    /**
     * @param appComponent
     */
    public OpenLeaguePresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
