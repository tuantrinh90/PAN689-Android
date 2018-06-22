package com.football.fantasy.fragments.leagues.open_leagues;

import android.text.TextUtils;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class OpenLeagueDataPresenter extends BaseDataPresenter<IOpenLeagueView> implements IOpenLeaguePresenter<IOpenLeagueView> {
    /**
     * @param appComponent
     */
    public OpenLeagueDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getOpenLeagues(String orderBy, int page, int perPage, String query, String numberOfUser) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            if (!TextUtils.isEmpty(orderBy)) {
                queries.put(Constant.KEY_SORT, orderBy);
            }
            queries.put(Constant.KEY_PAGE, String.valueOf(page));
            queries.put(Constant.KEY_PER_PAGE, String.valueOf(perPage));
            if (!TextUtils.isEmpty(query)) {
                queries.put(Constant.KEY_WORD, query);
            }
            if (!TextUtils.isEmpty(numberOfUser)) {
                queries.put(Constant.NUMBER_OF_USER, numberOfUser);
            }

            mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().getOpenLeagues(queries),
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
                        public void onSuccess(PagingResponse<LeagueResponse> leagueResponsePagingResponse) {
                            v.displayLeagues(leagueResponsePagingResponse.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void join(Integer leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().join(leagueId),
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
                            v.refreshData(response.getId());
                            v.onJoinSuccessful(response);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
