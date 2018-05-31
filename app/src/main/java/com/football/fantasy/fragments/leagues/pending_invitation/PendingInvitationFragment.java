package com.football.fantasy.fragments.leagues.pending_invitation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.interfaces.Optional;
import com.football.adapters.LeaguesAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.Constant;

import java.util.List;

import butterknife.BindView;

public class PendingInvitationFragment extends BaseMainMvpFragment<IPendingInvitationView, IPendingInvitationPresenter<IPendingInvitationView>> implements IPendingInvitationView {
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
    }

    void initView() {
        // leagueResponses
        leaguesAdapter = new LeaguesAdapter(mActivity, LeaguesAdapter.PENDING_INVITATIONS, leagueResponses, details -> {

        }, approve -> {
            presenter.invitationDecisions(approve, Constant.KEY_INVITATION_ACCEPT);
        }, reject -> {
            presenter.invitationDecisions(reject, Constant.KEY_INVITATION_DECLINE);
        }, join -> {

        });

        rvRecyclerView.init(mActivity, leaguesAdapter)
                .setOnExtRefreshListener(() -> {
                    rvRecyclerView.clearItems();
                    page = 1;
                    presenter.getPendingInvitations(page, ExtPagingListView.NUMBER_PER_PAGE);
                })
                .setOnExtLoadMoreListener(() -> {
                    page++;
                    presenter.getPendingInvitations(page, ExtPagingListView.NUMBER_PER_PAGE);
                });
        presenter.getPendingInvitations(page, ExtPagingListView.NUMBER_PER_PAGE);
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
        Optional.from(rvRecyclerView).doIfPresent(rv -> rv.addNewItems(its));
    }

    @Override
    public void removeItem(LeagueResponse leagueResponse) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> rv.removeItem(leagueResponse));
    }
}
