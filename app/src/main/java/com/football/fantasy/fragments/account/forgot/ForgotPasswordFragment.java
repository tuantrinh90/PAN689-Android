package com.football.fantasy.fragments.account.forgot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.R;
import com.football.fantasy.fragments.account.forgot_success.ForgotPasswordSuccessFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgotPasswordFragment extends BaseMvpFragment<IForgotPasswordView, IForgotPasswordPresenter<IForgotPasswordView>> implements IForgotPasswordView {
    public static ForgotPasswordFragment newInstance() {
        return new ForgotPasswordFragment();
    }

    @BindView(R.id.etEmail)
    EditTextApp etEmail;

    @Override
    public int getResourceId() {
        return R.layout.forgot_password_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @NonNull
    @Override
    public IForgotPasswordPresenter<IForgotPasswordView> createPresenter() {
        return new ForgotPasswordDataPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public int getTitleId() {
        return R.string.sign_in;
    }

    boolean isValid() {
        boolean result = true;
        if (!etEmail.isValidEmail(mActivity)) result = false;
        return result;
    }

    @OnClick(R.id.tvSend)
    void onClickSend() {
        if (!isValid()) return;
        presenter.forgotPassword(etEmail.getContent());
    }

    @Override
    public void onSuccess(String email) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        AloneFragmentActivity.with(this)
                .parameters(bundle).start(ForgotPasswordSuccessFragment.class);
    }
}
