package com.football.fantasy.fragments.leagues.action.setup_leagues;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.bon.customview.datetime.ExtDayMonthYearHourMinuteDialogFragment;
import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.radiobutton.ExtRadioButton;
import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageFilePath;
import com.bon.image.ImageUtils;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.bon.util.DateTimeUtils;
import com.bon.util.StringUtils;
import com.football.adapters.BudgetOptionAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.images.CircleImageViewApp;
import com.football.customizes.labels.LabelView;
import com.football.customizes.recyclerview.deco.GridSpacingItemDecoration;
import com.football.events.LeagueEvent;
import com.football.fantasy.BuildConfig;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
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
import butterknife.BindViews;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import java8.util.stream.StreamSupport;

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_DRAFT;
import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;
import static com.football.models.responses.LeagueResponse.LEAGUE_TYPE_OPEN;
import static com.football.models.responses.LeagueResponse.LEAGUE_TYPE_PRIVATE;
import static com.football.models.responses.LeagueResponse.SCORING_SYSTEM_POINTS;
import static com.football.models.responses.LeagueResponse.SCORING_SYSTEM_REGULAR;
import static com.football.models.responses.LeagueResponse.TRADE_REVIEW_CREATOR;
import static com.football.models.responses.LeagueResponse.TRADE_REVIEW_MEMBER;
import static com.football.models.responses.LeagueResponse.TRADE_REVIEW_NO_REVIEW;
import static com.football.models.responses.LeagueResponse.WAITING_FOR_START;

public class SetUpLeagueFragment extends BaseMvpFragment<ISetupLeagueView, ISetUpLeaguePresenter<ISetupLeagueView>> implements ISetupLeagueView {
    private static final String TAG = SetUpLeagueFragment.class.getSimpleName();
    private static final String KEY_LEAGUE = "league";
    private static final String KEY_LEAGUE_TITLE = "league_title";

