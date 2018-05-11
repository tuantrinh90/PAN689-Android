package com.football.common.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.eventbus.IEvent;
import com.bon.eventbus.RxBus;
import com.bon.interfaces.Optional;
import com.bon.util.KeyboardUtils;
import com.bon.util.StringUtils;
import com.football.application.AppContext;
import com.football.common.activities.BaseAppCompatActivity;
import com.football.di.AppComponent;
import com.football.interactors.IDataModule;
import com.football.interactors.database.IDbModule;
import com.football.interactors.service.IApiService;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dangpp on 2/21/2018.
 */
public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpFragment<V, P> implements IBaseFragment, IResourceFragment {
    private static final String TAG = BaseMvpFragment.class.getSimpleName();

    protected BaseAppCompatActivity mActivity;

    @Inject
    protected RxBus<IEvent> bus;

    @Inject
    protected IDataModule dataModule;

    @Inject
    protected IDbModule dbModule;

    @Inject
    protected IApiService apiService;

    // unbind butter knife
    private Unbinder unbinder;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseAppCompatActivity) {
            this.mActivity = (BaseAppCompatActivity) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inject component
        getAppContext().getComponent().inject((BaseMvpFragment<MvpView, MvpPresenter<MvpView>>) this);

        // retain this fragment when activity is re-initialized
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getResourceId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // update toolbar
        Optional.from(mActivity.getAppSupportActionBar())
                .doIfPresent(actionBar -> initToolbar(actionBar));

        // update title
        if (StringUtils.isEmpty(getTitleString())) {
            mActivity.setToolbarTitle(getTitleId());
        } else {
            mActivity.setToolbarTitle(getTitleString());
        }
    }

    @Override
    public void bindButterKnife(View view) {
        Optional.from(view).doIfPresent(v -> unbinder = ButterKnife.bind(this, v));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // hide loading, keyboard
        showProgress(false);
        KeyboardUtils.hideSoftKeyboard(mActivity);

        // unbind butter knife
        Optional.from(unbinder).doIfPresent(u -> u.unbind());
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            mActivity.showProgressDialog();
        } else {
            mActivity.hideProgressDialog();
        }
    }

    @Override
    public int getTitleId() {
        return 0;
    }

    @Override
    public String getTitleString() {
        return "";
    }

    @Override
    public AppContext getAppContext() {
        return mActivity.getAppContext();
    }

    @Override
    public AppComponent getAppComponent() {
        return mActivity.getAppContext().getComponent();
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayHomeAsUpEnabled(false);
        supportActionBar.setHomeAsUpIndicator(0);
        supportActionBar.setIcon(0);
        supportActionBar.show();
    }
}
