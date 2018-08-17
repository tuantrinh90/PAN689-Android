package com.football.fantasy.fragments.leagues.action.setup_teams;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageFilePath;
import com.bon.image.ImageLoaderUtils;
import com.bon.image.ImageUtils;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.bon.util.StringUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.images.CircleImageViewApp;
import com.football.events.LeagueEvent;
import com.football.events.TeamEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.requests.TeamRequest;
import com.football.models.responses.TeamResponse;
import com.football.utilities.Constant;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class SetupTeamFragment extends BaseMvpFragment<ISetupTeamView, ISetupTeamPresenter<ISetupTeamView>> implements ISetupTeamView {
    private static final String TAG = SetupTeamFragment.class.getSimpleName();

    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";
    private static final String KEY_TEAM = "TEAM";
    private static final String FROM_LEAGUES_TITLE = "LEAGUE_TITLE";

    public static Bundle newBundle(TeamResponse team, int leagueId, String leagueTitle) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE_ID, leagueId);
        if (team != null) bundle.putSerializable(KEY_TEAM, team);
        bundle.putString(FROM_LEAGUES_TITLE, leagueTitle);
        return bundle;
    }

    @BindView(R.id.etTeamName)
    EditTextApp etTeamName;
    @BindView(R.id.ivImagePick)
    CircleImageViewApp ivImagePick;
    @BindView(R.id.etDescription)
    EditTextApp etDescription;
    @BindView(R.id.tvCreateTeam)
    ExtTextView tvCreateTeam;

    private TeamResponse team;
    private int leagueId;

    private String leagueTitle;

    private File filePath;

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
        try {
            if (getArguments() == null) return;
            leagueId = getArguments().getInt(KEY_LEAGUE_ID);
            if (hasKeyTeam()) {
                team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
            }
            leagueTitle = getArguments().getString(FROM_LEAGUES_TITLE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private boolean hasKeyTeam() {
        return getArguments().containsKey(KEY_TEAM);
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.color_white)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_blue)));

        ivImagePick.getImageView().setImageResource(R.drawable.bg_image_pick);

        if (hasKeyTeam()) {
            tvCreateTeam.setText(getString(R.string.update_team));
            displayTeamInfo(team);
        }
    }

    void displayTeamInfo(TeamResponse team) {
        etTeamName.setContent(team.getName());
        ImageLoaderUtils.displayImage(team.getLogo(), ivImagePick.getImageView());
        etDescription.setContent(team.getDescription());
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
        } else if (etTeamName.getContent().length() > Constant.MAX_NAME_40_CHARACTERS) {
            result = false;
            etTeamName.setError(getString(R.string.message_max_team_name));
        }

        return result;
    }

    @OnClick(R.id.tvCreateTeam)
    public void onCreateTeamClicked() {
        if (!isValid()) return;

        TeamRequest request = new TeamRequest(leagueId, etTeamName.getContent(),
                etDescription.getContent(), filePath == null ? "" : filePath.getAbsolutePath());
        if (!hasKeyTeam()) {
            presenter.createTeam(request);
        } else {
            presenter.updateTeam(team.getId(), request);
        }
    }

    @Override
    public void createTeamSuccess() {
        bus.send(new LeagueEvent());
        goLeagueDetail();
    }

    @Override
    public void updateTeamSuccess(TeamResponse response) {
        bus.send(new LeagueEvent());
        bus.send(new TeamEvent(response, true));
        getActivity().finish();
    }

    private void goLeagueDetail() {
        AloneFragmentActivity.with(this)
                .parameters(LeagueDetailFragment.newBundle(leagueTitle, leagueId, LeagueDetailFragment.MY_LEAGUES))
                .start(LeagueDetailFragment.class);
        getActivity().finish();
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
