package com.football.utilities;

import android.support.annotation.NonNull;

import com.bon.interfaces.Optional;
import com.football.common.views.IBaseMvpView;
import com.football.listeners.ApiCallback;
import com.football.models.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RxUtilities {
    /**
     * @param observable
     * @param <T>
     */
    public static <T> Disposable async(@NonNull IBaseMvpView mvpView, @NonNull Observable<BaseResponse<T>> observable, ApiCallback<T> apiCallback) {
        Optional.from(apiCallback).doIfPresent(c -> c.onStart());
        return observable.compose(mvpView.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // .doOnError(e -> Optional.from(apiCallback).doIfPresent(c -> c.onError(ErrorHelper.getBaseErrorText(mvpView, ErrorHelper.createErrorBody(e)))))
                .subscribeWith(new DisposableObserver<BaseResponse<T>>() {
                    @Override
                    public void onNext(BaseResponse<T> response) {
                        System.out.println("onNext");
                        Optional.from(response).doIfPresent(r -> {
                            if (r.isSuccess()) {
                                Optional.from(apiCallback).doIfPresent(c -> c.onSuccess(r.getResponse()));
                            } else {
                                Optional.from(apiCallback).doIfPresent(c -> c.onError(ErrorHelper.getBaseErrorText(mvpView, ErrorHelper.getErrorBodyApp(r.getMessage()))));
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Optional.from(apiCallback).doIfPresent(c -> {
                            c.onError(ErrorHelper.getBaseErrorText(mvpView, ErrorHelper.createErrorBody(e)));
                            c.onComplete();
                        });
                    }

                    @Override
                    public void onComplete() {
                        Optional.from(apiCallback).doIfPresent(c -> c.onComplete());
                    }
                });
    }
}
