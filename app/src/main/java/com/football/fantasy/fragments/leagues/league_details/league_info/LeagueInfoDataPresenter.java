package com.football.fantasy.fragments.leagues.league_details.league_info;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class LeagueInfoDataPresenter extends BaseDataPresenter<ILeagueInfoView> implements ILeagueInfoPresenter<ILeagueInfoView> {
    /**
     * @param appComponent
     */
    public LeagueInfoDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
