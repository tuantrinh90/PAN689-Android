package com.bon.event_bus;

import android.os.Handler;
import android.os.Looper;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by dangpp on 2/9/2018.
 */

public class RxBus<T extends IEvent> {
    private PublishSubject<T> mPublishSubject = PublishSubject.create();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public RxBus() {
    }

    /**
     * @param clazz
     * @param <A>
     * @return
     */
    public <A extends IEvent> Observable<A> ofType(Class<A> clazz) {
        return mPublishSubject.ofType(clazz);
    }

    /**
     * Send event to broadcast.
     *
     * @param event event to be caught.
     */
    public void send(T event) {
        try {
            handler.post(() -> mPublishSubject.onNext(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
