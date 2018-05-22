package com.football.common.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.event_bus.IEvent;
import com.bon.event_bus.RxBus;
import com.bon.interfaces.Optional;
import com.bon.util.KeyboardUtils;
import com.bon.util.StringUtils;
import com.football.application.AppContext;
import com.football.common.activities.BaseAppCompatActivity;
import com.football.common.presenters.BaseDataPresenter;
import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.di.AppComponent;
import com.football.interactors.IDataModule;
import com.football.interactors.database.IDbModule;
import com.football.interactors.service.IApiService;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dangpp on 2/21/2018.
 */
public abstract class BaseMvpFragment<V extends IBaseMvpView, P extends IBaseDataPresenter<V>>
        extends MvpFragment<V, P> implements IBaseFragment, IResourceFragment, IBaseMvpView {
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

    // is child fragment
    boolean isChildFragment = false;

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
        getAppContext().getComponent().inject((BaseMvpFragment<IBaseMvpView, BaseDataPresenter<IBaseMvpView>>) this);

        // retain this fragment when activity is re-initialized
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getResourceId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // hide keyboard
        KeyboardUtils.hideKeyboard(mActivity, view);

        // only update title with isChildFragment = false
        if (isChildFragment) return;

        // update toolbar
        Optional.from(mActivity.getAppSupportActionBar()).doIfPresent(this::initToolbar);

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

        // hide loading
        showLoading(false);

        // hide keyboard
        KeyboardUtils.hideSoftKeyboard(mActivity);

        // unbind butter knife
        Optional.from(unbinder).doIfPresent(Unbinder::unbind);

        // unbind event
        Optional.from(presenter).doIfPresent(IBaseDataPresenter::unbindEvent);
    }

    @Override
    public void showLoading(boolean show) {
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
        supportActionBar.setElevation(0);
        supportActionBar.setDisplayShowTitleEnabled(false);
        supportActionBar.setDisplayHomeAsUpEnabled(false);
        supportActionBar.setHomeAsUpIndicator(0);
        supportActionBar.setIcon(0);
        supportActionBar.show();
    }

    /**
     * @param isChildFragment
     * @return
     */
    public BaseMvpFragment<V, P> setChildFragment(boolean isChildFragment) {
        this.isChildFragment = isChildFragment;
        return this;
    }
}
