package com.football.fantasy.fragments.leagues.team_preview;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ITeamPreviewPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getLineup(int teamId);
}
