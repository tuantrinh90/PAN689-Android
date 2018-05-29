package com.football.fantasy.fragments.leagues.pending_invitation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.football.adapters.LeaguesAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.League;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PendingInvitationFragment extends BaseMainMvpFragment<IPendingInvitationView, IPendingInvitationPresenter<IPendingInvitationView>> implements IPendingInvitationView {
    public static PendingInvitationFragment newInstance() {
        return new PendingInvitationFragment();
    }

    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    List<League> leagues;
    LeaguesAdapter leaguesAdapter;

    @Override
    public int getResourceId() {
        return R.layout.pending_invitation_fragment;
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
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 3, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 3, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 3, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 3, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 3, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 3, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 3, 1));
        }};

        leaguesAdapter = new LeaguesAdapter(mActivity, leagues, details -> {

        }, approve -> {

        }, reject -> {

        }, join -> {

        });
        rvRecyclerView.init(mActivity, leaguesAdapter);
    }

    @NonNull
    @Override
    public IPendingInvitationPresenter<IPendingInvitationView> createPresenter() {
        return new PendingInvitationDataPresenter(getAppComponent());
    }
}
