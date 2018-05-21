package com.football.fantasy.fragments.leagues.league_details.teams;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class TeamPresenter extends BaseDataPresenter<ITeamView> implements ITeamPresenter<ITeamView> {
    /**
     * @param appComponent
     */
    public TeamPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
