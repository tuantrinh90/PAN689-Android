package com.football.interactors;

import android.os.Handler;

import com.football.interactors.service.IApiService;
import com.football.interactors.service.IFileService;
import com.football.interactors.service.ILongApiService;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface IDataModule {
    Handler getHandler();

    IFileService getFileService();

    IApiService getApiService();

    ILongApiService getLongService();
}
