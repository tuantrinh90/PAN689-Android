package com.football.fantasy.fragments.leagues.pending_invitation;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

public class PendingInvitationDataPresenter extends BaseDataPresenter<IPendingInvitationView> implements IPendingInvitationPresenter<IPendingInvitationView> {
    /**
     * @param appComponent
     */
    public PendingInvitationDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPendingInvitations(int page, int perPage) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getPendingInvitations(page, perPage),
                    new ApiCallback<PagingResponse<LeagueResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(PagingResponse<LeagueResponse> pagingResponse) {
                            v.displayLeagues(pagingResponse.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error, R.string.ok, null);
                        }
                    }));
        });
    }

    @Override
    public void getLeagueDetail(LeagueResponse league) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getLeagueDetail(league.getId()),
                    new ApiCallback<LeagueResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(LeagueResponse response) {
                            if (response.getTeam() == null) {
                                invitationDecisions(response, Constant.KEY_INVITATION_ACCEPT);
                            } else {
                                v.showMessage(v.getAppActivity().getString(R.string.message_exist_league), R.string.ok, null);
                                v.removeItem(league);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error, R.string.ok, null);
                        }
                    }));
        });
    }

    @Override
    public void invitationDecisions(LeagueResponse leagueResponse, int status) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().invitationDecision(
                            leagueResponse.getInvitation().getId(),
                            new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("status", String.valueOf(status))
                                    .build()),
                    new ApiCallback<Object>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(Object o) {
                            v.removeItem(leagueResponse);
                            if (status == Constant.KEY_INVITATION_ACCEPT) {
                                v.goCreateTeam(leagueResponse);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error, R.string.ok, null);
                        }
                    }));
        });
    }
}
