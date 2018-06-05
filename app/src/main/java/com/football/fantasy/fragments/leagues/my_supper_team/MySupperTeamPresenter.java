package com.football.fantasy.fragments.leagues.my_supper_team;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class MySupperTeamPresenter extends BaseDataPresenter<IMySupperTeamView> implements IMySupperTeamPresenter<IMySupperTeamView> {
    /**
     * @param appComponent
     */
    public MySupperTeamPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
