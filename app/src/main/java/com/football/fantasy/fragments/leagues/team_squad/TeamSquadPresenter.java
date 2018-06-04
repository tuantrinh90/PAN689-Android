package com.football.fantasy.fragments.leagues.team_squad;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class TeamSquadPresenter extends BaseDataPresenter<ITeamSquadView> implements ITeamSquadPresenter<ITeamSquadView> {
    /**
     * @param appComponent
     */
    public TeamSquadPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
