package com.football.utilities;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bon.interfaces.Optional;
import com.bon.network.NetworkUtils;
import com.football.models.ErrorBody;

import java8.util.function.Consumer;

public class ConnectionUtils {
    public static void hasConnection(@NonNull Context context, Consumer<Boolean> consumer) {
        Optional.from(consumer).doIfPresent(c -> c.accept(NetworkUtils.isNetworkAvailable(context)));
    }

    public static boolean isOfflineMode(ErrorBody error) {
        if (error == null) return true;
        int errorCode = error.getStatus();
        return isOfflineMode(errorCode);
    }

    public static boolean isOfflineMode(int errorCode) {
        switch (errorCode) {
            case ErrorCodes.NO_INTERNET:
            case ErrorCodes.BAD_GATEWAY:
                return true;
            default:
                return false;
        }
    }
}
