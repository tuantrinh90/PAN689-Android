package com.football.common.views;

import com.bon.event_bus.IEvent;
import com.bon.event_bus.RxBus;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

public interface IBaseMvpView extends MvpView, LifecycleProvider<FragmentEvent> {
    void showLoading(boolean isLoading);

    void showMessage(String message);

    RxBus<IEvent> getRxBus();
}
