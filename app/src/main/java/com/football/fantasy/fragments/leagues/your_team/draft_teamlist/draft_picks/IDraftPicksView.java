package com.football.fantasy.fragments.leagues.your_team.draft_teamlist.draft_picks;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PickHistoryResponse;

import java.util.List;

public interface IDraftPicksView extends IBaseMvpView {

    void displayPickHistories(List<PickHistoryResponse> pickHistories);
}
