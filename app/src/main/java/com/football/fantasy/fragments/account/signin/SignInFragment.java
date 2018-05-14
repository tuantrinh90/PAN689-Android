package com.football.fantasy.fragments.account.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bon.customview.edittext.ExtEditText;
import com.bon.util.ActivityUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.MainActivity;
import com.football.fantasy.R;
import com.football.fantasy.fragments.account.forgot.ForgotPasswordFragment;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by dangpp on 3/1/2018.
 */

public class SignInFragment extends BaseMvpFragment<ISignInView, ISignInPresenter<ISignInView>> implements ISignInView {
    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @BindView(R.id.etEmail)
    ExtEditText etEmail;
    @BindView(R.id.etPassword)
    ExtEditText etPassword;

    @Override
    public ISignInPresenter<ISignInView> createPresenter() {
        return new SignInPresenter<>(getAppComponent());
    }

    @Override
    public int getResourceId() {
        return R.layout.sign_in_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public void showLoading(boolean isLoading) {
        showProgress(isLoading);
    }

    @OnClick(R.id.tvSignIn)
    void onClickSignIn() {
        Log.e("Sign", "onClickSignIn");
        ActivityUtils.startActivity(MainActivity.class);
    }

    @OnClick(R.id.tvForgotPassword)
    void onClickForgotPassword() {
        AloneFragmentActivity.with(this).start(ForgotPasswordFragment.class);
    }

    @OnClick(R.id.ivFacebook)
    void onClickFacebook() {
        Log.e("Sign", "onClickFacebook");
    }

    @OnClick(R.id.ivGoogle)
    void onClickGoogle() {
    }

    @OnClick(R.id.ivTwitter)
    void onClickTwitter() {
    }
}
