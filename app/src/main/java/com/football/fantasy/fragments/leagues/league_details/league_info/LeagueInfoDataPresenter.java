package com.football.fantasy.fragments.leagues.league_details.league_info;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

public class LeagueInfoDataPresenter extends BaseDataPresenter<ILeagueInfoView> implements ILeagueInfoPresenter<ILeagueInfoView> {
    /**
     * @param appComponent
     */
    public LeagueInfoDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void acceptInvite(Integer invitationId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().invitationDecision(
                            invitationId,
                            new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("status", String.valueOf(Constant.KEY_INVITATION_ACCEPT))
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
                            v.onAcceptSuccess();
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error, R.string.ok, null);
                        }
                    }));
        });
    }
}
