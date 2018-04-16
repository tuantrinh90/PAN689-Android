package com.football.di;

import android.os.Handler;

import com.bon.eventbus.IEvent;
import com.bon.eventbus.RxBus;
import com.football.events.SignInEvent;
import com.football.interactors.IDataModule;
import com.football.interactors.database.IDbModule;
import com.football.interactors.service.IApiService;

import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import java8.util.function.Consumer;
import rx.functions.Action0;

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
        component.inject(this);
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void getUsers(Consumer<List<String>> consumer) {
        Action0 f;
    }

    public void sentMessage() {
        bus.send(new SignInEvent("fdfdfdf"));
    }
}
