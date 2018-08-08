package com.football.fantasy.fragments.more.profile.change;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.interfaces.Optional;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.R;
import com.football.utilities.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordFragment extends BaseMvpFragment<IChangePasswordView, IChangePasswordPresenter<IChangePasswordView>> implements IChangePasswordView {

    @BindView(R.id.etOldPassword)
    EditTextApp etOldPassword;
    @BindView(R.id.etNewPassword)
    EditTextApp etNewPassword;
    @BindView(R.id.etConfirmPassword)
    EditTextApp etConfirmPassword;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.profile_change_password_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initToolbar();
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
    public IChangePasswordPresenter<IChangePasswordView> createPresenter() {
        return new ChangePasswordDataPresenter(getAppComponent());
    }

    @OnClick(R.id.tvSubmit)
    public void onViewClicked() {
        if (isValid()) {
            presenter.changePassword(etOldPassword.getContent(), etNewPassword.getContent());
        }
    }

    private boolean isValid() {
        if (isEmpty(etOldPassword) || isEmpty(etNewPassword) || isEmpty(etConfirmPassword)
                || !isValidCharacter(etOldPassword) || !isValidCharacter(etNewPassword) || !isValidCharacter(etConfirmPassword)) {
            return false;
        }
        if (!etNewPassword.getContent().equals(etConfirmPassword.getContent())) {
            etConfirmPassword.setError(getString(R.string.err_confirm_password));
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditTextApp editTextApp) {
        return editTextApp.isEmpty(mActivity);
    }

    private boolean isValidCharacter(EditTextApp editTextApp) {
        if (editTextApp.getContent().length() < Constant.MIN_PASSWORD) {
            editTextApp.setError(getString(R.string.min_password));
            return false;
        }
        return true;
    }

    @Override
    public void changePasswordSuccessful() {
        etOldPassword.setError(null);
        showMessage(R.string.message_change_password_success,
                R.string.ok,
                aVoid -> {
                    mActivity.finish();
                });
    }

    @Override
    public void displayCurrentPasswordError() {
        etOldPassword.setContent("");
        etOldPassword.setError(getString(R.string.old_password_incorrect));
    }
}
