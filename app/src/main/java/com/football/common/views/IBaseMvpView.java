package com.football.common.views;

import android.support.annotation.IdRes;

import com.bon.event_bus.IEvent;
import com.bon.event_bus.RxBus;
import com.football.common.activities.BaseAppCompatActivity;
import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import java8.util.function.Consumer;

public interface IBaseMvpView extends MvpView, LifecycleProvider<FragmentEvent> {
    void showLoading(boolean isLoading);

    RxBus<IEvent> getRxBus();

    BaseAppCompatActivity getAppActivity();

    void showMessage(String message, @IdRes int ok, Consumer<Void> consumer);

    void showMessage(String message, @IdRes int ok, @IdRes int cancel, Consumer<Void> okConsumer, Consumer<Void> cancelConsumer);

    <T> void notifyDataSetChanged(List<T> its);

    void showLoadingPagingListView(boolean isLoading);
}
