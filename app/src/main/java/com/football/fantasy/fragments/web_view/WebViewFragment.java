package com.football.fantasy.fragments.web_view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.webkit.WebView;

import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;

import butterknife.BindView;

public class WebViewFragment extends BaseMvpFragment<IWebViewView, IWebViewPresenter<IWebViewView>> implements IWebViewView {

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_URL = "URL";

    @BindView(R.id.web_view)
    WebView webView;

    private String title;
    private String url;

    public static void start(Fragment fragment, String title, String url) {
        AloneFragmentActivity.with(fragment)
                .parameters(newBundle(title, url))
                .start(WebViewFragment.class);
    }

    public static Bundle newBundle(String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_URL, url);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.web_view_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
    }

    private void getDataFromBundle() {
        title = getArguments().getString(KEY_TITLE);
        url = getArguments().getString(KEY_URL);
    }

    @NonNull
    @Override
    public IWebViewPresenter<IWebViewView> createPresenter() {
        return new WebViewDataPresenter(getAppComponent());
    }

    @Override
    public String getTitleString() {
        return title;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    private void initView() {
        webView.loadUrl(url);
    }
}
