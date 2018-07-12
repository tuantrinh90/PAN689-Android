package com.football.fantasy.fragments.leagues.league_details.invite_friends;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IInviteFriendPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getInviteFriends(int leagueId, String keyword, int page);

    void inviteFriend(int leagueId, int receiveId);
}
