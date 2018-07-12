package com.football.fantasy.fragments.leagues.pending_invitation;

import android.support.annotation.NonNull;

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

    int page = 1;

    @Override
    public int getResourceId() {
        return R.layout.pending_invitation_fragment;
    }

    @Override
    protected void initialized() {
        super.initialized();
        page = 1;
        initView();
        registerEvent();
        getPendingList();
    }

    void registerEvent() {
        try {
            // load my leagues
            mCompositeDisposable.add(bus.ofType(LeagueEvent.class).subscribeWith(new DisposableObserver<LeagueEvent>() {
                @Override
                public void onNext(LeagueEvent leagueEvent) {
                    refresh();
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
                                List<LeagueResponse> leagues = rvLeague.getAdapter().getDataSet();
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
            LeaguesAdapter adapter = new LeaguesAdapter(
                    LeaguesAdapter.PENDING_INVITATIONS,
                    new ArrayList<>(),
                    league -> { // click event
                        AloneFragmentActivity.with(this)
                                .parameters(LeagueDetailFragment.newBundle(
                                        getString(R.string.pending_invitation),
                                        league.getId(),
                                        LeagueDetailFragment.PENDING_LEAGUES,
                                        league.getInvitation().getId()))
                                .start(LeagueDetailFragment.class);
                    },
                    league -> { // approve event
                        if (league.getCurrentNumberOfUser() >= league.getNumberOfUser()) {
                            showMessage(getString(R.string.message_league_full));
                        } else {
                            checkExistInMyLeague(league);
                        }
                    },
                    league -> { // reject event
                        presenter.invitationDecisions(league, Constant.KEY_INVITATION_DECLINE);
                    },
                    null);

            rvLeague.
                    adapter(adapter)
                    .refreshListener(this::refresh)
                    .loadMoreListener(() -> {
                        page++;
                        getPendingList();
                    })
                    .build();
            rvLeague.startLoading();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void checkExistInMyLeague(LeagueResponse league) {
        presenter.getLeagueDetail(league);
    }

    private void refresh() {
        page = 1;
        rvLeague.clear();
        getPendingList();
        rvLeague.startLoading();
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
