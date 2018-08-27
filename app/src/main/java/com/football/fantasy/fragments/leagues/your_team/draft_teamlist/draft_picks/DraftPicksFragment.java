package com.football.fantasy.fragments.leagues.your_team.draft_teamlist.draft_picks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PickHistoryResponse;

import java.util.List;

public class DraftPicksFragment extends BaseMvpFragment<IDraftPicksView, IDraftPicksPresenter<IDraftPicksView>> implements IDraftPicksView {
    private static final String TAG = DraftPicksFragment.class.getSimpleName();

    static final String KEY_LEAGUE = "LEAGUE";

    private LeagueResponse league;

    private int page = 1;

    public static DraftPicksFragment newInstance(LeagueResponse league) {
        DraftPicksFragment fragment = new DraftPicksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.draft_picks_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        initView();
        getPickHistories();
        registerBus();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        }
    }

    void initView() {
    }

    private void getPickHistories() {
        presenter.getPickHistories(league.getId(), page);
    }

    private void refresh() {
        page = 1;
    }

    @NonNull
    @Override
    public IDraftPicksPresenter<IDraftPicksView> createPresenter() {
        return new DraftPicksPresenter(getAppComponent());
    }

    private void registerBus() {
    }


    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
        }
    }

    @Override
    public void displayPickHistories(List<PickHistoryResponse> pickHistories) {
        Log.d(TAG, "displayPickHistories: " + pickHistories.size());
    }
}
