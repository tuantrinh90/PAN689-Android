package com.football.fantasy.fragments.account.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;

import com.bon.customview.checkbox.ExtCheckBox;
import com.bon.customview.textview.ExtTextView;
import com.bon.logger.Logger;
import com.bon.textstyle.TextViewLinkMovementMethod;
import com.bon.util.ActivityUtils;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.R;
import com.football.fantasy.activities.MainActivity;
import com.football.fantasy.fragments.web_view.WebViewFragment;
import com.football.models.requests.SignupRequest;
import com.football.utilities.Constant;
import com.football.utilities.ServiceConfig;

import butterknife.BindView;
import butterknife.OnClick;

public class SignUpFragment extends BaseMvpFragment<ISignUpView, ISignUpPresenter<ISignUpView>> implements ISignUpView {
    static final String TAG = SignUpFragment.class.getSimpleName();

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @BindView(R.id.etFirstName)
    EditTextApp etFirstName;
    @BindView(R.id.etLastName)
    EditTextApp etLastName;
    @BindView(R.id.etEmail)
    EditTextApp etEmail;
    @BindView(R.id.etPassword)
    EditTextApp etPassword;
    @BindView(R.id.etConfirmPassword)
    EditTextApp etConfirmPassword;
    @BindView(R.id.tvErrorAgreed)
    ExtTextView tvErrorAgreed;
    @BindView(R.id.cbAgreed)
    ExtCheckBox cbAgreed;
    @BindView(R.id.tvAgreed)
    ExtTextView tvAgreed;

    @Override
    public int getResourceId() {
        return R.layout.sign_up_fragment;
    }

    @NonNull
    @Override
    public ISignUpPresenter<ISignUpView> createPresenter() {
        return new SignUpDataPresenter<>(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();

        // agreed
        tvAgreed.setMovementMethod(TextViewLinkMovementMethod.newInstance(mActivity, s -> {
            WebViewFragment.start(this, getString(R.string.register), s);
        }));

        TextViewLinkMovementMethod.stripUnderlines(tvAgreed, ContextCompat.getColor(mActivity, R.color.color_blue));
        TextViewLinkMovementMethod.changeColorTextSelector(tvAgreed, ContextCompat.getColor(mActivity, R.color.color_gray_selector));
    }

    void initView() {
        tvAgreed.setText(Html.fromHtml(getString(R.string.register_condition, ServiceConfig.TERMS, ServiceConfig.PRIVACY_POLICY)));
    }

    @OnClick(R.id.tvRegister)
    void onClickSignUp() {
        if (!isValid()) return;

        SignupRequest request = new SignupRequest(etFirstName.getContent(), etLastName.getContent(),
                etEmail.getContent(), etPassword.getContent(), etConfirmPassword.getContent());
        presenter.register(request);
    }

    private boolean isValid() {
        boolean valid = true;

        if (etFirstName.isEmpty(mActivity)) valid = false;
        if (etLastName.isEmpty(mActivity)) valid = false;
        if (!etEmail.isValidEmail(mActivity)) valid = false;
        if (etPassword.isEmpty(mActivity)) {
            valid = false;
        } else {
            if (etPassword.getContent().length() < Constant.MIN_PASSWORD) {
                etPassword.setError(getString(R.string.min_password));
                valid = false;
            }
        }

        if (etConfirmPassword.isEmpty(mActivity)) {
            valid = false;
        } else {
            if (!etPassword.getContent().equals(etConfirmPassword.getContent())) {
                etConfirmPassword.setError(getString(R.string.confirm_not_match));
                valid = false;
            }
        }

        tvErrorAgreed.setVisibility(View.GONE);
        if (!cbAgreed.isChecked()) {
            tvErrorAgreed.setVisibility(View.VISIBLE);
            valid = false;
        }

        return valid;
    }

    @Override
    public void onRegisterSuccess() {
        try {
            etFirstName.setContent("");
            etLastName.setContent("");
            etEmail.setContent("");
            etPassword.setContent("");
            etConfirmPassword.setContent("");
            cbAgreed.setChecked(false);
//            bus.send(new SignInEvent());
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void goToMain() {
        ActivityUtils.startActivity(MainActivity.class);
        getActivity().finish();
    }
}
