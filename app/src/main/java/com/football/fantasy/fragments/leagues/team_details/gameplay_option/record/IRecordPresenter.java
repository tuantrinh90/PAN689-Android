package com.football.fantasy.fragments.leagues.team_details.gameplay_option.record;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IRecordPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getTransferHistories(Integer teamId, String gameOption, int page);
}
