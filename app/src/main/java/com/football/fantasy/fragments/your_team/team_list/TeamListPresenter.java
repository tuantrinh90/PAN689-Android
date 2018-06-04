package com.football.fantasy.fragments.your_team.team_list;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class TeamListPresenter extends BaseDataPresenter<ITeamListView> implements ITeamListPresenter<ITeamListView> {

    /**
     * @param appComponent
     */
    public TeamListPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
