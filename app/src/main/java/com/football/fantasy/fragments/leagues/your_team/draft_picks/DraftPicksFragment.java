package com.football.fantasy.fragments.leagues.your_team.draft_picks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;

public class DraftPicksFragment extends BaseMvpFragment<IDraftPicksView, IDraftPicksPresenter<IDraftPicksView>> implements IDraftPicksView {
    private static final String TAG = DraftPicksFragment.class.getSimpleName();

    static final String KEY_LEAGUE = "LEAGUE";

    public static DraftPicksFragment newInstance(LeagueResponse league) {
        DraftPicksFragment fragment = new DraftPicksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.draft_teams_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        initView();
        registerBus();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
        }
    }

    void initView() {
    }

    private void refresh() {
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

}
