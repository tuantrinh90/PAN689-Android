package com.football.fantasy.fragments.more.settings;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ISettingsPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getSettings();

    void changeSettings(String param, boolean checked);

    void changeLanguage(String languageCode);

}
