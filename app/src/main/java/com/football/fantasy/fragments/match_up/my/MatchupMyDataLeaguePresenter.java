package com.football.fantasy.fragments.match_up.my;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MatchupMyDataLeaguePresenter extends BaseDataPresenter<IMatchupMyLeagueView> implements IMatchupMyLeaguePresenter<IMatchupMyLeagueView> {
    /**
     * @param appComponent
     */
    protected MatchupMyDataLeaguePresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getMatchResults() {
        getOptView().doIfPresent(v -> {

//            mCompositeDisposable.add(RxUtilities.async(v,
//                    dataModule.getApiService().changePassword(body),
//                    new ApiCallback<Object>() {
//                        @Override
//                        public void onStart() {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//                        }
//
//                        @Override
//                        public void onSuccess(Object response) {
//                            v.changePasswordSuccessful();
//                        }
//
//                        @Override
//                        public void onError(String e) {
//                            v.showMessage(e);
//                        }
//                    }));
        });
    }
}
