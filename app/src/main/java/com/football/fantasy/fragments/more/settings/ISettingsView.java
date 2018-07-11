package com.football.fantasy.fragments.more.settings;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.SettingsResponse;

public interface ISettingsView extends IBaseMvpView {
    void displaySettings(SettingsResponse response);

}
