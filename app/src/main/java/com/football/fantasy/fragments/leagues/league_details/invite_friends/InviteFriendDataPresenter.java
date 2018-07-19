package com.football.fantasy.fragments.leagues.league_details.invite_friends;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.FriendResponse;
import com.football.models.responses.InviteResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;

public class InviteFriendDataPresenter extends BaseDataPresenter<IInviteFriendView> implements IInviteFriendPresenter<IInviteFriendView> {
    /**
     * @param appComponent
     */
    public InviteFriendDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getInviteFriends(int leagueId, String keyword, int page) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put("league_id", String.valueOf(leagueId));
            queries.put("keyword", keyword);
            queries.put("page", String.valueOf(page));
            queries.put("per_page", String.valueOf(ExtPagingListView.NUMBER_PER_PAGE));

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getInviteFriends(queries),
                    new ApiCallback<PagingResponse<FriendResponse>>() {

                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(PagingResponse<FriendResponse> response) {
                            v.displayFriends(response.getData());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void inviteFriend(int leagueId, int receiveId) {
        getOptView().doIfPresent(v -> {
            int senderId = AppPreferences.getInstance(v.getAppActivity()).getInt(Constant.KEY_USER_ID);
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().inviteFriends(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("league_id", String.valueOf(leagueId))
                            .addFormDataPart("sender_id", String.valueOf(senderId))
                            .addFormDataPart("receiver_id", String.valueOf(receiveId))
                            .build()),
                    new ApiCallback<InviteResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(InviteResponse invite) {
                            v.inviteSuccess(invite.getReceiverId());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
