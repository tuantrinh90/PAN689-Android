package com.football.fantasy.fragments.leagues.team_lineup;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class TeamLineUpPresenter extends BaseDataPresenter<ITeamLineUpView> implements ITeamLineUpPresenter<ITeamLineUpView> {
    /**
     * @param appComponent
     */
    public TeamLineUpPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
