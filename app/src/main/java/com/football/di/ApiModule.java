package com.football.di;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bon.jackson.JacksonUtils;
import com.football.interactors.service.AccessInterceptor;
import com.football.interactors.service.IApiService;
import com.football.interactors.service.IFileService;
import com.football.interactors.service.ILongApiService;
import com.football.utilities.ServiceConfig;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by dangpp on 2/9/2018.
 */

@Module
public class ApiModule {
    private static final String TAG = ApiModule.class.getSimpleName();

    private enum OkHttpType {
        USUAL,
        LONG_TIMEOUT,
        FILE
    }

    @NonNull
    private Retrofit provideBaseRetrofit(String baseUrl, JacksonConverterFactory converterFactory, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    // @Singleton
    @Provides
    public AccessInterceptor provideAccessInterceptor(Context context) {
        return new AccessInterceptor(context);
    }

    @Provides
    public Retrofit provideRetrofit(JacksonConverterFactory converterFactory, OkHttpClient client) {
        return provideBaseRetrofit(ServiceConfig.BASE_URL, converterFactory, client);
    }

    @Provides
    public OkHttpClient provideOkHttp(HttpLoggingInterceptor logging, AccessInterceptor access) {
        return provideBaseOkHttp(logging, access, OkHttpType.USUAL);
    }

    private OkHttpClient provideBaseOkHttp(@Nullable HttpLoggingInterceptor logging, AccessInterceptor access,
                                           @NonNull OkHttpType type) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                //.retryOnConnectionFailure(ServiceConfig.RETRY_POLICY)
                .addInterceptor(access);

        int reqTimeOut;
        switch (type) {
            case FILE:
                addLogInterceptorIfNeed(logging, client);
                reqTimeOut = ServiceConfig.REQUEST_FILE_TIMEOUT;
                break;
            case LONG_TIMEOUT:
                addLogInterceptorIfNeed(logging, client);
                reqTimeOut = ServiceConfig.REQUEST_TIMEOUT_LONG;
                break;
            case USUAL:
            default:
                addLogInterceptorIfNeed(logging, client);
                reqTimeOut = ServiceConfig.REQUEST_TIMEOUT;
                break;
        }

        client.connectTimeout(reqTimeOut, TimeUnit.SECONDS)
                .readTimeout(reqTimeOut, TimeUnit.SECONDS)
                .writeTimeout(reqTimeOut, TimeUnit.SECONDS);

        return client.build();
    }

    private void addLogInterceptorIfNeed(@Nullable HttpLoggingInterceptor logging, OkHttpClient.Builder client) {
        client.addInterceptor(logging);
    }

    @Singleton
    @Provides
    public HttpLoggingInterceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Singleton
    @Provides
    public JacksonConverterFactory provideJacksonConverterFactory() {
        return JacksonConverterFactory.create(JacksonUtils.getJsonMapper());
    }

    @Provides
    public IApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(IApiService.class);
    }

    @Provides
    public IFileService provideFileService(HttpLoggingInterceptor logging, AccessInterceptor access) {
        Retrofit retrofit = provideBaseRetrofit(ServiceConfig.BASE_URL, provideJacksonConverterFactory(),
                provideBaseOkHttp(logging, access, OkHttpType.FILE));
        return retrofit.create(IFileService.class);
    }

    @Provides
    public ILongApiService provideLongService(HttpLoggingInterceptor logging, AccessInterceptor access) {
        OkHttpClient client = provideBaseOkHttp(logging, access, OkHttpType.LONG_TIMEOUT);
        Retrofit retrofit = provideBaseRetrofit(ServiceConfig.BASE_URL, provideJacksonConverterFactory(), client);
        return retrofit.create(ILongApiService.class);
    }
}
