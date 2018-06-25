package com.football.fantasy.fragments.leagues.action.setup_teams;

import android.text.TextUtils;

import com.bon.util.StringUtils;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.requests.TeamRequest;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.UploadResponse;
import com.football.utilities.RxUtilities;
import com.football.utilities.compressor.Compressor;

import java.io.File;
import java.io.IOException;

import java8.util.function.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SetupTeamDataPresenter extends BaseDataPresenter<ISetupTeamView> implements ISetupTeamPresenter<ISetupTeamView> {
    /**
     * @param appComponent
     */
    protected SetupTeamDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void createTeam(TeamRequest request) {
        getOptView().doIfPresent(v -> {
            v.showLoading(true);
            if (hasFile(request)) {
                upload(request, uploadResponse -> create(request, uploadResponse.getFileMachineName()));
            } else {
                create(request, "");
            }
        });
    }

    @Override
    public void updateTeam(Integer teamId, TeamRequest request) {
        getOptView().doIfPresent(v -> {
            v.showLoading(true);
            if (hasFile(request)) {
                upload(request, uploadResponse -> update(teamId, request, uploadResponse.getFileMachineName()));
            } else {
                update(teamId, request, "");
            }
        });
    }

    private boolean hasFile(TeamRequest request) {
        if (!StringUtils.isEmpty(request.getLogo())) {
            File logo = new File(request.getLogo());
            if (logo.exists()) {
                return true;
            }
        }
        return false;
    }

    private void upload(TeamRequest request, Consumer<UploadResponse> consumer) {
        getOptView().doIfPresent(v -> {
            File logo = null;
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

    private void create(TeamRequest request, String url) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().createTeam(createMultiPartBuilder(request, url).build()),
                    new ApiCallback<TeamResponse>() {
                        @Override
                        public void onSuccess(TeamResponse response) {
                            v.createTeamSuccess();
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

    private void update(Integer teamId, TeamRequest request, String url) {
        getOptView().doIfPresent(v -> {
            MultipartBody.Builder builder = createMultiPartBuilder(request, url);
            builder.addFormDataPart("_method", "PUT");
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().updateTeam(teamId, builder.build()),
                    new ApiCallback<TeamResponse>() {
                        @Override
                        public void onSuccess(TeamResponse response) {
                            v.updateTeamSuccess(response);
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

    private MultipartBody.Builder createMultiPartBuilder(TeamRequest request, String url) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("league_id", String.valueOf(request.getLeagueId()))
                .addFormDataPart("name", request.getName())
                .addFormDataPart("description", request.getDescription());

        if (!TextUtils.isEmpty(url)) {
            builder.addFormDataPart("logo", url);
        }
        return builder;
    }
}
