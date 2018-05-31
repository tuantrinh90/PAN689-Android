package com.football.fantasy.fragments.account.forgot_success;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;

import butterknife.OnClick;

public class ForgotPasswordSuccessFragment
        extends BaseMvpFragment<IForgotPasswordSuccessView, IForgotPasswordSuccessPresenter<IForgotPasswordSuccessView>> implements IForgotPasswordSuccessView {

    public static final String KEY_EMAIL = "email";

    private String email;

    @Override
    public int getResourceId() {
        return R.layout.forgot_password_success_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    private void getDataFromBundle() {
        email = getArguments().getString(KEY_EMAIL);
    }

    @NonNull
    @Override
    public IForgotPasswordSuccessPresenter<IForgotPasswordSuccessView> createPresenter() {
        return new ForgotPasswordSuccessDataPresenter(getAppComponent());
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


    @OnClick(R.id.tvReSend)
    void onClickResend() {
        presenter.recoverPassword(email);
    }

    @Override
    public void recoverSuccess() {
        getActivity().finish();
    }
}
