package com.football.fantasy.fragments.web_view;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class WebViewDataPresenter extends BaseDataPresenter<IWebViewView> implements IWebViewPresenter<IWebViewView> {
    /**
     * @param appComponent
     */
    protected WebViewDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
