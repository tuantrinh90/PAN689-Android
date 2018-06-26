package com.football.fantasy.fragments.more.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.interfaces.Optional;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.fantasy.fragments.more.profile.change.ChangePasswordFragment;
import com.football.fantasy.fragments.more.profile.edit.EditProfileFragment;
import com.football.models.responses.UserResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileFragment extends BaseMainMvpFragment<IProfileView, IProfilePresenter<IProfileView>> implements IProfileView {

    private static final String KEY_ACTION_EDIT = "ACTION_EDIT";
    private static final String KEY_ACTION_CHANGE_PASSWORD = "ACTION_CHANGE_PASSWORD";
    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.tvFullName)
    ExtTextView tvFullName;
    @BindView(R.id.ivAvatar)
    CircleImageViewApp ivAvatar;
    @BindView(R.id.tvDob)
    ExtTextView tvDob;
    @BindView(R.id.tvGender)
    ExtTextView tvGender;
    @BindView(R.id.tvAddress)
    ExtTextView tvAddress;
    @BindView(R.id.tvPhone)
    ExtTextView tvPhone;
    @BindView(R.id.tvEmail)
    ExtTextView tvEmail;
    @BindView(R.id.tvIntroduction)
    ExtTextView tvIntroduction;

    private List<ExtKeyValuePair> valuePairs = new ArrayList<>();

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.profile_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();

        presenter.getProfile();
    }

    @NonNull
    @Override
    public IProfilePresenter<IProfileView> createPresenter() {
        return new ProfileDataPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.information;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.color_white)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_blue)));

        valuePairs.add(new ExtKeyValuePair(KEY_ACTION_EDIT, "Edit"));
        valuePairs.add(new ExtKeyValuePair(KEY_ACTION_CHANGE_PASSWORD, "Change password"));
    }

    @OnClick({R.id.ivMenu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                ExtKeyValuePairDialogFragment.newInstance()
                        .setValue("")
                        .setExtKeyValuePairs(valuePairs)
                        .setOnSelectedConsumer(pair -> {
                            switch (pair.getKey()) {
                                case KEY_ACTION_EDIT:
                                    launchEditProfile();
                                    break;

                                case KEY_ACTION_CHANGE_PASSWORD:
                                    launchChangePassword();
                                    break;
                            }

                        }).show(getFragmentManager(), null);
                break;
        }
    }

    private void launchEditProfile() {
        AloneFragmentActivity
                .with(this)
                .start(EditProfileFragment.class);
    }

    private void launchChangePassword() {
        AloneFragmentActivity
                .with(this)
                .start(ChangePasswordFragment.class);
    }

    @Override
    public void displayUser(UserResponse user) {
        tvFullName.setText(user.getName());
        ImageLoaderUtils.displayImage(user.getPhoto(), ivAvatar.getImageView());
        tvDob.setText(user.getBirthday());
        tvGender.setText(user.getGender() == UserResponse.GENDER_MALE ? R.string.male : R.string.female);
        tvAddress.setText(user.getAddress());
        tvPhone.setText(user.getPhone());
        tvEmail.setText(user.getEmail());
        tvIntroduction.setText(user.getDescription());
    }
}
