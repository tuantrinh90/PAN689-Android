package com.football.fantasy.fragments.leagues.pending_invitation;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.models.responses.LeagueResponse;

public interface IPendingInvitationPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getPendingInvitations(int page, int perPage);

    void invitationDecisions(LeagueResponse leagueResponse, int status);
}
