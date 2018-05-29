package com.football.fantasy.fragments.account.signup;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;

import com.bon.customview.checkbox.ExtCheckBox;
import com.bon.textstyle.TextViewLinkMovementMethod;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.R;

import butterknife.BindView;
import butterknife.OnClick;

public class SignUpFragment extends BaseMvpFragment<ISignUpView, ISignUpPresenter<ISignUpView>> implements ISignUpView {
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
    @BindView(R.id.cbAgreed)
    ExtCheckBox cbAgreed;

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
        cbAgreed.setMovementMethod(TextViewLinkMovementMethod.newInstance(mActivity, s -> {
            String url = "https://www.google.com.vn/";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(mActivity, Uri.parse(url));
        }));
        TextViewLinkMovementMethod.stripUnderlines(cbAgreed, ContextCompat.getColor(mActivity, R.color.color_blue));
        TextViewLinkMovementMethod.changeColorTextSelector(cbAgreed, ContextCompat.getColor(mActivity, R.color.color_gray_selector));
    }

    void initView() {
        cbAgreed.setText(Html.fromHtml(getString(R.string.register_condition)));
    }

    @OnClick(R.id.tvRegister)
    void onClickSignUp() {

    }
}