package com.football.fantasy.fragments.more.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.activities.SplashActivity;
import com.football.models.responses.SettingsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingsFragment extends BaseMvpFragment<ISettingsView, ISettingsPresenter<ISettingsView>> implements ISettingsView {

    private static final String DUTCH = "nl";
    private static final String ENGLISH = "en";
    private static final String FRENCH = "fr";
    private static final String GERMAN = "de";

    @BindView(R.id.switch_notification)
    Switch switchNotification;
    @BindView(R.id.switch_email)
    Switch switchEmail;
    @BindView(R.id.spinner_language)
    Spinner spinnerLanguage;
    @BindView(R.id.text_language)
    ExtTextView textLanguage;

    private boolean inited = false;
    private List<ExtKeyValuePair> languages;

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
        initData();
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

    private void initData() {
        languages = new ArrayList<ExtKeyValuePair>() {{
            add(new ExtKeyValuePair(ENGLISH, getString(R.string.language_english)));
            add(new ExtKeyValuePair(DUTCH, getString(R.string.language_dutch)));
            add(new ExtKeyValuePair(FRENCH, getString(R.string.language_french)));
            add(new ExtKeyValuePair(GERMAN, getString(R.string.language_german)));
        }};
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.color_white)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_blue)));

        textLanguage.setText(R.string.language_english);
        String currentLang = Locale.getDefault().getLanguage();
        for (ExtKeyValuePair language : languages) {
            if (language.getKey().equals(currentLang)) {
                textLanguage.setText(language.getValue());
                break;
            }
        }
    }

    @OnClick({R.id.push_notification, R.id.email_alert, R.id.language})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.push_notification:
                switchNotification.toggle();
                break;

            case R.id.email_alert:
                switchEmail.toggle();
                break;

            case R.id.language:
                ExtKeyValuePairDialogFragment.newInstance()
                        .setExtKeyValuePairs(languages)
                        .setOnSelectedConsumer(extKeyValuePair -> {
                            if (extKeyValuePair != null && !TextUtils.isEmpty(extKeyValuePair.getKey())) {
                                presenter.changeLanguage(extKeyValuePair.getKey());
                                textLanguage.setText(extKeyValuePair.getValue());
                            }

                        }).show(getChildFragmentManager(), null);
                break;
        }
    }

    public void setLocale(String lang) {
        Resources resources = getResources();
        Locale myLocale = new Locale(lang);
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.locale = myLocale;
        resources.updateConfiguration(conf, dm);
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

    @Override
    public void changeLanguageSuccess(String languageCode) {
        setLocale(languageCode);

        // restart app
        Intent intent = new Intent(getAppContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
