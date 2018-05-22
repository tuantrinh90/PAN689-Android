package com.football.fantasy.fragments.leagues.pending_invitation;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class PendingInvitationDataPresenter extends BaseDataPresenter<IPendingInvitationView> implements IPendingInvitationPresenter<IPendingInvitationView> {
    /**
     * @param appComponent
     */
    public PendingInvitationDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
