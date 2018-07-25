package com.football.fantasy.fragments.leagues.league_details.trade_review;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;

public class TradeReviewFragment extends BaseMvpFragment<ITradeReviewView, ITradeReviewPresenter<ITradeReviewView>> implements ITradeReviewView {
    static final String TAG = TradeReviewFragment.class.getSimpleName();

    static final String KEY_LEAGUE = "key_leagues";

    public static Bundle newBundle(LeagueResponse leagueResponse) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, leagueResponse);
        return bundle;
    }

    LeagueResponse league;

    @Override
    public int getResourceId() {
        return R.layout.league_detail_ranking_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void getDataFromBundle() {
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    void initView() {
    }

    @Override
    public ITradeReviewPresenter<ITradeReviewView> createPresenter() {
        return new TradeReviewPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }
}
