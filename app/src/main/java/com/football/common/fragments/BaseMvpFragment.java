package com.football.common.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.interfaces.Optional;
import com.bon.util.KeyboardUtils;
import com.football.application.AppContext;
import com.football.common.activities.BaseAppCompatActivity;
import com.football.di.AppComponent;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dangpp on 2/21/2018.
 */

public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>>
        extends MvpFragment<V, P> implements IBaseFragment, IResourceFragment {
    private static final String TAG = BaseMvpFragment.class.getSimpleName();

    // base activity
    protected BaseAppCompatActivity mActivity;

    // rx java
    protected CompositeSubscription mSubscriptions = new CompositeSubscription();

    // butter knife
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
        unbinder = ButterKnife.bind(view);

        // update title
        mActivity.setToolbarTitle(getTitleId());

        // update toolbar
        Optional.from(mActivity.getSupportActionBar())
                .doIfPresent(actionBar -> initToolbar(actionBar));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // hide loading, keyboard
        showProgress(false);
        KeyboardUtils.hideSoftKeyboard(mActivity);

        // un-subscribe rx java
        if (!mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }

        // unbind view
        if (unbinder != null) {
            unbinder.unbind();
        }
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
    public AppContext getAppContext() {
        return mActivity.getAppContext();
    }

    @Override
    public AppComponent getAppComponent() {
        return mActivity.getAppContext().getComponent();
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        supportActionBar.setDisplayHomeAsUpEnabled(false);
        supportActionBar.setHomeAsUpIndicator(0);
        supportActionBar.setIcon(0);
    }
}
