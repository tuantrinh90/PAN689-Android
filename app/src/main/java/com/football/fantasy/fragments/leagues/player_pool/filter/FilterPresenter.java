package com.football.fantasy.fragments.leagues.player_pool.filter;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.RealClubResponse;
import com.football.utilities.RxUtilities;

public class FilterPresenter extends BaseDataPresenter<IFilterView> implements IFilterPresenter<IFilterView> {
    /**
     * @param appComponent
     */
    public FilterPresenter(AppComponent appComponent) {
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
