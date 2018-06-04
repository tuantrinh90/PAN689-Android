package com.football.fantasy.fragments.leagues.your_team;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class YourTeamPresenter extends BaseDataPresenter<IYourTeamView> implements IYourTeamPresenter<IYourTeamView> {

    /**
     * @param appComponent
     */
    public YourTeamPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
