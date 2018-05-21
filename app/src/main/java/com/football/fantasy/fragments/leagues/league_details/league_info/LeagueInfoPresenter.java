package com.football.fantasy.fragments.leagues.league_details.league_info;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class LeagueInfoPresenter extends BaseDataPresenter<ILeagueInfoView> implements ILeagueInfoPresenter<ILeagueInfoView> {
    /**
     * @param appComponent
     */
    public LeagueInfoPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
