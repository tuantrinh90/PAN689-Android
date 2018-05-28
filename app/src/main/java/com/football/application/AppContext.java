package com.football.application;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bon.application.ExtApplication;
import com.bon.logger.Logger;
import com.football.di.AppComponent;
import com.football.di.AppModule;
import com.football.di.DaggerAppComponent;
import com.football.fantasy.BuildConfig;
import com.football.fantasy.R;
import com.football.utilities.FragmentUtils;

import java8.util.function.Consumer;

/**
 * Created by dangpp on 2/9/2018.
 */

public class AppContext extends ExtApplication {
    private static final String TAG = AppContext.class.getSimpleName();

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        // dagger
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        component.inject(this);

        //  logger
        Logger.setEnableLog(BuildConfig.DEBUG);

        // update fragment utils
        FragmentUtils.setContainerViewId(R.id.fl_content);
    }

    /**
     * @param context
     * @return
     */
    @Nullable
    public static AppComponent getComponentFromContext(@Nullable Context context) {
        if (context == null) return null;

        AppComponent component;
        if (context instanceof AppContext) {
            component = ((AppContext) context).getComponent();
        } else {
            component = ((AppContext) context.getApplicationContext()).getComponent();
        }

        return component;
    }

    /**
     * @param context
     * @return
     */
    @Nullable
    public static AppContext from(@Nullable Context context) {
        if (context == null) return null;

        if (context instanceof AppContext) {
            return (AppContext) context;
        }

        return (AppContext) context.getApplicationContext();
    }

    /**
     * @param context
     * @param contextConsumer
     */
    public static void ifPresent(@Nullable Context context, Consumer<AppContext> contextConsumer) {
        if (context == null || contextConsumer == null) return;

        if (context instanceof AppContext) {
            contextConsumer.accept((AppContext) context);
            return;
        }

        contextConsumer.accept((AppContext) context.getApplicationContext());
    }

    /**
     * get app component
     *
     * @return
     */
    public AppComponent getComponent() {
        return component;
    }
}
