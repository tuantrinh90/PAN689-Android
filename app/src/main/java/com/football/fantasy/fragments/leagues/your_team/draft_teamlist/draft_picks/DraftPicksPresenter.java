package com.football.fantasy.fragments.leagues.your_team.draft_teamlist.draft_picks;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.PickHistoryResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class DraftPicksPresenter extends BaseDataPresenter<IDraftPicksView> implements IDraftPicksPresenter<IDraftPicksView>  {

    private Disposable getPickHistories;

    /**
     * @param appComponent
     */
    public DraftPicksPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPickHistories(int leagueId, int page) {
        if (getPickHistories != null) {
            getPickHistories.dispose();
        }

        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put("page", String.valueOf(page));

            getPickHistories = RxUtilities.async(v,
                    dataModule.getApiService().getPickHistories(leagueId, queries),
                    new ApiCallback<PagingResponse<PickHistoryResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(PagingResponse<PickHistoryResponse> response) {
                            v.displayPickHistories(response.getData());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    });
            mCompositeDisposable.add(getPickHistories);
        });
    }
}
