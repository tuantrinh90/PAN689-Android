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
import com.bon.util.DateTimeUtils;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.lineup.PlayerView;
import com.football.customizes.textview.ExtTextViewCountdown;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TradeDetailResponse;
import com.football.models.responses.TradeResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.fantasy.fragments.leagues.team_details.team_squad.trade.request.RequestFragment.REQUEST_BY_YOU;

public class ProposalReviewFragment extends BaseMvpFragment<IProposalReviewView, IProposalReviewPresenter<IProposalReviewView>> implements IProposalReviewView {

    private static final String KEY_TRADE = "TRADE";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_TYPE = "TYPE";

    @BindView(R.id.tvDeadline)
    ExtTextView tvDeadline;
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
    @BindView(R.id.to_you_buttons)
    View toYouButton;
    @BindView(R.id.by_you_buttons)
    View byYouButton;
    @BindView(R.id.header_by_you)
    View headerByYou;
    @BindView(R.id.header_to_you)
    View headerToYou;
    @BindView(R.id.tvTimeLeft)
    ExtTextViewCountdown tvTimeLeft;

    private String title;
    private TradeResponse trade;
    private int type;

    public static void start(Context context, String title, TradeResponse trade, int type) {
        AloneFragmentActivity.with(context)
                .parameters(ProposalReviewFragment.newBundle(title, trade, type))
                .start(ProposalReviewFragment.class);
    }

    private static Bundle newBundle(String title, TradeResponse trade, int type) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putSerializable(KEY_TRADE, trade);
        bundle.putInt(KEY_TYPE, type);
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
        type = getArguments().getInt(KEY_TYPE);
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
        if (type == REQUEST_BY_YOU) {
            byYouButton.setVisibility(View.VISIBLE);
            toYouButton.setVisibility(View.GONE);
            headerByYou.setVisibility(View.VISIBLE);
            headerToYou.setVisibility(View.GONE);

            displayViewByYou();
        } else {
            byYouButton.setVisibility(View.GONE);
            toYouButton.setVisibility(View.VISIBLE);
            headerByYou.setVisibility(View.GONE);
            headerToYou.setVisibility(View.VISIBLE);

            displayViewToYou();
        }

        tvTitleTeam1.setText(trade.getTeam().getName());
        tvTitleTeam2.setText(trade.getWithTeam().getName());

        displayPlayerViews();
    }

    private void displayPlayerViews() {

        PlayerView[] playerViews1st = new PlayerView[]{player11, player21};
        PlayerView[] playerViews2nd = new PlayerView[]{player12, player22};
        PlayerView[] playerViews3rd = new PlayerView[]{player13, player23};
        PlayerView[][] playerViews = new PlayerView[][]{playerViews1st, playerViews2nd, playerViews3rd};

        // trade items
        List<TradeDetailResponse> tradeItems = trade.getItems();
        for (int i = 0, playerViewsLength = playerViews.length; i < playerViewsLength; i++) {
            displayPlayer(playerViews[i][0], tradeItems.size() > i ? tradeItems.get(i).getFromPlayer() : null);
            displayPlayer(playerViews[i][1], tradeItems.size() > i ? tradeItems.get(i).getToPlayer() : null);
        }
    }

    private void displayPlayer(PlayerView playerView, PlayerResponse player) {
        playerView.setTextColor(ContextCompat.getColor(mActivity, R.color.color_black));
        if (player != null) playerView.setPosition(player.getMainPosition());
        playerView.setPlayer(player);
        playerView.setAddable(false);
        playerView.setRemovable(false);
    }

    private void displayViewByYou() {
        tvDeadline.setText(AppUtilities.getTime(trade.getDeadline(), Constant.FORMAT_DATE_TIME_SERVER, Constant.FORMAT_DATE_TIME));
        tvReject.setText(getString(R.string.rejected, trade.getTotalRejection()));
        tvApprove.setText(getString(R.string.approved, trade.getTotalApproval()));
        progressBar.setMax(trade.getTotalReview());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(trade.getTotalRejection(), true);
        } else {
            progressBar.setProgress(trade.getTotalRejection());
        }
    }

    private void displayViewToYou() {
        Calendar deadline = DateTimeUtils.convertStringToCalendar(trade.getDeadline(), Constant.FORMAT_DATE_TIME_SERVER);
        Calendar current = Calendar.getInstance();
        tvTimeLeft.setTime(deadline.getTimeInMillis() / 1000 - current.getTimeInMillis() / 1000);
    }

    @OnClick({R.id.buttonReject, R.id.buttonApprove, R.id.buttonCancel})
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

            case R.id.buttonCancel:
                DialogUtils.messageBox(mActivity,
                        0,
                        getString(R.string.app_name),
                        getString(R.string.message_confirm_proposal_cancelled),
                        getString(R.string.ok),
                        getString(R.string.cancel),
                        (dialog, which) -> {
                            presenter.cancelDecision(trade.getId());
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
