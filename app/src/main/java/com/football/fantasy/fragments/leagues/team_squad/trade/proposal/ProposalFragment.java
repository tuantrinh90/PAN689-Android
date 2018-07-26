package com.football.fantasy.fragments.leagues.team_squad.trade.proposal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

import butterknife.BindView;
import butterknife.OnClick;

public class ProposalFragment extends BaseMainMvpFragment<IProposalView, IProposalPresenter<IProposalView>> implements IProposalView {

    @BindView(R.id.tvCancel)
    ExtTextView tvCancel;

    public static void start(Context context, int leagueId) {
        AloneFragmentActivity.with(context)
                .parameters(ProposalFragment.newBundle(leagueId))
                .start(ProposalFragment.class);
    }

    private static Bundle newBundle(int leagueId) {
        Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_proposal_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
    }

    private void getDataFromBundle() {
    }

    @NonNull
    @Override
    public IProposalPresenter<IProposalView> createPresenter() {
        return new ProposalDataPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }

    private void initView() {
    }

    @OnClick({R.id.tvCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                mActivity.finish();
                break;
        }
    }

}
