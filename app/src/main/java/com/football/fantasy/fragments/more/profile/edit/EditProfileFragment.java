package com.football.fantasy.fragments.more.profile.edit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
import com.bon.image.ImageFilePath;
import com.bon.image.ImageLoaderUtils;
import com.bon.image.ImageUtils;
import com.bon.interfaces.Optional;
import com.bon.util.DateTimeUtils;
import com.bon.util.EmailUtils;
import com.bon.util.StringUtils;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.images.CircleImageViewApp;
import com.football.events.UserEvent;
import com.football.fantasy.R;
import com.football.models.requests.ProfileRequest;
import com.football.models.responses.UserResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

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
    private int gender;
    private File filePath;

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

        etEmail.setEnabled(false);
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

    @OnClick({R.id.ivAvatar, R.id.etDob, R.id.etGender, R.id.tvChange})
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
            case R.id.tvChange:
                save();
                break;
        }
    }

    private void pickAvatar() {
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
                                            ImageUtils.captureCamera(EditProfileFragment.this, filePath);
                                        }

                                        if (extKeyValuePair.getKey().equalsIgnoreCase(getString(R.string.gallery))) {
                                            ImageUtils.chooseImageFromGallery(EditProfileFragment.this, getString(R.string.select_value));
                                        }
                                    }).show(getFragmentManager(), null);
                        } else {
                            pickAvatar();
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
                .setValue(String.valueOf(gender))
                .setOnSelectedConsumer(pair -> {
                    if (!TextUtils.isEmpty(pair.getKey())) {
                        etGender.setContent(pair.getValue());
                        gender = Integer.valueOf(pair.getKey());
                    }
                })
                .show(getFragmentManager(), null);
    }

    private void save() {
        if (!isValid()) return;

        ProfileRequest profileRequest = new ProfileRequest.Builder()
                .firstName(etFirstName.getContent())
                .lastName(etLastName.getContent())
                .birthday(DateTimeUtils.convertCalendarToString(DateTimeUtils.convertStringToCalendar(etDob.getContent(), Constant.FORMAT_DATE), Constant.FORMAT_DATE_SERVER))
                .gender(gender)
                .photo(filePath)
                .address(etAddress.getContent())
                .phone(etPhone.getContent())
                .description(etIntroduction.getContent())
                .build();

        presenter.updateProfile(profileRequest);
    }

    private boolean isValid() {
        boolean valid = true;

        if (etFirstName.isEmpty(mActivity)) valid = false;
        else if (etLastName.isEmpty(mActivity)) valid = false;
        else if (etFirstName.getContent().length() > Constant.MAX_NAME_40_CHARACTERS) {
            etFirstName.setError(getString(R.string.field_max_40_character));
            valid = false;
        } else if (etLastName.getContent().length() > Constant.MAX_NAME_40_CHARACTERS) {
            etLastName.setError(getString(R.string.field_max_40_character));
            valid = false;
        } else if (etAddress.getContent().length() > Constant.MAX_255_CHARACTERS) {
            etAddress.setError(getString(R.string.field_max_255_character));
            valid = false;
        } else if (etPhone.getContent().length() < Constant.MIN_PHONE || etPhone.getContent().length() > Constant.MAX_PHONE) {
            etPhone.setError(getString(R.string.field_phone_invalid));
            valid = false;
        } /*else if (!EmailUtils.isValidate(etEmail.getContent())) {
            etEmail.setError(getString(R.string.error_email_address_not_valid));
            return false;
        }*/ else if (etIntroduction.getContent().length() > Constant.MAX_255_CHARACTERS) {
            etIntroduction.setError(getString(R.string.field_max_255_character));
            valid = false;
        }

        return valid;
    }

    @Override
    public void displayUser(UserResponse user) {
        this.gender = user.getGender();

        etFirstName.setContent(user.getFirstName());
        etLastName.setContent(user.getLastName());
        ImageLoaderUtils.displayImage(user.getPhoto(), ivAvatar.getImageView());
        calendarDob = DateTimeUtils.convertStringToCalendar(user.getBirthday(), Constant.FORMAT_DATE_SERVER);
        etDob.setContent(DateTimeUtils.convertCalendarToString(calendarDob, Constant.FORMAT_DATE));
        etGender.setContent(getString(user.getGender() == UserResponse.GENDER_MALE ? R.string.male : R.string.female));
        etAddress.setContent(user.getAddress());
        etPhone.setContent(user.getPhone());
//        etEmail.setContent(user.getEmail());
        etIntroduction.setContent(user.getDescription());
    }

    @Override
    public void updateSuccessful(UserResponse user) {
        bus.send(new UserEvent());
        mActivity.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ImageUtils.CAMERA_REQUEST: {
                    ivAvatar.setImageUri(ImageUtils.getUriImageDisplayFromFile(filePath));
                    break;
                }
                case ImageUtils.REQUEST_PICK_CONTENT: {
                    String pathFile = ImageFilePath.getPath(mActivity, data.getData());
                    StringUtils.isNotEmpty(pathFile, s -> {
                        filePath = new File(s);
                        ivAvatar.setImageUri(ImageUtils.getUriImageDisplayFromFile(filePath));
                    });
                    break;
                }
            }
        }
    }
}
