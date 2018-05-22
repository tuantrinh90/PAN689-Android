package com.football.fantasy.fragments.leagues.league_details.invite_friends;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class InviteFriendDataPresenter extends BaseDataPresenter<IInviteFriendView> implements IInviteFriendPresenter<IInviteFriendView> {
    /**
     * @param appComponent
     */
    public InviteFriendDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
