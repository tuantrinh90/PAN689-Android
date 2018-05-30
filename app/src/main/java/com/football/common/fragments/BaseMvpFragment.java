package com.football.common.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.event_bus.IEvent;
import com.bon.event_bus.RxBus;
import com.bon.interfaces.Optional;
import com.bon.util.DialogUtils;
import com.bon.util.KeyboardUtils;
import com.bon.util.StringUtils;
import com.football.application.AppContext;
import com.football.common.activities.BaseAppCompatActivity;
import com.football.common.presenters.BaseDataPresenter;
import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.interactors.IDataModule;
import com.football.interactors.database.IDbModule;
import com.football.interactors.service.IApiService;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import java8.util.function.Consumer;

/**
 * Created by dangpp on 2/21/2018.
 */
public abstract class BaseMvpFragment<V extends IBaseMvpView, P extends IBaseDataPresenter<V>>
        extends MvpFragment<V, P> implements IBaseFragment, IResourceFragment, IBaseMvpView {

    // base activity
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

    // lifecycle subject
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.ATTACH);

        // activity
        if (activity instanceof BaseAppCompatActivity) {
            this.mActivity = (BaseAppCompatActivity) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.CREATE);

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

        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);

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
    @CallSuper
    public void onStart() {
        super.onStart();
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.RESUME);
    }

    @Override
    @CallSuper
    public void onPause() {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onDetach() {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        // lifecycle
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);

        // hide loading
        showLoading(false);

        // hide keyboard
        KeyboardUtils.hideSoftKeyboard(mActivity);

        // unbind butter knife
        Optional.from(unbinder).doIfPresent(Unbinder::unbind);

        // unbind event
        Optional.from(presenter).doIfPresent(IBaseDataPresenter::unbindEvent);

        super.onDestroyView();
    }

    @Override
    public void bindButterKnife(View view) {
        Optional.from(view).doIfPresent(v -> unbinder = ButterKnife.bind(this, v));
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
    public BaseAppCompatActivity getAppActivity() {
        return mActivity;
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

    @Override
    public RxBus<IEvent> getRxBus() {
        return bus;
    }

    @Override
    public void showMessage(String message, int ok, Consumer<Void> consumer) {
        DialogUtils.messageBox(mActivity, getString(R.string.app_name), message, getString(ok),
                (dialog, which) -> Optional.from(consumer).doIfPresent(c -> c.accept(null)));
    }

    @Override
    public void showMessage(String message, int ok, int cancel, Consumer<Void> okConsumer, Consumer<Void> cancelConsumer) {
        DialogUtils.messageBox(mActivity, 0, getString(R.string.app_name), message, getString(ok), getString(cancel),
                (dialog, which) -> Optional.from(okConsumer).doIfPresent(c -> c.accept(null)),
                (dialog, which) -> Optional.from(cancelConsumer).doIfPresent(c -> c.accept(null)));
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
