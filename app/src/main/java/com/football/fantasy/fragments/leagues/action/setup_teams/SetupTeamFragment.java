package com.football.fantasy.fragments.leagues.action.setup_teams;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.image.ImageFilePath;
import com.bon.image.ImageLoaderUtils;
import com.bon.image.ImageUtils;
import com.bon.permission.PermissionUtils;
import com.bon.util.StringUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.your_team.YourTeamFragment;
import com.football.models.requests.TeamRequest;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SetupTeamFragment extends BaseMainMvpFragment<ISetupTeamView, ISetupTeamPresenter<ISetupTeamView>> implements ISetupTeamView {

    public static final String KEY_LEAGUE_ID = "LEAGUE_ID";

    @BindView(R.id.etTeamName)
    EditTextApp etTeamName;
    @BindView(R.id.ivImagePick)
    ImageView ivImagePick;
    @BindView(R.id.etDescription)
    EditTextApp etDescription;

    private int leagueId;
    private File filePath;

    public static SetupTeamFragment newInstance() {
        return new SetupTeamFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.setup_team_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    private void getDataFromBundle() {
        leagueId = getArguments().getInt(KEY_LEAGUE_ID);
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
        if (!PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_CODE_PERMISSION,
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) return;

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
    }

    @OnClick(R.id.tvCreateTeam)
    public void onCreateTeamClicked() {
        if (etTeamName.isEmpty(mActivity)) {
            return;
        }
        if (etDescription.isEmpty(mActivity)) {
            return;
        }

        String path = filePath == null ? "" : filePath.getAbsolutePath();

        TeamRequest request = new TeamRequest(leagueId, etTeamName.getContent(), etDescription.getContent(), path);
        presenter.createTeam(request);
    }

    @Override
    public void createTeamSuccess() {
        Bundle bundle = new Bundle();
        bundle.putInt(YourTeamFragment.KEY_LEAGUE_ID, leagueId);
        AloneFragmentActivity.with(this)
                .parameters(bundle)
                .start(YourTeamFragment.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.REQUEST_CODE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onClickImagePick();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImageUtils.CAMERA_REQUEST: {
                    ImageLoaderUtils.displayImage(ImageUtils.getUriImageDisplayFromFile(filePath), ivImagePick);
                    break;
                }
                case ImageUtils.REQUEST_PICK_CONTENT: {
                    String pathFile = ImageFilePath.getPath(mActivity, data.getData());
                    Log.e("pathFile", "pathFile:: " + pathFile);
                    StringUtils.isNotEmpty(pathFile, s -> {
                        filePath = new File(s);
                        ImageLoaderUtils.displayImage(ImageUtils.getUriImageDisplayFromFile(filePath), ivImagePick);
                    });
                    break;
                }
            }
        }
    }

}
