package com.football.common.presenters;

import android.os.Bundle;

import com.football.common.views.IBaseMvpView;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

/**
 * Created by dangpp on 2/21/2018.
 */
public interface IBaseDataPresenter<V extends IBaseMvpView> extends MvpPresenter<V> {
    void processArguments(Bundle arguments);

    void saveInstanceState(Bundle bundle);

    void restoreInstanceState(Bundle bundle);

    void unbindEvent();
}

