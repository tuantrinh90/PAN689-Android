package com.football.fantasy.fragments.leagues.league_details.invite_friends;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class InviteFriendPresenter extends BaseDataPresenter<IInviteFriendView> implements IInviteFriendPresenter<IInviteFriendView> {
    /**
     * @param appComponent
     */
    public InviteFriendPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
