package com.football.fantasy.fragments.leagues.player_pool.display;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.ExtPagingResponse;
import com.football.models.PagingResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.RealClubResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class PlayerPoolDisplayPresenter extends BaseDataPresenter<IPlayerPoolDisplayView> implements IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView> {
    /**
     * @param appComponent
     */
    public PlayerPoolDisplayPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getRealClubs() {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getRealClubs(),
                    new ApiCallback<PagingResponse<RealClubResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(PagingResponse<RealClubResponse> response) {
                            v.displayClubs(response.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
