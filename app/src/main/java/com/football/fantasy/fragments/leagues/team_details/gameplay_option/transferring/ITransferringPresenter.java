package com.football.fantasy.fragments.leagues.team_details.gameplay_option.transferring;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

import java.util.List;

public interface ITransferringPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getTeamTransferring(Integer teamId, String gameplay, String filterPositions, String filterClubs, List<ExtKeyValuePair> displayPairs, int[] sorts);

    void transferPlayer(Integer teamId, String gameplayOption, int fromPlayerId, int toPlayerId);
}
