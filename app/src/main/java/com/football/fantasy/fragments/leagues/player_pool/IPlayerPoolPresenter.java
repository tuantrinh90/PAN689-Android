package com.football.fantasy.fragments.leagues.player_pool;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

import java.util.List;

public interface IPlayerPoolPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getPlayers(String seasonId, int leagueId, int seasonIdToTransfer, PlayerResponse playerTransfer, String positions, String clubs, List<ExtKeyValuePair> displayPairs, int[] sorts, int page, String query);

    void getSeasons();
}
