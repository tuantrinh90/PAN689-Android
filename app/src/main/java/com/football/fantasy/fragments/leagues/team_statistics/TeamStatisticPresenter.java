package com.football.fantasy.fragments.leagues.team_statistics;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class TeamStatisticPresenter extends BaseDataPresenter<ITeamStatisticView> implements ITeamStatisticPresenter<ITeamStatisticView> {
    /**
     * @param appComponent
     */
    public TeamStatisticPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
