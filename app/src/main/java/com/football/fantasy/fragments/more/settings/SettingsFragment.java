package com.football.fantasy.fragments.more.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bon.interfaces.Optional;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.SettingsResponse;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingsFragment extends BaseMvpFragment<ISettingsView, ISettingsPresenter<ISettingsView>> implements ISettingsView {

    @BindView(R.id.switch_notification)
    Switch switchNotification;
    @BindView(R.id.switch_email)
    Switch switchEmail;

    private boolean inited = false;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    public static void start(Fragment fragment) {
        AloneFragmentActivity
                .with(fragment)
                .start(SettingsFragment.class);
    }

    @Override
    public int getResourceId() {
        return R.layout.settings_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();

        presenter.getSettings();
    }

    @NonNull
    @Override
    public ISettingsPresenter<ISettingsView> createPresenter() {
        return new SettingsDataPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.information;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.color_white)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_blue)));
    }

    @OnClick({R.id.push_notification, R.id.email_alert})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.push_notification:
                switchNotification.toggle();
                break;
            case R.id.email_alert:
                switchEmail.toggle();
                break;
        }
    }

    @OnCheckedChanged({R.id.switch_email, R.id.switch_notification})
    public void onCheckedChanged(CompoundButton button, boolean checked) {
        if (!inited) return;

        switch (button.getId()) {
            case R.id.switch_email:
                presenter.changeSettings("allow_email_alert", checked);
                break;

            case R.id.switch_notification:
                presenter.changeSettings("allow_notification", checked);
                break;
        }
    }

    @Override
    public void displaySettings(SettingsResponse response) {
        switchEmail.setChecked(response.getAllowEmailAlert());
        switchNotification.setChecked(response.getAllowNotification());

        inited = true;
    }
}
