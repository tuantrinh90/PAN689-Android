package com.football.fantasy.fragments.leagues.pending_invitation;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.RxUtilities;

public class PendingInvitationDataPresenter extends BaseDataPresenter<IPendingInvitationView> implements IPendingInvitationPresenter<IPendingInvitationView> {
    int page = 1;

    /**
     * @param appComponent
     */
    public PendingInvitationDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPendingInvitations(int perPage) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().getPendingInvitations(perPage), new ApiCallback<PagingResponse<LeagueResponse>>() {
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
                    v.notifyDataSetChanged(pagingResponse.getData());
                }

                @Override
                public void onError(String error) {
                    v.showMessage(error, R.string.ok, null);
                }
            }));
        });
    }
}
