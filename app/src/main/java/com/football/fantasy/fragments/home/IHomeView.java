package com.football.fantasy.fragments.home;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.NewsResponse;

import java.util.List;

public interface IHomeView extends IBaseMvpView {
    void notifyDataSetChangedNews(List<NewsResponse> its);

    void notifyDataSetChangedLeagues(List<LeagueResponse> its);

    void stopLoading();
}
