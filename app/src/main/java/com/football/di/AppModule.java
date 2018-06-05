package com.football.di;

import android.content.Context;

import com.bon.event_bus.IEvent;
import com.bon.event_bus.RxBus;
import com.football.application.AppContext;
import com.football.interactors.IDataModule;
import com.football.interactors.database.IDbModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dangpp on 2/9/2018.
 */

@Module
public class AppModule {
    private final AppContext appContext;

    public AppModule(AppContext appContext) {
        this.appContext = appContext;
    }

    @Singleton
    @Provides
    public RxBus<IEvent> provideEvenBus() {
        return new RxBus<>();
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return appContext.getApplicationContext();
    }

    // @Singleton
    @Provides
    public IDataModule provideDataModule() {
        return new DataModule(appContext.getComponent());
    }

    // @Singleton
    @Provides
    public IDbModule provideDbModule() {
        return new DbModule(appContext.getComponent());
    }

    // @Singleton
    @Provides
    public ApiModule provideApiModule() {
        return new ApiModule();
    }
}