    public static Bundle newBundle(LeagueResponse leagueResponse, String leagueTitle) {
        Bundle bundle = new Bundle();
        if (leagueResponse != null) bundle.putSerializable(KEY_LEAGUE, leagueResponse);
        bundle.putString(KEY_LEAGUE_TITLE, leagueTitle);
        return bundle;
    }

    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.etLeagueName)
    EditTextApp etLeagueName;
    @BindView(R.id.ivImagePick)
    CircleImageViewApp ivImagePick;
    @BindView(R.id.rgLeagueType)
    RadioGroup rgLeagueType;
    @BindView(R.id.rbOpenLeague)
    ExtRadioButton rbOpenLeague;
    @BindView(R.id.rbPrivateLeague)
    ExtRadioButton rbPrivateLeague;
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
    @BindView(R.id.rvBudgetOption)
    RecyclerView rvBudgetOption;
    @BindView(R.id.llTradeReview)
    LinearLayout llTradeReview;
    @BindView(R.id.lvTradeReviewSetting)
    LabelView lvTradeReviewSetting;
    @BindView(R.id.rgTradeReviewSetting)
    RadioGroup rgTradeReviewSetting;
    @BindView(R.id.rbNoTradeReview)
    ExtRadioButton rbNoTradeReview;
    @BindView(R.id.rbTradeReviewCreator)
    ExtRadioButton rbTradeReviewCreator;
    @BindView(R.id.rbTradeReviewUsers)
    ExtRadioButton rbTradeReviewUsers;
    @BindView(R.id.lvScoringSystem)
    LabelView lvScoringSystem;
    @BindView(R.id.rgScoringSystem)
    RadioGroup rgScoringSystem;
    @BindView(R.id.rbRegular)
    ExtRadioButton rbRegular;
    @BindView(R.id.rbPointPerStats)
    ExtRadioButton rbPointPerStats;
    @BindView(R.id.lvDraftTime)
    LabelView lvDraftTime;
    @BindView(R.id.etDraftTime)
    EditTextApp etDraftTime;
    @BindView(R.id.lvTimePerDraftPick)
    LabelView lvTimePerDraftPick;
    @BindView(R.id.etTimePerDraftPick)
    EditTextApp etTimePerDraftPick;
    @BindView(R.id.lvTeamSetupTime)
    LabelView lvTeamSetupTime;
    @BindView(R.id.etTeamSetupTime)
    EditTextApp etTeamSetupTime;
    @BindView(R.id.lvStartTime)
    LabelView lvStartTime;
    @BindView(R.id.etStartTime)
    EditTextApp etStartTime;
    @BindView(R.id.etDescription)
    EditTextApp etDescription;
    @BindView(R.id.tvCreateLeague)
    ExtTextView tvCreateLeague;
    @BindView(R.id.vKeyBoard)
    View vKeyBoard;

    @BindViews({R.id.rbOpenLeague, R.id.rbPrivateLeague, R.id.llTransfer, R.id.llDraft,
            R.id.etNumberOfUser, R.id.rvBudgetOption,
            R.id.rbNoTradeReview, R.id.rbTradeReviewCreator, R.id.rbTradeReviewUsers,
            R.id.rbRegular, R.id.rbPointPerStats,
            R.id.etDraftTime, R.id.etTimePerDraftPick, R.id.etTeamSetupTime})
    View[] viewsOngoing;

    private File filePath;
    private Calendar calendarDraftTime;
    private Calendar calendarStartTime;
    private Calendar calendarTeamSetupTime;
    private ExtKeyValuePair keyValuePairNumberOfUser;
    private ExtKeyValuePair keyValuePairTimePerDraft;
    private List<ExtKeyValuePair> valuePairsNumberOfUser;
    private List<ExtKeyValuePair> valuePairsTimePerDraft;

    private int leagueId;
    private String leagueTitle;

    private int inputChangedId; // edt cuối cùng đã thay đổi
    private BudgetOptionAdapter budgetOptionAdapter;
    private List<BudgetResponse> budgetResponses;
    private BudgetResponse budgetResponse;
    private LeagueResponse league;

    @Override
    public int getResourceId() {
        return R.layout.setup_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initData();
        initView();
        displayData();
        loadData();
    }

    void getDataFromBundle() {
        if (getArguments() == null) return;
        if (getArguments().containsKey(KEY_LEAGUE)) {
            league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        }
        leagueTitle = getArguments().getString(KEY_LEAGUE_TITLE);
    }

    void loadData() {
        presenter.getBudgets(leagueId);
    }

    private void initData() {
        keyValuePairNumberOfUser = new ExtKeyValuePair("6", "06");
        keyValuePairTimePerDraft = new ExtKeyValuePair("30", getString(R.string.s30));
        valuePairsNumberOfUser = new ArrayList<ExtKeyValuePair>() {{
            add(new ExtKeyValuePair("4", "04"));
            add(new ExtKeyValuePair("6", "06"));
            add(new ExtKeyValuePair("8", "08"));
            add(new ExtKeyValuePair("10", "10"));
            add(new ExtKeyValuePair("12", "12"));
        }};
        valuePairsTimePerDraft = new ArrayList<ExtKeyValuePair>() {{
            add(new ExtKeyValuePair("30", getString(R.string.s30)));
            add(new ExtKeyValuePair("60", getString(R.string.s60)));
            add(new ExtKeyValuePair("90", getString(R.string.s90)));
            add(new ExtKeyValuePair("120", getString(R.string.s120)));
        }};
    }

    private void displayData() {
        try {
            ivImagePick.getImageView().setImageResource(R.drawable.bg_image_pick);

            // display data to views
            if (league != null) {
                // update id
                leagueId = league.getId();

                // time
                calendarStartTime = league.getStartAtCalendar();
                calendarDraftTime = league.getDraftTimeCalendar();
                calendarTeamSetupTime = league.getTeamSetUpCalendar();

                // fill data
                etLeagueName.setContent(league.getName());
                ivImagePick.setImageUri(league.getLogo());
                rgLeagueType.check(league.getLeagueType().equals(LEAGUE_TYPE_OPEN) ? R.id.rbOpenLeague : R.id.rbPrivateLeague);

                toggleTransfer(league.equalsGameplay(GAMEPLAY_OPTION_TRANSFER));

                // transfer mode
                String valueTransfer = String.format("%02d", league.getNumberOfUser());
                keyValuePairNumberOfUser = new ExtKeyValuePair(String.valueOf(league.getNumberOfUser()), valueTransfer);

                // draft mode
                String valueDraft = String.format("%02d", league.getTimeToPick());
                keyValuePairTimePerDraft = new ExtKeyValuePair(valueDraft, valueDraft);

                etNumberOfUser.setContent(valueTransfer);
                etTimePerDraftPick.setContent(valueDraft);
                rgScoringSystem.check(league.getScoringSystem().equals(SCORING_SYSTEM_REGULAR) ? R.id.rbRegular : R.id.rbPointPerStats);
                etDescription.setContent(league.getDescription());


                // display time
                formatDateTime();

                // disable views khi qua teamSetupTime
                if (!AppUtilities.isSetupTime(league)) {
                    for (View view : viewsOngoing) {
                        view.setEnabled(false);
                        view.setClickable(false);
                        view.setFocusable(false);
                        if (view instanceof RecyclerView) {
                            ((BudgetOptionAdapter) ((RecyclerView) view).getAdapter()).clickEnable(false);
                        }
                    }
                }
                // khi OnGoing thì ko edit đc startTime
                if (!league.equalsStatus(WAITING_FOR_START)) {
                    etStartTime.setEnabled(false);
                    etStartTime.setClickable(false);
                    etStartTime.setFocusable(false);
                }
            } else {

                if (BuildConfig.DEBUG && Build.FINGERPRINT.contains("generic")) {
                    etLeagueName.setContent("Draft Android ");

                    onClickDraft();

                    calendarDraftTime = Calendar.getInstance();
                    calendarDraftTime.add(Calendar.SECOND, 30);
                    etDraftTime.setContent(DateTimeUtils.convertCalendarToString(calendarDraftTime, Constant.FORMAT_DATE_TIME));

                    keyValuePairNumberOfUser = valuePairsNumberOfUser.get(0);
                    setUpdateNumberOfUser();

                    calendarStartTime = Calendar.getInstance();
                    calculateStartTime();
                } else {
                    // time
                    calendarStartTime = Calendar.getInstance();
                    calendarDraftTime = Calendar.getInstance();
                    calendarTeamSetupTime = Calendar.getInstance();
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void initView() {
        onClickTransfer();
        initBudgetOption();
        formatDateTime();
        setUpdateNumberOfUser();
        setUpdateTimePerDraft();
        focusDescription();

        if (!isCreateMode()) {
            tvCreateLeague.setText(R.string.update_league);
            tvHeader.setText(R.string.update_league);
        }
    }

    void focusDescription() {
        try {
            etDescription.getContentView().setOnFocusChangeListener((v, hasFocus) -> {
                Optional.from(vKeyBoard).doIfPresent(k -> k.setVisibility(hasFocus ? View.VISIBLE : View.GONE));
            });
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void initBudgetOption() {
        try {
            budgetOptionAdapter = new BudgetOptionAdapter(
                    mActivity,
                    budgetResponses,
                    budgetResponse -> {
                        if (league != null) {
                            return;
                        }

                        this.budgetResponse = budgetResponse;
                        if (budgetResponses != null && budgetResponses.size() > 0) {
                            StreamSupport.stream(budgetResponses).forEach(n -> n.setIsActivated(n.getId().equals(budgetResponse.getId())));
                            budgetOptionAdapter.notifyDataSetChanged(budgetResponses);
                        }
                    });

            rvBudgetOption.setLayoutManager(new GridLayoutManager(mActivity, 3));
            rvBudgetOption.addItemDecoration(new GridSpacingItemDecoration(3, (int) getResources().getDimension(R.dimen.padding_content), false, 0));
            rvBudgetOption.setAdapter(budgetOptionAdapter);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private boolean cannotEdit() {
        return league != null && !AppUtilities.isSetupTime(league);
    }

    @NonNull
    @Override
    public ISetUpLeaguePresenter<ISetupLeagueView> createPresenter() {
        return new SetUpLeagueDataPresenter(getAppComponent());
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

    void setUpdateNumberOfUser() {
        etNumberOfUser.setContent(keyValuePairNumberOfUser.getValue());
    }


    void setUpdateTimePerDraft() {
        etTimePerDraftPick.setContent(keyValuePairTimePerDraft.getValue());
    }

    private void calculateStartTime() {
        if (llDraft.isActivated() && !TextUtils.isEmpty(etDraftTime.getContent())) {
            // set default for start time
            int draftEstimate = AppUtilities.getDraftEstimate(
                    Integer.valueOf(keyValuePairNumberOfUser.getKey()),
                    Integer.valueOf(keyValuePairTimePerDraft.getKey()));
            calendarStartTime.setTime(calendarDraftTime.getTime());
            calendarStartTime.add(Calendar.MINUTE, draftEstimate);
            etStartTime.setContent(DateTimeUtils.convertCalendarToString(calendarStartTime, Constant.FORMAT_DATE_TIME));
        }
    }

    void formatDateTime() {
        try {
            etDraftTime.setContent(DateTimeUtils.convertCalendarToString(calendarDraftTime, Constant.FORMAT_DATE_TIME));
            etStartTime.setContent(DateTimeUtils.convertCalendarToString(calendarStartTime, Constant.FORMAT_DATE_TIME));
            etTeamSetupTime.setContent(DateTimeUtils.convertCalendarToString(calendarTeamSetupTime, Constant.FORMAT_DATE_TIME));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private boolean isCreateMode() {
        return getArguments() == null || !getArguments().containsKey(KEY_LEAGUE);
    }

    // info click

    @OnClick({R.id.lvLeagueType, R.id.lvGamePlayOption, R.id.lvNumberOfUsers, R.id.lvBudgetOption,
            R.id.lvTradeReviewSetting, R.id.lvScoringSystem, R.id.lvDraftTime, R.id.lvTimePerDraftPick,
            R.id.lvTeamSetupTime, R.id.lvStartTime})
    public void onInfoClick(View view) {
        switch (view.getId()) {
            case R.id.lvLeagueType:
                showMessage(getString(R.string.message_info_league_type));
                break;
            case R.id.lvGamePlayOption:
                showMessage(getString(R.string.message_info_gameplay_option));
                break;
            case R.id.lvNumberOfUsers:
                showMessage(getString(R.string.message_info_number_of_user));
                break;
            case R.id.lvBudgetOption:
                showMessage(getString(R.string.message_info_budget_option));
                break;
            case R.id.lvTradeReviewSetting:
//                showMessage(getString());
                break;
            case R.id.lvScoringSystem:
                showMessage(getString(R.string.message_info_scoring_system));
                break;
            case R.id.lvDraftTime:
                showMessage(getString(R.string.message_info_team_setup_time_draft));
                break;
            case R.id.lvTimePerDraftPick:
                showMessage(getString(R.string.message_info_time_per_draft));
                break;
            case R.id.lvTeamSetupTime:
                showMessage(getString(R.string.message_info_team_setup_time_transfer));
                break;
            case R.id.lvStartTime:
                showMessage(getString(llDraft.isActivated() ? R.string.message_info_start_time_draft : R.string.message_info_start_time_transfer));
                break;
        }
    }

    @OnClick(R.id.ivImagePick)
    void onClickImagePick() {
        mCompositeDisposable.add(mActivity.getRxPermissions().request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
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
                                            ImageUtils.captureCamera(SetUpLeagueFragment.this, filePath);
                                        }

                                        if (extKeyValuePair.getKey().equalsIgnoreCase(getString(R.string.gallery))) {
                                            ImageUtils.chooseImageFromGallery(SetUpLeagueFragment.this, getString(R.string.select_value));
                                        }
                                    }).show(getChildFragmentManager(), null);
                        } else {
                            showMessage(
                                    R.string.message_permission_image,
                                    R.string.ok,
                                    R.string.cancel,
                                    aVoid -> {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }, null);
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

    @OnClick(R.id.lvGamePlayOption)
    void onClickGamePlayOption() {
    }

    @OnClick(R.id.llTransfer)
    void onClickTransfer() {
        if (league == null) {
            toggleTransfer(true);
        }
    }

    @OnClick(R.id.llDraft)
    void onClickDraft() {
        if (league == null) {
            toggleTransfer(false);
        }
    }

    private void toggleTransfer(boolean active) {
        llTransfer.setActivated(active);
        llDraft.setActivated(!active);
//        llTradeReview.setVisibility(active ? View.GONE : View.VISIBLE);
        llTradeReview.setVisibility(View.GONE); // always gone
        lvBudgetOption.setVisibility(active ? View.VISIBLE : View.GONE);
        rvBudgetOption.setVisibility(active ? View.VISIBLE : View.GONE);
        lvDraftTime.setVisibility(active ? View.GONE : View.VISIBLE);
        etDraftTime.setVisibility(active ? View.GONE : View.VISIBLE);
        lvTimePerDraftPick.setVisibility(active ? View.GONE : View.VISIBLE);
        etTimePerDraftPick.setVisibility(active ? View.GONE : View.VISIBLE);
        lvTeamSetupTime.setVisibility(active ? View.VISIBLE : View.GONE);
        etTeamSetupTime.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.etNumberOfUser)
    void onClickNumberOfUser() {
        if (cannotEdit()) {
            return;
        }

        ExtKeyValuePairDialogFragment.newInstance()
                .setExtKeyValuePairs(valuePairsNumberOfUser)
                .setValue(keyValuePairNumberOfUser.getKey())
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        keyValuePairNumberOfUser = extKeyValuePair;
                        setUpdateNumberOfUser();

                        calculateStartTime();
                    }
                })
                .show(getChildFragmentManager(), null);
    }

    @OnClick(R.id.lvBudgetOption)
    void onClickBudgetOption() {

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
                .setValueDate(calendarDraftTime == null ? Calendar.getInstance() : calendarDraftTime)
                .setConditionFunction(calendar -> calendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    calendarDraftTime.setTime(calendar.getTime());
                    etDraftTime.setContent(DateTimeUtils.convertCalendarToString(calendarDraftTime, Constant.FORMAT_DATE_TIME));
                    calculateStartTime();
                }).show(getChildFragmentManager(), null);
    }

    @OnClick(R.id.etTimePerDraftPick)
    void onClickTimePerDraftPick() {
        if (cannotEdit()) {
            return;
        }

        ExtKeyValuePairDialogFragment.newInstance()
                .setExtKeyValuePairs(valuePairsTimePerDraft)
                .setValue(keyValuePairTimePerDraft.getKey())
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        keyValuePairTimePerDraft = extKeyValuePair;
                        setUpdateTimePerDraft();
                        calculateStartTime();
                    }

                })
                .show(getChildFragmentManager(), null);
    }

    @OnClick(R.id.etTeamSetupTime)
    void onClickTeamSetupTime() {
        if (cannotEdit()) {
            return;
        }

        ExtDayMonthYearHourMinuteDialogFragment.newInstance()
                .setMinDate(AppUtilities.getMinCalendar())
                .setMaxDate(AppUtilities.getMaxCalendar())
                .setValueDate(calendarTeamSetupTime == null ? Calendar.getInstance() : calendarTeamSetupTime)
                .setConditionFunction(calendar -> calendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    inputChangedId = R.id.etTeamSetupTime;
                    calendarTeamSetupTime = calendar;
                    calendarTeamSetupTime = DateTimeUtils.getCalendarNoTime(calendar.getTimeInMillis());
                    etTeamSetupTime.setContent(DateTimeUtils.convertCalendarToString(calendarTeamSetupTime, Constant.FORMAT_DATE_TIME));

                    // cập nhật startTime khi ko có dữ liệu
                    if (llTransfer.isActivated() && TextUtils.isEmpty(etStartTime.getContent())) {
                        calendarStartTime = (Calendar) calendarTeamSetupTime.clone();
                        setTextTime(etStartTime, calendarStartTime);
                    }
                }).show(getChildFragmentManager(), null);
    }

    @OnClick(R.id.etStartTime)
    void onClickStartTime() {
        ExtDayMonthYearHourMinuteDialogFragment.newInstance()
                .setMinDate(AppUtilities.getMinCalendar())
                .setMaxDate(AppUtilities.getMaxCalendar())
                .setValueDate(calendarStartTime == null ? Calendar.getInstance() : calendarStartTime)
                .setConditionFunction(calendar -> calendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    inputChangedId = R.id.etStartTime;
                    calendarStartTime = calendar;
                    if (calendarTeamSetupTime == null) {
                        calendarTeamSetupTime = DateTimeUtils.getCalendarNoTime(calendarStartTime.getTimeInMillis());
                    }
                    setTextTime(etStartTime, calendarStartTime);
                }).show(getChildFragmentManager(), null);
    }

    @OnClick(R.id.tvCreateLeague)
    void onClickCreateLeague() {
        if (!isValid()) return;

        LeagueRequest leagueRequest = getLeagueRequest();
        if (isCreateMode()) {
            presenter.createLeague(leagueRequest);
        } else {
            presenter.updateLeague(leagueId, leagueRequest);
        }
    }

    private void setTextTime(EditTextApp editTextApp, Calendar calendar) {
        editTextApp.setContent(DateTimeUtils.convertCalendarToString(calendar, Constant.FORMAT_DATE_TIME));
    }

    LeagueRequest getLeagueRequest() {
        LeagueRequest leagueRequest = new LeagueRequest();
        leagueRequest.setLeagueId(league == null ? 0 : league.getId());
        leagueRequest.setName(etLeagueName.getContent());
        leagueRequest.setLogo(filePath == null ? "" : filePath.getAbsolutePath());
        leagueRequest.setLeagueType(rbOpenLeague.isChecked() ? LEAGUE_TYPE_OPEN : LEAGUE_TYPE_PRIVATE);
        leagueRequest.setGameplayOption(llTransfer.isActivated() ? GAMEPLAY_OPTION_TRANSFER : GAMEPLAY_OPTION_DRAFT);
        leagueRequest.setNumberOfUser(Integer.parseInt(keyValuePairNumberOfUser.getKey()));
        leagueRequest.setTradeReview(rgTradeReviewSetting.getCheckedRadioButtonId() == R.id.rbNoTradeReview ?
                TRADE_REVIEW_NO_REVIEW : (rgTradeReviewSetting.getCheckedRadioButtonId() == R.id.rbTradeReviewCreator ?
                TRADE_REVIEW_CREATOR : TRADE_REVIEW_MEMBER));
        leagueRequest.setBudgetId(budgetResponse == null ? 0 : budgetResponse.getId());
        leagueRequest.setScoringSystem(rbRegular.isChecked() ? SCORING_SYSTEM_REGULAR : SCORING_SYSTEM_POINTS);
        leagueRequest.setTeamSetup(DateTimeUtils.convertCalendarToString(calendarTeamSetupTime, Constant.FORMAT_DATE_TIME_SERVER));
        leagueRequest.setStartAt(DateTimeUtils.convertCalendarToString(calendarStartTime, Constant.FORMAT_DATE_TIME_SERVER));
        leagueRequest.setDraftTime(DateTimeUtils.convertCalendarToString(calendarDraftTime, Constant.FORMAT_DATE_TIME_SERVER));
        leagueRequest.setTimeToPick(Integer.valueOf(keyValuePairTimePerDraft.getKey()));
        leagueRequest.setDescription(etDescription.getContent());
        return leagueRequest;
    }

    boolean isValid() {
        boolean result = true;
        if (etLeagueName.isEmpty(mActivity)) {
            result = false;
        }

        if (etNumberOfUser.isEmpty(mActivity)) {
            result = false;
        }

        if (llTransfer.isActivated()) {
            if (etTeamSetupTime.isEmpty(mActivity)) {
                result = false;
            } else if (etStartTime.isEmpty(mActivity)) {
                result = false;
            } else {
                etTeamSetupTime.setError(null);
                if (calendarStartTime.getTimeInMillis() < calendarTeamSetupTime.getTimeInMillis()) {
                    if (inputChangedId == R.id.etStartTime) {
                        etStartTime.setError(getString(R.string.league_invalid_start_time));
                    } else {
                        etTeamSetupTime.setError(getString(R.string.league_invalid_setup_time));
                    }
                    result = false;
                }
            }
        } else {
            if (etDraftTime.isEmpty(mActivity)) {
                result = false;
            } else if (etStartTime.isEmpty(mActivity)) {
                result = false;
            } else {
                etDraftTime.setError(null);
                if (calendarStartTime.getTimeInMillis() < calendarDraftTime.getTimeInMillis()) {
                    etDraftTime.setError(getString(R.string.league_invalid_start_draft_time_after_start_time));
                    result = false;

                }
                // check khoảng thời gian giữa draftTime và startTime có phù hợp ko
                else if (calendarStartTime.getTimeInMillis() < calendarDraftTime.getTimeInMillis() +
                        AppUtilities.getDraftEstimate(
                                Integer.valueOf(keyValuePairNumberOfUser.getKey()),
                                Integer.valueOf(keyValuePairTimePerDraft.getKey())) * 60 * 1000) {
                    showMessage(getString(R.string.league_invalid_start_draft_time));
                    result = false;
                }
            }
        }

        return result;
    }

    @Override
    public void displayBudgets(List<BudgetResponse> budgets) {
        try {
            budgetResponses = budgets;

            // set default value
            if (budgetResponses != null && budgetResponses.size() > 0) {
                if (league != null) {
                    StreamSupport.stream(budgetResponses).forEach(n -> {
                        if (n.getId().equals(league.getBudgetId())) {
                            n.setIsActivated(true);
                            budgetResponse = n;
                        } else {
                            n.setIsActivated(false);
                        }
                    });
                } else {
                    budgetResponses.get(0).setIsActivated(true);
                    budgetResponse = budgetResponses.get(0);
                }
            }

            // update data
            budgetOptionAdapter.notifyDataSetChanged(budgetResponses);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void openCreateTeam(LeagueResponse league) {
        bus.send(new LeagueEvent());
        AloneFragmentActivity.with(this)
                .parameters(SetupTeamFragment.newBundle(
                        null,
                        league.getId(),
                        leagueTitle))
                .start(SetupTeamFragment.class);
        mActivity.finish();
    }

    @Override
    public void updateSuccess(LeagueResponse league) {
        bus.send(new LeagueEvent(LeagueEvent.ACTION_UPDATE, league));
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
