package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_reveiew;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.DateTimeUtils;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.lineup.PlayerView;
import com.football.customizes.textview.ExtTextViewCountdown;
import com.football.events.TradeEvent;
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
import static com.football.fantasy.fragments.leagues.team_details.team_squad.trade.request.RequestFragment.REQUEST_TO_YOU;

public class ProposalReviewFragment extends BaseMvpFragment<IProposalReviewView, IProposalReviewPresenter<IProposalReviewView>> implements IProposalReviewView {

    private static final String KEY_TRADE = "TRADE";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_TYPE = "TYPE";

    private static final int TYPE_REVIEWING = 10;
    private static final int TYPE_RESULT = 11;

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
    View acceptRejectButtons;
    @BindView(R.id.by_you_buttons)
    View byYouButton;
    @BindView(R.id.header_by_you)
    View headerRejectApproved;
    @BindView(R.id.header_to_you)
    View headerTimeLeft;
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

    public static void start(Context context, String title, TradeResponse trade, String type) {
        AloneFragmentActivity.with(context)
                .parameters(ProposalReviewFragment.newBundle(
                        title,
                        trade,
                        type.equals(TradeResponse.TYPE_REVIEWING) ? TYPE_REVIEWING : TYPE_RESULT))
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
        type = getArguments().getInt(KEY_TYPE, -1);
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
        // || (type == -1 && trade.getStatus().equals(TradeResponse.STATUS_SUCCESSFUL))
        byYouButton.setVisibility(View.GONE);
        acceptRejectButtons.setVisibility(View.GONE);
        headerRejectApproved.setVisibility(View.GONE);
        headerTimeLeft.setVisibility(View.GONE);

        if (type == REQUEST_BY_YOU) {
            if (!isApproved()) {
                byYouButton.setVisibility(View.VISIBLE);
            }

            displayViewRejectApproved();

        } else if (type == REQUEST_TO_YOU) {
            headerTimeLeft.setVisibility(View.VISIBLE);
            if (!isApproved()) {
                acceptRejectButtons.setVisibility(View.VISIBLE);
            }

            displayViewTimeLeft();

        } else if (type == TYPE_REVIEWING) {
            // accept
            if (trade.getStatus() == TradeResponse.STATUS_ACCEPT) {
                displayViewRejectApproved();
            }
            // reject
            else if (trade.getStatus() == TradeResponse.STATUS_REJECT) {
                displayViewRejectApproved();
            }
            // pending
            else {
                displayViewRejectApproved();
                acceptRejectButtons.setVisibility(View.VISIBLE);
            }

        } else if (type == TYPE_RESULT) {
            displayViewRejectApproved();
        }

        tvTitleTeam1.setText(trade.getTeam().getName());
        tvTitleTeam2.setText(trade.getWithTeam().getName());

        displayPlayerViews();
    }

    /**
     * Dành cho màn hình RequestByYou, RequestToYou
     */
    private boolean isApproved() {
        return trade.getReviewStatus() == TradeResponse.STATUS_SUCCESSFUL;
    }

    /**
     * Dành cho màn hình TradeReview
     */
    private boolean isAccept() {
        return trade.getStatus() == TradeResponse.STATUS_ACCEPT;
    }

    private void displayPlayerViews() {
        PlayerView[] playerViews1st = new PlayerView[]{player11, player21};
        PlayerView[] playerViews2nd = new PlayerView[]{player12, player22};
        PlayerView[] playerViews3rd = new PlayerView[]{player13, player23};
        PlayerView[][] playerViews = new PlayerView[][]{playerViews1st, playerViews2nd, playerViews3rd};

        //reset views
        for (PlayerView[] playerView : playerViews) {
            displayPlayer(playerView[0], null);
            displayPlayer(playerView[1], null);
        }

        // trade items
        List<TradeDetailResponse> tradeItems = trade.getItems();
        for (TradeDetailResponse trade : tradeItems) {
            try {
                displayPlayer(playerViews[trade.getOrder() - 1][0], trade.getFromPlayer());
                displayPlayer(playerViews[trade.getOrder() - 1][1], trade.getToPlayer());
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayPlayer(PlayerView playerView, PlayerResponse player) {
        playerView.setTextColor(ContextCompat.getColor(mActivity, R.color.color_black));
        if (player != null) playerView.setPosition(player.getMainPosition());
        playerView.setPlayer(player);
        playerView.setAddable(false);
        playerView.setRemovable(false);
    }

    private void displayViewRejectApproved() {
        headerRejectApproved.setVisibility(View.VISIBLE);
        tvDeadline.setText(AppUtilities.getTimeFormatted(TextUtils.isEmpty(trade.getReviewDeadline()) ?
                trade.getDeadline() :
                trade.getReviewDeadline()));
        tvReject.setText(getString(R.string.rejected, trade.getTotalRejection()));
        int approved = trade.getTotalApproval() - 2 < 0 ? 0 : trade.getTotalApproval() - 2;
        tvApprove.setText(getString(R.string.approved, approved));
        progressBar.setMax(trade.getTotalRejection() + approved);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(trade.getTotalRejection(), true);
        } else {
            progressBar.setProgress(trade.getTotalRejection());
        }
    }

    private void displayViewTimeLeft() {
        Calendar deadline = DateTimeUtils.convertStringToCalendar(trade.getReviewDeadline() == null ?
                trade.getDeadline() : trade.getReviewDeadline(), Constant.FORMAT_DATE_TIME_SERVER);
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
                            decision(TradeResponse.STATUS_REJECT);
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
                            decision(TradeResponse.STATUS_ACCEPT);
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
        // bắn cho RequestFragment và TradeReview-LeagueDetail
        bus.send(new TradeEvent());

        mActivity.finish();
    }
}
