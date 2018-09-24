package com.football.fantasy.fragments.leagues.team_details.gameplay_option.record;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.TransferHistoryResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class RecordDataPresenter extends BaseDataPresenter<IRecordView> implements IRecordPresenter<IRecordView> {
    /**
     * @param appComponent
     */
    protected RecordDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTransferHistories(Integer teamId, String gameOption, int page) {
        getOptView().doIfPresent(v -> {

            Map<String, String> queries = new HashMap<>();
            queries.put("gameplay_option", gameOption);
            queries.put("page", String.valueOf(page));

            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getTransferHistories(teamId, queries),
                    new ApiCallback<PagingResponse<TransferHistoryResponse>>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onComplete() {
                            v.stopLoading();
                        }

                        @Override
                        public void onSuccess(PagingResponse<TransferHistoryResponse> response) {
                            v.displayHistories(response.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
