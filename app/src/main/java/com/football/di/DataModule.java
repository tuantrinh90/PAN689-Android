package com.football.di;

import android.os.Handler;

import com.bon.event_bus.IEvent;
import com.bon.event_bus.RxBus;
import com.football.interactors.IDataModule;
import com.football.interactors.database.IDbModule;
import com.football.interactors.service.IApiService;

import javax.inject.Inject;

import dagger.Module;

/**
 * Created by dangpp on 2/9/2018.
 */
@Module
public class DataModule implements IDataModule {
    private static final String TAG = DataModule.class.getSimpleName();

    private final AppComponent component;
    private final Handler handler = new Handler();

    @Inject
    RxBus<IEvent> bus;

    @Inject
    IApiService apiService;

    @Inject
    IDbModule dbModule;

    public DataModule(AppComponent component) {
        this.component = component;
        this.component.inject(this);
    }

    @Override
    public Handler getHandler() {
        return handler;
    }
}
