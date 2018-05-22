package com.football.fantasy.fragments.account.signup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

        // agreed
        cbAgreed.setMovementMethod(TextViewLinkMovementMethod.newInstance(mActivity, null));
        TextViewLinkMovementMethod.stripUnderlines(cbAgreed, ContextCompat.getColor(mActivity, R.color.color_blue));
        TextViewLinkMovementMethod.changeColorTextSelector(cbAgreed, ContextCompat.getColor(mActivity, R.color.color_gray_selector));
    }

    @OnClick(R.id.tvRegister)
    void onClickSignUp() {

    }
}
