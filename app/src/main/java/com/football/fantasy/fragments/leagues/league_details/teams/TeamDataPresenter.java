package com.football.fantasy.fragments.leagues.league_details.teams;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class TeamDataPresenter extends BaseDataPresenter<ITeamView> implements ITeamPresenter<ITeamView> {
    /**
     * @param appComponent
     */
    public TeamDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
