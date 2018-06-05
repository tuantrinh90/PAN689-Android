package com.football.fantasy.fragments.leagues.action.setup_leagues;

import com.bon.util.StringUtils;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.requests.LeagueRequest;
import com.football.models.responses.FormResponse;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.RxUtilities;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SetUpLeagueDataPresenter extends BaseDataPresenter<ISetupLeagueView> implements ISetUpLeaguePresenter<ISetupLeagueView> {
    /**
     * @param appComponent
     */
    public SetUpLeagueDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getBudgets(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getFormOption(leagueId),
                    new ApiCallback<FormResponse>() {
                        @Override
                        public void onSuccess(FormResponse form) {
                            v.displayBudgets(form.budgets);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void createLeague(LeagueRequest request) {
        getOptView().doIfPresent(v -> {
            v.showLoading(true);
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().createLeague(createMultiPartBuilder(request).build()),
                    new ApiCallback<LeagueResponse>() {
                        @Override
                        public void onSuccess(LeagueResponse response) {
                            v.openCreateTeam(response.getId());
                            v.showLoading(false);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                            v.showLoading(false);
                        }
                    }));
        });
    }

    @Override
    public void updateLeague(int leagueId, LeagueRequest request) {
        getOptView().doIfPresent(v -> {
            v.showLoading(true);
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().updateLeague(leagueId, createMultiPartBuilder(request).build()),
                    new ApiCallback<LeagueResponse>() {
                        @Override
                        public void onSuccess(LeagueResponse response) {
                            v.updateSuccess();
                            v.showLoading(false);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                            v.showLoading(false);
                        }
                    }));
        });
    }

//    private Observable zip(Observable<BaseResponse<LeagueResponse>> observableLeague, Observable<BaseResponse<Object>> observableUpload) {
//        return Observable.zip(
//                observableLeague,
//                dataModule.getApiService().updateLeague(leagueId, createMultiPartBuilder(request).build()),
//                (response, response2) -> {
//
//                }
//        );
//    }

    private MultipartBody.Builder createMultiPartBuilder(LeagueRequest request) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", request.getName())
                .addFormDataPart("league_type", request.getLeagueType())
                .addFormDataPart("number_of_user", String.valueOf(request.getNumberOfUser()))
                .addFormDataPart("scoring_system", request.getScoringSystem())
                .addFormDataPart("start_at", request.getStartAt())
                .addFormDataPart("description", request.getDescription())
                .addFormDataPart("gameplay_option", request.getGameplayOption())
                .addFormDataPart("budget_id", String.valueOf(request.getBudgetId()))
                .addFormDataPart("team_setup", request.getTeamSetup())
                .addFormDataPart("trade_review", request.getTradeReview())
                .addFormDataPart("draft_time", request.getDraftTime())
                .addFormDataPart("time_to_pick", request.getTimeToPick());
        if (!StringUtils.isEmpty(request.getLogo())) {
            File logo = new File(request.getLogo());
            if (logo.exists()) {
                builder.addFormDataPart("logo", logo.getName(), RequestBody.create(MediaType.parse("image/*"), logo));
            }
        }

        return builder;
    }

//    private MultipartBody.Builder createFileMultiPartBuilder(LeagueRequest request) {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//        if (!StringUtils.isEmpty(request.getLogo())) {
//            File logo = new File(request.getLogo());
//            if (logo.exists()) {
//                builder.addFormDataPart("logo", logo.getName(), RequestBody.create(MediaType.parse("image/*"), logo));
//            }
//        }
//        return builder;
//    }
}
