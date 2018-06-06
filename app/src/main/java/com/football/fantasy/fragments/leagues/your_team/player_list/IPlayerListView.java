package com.football.fantasy.fragments.leagues.your_team.player_list;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

import java.util.List;

public interface IPlayerListView extends IBaseMvpView {

    void notifyDataSetChangedPlayers(List<PlayerResponse> data);

}
