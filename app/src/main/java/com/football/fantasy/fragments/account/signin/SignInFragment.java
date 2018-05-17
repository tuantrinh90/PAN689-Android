package com.football.fantasy.fragments.account.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bon.util.ActivityUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
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
    EditTextApp etEmail;
    @BindView(R.id.etPassword)
    EditTextApp etPassword;

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
        initView();
    }

    void initView(){

    }

    @Override
    public void showLoading(boolean isLoading) {
        showProgress(isLoading);
    }

    @Override
    public void goToMain() {
        ActivityUtils.startActivity(MainActivity.class);
        getActivity().finish();
    }

    @OnClick(R.id.tvSignIn)
    void onClickSignIn() {
        Log.e("Sign", "onClickSignIn");
        goToMain();
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
