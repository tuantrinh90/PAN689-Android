package com.football.common.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.bon.event_bus.IEvent;
import com.bon.event_bus.RxBus;
import com.bon.interfaces.Optional;
import com.football.common.views.IBaseMvpView;
import com.football.di.AppComponent;
import com.football.interactors.IDataModule;
import com.football.interactors.database.IDbModule;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by dangpp on 2/21/2018.
 */
public abstract class BaseDataPresenter<V extends IBaseMvpView> extends MvpBasePresenter<V>
        implements IBaseDataPresenter<V> {

    @Inject
    protected IDataModule dataModule;

    @Inject
    protected IDbModule dbModule;

    @Inject
    protected RxBus<IEvent> bus;

    // composite disposable
    protected CompositeDisposable mCompositeDisposable;

    /**
     * @param appComponent
     */
    public BaseDataPresenter(AppComponent appComponent) {
        // leave casting to match generic type for Dagger2
        appComponent.inject((BaseDataPresenter<IBaseMvpView>) this);
    }

    @Override
    public void attachView(@NonNull V view) {
        super.attachView(view);
        if (mCompositeDisposable == null)
            mCompositeDisposable = new CompositeDisposable();
    }

    /**
     * Wrap view getter to optional value.
     *
     * @return {@link Optional} of view.
     */
    @NonNull
    protected Optional<V> getOptView() {
        return new Optional<>(getView());
    }

    @Override
    public void processArguments(Bundle arguments) {

    }

    @Override
    public void saveInstanceState(Bundle bundle) {

    }

    @Override
    public void restoreInstanceState(Bundle bundle) {

    }

    @Override
    public void unbindEvent() {
//        if (!mCompositeDisposable.isDisposed()) {
//        }
        mCompositeDisposable.clear();
    }
}
