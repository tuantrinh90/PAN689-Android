package com.football.fantasy.fragments.leagues.team_details.gameplay_option.record;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TransferHistoryResponse;

import java.util.List;

public interface IRecordView extends IBaseMvpView {
    void displayHistories(List<TransferHistoryResponse> histories);

}
