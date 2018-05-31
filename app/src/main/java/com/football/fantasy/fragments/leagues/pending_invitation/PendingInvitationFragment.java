package com.football.fantasy.fragments.leagues.pending_invitation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.listview.listener.ExtClickListener;
import com.bon.customview.listview.listener.ExtLoadMoreListener;
import com.bon.customview.listview.listener.ExtRefreshListener;
import com.bon.interfaces.Optional;
import com.football.adapters.LeaguesAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;

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
        leaguesAdapter = new LeaguesAdapter(mActivity, leagueResponses, details -> {

        }, approve -> {

        }, reject -> {

        }, join -> {

        });
        rvRecyclerView.init(mActivity, leaguesAdapter)
                .setOnExtClickListener(new ExtClickListener<LeagueResponse>() {

                    @Override
                    public void onClick(int position, LeagueResponse item) {

                    }
                })
                .setOnExtRefreshListener(new ExtRefreshListener() {
                    @Override
                    public void onRefresh() {

                    }
                })
                .setOnExtLoadMoreListener(new ExtLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                    }
                });
        presenter.getPendingInvitations(1);
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
}
