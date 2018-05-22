package com.football.fantasy.fragments.leagues.league_details;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class LeagueDetailDataPresenter extends BaseDataPresenter<ILeagueDetailView> implements ILeagueDetailPresenter<ILeagueDetailView> {
    /**
     * @param appComponent
     */
    public LeagueDetailDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
