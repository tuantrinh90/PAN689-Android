package com.football.fantasy.fragments.leagues.action.setup_leagues;

import android.text.TextUtils;

import com.bon.util.StringUtils;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.requests.LeagueRequest;
import com.football.models.responses.FormResponse;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.UploadResponse;
import com.football.utilities.RxUtilities;
import com.football.utilities.compressor.Compressor;

import java.io.File;
import java.io.IOException;

import java8.util.function.Consumer;
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
                            v.displayBudgets(form.getBudgets());
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
            if (hasFile(request)) {
                upload(request, uploadResponse -> create(request, uploadResponse.getFileName()));
            } else {
                create(request, "");
            }
        });
    }

    @Override
    public void updateLeague(int leagueId, LeagueRequest request) {
        getOptView().doIfPresent(v -> {
            v.showLoading(true);
            if (hasFile(request)) {
                upload(request, uploadResponse -> update(request, leagueId, uploadResponse.getFileName()));
            } else {
                update(request, leagueId, "");
            }
        });
    }

    private boolean hasFile(LeagueRequest request) {
        if (!StringUtils.isEmpty(request.getLogo())) {
            File logo = new File(request.getLogo());
            if (logo.exists()) {
                return true;
            }
        }
        return false;
    }

    private void upload(LeagueRequest request, Consumer<UploadResponse> consumer) {
        getOptView().doIfPresent(v -> {
            File logo;
            try {
                logo = new Compressor(v.getAppActivity().getAppContext())
                        .compressToFile(new File(request.getLogo()));
            } catch (IOException e) {
                logo = new File(request.getLogo());
                e.printStackTrace();
            }
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().upload(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", logo.getName(), RequestBody.create(MediaType.parse("image/*"), logo))
                            .addFormDataPart("storage", "leagues/images")
                            .build()),
                    new ApiCallback<UploadResponse>() {
                        @Override
                        public void onSuccess(UploadResponse response) {
                            consumer.accept(response);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                            v.showLoading(false);
                        }
                    }));
        });
    }

    private void create(LeagueRequest request, String url) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().createLeague(createMultiPartBuilder(request, url).build()),
                    new ApiCallback<LeagueResponse>() {
                        @Override
                        public void onSuccess(LeagueResponse response) {
                            v.openCreateTeam(response);
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

    private void update(LeagueRequest request, int leagueId, String url) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().updateLeague(leagueId, createMultiPartBuilder(request, url).build()),
                    new ApiCallback<LeagueResponse>() {
                        @Override
                        public void onSuccess(LeagueResponse response) {
                            v.updateSuccess(response);
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

    private MultipartBody.Builder createMultiPartBuilder(LeagueRequest request, String url) {
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

        if (!TextUtils.isEmpty(url)) {
            builder.addFormDataPart("logo", url);
        }

        if (request.getLeagueId() > 0) {
            builder.addFormDataPart("_method", "PUT");
        }
        return builder;
    }

}
