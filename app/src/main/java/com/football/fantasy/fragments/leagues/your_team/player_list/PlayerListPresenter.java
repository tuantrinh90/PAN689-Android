package com.football.fantasy.fragments.leagues.your_team.player_list;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.ExtPagingResponse;
import com.football.models.PagingResponse;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.RxUtilities;

public class PlayerListPresenter extends BaseDataPresenter<IPlayerListView> implements IPlayerListPresenter<IPlayerListView> {

    /**
     * @param appComponent
     */
    public PlayerListPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPlayers(int leagueId, String orderBy, int page, int perPage, String query, String mainPosition) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getPlayerList(/*leagueId,*/ orderBy, page, perPage, query, mainPosition),
                    new ApiCallback<ExtPagingResponse<PlayerResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(ExtPagingResponse<PlayerResponse> response) {
                            v.notifyDataSetChangedPlayers(response.getData());
                            v.displayStatistic(response.getStatistic());
                        }

                        @Override
                        public void onError(String error) {
                            v.notifyDataSetChangedPlayers(null);
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
