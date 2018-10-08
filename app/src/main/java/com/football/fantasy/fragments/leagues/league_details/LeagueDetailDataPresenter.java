package com.football.fantasy.fragments.leagues.league_details;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StopResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.RxUtilities;

import java.util.ArrayList;
import java.util.Calendar;

public class LeagueDetailDataPresenter extends BaseDataPresenter<ILeagueDetailView> implements ILeagueDetailPresenter<ILeagueDetailView> {
    /**
     * @param appComponent
     */
    public LeagueDetailDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getLeagueDetail(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getLeagueDetail(leagueId),
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
                            v.displayMenu(response);
                            v.displayLeaguePager(response);
                            v.displayLeague(response);

                            // mở màn hình Tạo Team nếu chưa có team
                            if (response.getOwner() || response.getIsJoined()) {
                                TeamResponse team = response.getTeam();
                                if (team == null) {
                                    v.goCreateTeam();
                                    return;
                                }
                            }

                            // mở màn hình Lineup luôn nếu đang trong draftTime
                            boolean goLineup = false;
                            if (response.equalsGameplay(LeagueResponse.GAMEPLAY_OPTION_DRAFT)) {
                                Calendar setupTime = response.getDraftTimeCalendar();

                                // calculator draftTime
                                int draftEstimate = AppUtilities.getDraftEstimate(
                                        response.getCurrentNumberOfUser(),
                                        response.getTimeToPick());
                                Calendar draftTime = response.getDraftTimeCalendar();
                                draftTime.add(Calendar.MINUTE, draftEstimate);

                                Calendar currentTime = Calendar.getInstance();

                                if (currentTime.after(setupTime) && currentTime.before(draftTime)) {
                                    v.goLineup();
                                    goLineup = true;
                                }
                            }
                            v.handleActionNotification(goLineup);

                            // show message and điều hướng đến playerPool để pick 1 cầu thủ
                            if (response.getDeletedPlayers() != null && response.getDeletedPlayers().size() > 0) {
                                long transferValue = 0;
                                ArrayList<Integer> ids = new ArrayList<>();
                                for (PlayerResponse player : response.getDeletedPlayers()) {
                                    transferValue += player.getTransferValue();
                                    ids.add(player.getId());
                                }
                                v.handleDeletePlayers(ids, transferValue);
                            }
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void stopLeague(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().stopLeague(leagueId),
                    new ApiCallback<StopResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(StopResponse response) {
                            v.stopOrLeaveLeagueSuccess();
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void leaveLeague(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().leaveLeague(
                            leagueId),
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
                        public void onSuccess(Object response) {
                            v.stopOrLeaveLeagueSuccess();
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

}
