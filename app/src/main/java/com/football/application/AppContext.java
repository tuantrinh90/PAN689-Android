package com.football.application;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bon.application.ExtApplication;
import com.bon.image.ImageLoaderUtils;
import com.bon.logger.Logger;
import com.bon.share_preferences.AppPreferences;
import com.football.di.AppComponent;
import com.football.di.AppModule;
import com.football.di.DaggerAppComponent;
import com.football.fantasy.BuildConfig;
import com.football.fantasy.R;
import com.football.utilities.Constant;
import com.football.utilities.FragmentUtils;
import com.football.utilities.ServiceConfig;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import java8.util.function.Consumer;

/**
 * Created by dangpp on 2/9/2018.
 */

public class AppContext extends ExtApplication {
    private static final String TAG = AppContext.class.getSimpleName();

    private AppComponent component;
    private Socket mSocket;

//    {
//        try {
//            mSocket = IO.socket(ServiceConfig.SOCKET_URL);
//        } catch (URISyntaxException e) {
//        }
//    }

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

        ImageLoaderUtils.setDefaultIcon(R.mipmap.ic_launcher);
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

    public Socket getSocket() {
        return mSocket;
    }

    public void connectSocket() {
        try {
            String token = AppPreferences.getInstance(this).getString(Constant.KEY_TOKEN);
            IO.Options options = new IO.Options();
            options.query = "token=" + token;

            mSocket = IO.socket(ServiceConfig.SOCKET_URL);
            mSocket.on(Socket.EVENT_CONNECT, args -> {
                Log.e("args", "EVENT_CONNECT::args:: " + argsToString(args));

            }).on(Socket.EVENT_CONNECT_ERROR, args -> {
                Log.e("args", "EVENT_CONNECT_ERROR::args:: " + argsToString(args));
                //dd
            }).on(Socket.EVENT_CONNECT_TIMEOUT, args -> {
                Log.e("args", "EVENT_CONNECT_TIMEOUT::args:: " + argsToString(args));
                //đ
            }).on(Socket.EVENT_ERROR, args -> {
                Log.e("args", "EVENT_ERROR::args:: " + argsToString(args));
            });
            mSocket.connect();
        } catch (URISyntaxException e) {
            Log.e(TAG, "connectSocket: ", e);
        }
    }

    private String argsToString(Object... args) {
        StringBuilder s = new StringBuilder();
        if (args.length > 0) {
            for (Object arg : args) {
                s.append(arg);
            }
        }
        return s.toString();
    }

    public void off(String event) {
        if (mSocket != null) {
            mSocket.off(event);
        }
    }

    public void disconnect() {
        if (mSocket != null) {
            mSocket.disconnect();
        }
    }

}
