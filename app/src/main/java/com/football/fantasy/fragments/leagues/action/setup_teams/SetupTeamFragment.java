package com.football.fantasy.fragments.leagues.action.setup_teams;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.image.ImageFilePath;
import com.bon.image.ImageUtils;
import com.bon.util.StringUtils;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.requests.TeamRequest;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class SetupTeamFragment extends BaseMainMvpFragment<ISetupTeamView, ISetupTeamPresenter<ISetupTeamView>> implements ISetupTeamView {
    static final String KEY_LEAGUE_ID = "LEAGUE_ID";
    static final String KEY_TEAM = "TEAM";

    public static Bundle newBundle(int leagueId, TeamResponse teamResponse) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        if (teamResponse != null) bundle.putSerializable(KEY_TEAM, teamResponse);
        return bundle;
    }

    @BindView(R.id.etTeamName)
    EditTextApp etTeamName;
    @BindView(R.id.ivImagePick)
    CircleImageViewApp ivImagePick;
    @BindView(R.id.etDescription)
    EditTextApp etDescription;

    int leagueId;
    LeagueResponse leagueResponse;
    File filePath;

    @Override
    public int getResourceId() {
        return R.layout.setup_team_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void getDataFromBundle() {
        if (getArguments() == null) return;
        leagueId = getArguments().getInt(KEY_LEAGUE_ID);
        if (getArguments().containsKey(KEY_TEAM)) {
            leagueResponse = (LeagueResponse) getArguments().getSerializable(KEY_TEAM);
        }
    }

    void initView() {
        ivImagePick.getImageView().setImageResource(R.drawable.bg_image_pick);
    }

    @NonNull
    @Override
    public ISetupTeamPresenter<ISetupTeamView> createPresenter() {
        return new SetupTeamDataPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.leagues;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    @OnClick(R.id.ivImagePick)
    void onClickImagePick() {
        mCompositeDisposable.add(mActivity.getRxPermissions().request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean granted) {
                        if (granted) {
                            ExtKeyValuePairDialogFragment.newInstance()
                                    .setExtKeyValuePairs(new ArrayList<ExtKeyValuePair>() {{
                                        add(new ExtKeyValuePair(getString(R.string.camera), getString(R.string.camera)));
                                        add(new ExtKeyValuePair(getString(R.string.gallery), getString(R.string.gallery)));
                                    }})
                                    .setOnSelectedConsumer(extKeyValuePair -> {
                                        if (extKeyValuePair.getKey().equalsIgnoreCase(getString(R.string.camera))) {
                                            filePath = ImageUtils.getImageUrlPng();
                                            ImageUtils.captureCamera(SetupTeamFragment.this, filePath);
                                        }

                                        if (extKeyValuePair.getKey().equalsIgnoreCase(getString(R.string.gallery))) {
                                            ImageUtils.chooseImageFromGallery(SetupTeamFragment.this, getString(R.string.select_value));
                                        }

                                    }).show(getFragmentManager(), null);
                        } else {
                            onClickImagePick();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    boolean isValid() {
        boolean result = true;

        if (etTeamName.isEmpty(mActivity)) {
            result = false;
        }

        return result;
    }

    @OnClick(R.id.tvCreateTeam)
    public void onCreateTeamClicked() {
        if (!isValid()) return;
        TeamRequest request = new TeamRequest(leagueId, etTeamName.getContent(),
                etDescription.getContent(), filePath == null ? "" : filePath.getAbsolutePath());
        presenter.createTeam(request);
    }

    @Override
    public void createTeamSuccess() {
        mActivity.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImageUtils.CAMERA_REQUEST: {
                    ivImagePick.setImageUri(ImageUtils.getUriImageDisplayFromFile(filePath));
                    break;
                }
                case ImageUtils.REQUEST_PICK_CONTENT: {
                    String pathFile = ImageFilePath.getPath(mActivity, data.getData());
                    Log.e("pathFile", "pathFile:: " + pathFile);
                    StringUtils.isNotEmpty(pathFile, s -> {
                        filePath = new File(s);
                        ivImagePick.setImageUri(ImageUtils.getUriImageDisplayFromFile(filePath));
                    });
                    break;
                }
            }
        }
    }

}
