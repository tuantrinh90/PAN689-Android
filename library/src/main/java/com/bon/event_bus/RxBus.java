package com.bon.event_bus;

import android.os.Handler;
import android.os.Looper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

/**
 * Created by dangpp on 2/9/2018.
 */

public class RxBus<T extends IEvent> {
  //  private Subject<T> mPublishSubject = (Subject<T>) PublishSubject.create();
  //  private final Handler handler = new Handler(Looper.getMainLooper());

    public RxBus() {
    }

//    public <A extends IEvent> Observable<A> subscribe(Class<A> cls) {
//        return mPublishSubject
//                .filter(o -> o.getClass().equals(cls))
//                .map(o -> (A) o).observeOn(AndroidSchedulers.mainThread());
//    }

    /**
     * Send event to broadcast.
     *
     * @param event event to be caught.
     */
//    public void send(T event) {
//        try {
//            handler.post(() -> mPublishSubject.onNext(event));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
