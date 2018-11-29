package com.football.fantasy.fragments.more.settings;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.SettingsResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SettingsDataPresenter extends BaseDataPresenter<ISettingsView> implements ISettingsPresenter<ISettingsView> {
    /**
     * @param appComponent
     */
    protected SettingsDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getSettings() {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getSettings(),
                    new ApiCallback<SettingsResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(SettingsResponse response) {
                            v.displaySettings(response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void changeSettings(String param, boolean checked) {
        getOptView().doIfPresent(v -> {
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(param, String.valueOf(checked ? 1 : 0))
                    .build();

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().changeSettings(body),
                    new ApiCallback<SettingsResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(SettingsResponse response) {
//                            v.displaySettings(response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void changeLanguage(String languageCode) {
        getOptView().doIfPresent(v -> {
            AppPreferences.getInstance(v.getAppActivity()).putString(Constant.KEY_LANGUAGE, languageCode);
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("locale", languageCode)
                    .build();
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().updateLanguages(body),
                    new ApiCallback<Object>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(Object response) {
                            v.changeLanguageSuccess(languageCode);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    private void setLocale(Context context, String lang) {
        Resources resources = context.getResources();
        Locale myLocale = new Locale(lang);
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.locale = myLocale;
        resources.updateConfiguration(conf, dm);
    }
}
