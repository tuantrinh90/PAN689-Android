package com.football.fantasy.fragments.leagues.action.team;

import com.bon.util.StringUtils;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.requests.TeamRequest;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreateTeamDataPresenter extends BaseDataPresenter<ICreateTeamView> implements ICreateTeamPresenter<ICreateTeamView> {
    /**
     * @param appComponent
     */
    protected CreateTeamDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void createTeam(TeamRequest request) {
        getOptView().doIfPresent(v -> {
            v.showLoading(true);
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().createTeam(createMultiPartBuilder(request).build()),
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

    private MultipartBody.Builder createMultiPartBuilder(TeamRequest request) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("league_id", String.valueOf(request.league_id))
                .addFormDataPart("name", request.name)
                .addFormDataPart("description", request.description);

        if (!StringUtils.isEmpty(request.logo)) {
            File logo = new File(request.logo);
            if (logo.exists()) {
                builder.addFormDataPart("logo", logo.getName(), RequestBody.create(MediaType.parse("image/*"), logo));
            }
        }

        return builder;
    }
}
