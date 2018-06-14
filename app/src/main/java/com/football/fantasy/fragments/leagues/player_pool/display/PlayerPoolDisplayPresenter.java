package com.football.fantasy.fragments.leagues.player_pool.display;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.ExtPagingResponse;
import com.football.models.PagingResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.RealClubResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class PlayerPoolDisplayPresenter extends BaseDataPresenter<IPlayerPoolDisplayView> implements IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView> {
    /**
     * @param appComponent
     */
    public PlayerPoolDisplayPresenter(AppComponent appComponent) {
        super(appComponent);
    }


}
