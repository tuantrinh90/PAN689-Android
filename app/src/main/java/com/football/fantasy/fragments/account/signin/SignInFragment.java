package com.football.fantasy.fragments.account.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bon.facebook.FacebookUtils;
import com.bon.util.ActivityUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.BuildConfig;
import com.football.fantasy.R;
import com.football.fantasy.activities.MainActivity;
import com.football.fantasy.fragments.account.forgot.ForgotPasswordFragment;
import com.football.helpers.sociallogin.facebook.FacebookHelper;
import com.football.helpers.sociallogin.facebook.FacebookListener;
import com.football.helpers.sociallogin.google.GoogleHelper;
import com.football.helpers.sociallogin.google.GoogleListener;
import com.football.helpers.sociallogin.twitter.TwitterHelper;
import com.football.helpers.sociallogin.twitter.TwitterListener;
import com.football.models.requests.LoginRequest;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dangpp on 3/1/2018.
 */
public class SignInFragment extends BaseMvpFragment<ISignInView, ISignInDataPresenter<ISignInView>>
        implements ISignInView, FacebookListener, TwitterListener, GoogleListener {
    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    private static final String TAG = "SignInFragment";

    @BindView(R.id.etEmail)
    EditTextApp etEmail;
    @BindView(R.id.etPassword)
    EditTextApp etPassword;

    private FacebookHelper mFacebook;
    private GoogleHelper mGoogle;
    private TwitterHelper mTwitter;

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
        initSocialLogin();
    }

    void initView() {
        if (BuildConfig.DEBUG) {
            etEmail.setContent("test@gmail.com");
            etPassword.setContent("1");
        }
    }

    private void initSocialLogin() {
        Log.d(TAG, "initSocialLogin: " + FacebookUtils.getHashKey(mActivity));
        mFacebook = new FacebookHelper(this);
        mTwitter = new TwitterHelper(this, mActivity,
                getString(R.string.twitter_api_key),
                getString(R.string.twitter_secret_key));
        mGoogle = new GoogleHelper(this, getActivity(), null);
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
        mFacebook.performSignIn(this);
    }

    @OnClick(R.id.ivGoogle)
    void onClickGoogle() {
        mGoogle.performSignIn(this);
    }

    @OnClick(R.id.ivTwitter)
    void onClickTwitter() {
        mTwitter.performSignIn(mActivity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebook.onActivityResult(requestCode, resultCode, data);
        mTwitter.onActivityResult(requestCode, resultCode, data);
        mGoogle.onActivityResult(requestCode, resultCode, data);
    }

    // facebook login
    @Override
    public void onFbSignInFail(String errorMessage) {
        Log.e(TAG, "onFbSignInFail: ");
    }

    @Override
    public void onFbSignInSuccess(String authToken, String userId) {
        Log.d(TAG, "onFbSignInSuccess: " + authToken);
    }

    @Override
    public void onFBSignOut() {

    }
    // ----------------

    // twitter login
    @Override
    public void onTwitterError(String errorMessage) {
        Log.e(TAG, "onTwitterError: " + errorMessage);
    }

    @Override
    public void onTwitterSignIn(String authToken, String secret, long userId) {
        Log.d(TAG, "onTwitterSignIn: " + authToken);
    }
    // ----------------

    // google login
    @Override
    public void onGoogleAuthSignIn(String authToken, String userId) {
        Log.d(TAG, "onGoogleAuthSignIn: " + authToken);
    }

    @Override
    public void onGoogleAuthSignInFailed(String errorMessage) {
        Log.e(TAG, "onGoogleAuthSignInFailed: " + errorMessage);
    }

    @Override
    public void onGoogleAuthSignOut() {

    }
    // ----------------
}
