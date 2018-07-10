package com.football.fantasy.fragments.leagues.team_squad.trade.record;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TransferHistoryResponse;

import java.util.List;

public interface IRecordView extends IBaseMvpView {
    void displayHistories(List<TransferHistoryResponse> histories);

}
