package com.football.fantasy.fragments.leagues.action.setup_teams;

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
                .addFormDataPart("league_id", String.valueOf(request.getLeagueId()))
                .addFormDataPart("name", request.getName())
                .addFormDataPart("description", request.getDescription());

        if (!StringUtils.isEmpty(request.getLogo())) {
            File logo = new File(request.getLogo());
            if (logo.exists()) {
                builder.addFormDataPart("logo", logo.getName(), RequestBody.create(MediaType.parse("image/*"), logo));
            }
        }

        return builder;
    }
}
