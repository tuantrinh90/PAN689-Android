package com.football.fantasy.fragments.match_up.my;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.MatchupLeagueAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MatchupMyLeagueFragment extends BaseMainMvpFragment<IMatchupMyLeagueView, IMatchupMyLeaguePresenter<IMatchupMyLeagueView>> implements IMatchupMyLeagueView {
    public static MatchupMyLeagueFragment newInstance() {
        return new MatchupMyLeagueFragment();
    }

    @BindView(R.id.rvMyLeague)
    ExtRecyclerView<String> rvMyLeague;

    private MatchupLeagueAdapter mAdapter;

    @Override
    public int getResourceId() {
        return R.layout.match_up_my_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
    }

    private void initView() {
        List<String> arr = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            arr.add("item " + i);
        }
        mAdapter = new MatchupLeagueAdapter(arr);

        // init recyclerView
        rvMyLeague
                .adapter(mAdapter)
                .itemViewCacheSize(8)
                .build();
    }

    @NonNull
    @Override
    public IMatchupMyLeaguePresenter<IMatchupMyLeagueView> createPresenter() {
        return new MatchupMyDataLeaguePresenter(getAppComponent());
    }
}
