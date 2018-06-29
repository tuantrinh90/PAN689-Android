package com.football.fantasy.fragments.leagues.team_squad.trade.transferring;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

import java.util.List;

public interface ITransferringPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getPlayers(String filterPositions, String filterClubs, List<ExtKeyValuePair> displayPairs, int[] sorts, int page);
}
