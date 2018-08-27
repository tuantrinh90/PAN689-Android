package com.football.fantasy.fragments.leagues.your_team.draft_teamlist;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class DraftTeamListPresenter extends BaseDataPresenter<IDraftTeamListView> implements IDraftTeamListPresenter<IDraftTeamListView> {

    /**
     * @param appComponent
     */
    public DraftTeamListPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}
