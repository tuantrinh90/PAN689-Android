package com.football.common.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.MenuItem;

import com.bon.activity.ExtBaseActivity;
import com.bon.eventbus.IEvent;
import com.bon.eventbus.RxBus;
import com.bon.util.KeyboardUtils;
import com.football.application.AppContext;
import com.football.common.actions.IToolbarAction;
import com.football.fantasy.BuildConfig;
import com.football.interactors.IDataModule;
import com.football.interactors.database.IDbModule;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

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

    // rx java
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inject
        getAppContext().getComponent().inject(this);

        // strict mode
        // StrictMode helps us detect  sensitive activities, such  as disk  accesses or
        // network  calls that  we are accidentally  performing  on  the main  thread.
        // This configuration  will  report  every  violation  about  them ain  thread usage and every
        // violation  concerning  possible memory  leaks:  Activities, BroadcastReceivers, Sqlite objects, and more.
        // Choosing  penaltyLog(), StrictMode will  print  a message on  log cat  when  a        violation  occurs
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

    @Override
    public void setToolbarTitle(@StringRes int titleId) {
        if (getSupportActionBar() != null) {
            if (titleId == 0) {
                setToolbarTitle("");
            } else {
                setToolbarTitle(getResources().getString(titleId));
            }
        }
    }

    @Override
    public void setToolbarTitle(@NonNull String titleId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // hide keyboard
        KeyboardUtils.hideSoftKeyboard(this);

        // un-subscribe rx java
        if (!mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }
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
