package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_reveiew;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ProgressBar;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.lineup.PlayerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal.ProposalFragment;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TradeResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class ProposalReviewFragment extends BaseMvpFragment<IProposalReviewView, IProposalReviewPresenter<IProposalReviewView>> implements IProposalReviewView {

    private static final String KEY_TRADE = "TRADE";
    private static final String KEY_TITLE = "TITLE";

    @BindView(R.id.tvTime)
    ExtTextView tvTime;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tvReject)
    ExtTextView tvReject;
    @BindView(R.id.tvApprove)
    ExtTextView tvApprove;
    @BindView(R.id.tvTitleTeam1)
    ExtTextView tvTitleTeam1;
    @BindView(R.id.player11)
    PlayerView player11;
    @BindView(R.id.player12)
    PlayerView player12;
    @BindView(R.id.player13)
    PlayerView player13;
    @BindView(R.id.tvTitleTeam2)
    ExtTextView tvTitleTeam2;
    @BindView(R.id.player21)
    PlayerView player21;
    @BindView(R.id.player22)
    PlayerView player22;
    @BindView(R.id.player23)
    PlayerView player23;
    @BindViews({R.id.player11, R.id.player12, R.id.player13, R.id.player21, R.id.player22, R.id.player23})
    PlayerView[] playerViews;

    private String title;
    private TradeResponse trade;

    public static void start(Context context, String title, TradeResponse trade) {
        AloneFragmentActivity.with(context)
                .parameters(ProposalReviewFragment.newBundle(title, trade))
                .start(ProposalReviewFragment.class);
    }

    private static Bundle newBundle(String title, TradeResponse trade) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putSerializable(KEY_TRADE, trade);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_team_details_gameplay_option_proposal_review_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initData();
        initView();
    }

    private void getDataFromBundle() {
        title = getArguments().getString(KEY_TITLE);
        trade = (TradeResponse) getArguments().getSerializable(KEY_TRADE);
    }

    @Override
    public String getTitleString() {
        return title;
    }

    @NonNull
    @Override
    public IProposalReviewPresenter<IProposalReviewView> createPresenter() {
        return new ProposalDataReviewPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    private void initData() {
    }

    private void initView() {
        for (PlayerView player : playerViews) {
            player.setTextColor(ContextCompat.getColor(mActivity, R.color.color_black));
            player.setPlayer(null);
            player.setAddable(false);
            player.setRemovable(false);
        }

        tvTime.setText(AppUtilities.getTime(trade.getDeadline(), Constant.FORMAT_DATE_TIME_SERVER, Constant.FORMAT_DATE_TIME));
        tvReject.setText(getString(R.string.rejected, trade.getTotalRejection()));
        tvApprove.setText(getString(R.string.approved, trade.getTotalApproval()));
        tvTitleTeam1.setText(trade.getTeam().getName());
        tvTitleTeam2.setText(trade.getWithTeam().getName());
        progressBar.setMax(trade.getTotalReview());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(trade.getTotalRejection(), true);
        } else {
            progressBar.setProgress(trade.getTotalRejection());
        }
    }

    @OnClick({R.id.buttonReject, R.id.buttonApprove})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.buttonReject:
                DialogUtils.messageBox(mActivity,
                        0,
                        getString(R.string.app_name),
                        getString(R.string.message_confirm_proposal_reject),
                        getString(R.string.ok),
                        getString(R.string.cancel),
                        (dialog, which) -> {
                            decision(TradeResponse.DECISION_REJECT);
                        }, null);
                break;
            case R.id.buttonApprove:
                DialogUtils.messageBox(mActivity,
                        0,
                        getString(R.string.app_name),
                        getString(R.string.message_confirm_proposal_approved),
                        getString(R.string.ok),
                        getString(R.string.cancel),
                        (dialog, which) -> {
                            decision(TradeResponse.DECISION_ACCEPT);
                        }, null);
                break;
        }
    }

    private void decision(int status) {
        presenter.submitDecision(trade.getId(), trade.getTeamId(), status);
    }

    @Override
    public void submitSuccess(TradeResponse response) {
        mActivity.finish();
    }
}
