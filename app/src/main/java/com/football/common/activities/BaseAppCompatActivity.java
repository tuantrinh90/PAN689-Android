package com.football.common.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bon.customview.textview.ExtTextView;
import com.bon.event_bus.IEvent;
import com.bon.event_bus.RxBus;
import com.bon.interfaces.Optional;
import com.bon.util.KeyboardUtils;
import com.football.application.AppContext;
import com.football.common.actions.IToolbarAction;
import com.football.fantasy.R;
import com.football.interactors.IDataModule;
import com.football.interactors.database.IDbModule;
import com.football.interactors.service.IApiService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dangpp on 2/21/2018.
 */

public abstract class BaseAppCompatActivity extends ExtBaseActivity implements IToolbarAction {
    private static final String TAG = BaseAppCompatActivity.class.getSimpleName();

    @Inject
    protected RxBus<IEvent> bus;

    @Inject
    protected IDataModule dataModule;

    @Inject
    protected IDbModule dbModule;

    @Inject
    protected IApiService apiService;

    // get view id
    protected abstract int getContentViewId();

    // butter knife
    Unbinder unbinder;

    // title app
    ExtTextView tvTitleToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inject component
        getAppContext().getComponent().inject(this);

        // set content view
        if (getContentViewId() > 0) {
            setContentView(getContentViewId());
            unbinder = ButterKnife.bind(this);

            // get title toolbar
            Optional.from(getToolBar()).doIfPresent(toolbar -> tvTitleToolBar = toolbar.findViewById(R.id.toolbar_title));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // hide key board
        KeyboardUtils.hideSoftKeyboard(this);

        // unbind butter knife
        Optional.from(unbinder).doIfPresent(Unbinder::unbind);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case android.R.id.home:
                result = onHomeClicked(item);
                break;
        }

        return result || super.onOptionsItemSelected(item);
    }

    /**
     * get support action bar
     *
     * @return
     */
    public abstract ActionBar getAppSupportActionBar();

    public abstract Toolbar getToolBar();

    @Override
    public void setToolbarTitle(@StringRes int titleId) {
        Optional.from(getAppSupportActionBar()).doIfPresent(app -> {
            if (titleId == 0) {
                setToolbarTitle("");
            } else {
                setToolbarTitle(getResources().getString(titleId));
            }
        });
    }

    @Override
    public void setToolbarTitle(@NonNull String titleId) {
        // Optional.from(getAppSupportActionBar()).doIfPresent(app -> app.setTitle(titleId));
        Optional.from(tvTitleToolBar).doIfPresent(tv -> tv.setText(titleId));
    }

    @Override
    public void initFragmentDefault() {

    }

    /**
     * get application context
     *
     * @return
     */
    public AppContext getAppContext() {
        return (AppContext) getApplicationContext();
    }

    /**
     * Override this method to return just {@code false} within Activity, if you need to override
     * home click in fragment.
     *
     * @param item The menu item that was selected.
     * @return Return {@code false} to allow normal menu processing to proceed, {@code true} to consume it here.
     */
    protected boolean onHomeClicked(MenuItem item) {
        onBackPressed();
        return true;
    }
}
