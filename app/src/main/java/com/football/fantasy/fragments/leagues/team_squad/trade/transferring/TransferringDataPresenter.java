package com.football.fantasy.fragments.leagues.team_squad.trade.transferring;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

import java.util.List;

public class TransferringDataPresenter extends BaseDataPresenter<ITransferringView> implements ITransferringPresenter<ITransferringView> {
    /**
     * @param appComponent
     */
    protected TransferringDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPlayers(String filterPositions, String filterClubs, List<ExtKeyValuePair> displayPairs, int[] sorts, int page) {

    }
}
