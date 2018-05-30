package com.football.fantasy.fragments.account.signup;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.bon.customview.checkbox.ExtCheckBox;
import com.bon.textstyle.TextViewLinkMovementMethod;
import com.bon.util.ActivityUtils;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.R;
import com.football.fantasy.activities.MainActivity;
import com.football.models.requests.SignupRequest;

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
        if (isValid()) {
            SignupRequest request = new SignupRequest(etFirstName.getContent(), etLastName.getContent(), etEmail.getContent(), etPassword.getContent(), etConfirmPassword.getContent());
            presenter.register(request);
        }
    }

    private boolean isValid() {
        boolean valid = !etFirstName.isEmpty(getContext()) && !etLastName.isEmpty(getContext())
                && !etPassword.isEmpty(getContext()) && !etConfirmPassword.isEmpty(getContext());
        if (valid) {
            if (!etEmail.isValidEmail(getContext())) {
                return false;

            } else if (etPassword.getContent().length() < 6) {
                Toast.makeText(mActivity, R.string.password_description, Toast.LENGTH_SHORT).show();
                return false;

            } else if (!etPassword.getContent().equals(etConfirmPassword.getContent())) {
                Toast.makeText(mActivity, "Not match", Toast.LENGTH_SHORT).show();
                return false;

            } else if (!cbAgreed.isChecked()) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void goToMain() {
        ActivityUtils.startActivity(MainActivity.class);
        getActivity().finish();
    }
}
