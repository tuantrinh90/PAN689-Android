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
import com.bon.customview.radiobutton.ExtRadioButton;
import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageFilePath;
import com.bon.image.ImageLoaderUtils;
import com.bon.image.ImageUtils;
import com.bon.permission.PermissionUtils;
import com.bon.util.DateTimeUtils;
import com.bon.util.StringUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.labels.LabelView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.team.CreateTeamFragment;
import com.football.models.requests.LeagueRequest;
import com.football.models.responses.BudgetResponse;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ActionLeagueFragment extends BaseMainMvpFragment<IActionLeagueView, IActionLeaguePresenter<IActionLeagueView>> implements IActionLeagueView {

    public static final String KEY_LEAGUE = "league";

    public static ActionLeagueFragment newInstance() {
        return new ActionLeagueFragment();
    }

    @BindView(R.id.etLeagueName)
    EditTextApp etLeagueName;
    @BindView(R.id.ivImagePick)
    ImageView ivImagePick;
    @BindView(R.id.lvLeagueType)
    LabelView lvLeagueType;
    @BindView(R.id.rbOpenLeague)
    ExtRadioButton rbOpenLeague;
    @BindView(R.id.rbRegular)
    ExtRadioButton rbRegular;
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
    @BindView(R.id.llBudgetOptionChallenge)
    LinearLayout llBudgetOptionChallenge;
    @BindView(R.id.llBudgetOptionDream)
    LinearLayout llBudgetOptionDream;
    @BindView(R.id.tvBudgetOption1)
    ExtTextView tvBudgetOption1;
    @BindView(R.id.tvBudgetOption1Value)
    ExtTextView tvBudgetOption1Value;
    @BindView(R.id.tvBudgetOption2)
    ExtTextView tvBudgetOption2;
    @BindView(R.id.tvBudgetOption2Value)
    ExtTextView tvBudgetOption2Value;
    @BindView(R.id.tvBudgetOption3)
    ExtTextView tvBudgetOption3;
    @BindView(R.id.tvBudgetOption3Value)
    ExtTextView tvBudgetOption3Value;


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
    @BindView(R.id.etTeamSetupTime)
    EditTextApp etTeamSetupTime;
    @BindView(R.id.etStartTime)
    EditTextApp etStartTime;
    @BindView(R.id.etDescription)
    EditTextApp etDescription;
    @BindView(R.id.tvCreateLeague)
    ExtTextView tvCreateLeague;
    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;

    File filePath;
    Calendar calendarDraftTime = Calendar.getInstance();
    Calendar calendarStartTime = Calendar.getInstance();
    Calendar calendarTeamSetupTime = Calendar.getInstance();
    ExtKeyValuePair keyValuePairNumberOfUser = new ExtKeyValuePair("06", "06");

    private int leagueId;
    private List<BudgetResponse> budgets;

    @Override
    public int getResourceId() {
        return R.layout.action_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        presenter.getBudgets(leagueId);

        initView();
        displayData();
    }

    private void displayData() {
        LeagueResponse league = (LeagueResponse) (getArguments() != null ? getArguments().getSerializable(KEY_LEAGUE) : null);
        // display data to views
        if (league != null) {
            leagueId = league.getId();

            etLeagueName.setContent(league.getName());
            ImageLoaderUtils.displayImage(league.getLogo(), ivImagePick);
            rbOpenLeague.setChecked(league.getLeagueType().equals(LeagueRequest.LEAGUE_TYPE_OPEN));
            if (league.getGameplayOption().equals(LeagueRequest.GAMEPLAY_OPTION_TRANSFER)) {
                onClickTransfer();
            } else {
                onClickDraft();
            }
            etNumberOfUser.setContent(String.format("%02d", league.getNumberOfUser()));
            switch (league.getBudgetId()) {
                case LeagueRequest.BUDGET_BOTTOM:
                    onClickBudgetOptionBottom();
                    break;

                case LeagueRequest.BUDGET_CHALLENGE:
                    onClickBudgetOptionChallenge();
                    break;

                case LeagueRequest.BUDGET_DREAM:
                    onClickBudgetOptionDream();
                    break;
            }
            rbRegular.setChecked(league.getScoringSystem().equals(LeagueRequest.SCORING_SYSTEM_REGULAR));
            etTeamSetupTime.setContent(league.getTeamSetup());
            etStartTime.setContent(league.getStartAt());
            etDescription.setContent(league.getDescription());


        }
    }

    void initView() {
        onClickTransfer();
        onClickBudgetOptionBottom();
        formatDateTime();
        setData();

        if (!isCreateMode()) {
            tvCreateLeague.setText(R.string.update_league);
            tvHeader.setText(R.string.update_league);
        }
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
        etDraftTime.setContent(DateTimeUtils.convertCalendarToString(calendarDraftTime, Constant.FORMAT_DATE_TIME_SERVER));
        etStartTime.setContent(DateTimeUtils.convertCalendarToString(calendarStartTime, Constant.FORMAT_DATE_TIME_SERVER));
        etTeamSetupTime.setContent(DateTimeUtils.convertCalendarToString(calendarTeamSetupTime, Constant.FORMAT_DATE_TIME_SERVER));
    }

    private boolean isTransfer() {
        return llTransfer.isActivated();
    }

    private boolean isCreateMode() {
        return getArguments() == null || !getArguments().containsKey(KEY_LEAGUE);
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
                .setMaxDate(calendarStartTime)
                .setValueDate(calendarDraftTime)
                .setConditionFunction(calendar -> calendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    calendarDraftTime = calendar;
                    etDraftTime.setContent(DateTimeUtils.convertCalendarToString(calendarDraftTime, Constant.FORMAT_DATE_TIME_SERVER));

                }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.etTimePerDraftPick)
    void onClickTimePerDraftPick() {
    }

    @OnClick(R.id.etTeamSetupTime)
    void onClickTeamSetupTime() {
        ExtDayMonthYearHourMinuteDialogFragment.newInstance()
                .setMinDate(AppUtilities.getMinCalendar())
                .setMaxDate(AppUtilities.getMaxCalendar())
                .setValueDate(calendarTeamSetupTime)
                .setConditionFunction(calendar -> calendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    calendarTeamSetupTime = calendar;
                    etTeamSetupTime.setContent(DateTimeUtils.convertCalendarToString(calendarTeamSetupTime, Constant.FORMAT_DATE_TIME_SERVER));
                }).show(getFragmentManager(), null);
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
//                    if (calendarStartTime.before(isTransfer() ? calendarTeamSetupTime : calendarDraftTime)) {
//                        etStartTime.setError()
//                        return;
//                    }
                    etStartTime.setContent(DateTimeUtils.convertCalendarToString(calendarStartTime, Constant.FORMAT_DATE_TIME_SERVER));

                }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.tvCreateLeague)
    void onClickCreateLeague() {
        LeagueRequest leagueRequest = new LeagueRequest();
        if (isValid(leagueRequest)) {
            if (isCreateMode()) {
                presenter.createLeague(leagueRequest);
            } else {
                presenter.updateLeague(leagueId, leagueRequest);
            }
        }
    }

    private boolean isValid(LeagueRequest leagueRequest) {
        // league name
        if (!etLeagueName.isEmpty(mActivity)) {
            String name = etLeagueName.getContent();
            if (name.length() > Constant.MAX_LENGTH_LEAGUE_NAME) {
                etLeagueName.setError(getString(R.string.error_league_name_not_valid));
                return false;
            }
            leagueRequest.name = name;
        } else {
            return false;
        }

        // logo
        leagueRequest.logo = filePath == null ? "" : filePath.getAbsolutePath();

        // league type
        leagueRequest.league_type = rbOpenLeague.isChecked() ? LeagueRequest.LEAGUE_TYPE_OPEN : LeagueRequest.LEAGUE_TYPE_PRIVATE;

        // number_of_user
        leagueRequest.number_of_user = Integer.parseInt(etNumberOfUser.getContent());

        // scoring_system
        leagueRequest.scoring_system = rbRegular.isChecked() ? LeagueRequest.SCORING_SYSTEM_REGULAR : LeagueRequest.SCORING_SYSTEM_POINTS;

        //start_at
        if (etStartTime.isEmpty(mActivity)) {
            return false;
        }
        leagueRequest.start_at = etStartTime.getContent();

        // description
        if (!etDescription.isEmpty(mActivity)) {
            String des = etDescription.getContent();
            if (des.length() > Constant.MAX_LENGTH_LEAGUE_NAME) {
                etDescription.setError(getString(R.string.error_league_description_not_valid));
                return false;
            }
            leagueRequest.description = des;
        } else {
            return false;
        }

        // gameplay_option
        leagueRequest.gameplay_option = llTransfer.isActivated() ? LeagueRequest.GAMEPLAY_OPTION_TRANSFER : LeagueRequest.GAMEPLAY_OPTION_DRAFT;

        // budget_id
        try {
            leagueRequest.budget_id = llBudgetOptionBottom.isActivated() ? budgets.get(0).getId() :
                    (llBudgetOptionChallenge.isActivated() ? budgets.get(1).getId()
                            : (llBudgetOptionDream.isActivated() ? budgets.get(2).getId() : budgets.get(0).getId()));

        } catch (Exception e) {
            // default
            leagueRequest.budget_id = llBudgetOptionBottom.isActivated() ? 1 : (llBudgetOptionChallenge.isActivated() ? 2 : (llBudgetOptionDream.isActivated() ? 3 : 1));
        }

        // team_setup
        if (etTeamSetupTime.isEmpty(mActivity)) {
            return false;
        }
        leagueRequest.team_setup = etTeamSetupTime.getContent();

        return true;
    }

    @Override
    public void displayBudgets(List<BudgetResponse> budgets) {
        this.budgets = budgets;

        try {
            tvBudgetOption1.setText(budgets.get(0).getName());
            tvBudgetOption2.setText(budgets.get(1).getName());
            tvBudgetOption3.setText(budgets.get(2).getName());

            tvBudgetOption1Value.setText(getString(R.string.money_prefix, budgets.get(0).getValueDisplay() + ""));
            tvBudgetOption2Value.setText(getString(R.string.money_prefix, budgets.get(1).getValueDisplay() + ""));
            tvBudgetOption3Value.setText(getString(R.string.money_prefix, budgets.get(2).getValueDisplay() + ""));

        } catch (Exception e) {

        }

    }

    @Override
    public void openCreateTeam(Integer leagueId) {
        Bundle bundle = new Bundle();
        bundle.putInt(CreateTeamFragment.KEY_LEAGUE_ID, leagueId);

        AloneFragmentActivity.with(this)
                .parameters(bundle)
                .start(CreateTeamFragment.class);
        mActivity.finish();
    }

    @Override
    public void updateSuccess() {
        showMessage(getString(R.string.update_success));
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
