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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.BehaviorSubject;
import java8.util.function.Consumer;

/**
 * Created by dangpp on 2/21/2018.
 */
public abstract class BaseMvpFragment<V extends IBaseMvpView, P extends IBaseDataPresenter<V>>
        extends MvpFragment<V, P> implements IBaseFragment, IResourceFragment, IBaseMvpView {

    private static final int ATTACKED_BUT_NOT_CREATED = 0;
    private static final int VIEW_CREATED = 1;
    private static final int VIEW_INITIALIZED = 2;

    private boolean isAttack;
    private int fragmentState = -1;

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

    // rxjava
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    // lifecycle subject
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

    // is child fragment
    boolean isChildFragment = false;

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
        isAttack = true;

        // activity
        if (activity instanceof BaseAppCompatActivity) {
            this.mActivity = (BaseAppCompatActivity) activity;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isAttack) {
                fragmentState = ATTACKED_BUT_NOT_CREATED;
            } else if (fragmentState == VIEW_CREATED) {
                initialized();
            }
        }
    }

    protected void initialized() {
        fragmentState = VIEW_INITIALIZED;
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

        bindButterKnife(view);
        if (fragmentState == ATTACKED_BUT_NOT_CREATED) {
            initialized();
        } else {
            fragmentState = VIEW_CREATED;
        }

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
        isAttack = false;
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

        // rxjava
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }

        mActivity = null;
        super.onDestroyView();
    }

    @Override
    public void bindButterKnife(View view) {
        Optional.from(view).doIfPresent(v -> unbinder = ButterKnife.bind(this, v));
    }

    @Override
    public void showLoading(boolean show) {
        if (mActivity != null) {
            if (show) {
                mActivity.showProgressDialog();
            } else {
                mActivity.hideProgressDialog();
            }
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
    public void showMessage(String message) {
        if (StringUtils.isEmpty(message)) return;
        showMessage(message, R.string.ok, null);
    }

    @Override
    public void showMessage(String message, int ok, Consumer<Void> consumer) {
        mActivity.showMessage(message, ok, consumer);
    }

    @Override
    public void showMessage(String message, int ok, int cancel, Consumer<Void> okConsumer, Consumer<Void> cancelConsumer) {
        mActivity.showMessage(message, ok, cancel, okConsumer, cancelConsumer);
    }

    @Override
    public void showMessage(int message, int ok, Consumer<Void> consumer) {
        showMessage(getString(message), ok, consumer);
    }

    @Override
    public void showMessage(int message, int ok, int cancel, Consumer<Void> okConsumer, Consumer<Void> cancelConsumer) {
        showMessage(getString(message), ok, cancel, okConsumer, cancelConsumer);
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {

    }

    /**
     * @param isChildFragment
     * @return
     */
    public BaseMvpFragment<V, P> setChildFragment(boolean isChildFragment) {
        this.isChildFragment = isChildFragment;
        return this;
    }

    protected <A extends IEvent> void onEvent(Class<A> clazz, Consumer<A> callback) {
        try {
            mCompositeDisposable.add(bus.ofType(clazz).subscribeWith(new DisposableObserver<A>() {
                @Override
                public void onNext(A event) {
                    try {
                        callback.accept(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
