package com.football.fantasy.fragments.leagues.league_details.invite_friends;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.FriendResponse;

import java.util.List;

public interface IInviteFriendView extends IBaseMvpView {
    void displayFriends(List<FriendResponse> friends);

    void inviteSuccess(Integer receiverId);
}
