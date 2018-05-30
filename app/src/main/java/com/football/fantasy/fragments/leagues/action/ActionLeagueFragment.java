package com.football.fantasy.fragments.leagues.action;

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
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.bon.customview.datetime.ExtDayMonthYearHourMinuteDialogFragment;
import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageFilePath;
import com.bon.image.ImageLoaderUtils;
import com.bon.image.ImageUtils;
import com.bon.permission.PermissionUtils;
import com.bon.util.DateTimeUtils;
import com.bon.util.StringUtils;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.labels.LabelView;
import com.football.fantasy.R;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class ActionLeagueFragment extends BaseMainMvpFragment<IActionLeagueView, IActionLeaguePresenter<IActionLeagueView>> implements IActionLeagueView {
    public static ActionLeagueFragment newInstance() {
        return new ActionLeagueFragment();
    }

    @BindView(R.id.etLeagueName)
    EditTextApp etLeagueName;
    @BindView(R.id.ivImagePick)
    ImageView ivImagePick;
    @BindView(R.id.lvLeagueType)
    LabelView lvLeagueType;
    @BindView(R.id.rgLeagueType)
    RadioGroup rgLeagueType;
    @BindView(R.id.lvGamePlayOption)
    LabelView lvGamePlayOption;
    @BindView(R.id.llTransfer)
    LinearLayout llTransfer;
    @BindView(R.id.llDraft)
    LinearLayout llDraft;
    @BindView(R.id.lvNumberOfUsers)
    LabelView lvNumberOfUsers;
    @BindView(R.id.etNumberOfUser)
    EditTextApp etNumberOfUser;

    @BindView(R.id.lvBudgetOption)
    LabelView lvBudgetOption;
    @BindView(R.id.llBudgetOptionBottom)
    LinearLayout llBudgetOptionBottom;
    @BindView(R.id.tvBudgetOptionBottom)
    ExtTextView tvBudgetOptionBottom;
    @BindView(R.id.llBudgetOptionChallenge)
    LinearLayout llBudgetOptionChallenge;
    @BindView(R.id.tvBudgetOptionChallenge)
    ExtTextView tvBudgetOptionChallenge;
    @BindView(R.id.llBudgetOptionDream)
    LinearLayout llBudgetOptionDream;
    @BindView(R.id.tvBudgetOptionDream)
    ExtTextView tvBudgetOptionDream;

    @BindView(R.id.lvTradeReviewSetting)
    LabelView lvTradeReviewSetting;
    @BindView(R.id.rgTradeReviewSetting)
    RadioGroup rgTradeReviewSetting;
    @BindView(R.id.lvScoringSystem)
    LabelView lvScoringSystem;
    @BindView(R.id.rgScoringSystem)
    RadioGroup rgScoringSystem;
    @BindView(R.id.etDraftTime)
    EditTextApp etDraftTime;
    @BindView(R.id.etTimePerDraftPick)
    EditTextApp etTimePerDraftPick;
    @BindView(R.id.lvStartTime)
    LabelView lvStartTime;
    @BindView(R.id.etStartTime)
    EditTextApp etStartTime;
    @BindView(R.id.etDescription)
    EditTextApp etDescription;
    @BindView(R.id.tvCreateLeague)
    ExtTextView tvCreateLeague;

    File filePath;
    Calendar calendarDraftTime = Calendar.getInstance();
    Calendar calendarStartTime = Calendar.getInstance();
    ExtKeyValuePair keyValuePairNumberOfUser = new ExtKeyValuePair("06", "06");

    @Override
    public int getResourceId() {
        return R.layout.action_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
        onClickTransfer();
        onClickBudgetOptionBottom();
        formatDateTime();
        setData();
    }

    @NonNull
    @Override
    public IActionLeaguePresenter<IActionLeagueView> createPresenter() {
        return new ActionLeagueDataPresenter(getAppComponent());
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

    void setData() {
        etNumberOfUser.setContent(keyValuePairNumberOfUser.getValue());
    }

    void formatDateTime() {
        etDraftTime.setContent(DateTimeUtils.convertCalendarToString(calendarDraftTime, Constant.FORMAT_DATE_TIME));
        etStartTime.setContent(DateTimeUtils.convertCalendarToString(calendarStartTime, Constant.FORMAT_DATE_TIME));
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
                        ImageUtils.captureCamera(ActionLeagueFragment.this, filePath);
                    }

                    if (extKeyValuePair.getKey().equalsIgnoreCase(getString(R.string.gallery))) {
                        ImageUtils.chooseImageFromGallery(ActionLeagueFragment.this, getString(R.string.select_value));
                    }
                }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.lvGamePlayOption)
    void onClickGamePlayOption() {
    }

    @OnClick(R.id.llTransfer)
    void onClickTransfer() {
        llTransfer.setActivated(true);
        llDraft.setActivated(false);
    }

    @OnClick(R.id.llDraft)
    void onClickDraft() {
        llTransfer.setActivated(false);
        llDraft.setActivated(true);
    }

    @OnClick(R.id.lvNumberOfUsers)
    void onClickNumberOfUsers() {
    }

    @OnClick(R.id.etNumberOfUser)
    void onClickNumberOfUser() {
        ExtKeyValuePairDialogFragment.newInstance()
                .setExtKeyValuePairs(new ArrayList<ExtKeyValuePair>() {{
                    add(new ExtKeyValuePair("04", "04"));
                    add(new ExtKeyValuePair("06", "06"));
                    add(new ExtKeyValuePair("08", "08"));
                    add(new ExtKeyValuePair("10", "10"));
                    add(new ExtKeyValuePair("12", "12"));
                }})
                .setValue(keyValuePairNumberOfUser.getKey())
                .setOnSelectedConsumer(extKeyValuePair -> {
                    keyValuePairNumberOfUser = extKeyValuePair;
                    setData();
                })
                .show(getFragmentManager(), null);
    }

    @OnClick(R.id.lvBudgetOption)
    void onClickBudgetOption() {

    }

    @OnClick(R.id.llBudgetOptionBottom)
    void onClickBudgetOptionBottom() {
        llBudgetOptionBottom.setActivated(true);
        llBudgetOptionChallenge.setActivated(false);
        llBudgetOptionDream.setActivated(false);
    }

    @OnClick(R.id.llBudgetOptionChallenge)
    void onClickBudgetOptionChallenge() {
        llBudgetOptionBottom.setActivated(false);
        llBudgetOptionChallenge.setActivated(true);
        llBudgetOptionDream.setActivated(false);
    }

    @OnClick(R.id.llBudgetOptionDream)
    void onClickBudgetOptionDream() {
        llBudgetOptionBottom.setActivated(false);
        llBudgetOptionChallenge.setActivated(false);
        llBudgetOptionDream.setActivated(true);
    }

    @OnClick(R.id.lvTradeReviewSetting)
    void onClickTradeReviewSetting() {
    }

    @OnClick(R.id.lvScoringSystem)
    void onClickScoringSystem() {
    }

    @OnClick(R.id.etDraftTime)
    void onClickDraftTime() {
        ExtDayMonthYearHourMinuteDialogFragment.newInstance()
                .setMinDate(AppUtilities.getMinCalendar())
                .setMaxDate(AppUtilities.getMaxCalendar())
                .setValueDate(calendarDraftTime)
                .setConditionFunction(calendar -> calendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    calendarDraftTime = calendar;
                    formatDateTime();
                }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.etTimePerDraftPick)
    void onClickTimePerDraftPick() {
    }

    @OnClick(R.id.etStartTime)
    void onClickStartTime() {
        ExtDayMonthYearHourMinuteDialogFragment.newInstance()
                .setMinDate(AppUtilities.getMinCalendar())
                .setMaxDate(AppUtilities.getMaxCalendar())
                .setValueDate(calendarStartTime)
                .setConditionFunction(calendar -> calendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    calendarStartTime = calendar;
                    formatDateTime();
                }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.tvCreateLeague)
    void onClickCreateLeague() {
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

        Log.e("onActivityResult", "onActivityResult");
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImageUtils.CAMERA_REQUEST: {
                    ImageLoaderUtils.displayImage(ImageUtils.getUriImageDisplayFromFile(filePath), ivImagePick);
                    break;
                }
                case ImageUtils.REQUEST_PICK_CONTENT: {
                    String pathFile = ImageFilePath.getPath(mActivity, data.getData());
                    Log.e("pathFile", "pathFile:: " + pathFile);
                    StringUtils.isNotEmpty(pathFile, s -> ImageLoaderUtils.displayImage(ImageUtils.getUriImageDisplayFromFile(new File(s)), ivImagePick));
                    break;
                }
            }
        }
    }
}
