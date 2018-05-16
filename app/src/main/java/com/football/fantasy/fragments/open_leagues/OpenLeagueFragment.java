package com.football.fantasy.fragments.open_leagues;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.football.adapters.LeaguesAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.fantasy.R;
import com.football.models.League;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OpenLeagueFragment extends BaseMvpFragment<IOpenLeagueView, IOpenLeaguePresenter<IOpenLeagueView>> implements IOpenLeagueView {
    public static OpenLeagueFragment newInstance() {
        return new OpenLeagueFragment();
    }

    @BindView(R.id.svSearchView)
    SearchView svSearchView;
    @BindView(R.id.rvRecyclerView)
    RecyclerView rvRecyclerView;

    List<League> leagues;
    LeaguesAdapter leaguesAdapter;

    @Override
    public int getResourceId() {
        return R.layout.open_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
        // leagues
        leagues = new ArrayList<League>() {{
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
        }};

        leaguesAdapter = new LeaguesAdapter(mActivity, leagues, details -> {

        }, approve -> {

        }, reject -> {

        }, join -> {

        });
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvRecyclerView.setAdapter(leaguesAdapter);

        // search view
        svSearchView.setFilterListener(v -> {

        });

        // action search
        svSearchView.getSearchView().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                return true;
            }

            return false;
        });
    }

    @Override
    public IOpenLeaguePresenter<IOpenLeagueView> createPresenter() {
        return new OpenLeaguePresenter(getAppComponent());
    }
}
