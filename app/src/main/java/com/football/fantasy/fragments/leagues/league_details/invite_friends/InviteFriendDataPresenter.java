package com.football.fantasy.fragments.leagues.league_details.invite_friends;

import android.util.Log;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.FriendResponse;
import com.football.models.responses.InviteResponse;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import java.util.List;

import okhttp3.MultipartBody;

public class InviteFriendDataPresenter extends BaseDataPresenter<IInviteFriendView> implements IInviteFriendPresenter<IInviteFriendView> {
    /**
     * @param appComponent
     */
    public InviteFriendDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getInviteFriends(int leagueId, String keyword) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getInviteFriends(leagueId, keyword),
                    new ApiCallback<List<FriendResponse>>() {
                        @Override
                        public void onSuccess(List<FriendResponse> friends) {
                            v.displayFriends(friends);
                        }

                        @Override
                        public void onError(String e) {
                            Log.e("eee", e);
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void inviteFriend(int leagueId, int receiveId) {
        getOptView().doIfPresent(v -> {
            UserResponse user = AppPreferences.getInstance(v.getAppActivity()).getObject(Constant.KEY_USER, UserResponse.class);
            int senderId = user.getId();

            v.showLoading(true);
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().inviteFriends(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("league_id", String.valueOf(leagueId))
                            .addFormDataPart("sender_id", String.valueOf(senderId))
                            .addFormDataPart("receiver_id", String.valueOf(receiveId))
                            .build()),
                    new ApiCallback<InviteResponse>() {
                        @Override
                        public void onSuccess(InviteResponse invite) {
                            v.showLoading(false);
                            v.showMessage("Invite successful");
                            v.inviteSuccess(invite.getReceiverId());
                        }

                        @Override
                        public void onError(String e) {
                            Log.e("eee", e);
                            v.showLoading(false);
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
