package com.football.fantasy.fragments.leagues.team_details;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class TeamDetailPresenter extends BaseDataPresenter<ITeamDetailView> implements ITeamDetailPresenter<ITeamDetailView> {
    /**
     * @param appComponent
     */
    public TeamDetailPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
