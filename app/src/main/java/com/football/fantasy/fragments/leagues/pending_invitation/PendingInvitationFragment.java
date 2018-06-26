package com.football.fantasy.fragments.leagues.pending_invitation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.logger.Logger;
import com.football.adapters.LeaguesAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.events.LeagueEvent;
import com.football.events.StopLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class PendingInvitationFragment extends BaseMainMvpFragment<IPendingInvitationView, IPendingInvitationPresenter<IPendingInvitationView>> implements IPendingInvitationView {
    static final String TAG = PendingInvitationFragment.class.getSimpleName();

    public static PendingInvitationFragment newInstance() {
        return new PendingInvitationFragment();
    }

    @BindView(R.id.rv_pending)
    ExtRecyclerView<LeagueResponse> rvLeague;

    private boolean initialized = false;

    LeaguesAdapter mAdapter;

    int page = 1;

    @Override
    public int getResourceId() {
        return R.layout.pending_invitation_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !initialized) {
            initialized = true;
            initView();
            registerEvent();
        }
    }

    void registerEvent() {
        try {
            // load my leagues
            mCompositeDisposable.add(bus.ofType(LeagueEvent.class).subscribeWith(new DisposableObserver<LeagueEvent>() {
                @Override
                public void onNext(LeagueEvent leagueEvent) {
                    try {
                        page = 1;
                        rvLeague.clear();
                        rvLeague.startLoading();
                        getPendingList();
                    } catch (Exception e) {
                        Logger.e(TAG, e);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            }));

            // load my leagues, remove
            mCompositeDisposable.add(bus.ofType(StopLeagueEvent.class)
                    .subscribeWith(new DisposableObserver<StopLeagueEvent>() {
                        @Override
                        public void onNext(StopLeagueEvent stopLeagueEvent) {
                            try {
                                List<LeagueResponse> leagues = mAdapter.getDataSet();
                                if (leagues != null && leagues.size() > 0) {
                                    leagues = StreamSupport.stream(leagues).filter(n -> n.getId() != stopLeagueEvent.getLeagueId()).collect(Collectors.toList());
                                    rvLeague.clear();
                                    rvLeague.addItems(leagues);
                                }
                            } catch (Exception e) {
                                Logger.e(TAG, e);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    void initView() {
        try {
            // leagueResponses
            mAdapter = new LeaguesAdapter(
                    LeaguesAdapter.PENDING_INVITATIONS,
                    new ArrayList<>(),
                    league -> { // click event
                        AloneFragmentActivity.with(this)
                                .parameters(LeagueDetailFragment.newBundle(getString(R.string.pending_invitation), league.getId(), LeagueDetailFragment.PENDING_LEAGUES))
                                .start(LeagueDetailFragment.class);
                    },
                    league -> { // approve event
                        if (league.getCurrentNumberOfUser() >= league.getNumberOfUser()) {
                            showMessage(getString(R.string.message_league_full));
                        } else {
                            presenter.invitationDecisions(league, Constant.KEY_INVITATION_ACCEPT);
                        }
                    },
                    league -> { // reject event
                        presenter.invitationDecisions(league, Constant.KEY_INVITATION_DECLINE);
                    },
                    null);

            rvLeague.
                    adapter(mAdapter)
                    .refreshListener(() -> {
                        rvLeague.clear();
                        rvLeague.startLoading();
                        page = 1;
                        getPendingList();
                    })
                    .loadMoreListener(() -> {
                        page++;
                        getPendingList();
                    })
                    .build();
            rvLeague.startLoading();
            getPendingList();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void getPendingList() {
        presenter.getPendingInvitations(page, ExtPagingListView.NUMBER_PER_PAGE);
    }

    @NonNull
    @Override
    public IPendingInvitationPresenter<IPendingInvitationView> createPresenter() {
        return new PendingInvitationDataPresenter(getAppComponent());
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
    }

    @Override
    public void displayLeagues(List<LeagueResponse> its) {
        rvLeague.addItems(its);
    }

    @Override
    public void removeItem(LeagueResponse league) {
        rvLeague.removeItem(league);
    }

    @Override
    public void goCreateTeam(LeagueResponse league) {
        bus.send(new LeagueEvent());
        AloneFragmentActivity.with(this)
                .parameters(SetupTeamFragment.newBundle(
                        null,
                        league.getId(),
                        getString(R.string.pending_invitation),
                        LeagueDetailFragment.PENDING_LEAGUES))
                .start(SetupTeamFragment.class);
    }
}
