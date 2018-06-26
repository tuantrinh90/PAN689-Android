package com.football.fantasy.fragments.more.profile.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;

import com.bon.customview.datetime.ExtDayMonthYearDialogFragment;
import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.image.ImageLoaderUtils;
import com.bon.interfaces.Optional;
import com.bon.util.DateTimeUtils;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.responses.UserResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class EditProfileFragment extends BaseMainMvpFragment<IEditProfileView, IEditProfilePresenter<IEditProfileView>> implements IEditProfileView {

    @BindView(R.id.etFirstName)
    EditTextApp etFirstName;
    @BindView(R.id.etLastName)
    EditTextApp etLastName;
    @BindView(R.id.ivAvatar)
    CircleImageViewApp ivAvatar;
    @BindView(R.id.etDob)
    EditTextApp etDob;
    @BindView(R.id.etGender)
    EditTextApp etGender;
    @BindView(R.id.etAddress)
    EditTextApp etAddress;
    @BindView(R.id.etPhone)
    EditTextApp etPhone;
    @BindView(R.id.etEmail)
    EditTextApp etEmail;
    @BindView(R.id.etIntroduction)
    EditTextApp etIntroduction;

    Calendar calendarDob;
    private UserResponse user;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.profile_edit_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initToolbar();

        presenter.getProfile();
    }

    void initToolbar() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.color_white)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_blue)));

    }

    @Override
    public int getTitleId() {
        return R.string.more_my_profile;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    @NonNull
    @Override
    public IEditProfilePresenter<IEditProfileView> createPresenter() {
        return new EditProfileDataPresenter(getAppComponent());
    }

    @OnClick({R.id.ivAvatar, R.id.etDob, R.id.etGender})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivAvatar:
                pickAvatar();
                break;
            case R.id.etDob:
                pickDob();
                break;
            case R.id.etGender:
                pickGender();
                break;
        }
    }

    private void pickAvatar() {

    }

    private void pickDob() {
        ExtDayMonthYearDialogFragment.newInstance()
                .setMinDate(AppUtilities.getMinCalendar())
                .setMaxDate(AppUtilities.getMaxCalendar())
                .setValueDate(calendarDob == null ? Calendar.getInstance() : calendarDob)
                .setConditionFunction(calendar -> calendar.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis())
                .setCalendarConsumer(calendar -> {
                    calendarDob = calendar;
                    etDob.setContent(DateTimeUtils.convertCalendarToString(calendarDob, Constant.FORMAT_DATE));

                }).show(getFragmentManager(), null);
    }

    private void pickGender() {
        ExtKeyValuePairDialogFragment.newInstance()
                .setExtKeyValuePairs(new ArrayList<ExtKeyValuePair>() {{
                    add(new ExtKeyValuePair(String.valueOf(UserResponse.GENDER_FEMALE), "Female"));
                    add(new ExtKeyValuePair(String.valueOf(UserResponse.GENDER_MALE), "Male"));
                    add(new ExtKeyValuePair(String.valueOf(UserResponse.GENDER_OTHER), "Other"));
                }})
                .setValue(String.valueOf(user.getGender()))
                .setOnSelectedConsumer(pair -> {
                    if (!TextUtils.isEmpty(pair.getKey())) {
                        etGender.setContent(pair.getValue());
                        user.setGender(Integer.valueOf(pair.getKey()));
                    }
                })
                .show(getFragmentManager(), null);
    }


    @Override
    public void displayUser(UserResponse user) {
        this.user = user;
        etFirstName.setContent(user.getFirstName());
        etLastName.setContent(user.getLastName());
        ImageLoaderUtils.displayImage(user.getPhoto(), ivAvatar.getImageView());
        calendarDob = DateTimeUtils.convertStringToCalendar(user.getBirthday(), Constant.FORMAT_DATE_SERVER);
        etDob.setContent(DateTimeUtils.convertCalendarToString(calendarDob, Constant.FORMAT_DATE));
        etGender.setContent(getString(user.getGender() == UserResponse.GENDER_MALE ? R.string.male : R.string.female));
        etAddress.setContent(user.getAddress());
        etPhone.setContent(user.getPhone());
        etEmail.setContent(user.getEmail());
        etIntroduction.setContent(user.getDescription());
    }
}
