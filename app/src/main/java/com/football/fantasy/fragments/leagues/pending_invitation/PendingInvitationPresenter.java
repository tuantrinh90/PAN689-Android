package com.football.fantasy.fragments.leagues.pending_invitation;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class PendingInvitationPresenter extends BaseDataPresenter<IPendingInvitationView> implements IPendingInvitationPresenter<IPendingInvitationView> {
    /**
     * @param appComponent
     */
    public PendingInvitationPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
