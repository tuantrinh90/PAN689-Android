package com.football.fantasy.fragments.leagues.league_details;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StopResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import java.util.ArrayList;
import java.util.Calendar;

import static com.football.models.responses.LeagueResponse.DRAFT_FINISHED;
import static com.football.models.responses.LeagueResponse.FINISHED;
import static com.football.models.responses.LeagueResponse.WAITING_FOR_START;

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

                                // nếu đang trong thời gian setupTime và chưa chuyển trạng thái sang ON-GOING
                                // dành cho trường hợp ấn EndTurn liên tục để kết thúc sớm trước startTime
                                if (response.equalsStatus(WAITING_FOR_START)
                                        && currentTime.after(setupTime) && currentTime.before(draftTime)
                                        && response.getDraftRunning() != DRAFT_FINISHED) {
                                    v.goLineup();
                                    goLineup = true;
                                }
                            }
                            v.handleActionNotification(goLineup);

                            // đến FINISH rồi thì ko xử lý
                            if (!response.getStatus().equals(FINISHED)) {
                                // show message and điều hướng đến playerPool để pick 1 cầu thủ
                                if (response.getDeletedPlayers() != null
                                        && response.getDeletedPlayers().size() > 0
                                        && response.getTeam() != null
                                        && response.getTeam().getTotalTransferRoundPlayers() < 18) {
                                    long transferValue = 0;
                                    ArrayList<Integer> ids = new ArrayList<>();
                                    for (PlayerResponse player : response.getDeletedPlayers()) {
                                        transferValue += player.getTransferValue();
                                        ids.add(player.getId());
                                    }
                                    v.handleLessThan18Players(ids, transferValue);
                                }
                                // show message and điều hướng đến Transferring để delete cầu thủ
                                else if (response.getTeam() != null && response.getTeam().getTotalTransferRoundPlayers() > Constant.MAX_PLAYERS) {
                                    v.handleMoreThan18Players(response.getTeam().getTotalTransferRoundPlayers() - Constant.MAX_PLAYERS);
                                }
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
