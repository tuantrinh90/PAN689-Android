package com.football.fantasy.fragments.leagues.team_details.team_squad;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ITeamSquadPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getTeamSquad(Integer teamId, String property, String direction);
}
