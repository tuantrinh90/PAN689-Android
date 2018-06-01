package com.football.fantasy.fragments.leagues.action;

import com.bon.util.StringUtils;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.requests.LeagueRequest;
import com.football.models.responses.BudgetResponse;
import com.football.models.responses.FormResponse;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.RxUtilities;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ActionLeagueDataPresenter extends BaseDataPresenter<IActionLeagueView> implements IActionLeaguePresenter<IActionLeagueView> {
    /**
     * @param appComponent
     */
    public ActionLeagueDataPresenter(AppComponent appComponent) {
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
                            v.openCreateTeam();
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

    private MultipartBody.Builder createMultiPartBuilder(LeagueRequest request) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", request.name)
                .addFormDataPart("league_type", request.league_type)
                .addFormDataPart("number_of_user", String.valueOf(request.number_of_user))
                .addFormDataPart("scoring_system", request.scoring_system)
                .addFormDataPart("start_at", request.start_at)
                .addFormDataPart("description", request.description)
                .addFormDataPart("gameplay_option", request.gameplay_option)
                .addFormDataPart("budget_id", String.valueOf(1/*request.budget_id*/)) // todo: test
                .addFormDataPart("team_setup", request.team_setup)
                .addFormDataPart("trade_review", request.trade_review)
                .addFormDataPart("draft_time", request.draft_time)
                .addFormDataPart("time_to_pick", request.time_to_pick);
        if (!StringUtils.isEmpty(request.logo)) {
            File logo = new File(request.logo);
            if (logo.exists()) {
                builder.addFormDataPart("logo", logo.getName(), RequestBody.create(MediaType.parse("image/*"), logo));
            }
        }

        return builder;
    }
}
