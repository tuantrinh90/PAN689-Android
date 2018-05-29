package com.football.utilities;

import com.football.models.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtilities {
    /**
     * @param observable
     * @param <T>
     */
    public static <T> Observable<BaseResponse<T>> async(Observable<BaseResponse<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
