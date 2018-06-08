package com.football.fantasy.fragments.leagues.pending_invitation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.football.adapters.LeaguesAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.events.LeagueEvent;
import com.football.events.StopLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.Constant;

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

    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    List<LeagueResponse> leagueResponses;
    LeaguesAdapter leaguesAdapter;

    int page = 1;

    @Override
    public int getResourceId() {
        return R.layout.pending_invitation_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        registerEvent();
    }

    void registerEvent() {
        try {
            // load my leagues
            mCompositeDisposable.add(bus.ofType(LeagueEvent.class).subscribeWith(new DisposableObserver<LeagueEvent>() {
                @Override
                public void onNext(LeagueEvent leagueEvent) {
                    try {
                        page = 1;
                        rvRecyclerView.clearItems();
                        presenter.getPendingInvitations(page, ExtPagingListView.NUMBER_PER_PAGE);
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
                                List<LeagueResponse> leagueResponses = leaguesAdapter.getItems();
                                if (leagueResponses != null && leagueResponses.size() > 0) {
                                    leagueResponses = StreamSupport.stream(leagueResponses).filter(n -> n.getId() != stopLeagueEvent.getLeagueId()).collect(Collectors.toList());
                                    rvRecyclerView.notifyDataSetChanged(leagueResponses);
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
            leaguesAdapter = new LeaguesAdapter(mActivity, LeaguesAdapter.PENDING_INVITATIONS, leagueResponses, details -> {
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundle(getString(R.string.pending_invitation), details.getId(), LeagueDetailFragment.PENDING_LEAGUES))
                        .start(LeagueDetailFragment.class);
            }, approve -> {
                presenter.invitationDecisions(approve, Constant.KEY_INVITATION_ACCEPT);
            }, reject -> {
                presenter.invitationDecisions(reject, Constant.KEY_INVITATION_DECLINE);
            }, null);

            rvRecyclerView.init(mActivity, leaguesAdapter)
                    .setOnExtRefreshListener(() -> {
                        try {
                            rvRecyclerView.clearItems();
                            page = 1;
                            presenter.getPendingInvitations(page, ExtPagingListView.NUMBER_PER_PAGE);
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    })
                    .setOnExtLoadMoreListener(() -> {
                        try {
                            page++;
                            presenter.getPendingInvitations(page, ExtPagingListView.NUMBER_PER_PAGE);
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    });
            presenter.getPendingInvitations(page, ExtPagingListView.NUMBER_PER_PAGE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @NonNull
    @Override
    public IPendingInvitationPresenter<IPendingInvitationView> createPresenter() {
        return new PendingInvitationDataPresenter(getAppComponent());
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            if (isLoading) {
                rv.startLoading(true);
            } else {
                rv.stopLoading(true);
            }
        });
    }

    @Override
    public void notifyDataSetChanged(List<LeagueResponse> its) {
        try {
            Optional.from(rvRecyclerView).doIfPresent(rv -> rv.addNewItems(its));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void removeItem(LeagueResponse leagueResponse) {
        try {
            Optional.from(rvRecyclerView).doIfPresent(rv -> rv.removeItem(leagueResponse));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void goCreateTeam(LeagueResponse leagueResponse) {
        AloneFragmentActivity.with(this)
                .parameters(SetupTeamFragment.newBundle(
                        leagueResponse,
                        null,
                        getString(R.string.pending_invitation),
                        LeagueDetailFragment.PENDING_LEAGUES))
                .start(SetupTeamFragment.class);
    }
}
