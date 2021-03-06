package com.football.fantasy.fragments.leagues.league_details.league_info;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ILeagueInfoPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void joinLeague(Integer id);

    void acceptInvite(Integer invitationId);

    void startLeague(Integer leagueId);

}
