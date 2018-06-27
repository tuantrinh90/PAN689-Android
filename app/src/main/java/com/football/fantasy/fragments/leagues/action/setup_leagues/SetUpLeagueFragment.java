package com.football.fantasy.fragments.leagues.action.setup_leagues;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.images.CircleImageViewApp;
import com.football.customizes.labels.LabelView;
import com.football.events.LeagueEvent;
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
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import java8.util.stream.StreamSupport;

public class SetUpLeagueFragment extends BaseMainMvpFragment<ISetupLeagueView, ISetUpLeaguePresenter<ISetupLeagueView>> implements ISetupLeagueView {
    static final String TAG = SetUpLeagueFragment.class.getSimpleName();
    static final String KEY_LEAGUE = "league";
    static final String KEY_LEAGUE_TITLE = "league_title";
    static final String KEY_LEAGUE_TYPE = "league_type";

    public static Bundle newBundle(LeagueResponse leagueResponse, String leagueTitle, String leagueType) {
        Bundle bundle = new Bundle();
        if (leagueResponse != null) bundle.putSerializable(KEY_LEAGUE, leagueResponse);
        bundle.putString(KEY_LEAGUE_TITLE, leagueTitle);
        bundle.putString(KEY_LEAGUE_TYPE, leagueType);
        return bundle;
    }

    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.etLeagueName)
    EditTextApp etLeagueName;
    @BindView(R.id.ivImagePick)
    CircleImageViewApp ivImagePick;
    @BindView(R.id.lvLeagueType)
    LabelView lvLeagueType;
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

    File filePath;
    Calendar calendarDraftTime;
    Calendar calendarStartTime;
    Calendar calendarTeamSetupTime;
    ExtKeyValuePair keyValuePairNumberOfUser = new ExtKeyValuePair("6", "06");
    List<ExtKeyValuePair> valuePairs = new ArrayList<ExtKeyValuePair>() {{
        add(new ExtKeyValuePair("4", "04"));
        add(new ExtKeyValuePair("6", "06"));
        add(new ExtKeyValuePair("8", "08"));
        add(new ExtKeyValuePair("10", "10"));
        add(new ExtKeyValuePair("12", "12"));
    }};

    int leagueId;
    String leagueTitle;
    String leagueType;

    BudgetOptionAdapter budgetOptionAdapter;
    List<BudgetResponse> budgetResponses;
    BudgetResponse budgetResponse;
    LeagueResponse league;

    @Override
    public int getResourceId() {
        return R.layout.setup_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

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
        leagueType = getArguments().getString(KEY_LEAGUE_TYPE);
    }

    void loadData() {
        presenter.getBudgets(leagueId);
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
                rgLeagueType.check(league.getLeagueType().equals(LeagueRequest.LEAGUE_TYPE_OPEN) ? R.id.rbOpenLeague : R.id.rbPrivateLeague);

                if (league.getGameplayOption().equals(LeagueRequest.GAMEPLAY_OPTION_TRANSFER)) {
                    onClickTransfer();
                } else {
                    onClickDraft();
                }

                String value = String.format("%02d", league.getNumberOfUser());
                keyValuePairNumberOfUser = new ExtKeyValuePair(String.valueOf(league.getNumberOfUser()), value);
                etNumberOfUser.setContent(value);
                rbRegular.setChecked(league.getScoringSystem().equals(LeagueRequest.SCORING_SYSTEM_REGULAR));
                etDescription.setContent(league.getDescription());

                // display time
                formatDateTime();
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void initView() {
        onClickTransfer();
        initBudgetOption();
        formatDateTime();
        setUpdateDataKeyValuePair();
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
            budgetOptionAdapter = new BudgetOptionAdapter(mActivity, budgetResponses, budgetResponse -> {
                this.budgetResponse = budgetResponse;
                if (budgetResponses != null && budgetResponses.size() > 0) {
                    StreamSupport.stream(budgetResponses).forEach(n -> n.setIsActivated(n.getId() == budgetResponse.getId()));
                    budgetOptionAdapter.notifyDataSetChanged(budgetResponses);
                }
            });

            rvBudgetOption.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
            rvBudgetOption.setAdapter(budgetOptionAdapter);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
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

    void setUpdateDataKeyValuePair() {
        etNumberOfUser.setContent(keyValuePairNumberOfUser.getValue());
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

    @OnClick(R.id.ivImagePick)
    void onClickImagePick() {
        mActivity.getRxPermissions().request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
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
                });
    }

    @OnClick(R.id.lvGamePlayOption)
    void onClickGamePlayOption() {
    }

    @OnClick(R.id.llTransfer)
    void onClickTransfer() {
        llTransfer.setActivated(true);
        llDraft.setActivated(false);
        llTradeReview.setVisibility(View.GONE);
        lvBudgetOption.setVisibility(View.VISIBLE);
        rvBudgetOption.setVisibility(View.VISIBLE);
        lvDraftTime.setVisibility(View.GONE);
        etDraftTime.setVisibility(View.GONE);
        lvTimePerDraftPick.setVisibility(View.GONE);
        etTimePerDraftPick.setVisibility(View.GONE);
        lvTeamSetupTime.setVisibility(View.VISIBLE);
        etTeamSetupTime.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.llDraft)
    void onClickDraft() {
//        llTransfer.setActivated(false);
//        llDraft.setActivated(true);
//        llTradeReview.setVisibility(View.VISIBLE);
//        lvBudgetOption.setVisibility(View.GONE);
//        rvBudgetOption.setVisibility(View.GONE);
//        lvDraftTime.setVisibility(View.VISIBLE);
//        etDraftTime.setVisibility(View.VISIBLE);
//        lvTimePerDraftPick.setVisibility(View.VISIBLE);
//        etTimePerDraftPick.setVisibility(View.VISIBLE);
//        lvTeamSetupTime.setVisibility(View.GONE);
//        etTeamSetupTime.setVisibility(View.GONE);
    }

    @OnClick(R.id.lvNumberOfUsers)
    void onClickNumberOfUsers() {
    }

    @OnClick(R.id.etNumberOfUser)
    void onClickNumberOfUser() {
        ExtKeyValuePairDialogFragment.newInstance()
                .setExtKeyValuePairs(valuePairs)
                .setValue(keyValuePairNumberOfUser.getKey())
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (!android.text.TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        keyValuePairNumberOfUser = extKeyValuePair;
                        setUpdateDataKeyValuePair();
                    }
                })
                .show(getFragmentManager(), null);
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
                    calendarDraftTime = calendar;
                    formatDateTime();
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
                .setValueDate(calendarTeamSetupTime == null ? Calendar.getInstance() : calendarTeamSetupTime)
                .setConditionFunction(calendar -> calendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    calendarTeamSetupTime = calendar;
                    if (calendarStartTime == null) {
                        calendarStartTime = DateTimeUtils.getCalendarNoTime(calendarTeamSetupTime.getTimeInMillis());
                    }
                    formatDateTime();
                }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.etStartTime)
    void onClickStartTime() {
        ExtDayMonthYearHourMinuteDialogFragment.newInstance()
                .setMinDate(AppUtilities.getMinCalendar())
                .setMaxDate(AppUtilities.getMaxCalendar())
                .setValueDate(calendarStartTime == null ? Calendar.getInstance() : calendarStartTime)
                .setConditionFunction(calendar -> calendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    calendarStartTime = calendar;
                    if (calendarTeamSetupTime == null) {
                        calendarTeamSetupTime = DateTimeUtils.getCalendarNoTime(calendarStartTime.getTimeInMillis());
                    }
                    formatDateTime();
                }).show(getFragmentManager(), null);
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

    LeagueRequest getLeagueRequest() {
        LeagueRequest leagueRequest = new LeagueRequest();
        leagueRequest.setLeagueId(league == null ? 0 : league.getId());
        leagueRequest.setName(etLeagueName.getContent());
        leagueRequest.setLogo(filePath == null ? "" : filePath.getAbsolutePath());
        leagueRequest.setLeagueType(rbOpenLeague.isChecked() ? LeagueRequest.LEAGUE_TYPE_OPEN : LeagueRequest.LEAGUE_TYPE_PRIVATE);
        leagueRequest.setGameplayOption(llTransfer.isActivated() ? LeagueRequest.GAMEPLAY_OPTION_TRANSFER : LeagueRequest.GAMEPLAY_OPTION_DRAFT);
        leagueRequest.setNumberOfUser(Integer.parseInt(keyValuePairNumberOfUser.getKey()));
        leagueRequest.setTradeReview(rgTradeReviewSetting.getCheckedRadioButtonId() == R.id.rbNoTradeReview ?
                LeagueRequest.TRADE_REVIEW_NO_REVIEW : (rgTradeReviewSetting.getCheckedRadioButtonId() == R.id.rbTradeReviewCreator ?
                LeagueRequest.TRADE_REVIEW_CREATOR : LeagueRequest.TRADE_REVIEW_MEMBER));
        leagueRequest.setBudgetId(budgetResponse == null ? 0 : budgetResponse.getId());
        leagueRequest.setScoringSystem(rbRegular.isChecked() ? LeagueRequest.SCORING_SYSTEM_REGULAR : LeagueRequest.SCORING_SYSTEM_POINTS);
        leagueRequest.setTeamSetup(DateTimeUtils.convertCalendarToString(calendarTeamSetupTime, Constant.FORMAT_DATE_TIME_SERVER));
        leagueRequest.setStartAt(DateTimeUtils.convertCalendarToString(calendarStartTime, Constant.FORMAT_DATE_TIME_SERVER));
        leagueRequest.setDraftTime(DateTimeUtils.convertCalendarToString(calendarDraftTime, Constant.FORMAT_DATE_TIME_SERVER));
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
            } else {
                etTeamSetupTime.setError(null);
                if (calendarStartTime.getTimeInMillis() < calendarTeamSetupTime.getTimeInMillis()) {
                    etTeamSetupTime.setError(getString(R.string.league_invalid_setup_time));
                    result = false;
                }
            }
        } else {
            if (etDraftTime.isEmpty(mActivity)) {
                result = false;
            } else {
                etDraftTime.setError(null);
                if (calendarStartTime.getTimeInMillis() < calendarDraftTime.getTimeInMillis()) {
                    etDraftTime.setError(getString(R.string.league_invalid_start_draft_time));
                    result = false;
                }
            }
        }

        //startAt
        if (etStartTime.isEmpty(mActivity)) {
            result = false;
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
                        if (n.getId() == league.getBudgetId()) {
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
                        leagueTitle,
                        leagueType))
                .start(SetupTeamFragment.class);
        getActivity().finish();
    }

    @Override
    public void updateSuccess(LeagueResponse league) {
        bus.send(new LeagueEvent(league));
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
