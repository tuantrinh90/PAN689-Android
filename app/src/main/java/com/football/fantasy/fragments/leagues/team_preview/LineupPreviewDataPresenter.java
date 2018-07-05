package com.football.fantasy.fragments.leagues.team_preview;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LineupResponse;
import com.football.utilities.RxUtilities;

public class LineupPreviewDataPresenter extends BaseDataPresenter<ILineupPreviewView> implements ITeamPreviewPresenter<ILineupPreviewView> {
    /**
     * @param appComponent
     */
    protected LineupPreviewDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getLineup(int teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getLineup(teamId),
                    new ApiCallback<LineupResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(LineupResponse response) {
                            v.displayTeamState(response.getTeam());
                            v.displayLineupPlayers(response.getPlayers());
                            v.displayStatistic(response.getStatistic());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
