package com.football.fantasy.fragments.account.signin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.util.ActivityUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.BuildConfig;
import com.football.fantasy.R;
import com.football.fantasy.activities.MainActivity;
import com.football.fantasy.fragments.account.forgot.ForgotPasswordFragment;
import com.football.models.requests.LoginRequest;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dangpp on 3/1/2018.
 */
public class SignInFragment extends BaseMvpFragment<ISignInView, ISignInDataPresenter<ISignInView>> implements ISignInView {
    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @BindView(R.id.etEmail)
    EditTextApp etEmail;
    @BindView(R.id.etPassword)
    EditTextApp etPassword;

    @NonNull
    @Override
    public ISignInDataPresenter<ISignInView> createPresenter() {
        return new SignInDataPresenter<>(getAppComponent());
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

    void initView() {
        if (BuildConfig.DEBUG) {
            etEmail.setContent("test@gmail.com");
            etPassword.setContent("1");
        }
    }

    @Override
    public boolean isValid() {
        boolean result = true;
        if (!etEmail.isValidEmail(mActivity)) result = false;
        if (etPassword.isEmpty(mActivity)) result = false;
        return result;
    }

    @Override
    public void goToMain() {
        ActivityUtils.startActivity(MainActivity.class);
        getActivity().finish();
    }

    @Override
    public LoginRequest getLoginRequest() {
        return new LoginRequest(etEmail.getContent(), etPassword.getContent(), "");
    }

    @OnClick(R.id.tvSignIn)
    void onClickSignIn() {
        presenter.onSignIn();
    }

    @OnClick(R.id.tvForgotPassword)
    void onClickForgotPassword() {
        AloneFragmentActivity.with(this).start(ForgotPasswordFragment.class);
    }

    @OnClick(R.id.ivFacebook)
    void onClickFacebook() {

    }

    @OnClick(R.id.ivGoogle)
    void onClickGoogle() {
    }

    @OnClick(R.id.ivTwitter)
    void onClickTwitter() {
    }
}
