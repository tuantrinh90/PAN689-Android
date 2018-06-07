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
import com.bon.logger.Logger;
import com.bon.util.StringUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.images.CircleImageViewApp;
import com.football.events.LeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.requests.TeamRequest;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class SetupTeamFragment extends BaseMainMvpFragment<ISetupTeamView, ISetupTeamPresenter<ISetupTeamView>> implements ISetupTeamView {
    static final String TAG = SetupTeamFragment.class.getSimpleName();

    public static final int INVITATION_ACCEPT = 1;
    public static final int INVITATION_NONE = 0;

    static final String KEY_LEAGUE = "LEAGUE";
    static final String KEY_TEAM = "TEAM";
    static final String KEY_INVITATION_ACCEPT = "INVITATION_ACCEPT";
    static final String FROM_LEAGUES_TITLE = "LEAGUE_TITLE";
    static final String FROM_LEAGUES_TYPE = "LEAGUE_TYPE";

    public static Bundle newBundle(LeagueResponse leagueResponse, TeamResponse teamResponse,
                                   String leagueTitle, String leagueType, int invitation) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, leagueResponse);
        if (teamResponse != null) bundle.putSerializable(KEY_TEAM, teamResponse);
        bundle.putString(FROM_LEAGUES_TITLE, leagueTitle);
        bundle.putString(FROM_LEAGUES_TYPE, leagueType);
        bundle.putInt(KEY_INVITATION_ACCEPT, invitation);
        return bundle;
    }

    @BindView(R.id.etTeamName)
    EditTextApp etTeamName;
    @BindView(R.id.ivImagePick)
    CircleImageViewApp ivImagePick;
    @BindView(R.id.etDescription)
    EditTextApp etDescription;

    TeamResponse teamResponse;
    LeagueResponse leagueResponse;
    String leagueTitle;
    String leagueType;
    int invation; // trạng thái user vào từ MH Pending
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
        try {
            if (getArguments() == null) return;
            leagueResponse = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
            if (getArguments().containsKey(KEY_TEAM)) {
                teamResponse = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
            }
            leagueTitle = getArguments().getString(FROM_LEAGUES_TITLE);
            leagueType = getArguments().getString(FROM_LEAGUES_TYPE);
            invation = getArguments().getInt(KEY_INVITATION_ACCEPT, INVITATION_NONE);
        } catch (Exception e) {
            Logger.e(TAG, e);
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

        TeamRequest request = new TeamRequest(leagueResponse.getId(), etTeamName.getContent(),
                etDescription.getContent(), filePath == null ? "" : filePath.getAbsolutePath());
        presenter.createTeam(request);
    }

    @Override
    public void createTeamSuccess() {
        bus.send(new LeagueEvent());
        if (invation == INVITATION_ACCEPT) {
            presenter.invitationAccept(leagueResponse.getId());
        } else {
            goLeagueDetail();
        }
    }

    @Override
    public void onInvitationAccept() {
        getActivity().finish();
    }

    private void goLeagueDetail() {
        AloneFragmentActivity.with(this)
                .parameters(LeagueDetailFragment.newBundle(leagueTitle, leagueResponse.getId(), leagueType))
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
