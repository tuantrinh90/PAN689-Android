package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.lineup.PlayerView;
import com.football.events.PlayerEvent;
import com.football.events.TradeEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_team_squad.ProposalTeamSquadFragment;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TradeResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class TradeProposalFragment extends BaseMvpFragment<ITradeProposalView, ITradeProposalPresenter<ITradeProposalView>> implements ITradeProposalView {

    private static final String KEY_FROM_TEAM_ID = "TEAM";
    private static final String KEY_TO_TEAM_ID = "TO_TEAM";
    private static final String KEY_FROM_TEAM_NAME = "FROM_TEAM_NAME";
    private static final String KEY_TO_TEAM_NAME = "TO_TEAM_NAME";

    private static final int MY_TEAM_INDEX_1 = 0;
    private static final int MY_TEAM_INDEX_2 = 1;
    private static final int MY_TEAM_INDEX_3 = 2;
    private static final int OTHER_TEAM_INDEX_1 = 3;
    private static final int OTHER_TEAM_INDEX_2 = 4;
    private static final int OTHER_TEAM_INDEX_3 = 5;

    @BindView(R.id.tvCancel)
    ExtTextView tvCancel;
    @BindView(R.id.tvTitleTeam1)
    ExtTextView tvTitleTeam1;
    @BindView(R.id.tvTitleTeam2)
    ExtTextView tvTitleTeam2;
    @BindViews({R.id.player11, R.id.player12, R.id.player13, R.id.player21, R.id.player22, R.id.player23})
    PlayerView[] playerViews;
    @BindView(R.id.buttonMakeProposal)
    ExtTextView buttonMakeProposal;

    private int fromTeamId;
    private int toTeamId;
    private String fromTeamName;
    private String toTeamName;

    public static void start(Context context, int fromTeam, String fromTeamName, int toTeam, String toTeamName) {
        AloneFragmentActivity.with(context)
                .parameters(TradeProposalFragment.newBundle(fromTeam, fromTeamName, toTeam, toTeamName))
                .start(TradeProposalFragment.class);
    }

    private static Bundle newBundle(int fromTeam, String fromTeamName, int toTeam, String toTeamName) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FROM_TEAM_ID, fromTeam);
        bundle.putString(KEY_FROM_TEAM_NAME, fromTeamName);
        bundle.putInt(KEY_TO_TEAM_ID, toTeam);
        bundle.putString(KEY_TO_TEAM_NAME, toTeamName);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_team_details_gameplay_option_proposal_fragment;
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
        fromTeamId = getArguments().getInt(KEY_FROM_TEAM_ID);
        toTeamId = getArguments().getInt(KEY_TO_TEAM_ID);
        fromTeamName = getArguments().getString(KEY_FROM_TEAM_NAME);
        toTeamName = getArguments().getString(KEY_TO_TEAM_NAME);
    }

    @NonNull
    @Override
    public ITradeProposalPresenter<ITradeProposalView> createPresenter() {
        return new TradeProposalPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }

    private void initData() {
        // action add click onEvent PlayerList
        onEvent(PlayerEvent.class, event -> {
            if (event.getAction() == PlayerEvent.ACTION_ADD_CLICK) {
                PlayerResponse player = event.getData();
                playerViews[event.getPosition()].setPosition(player.getMainPosition());
                playerViews[event.getPosition()].setPlayer(player);

                setEnableMakeProposalButton();
            }
        });
    }

    private void initView() {
        tvTitleTeam1.setText(fromTeamName);
        tvTitleTeam2.setText(toTeamName);

        setEnableMakeProposalButton();

        for (PlayerView player : playerViews) {
            player.setTextColor(ContextCompat.getColor(mActivity, R.color.color_black));
            player.setPlayer(null);
            player.setAddable(true);
            player.setRemovable(true);
            player.setOnPlayerViewClickListener(new PlayerView.OnPlayerViewClickListener() {
                @Override
                public void onRemove(PlayerView view, PlayerResponse player, int position, int order) {
                    TradeProposalFragment.this.onRemove(view);
                }

                @Override
                public void onAddPlayer(PlayerView view, int position, int order) {
                    TradeProposalFragment.this.onAddPlayer(view);
                }

                @Override
                public void onClickPlayer(PlayerView view, PlayerResponse player, int position, int order) {

                }

                @Override
                public void onEdit(PlayerView view, PlayerResponse player, int position, int order) {

                }
            });
        }
    }

    private void setEnableMakeProposalButton() {
        boolean hasLeftPlayer = false;
        boolean hasRightPlayer = false;

        for (int i = 0; i < playerViews.length; i++) {
            PlayerView playerView = playerViews[i];
            if (playerView.getPlayer() != null) {
                if (i == MY_TEAM_INDEX_1 || i == MY_TEAM_INDEX_2 || i == MY_TEAM_INDEX_3) {
                    hasLeftPlayer = true;
                } else if (i == OTHER_TEAM_INDEX_1 || i == OTHER_TEAM_INDEX_2 || i == OTHER_TEAM_INDEX_3) {
                    hasRightPlayer = true;
                }
            }
        }

        boolean enable = hasLeftPlayer && hasRightPlayer;
        buttonMakeProposal.setEnabled(enable);
        buttonMakeProposal.setBackgroundResource(enable ? R.drawable.bg_button_yellow : R.drawable.bg_button_white);
    }

    private void onAddPlayer(PlayerView view) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (PlayerView playerView : playerViews) {
            if (playerView.getPlayer() != null) {
                ids.add(playerView.getPlayer().getId());
            }
        }
        switch (view.getId()) {
            case R.id.player11:
                ProposalTeamSquadFragment.start(getContext(), fromTeamId, fromTeamName, MY_TEAM_INDEX_1, ids);
                break;
            case R.id.player12:
                ProposalTeamSquadFragment.start(getContext(), fromTeamId, fromTeamName, MY_TEAM_INDEX_2, ids);
                break;
            case R.id.player13:
                ProposalTeamSquadFragment.start(getContext(), fromTeamId, fromTeamName, MY_TEAM_INDEX_3, ids);
                break;
            case R.id.player21:
                ProposalTeamSquadFragment.start(getContext(), toTeamId, toTeamName, OTHER_TEAM_INDEX_1, ids);
                break;
            case R.id.player22:
                ProposalTeamSquadFragment.start(getContext(), toTeamId, toTeamName, OTHER_TEAM_INDEX_2, ids);
                break;
            case R.id.player23:
                ProposalTeamSquadFragment.start(getContext(), toTeamId, toTeamName, OTHER_TEAM_INDEX_3, ids);
                break;
        }
    }

    private void onRemove(PlayerView player) {
        DialogUtils.messageBox(mActivity,
                0,
                getString(R.string.app_name),
                getString(R.string.message_confirm_remove_lineup_player),
                getString(R.string.ok),
                getString(R.string.cancel),
                (dialog, which) -> {
                    player.setPosition(PlayerResponse.POSITION_NONE);
                    player.setPlayer(null);

                    setEnableMakeProposalButton();
                },
                (dialog, which) -> {
                });
    }

    @OnClick({R.id.tvCancel})
    public void onPlayerClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                mActivity.finish();
                break;
        }
    }

    @OnClick({R.id.tvCancel, R.id.buttonMakeProposal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                mActivity.finish();
                break;
            case R.id.buttonMakeProposal:
                int[] fromPlayerIds = new int[3];
                int[] toPlayerIds = new int[3];

                // đếm số lượng cầu thủ
                int fromCounter = 0;
                int toCounter = 0;
                for (int i = 0; i < 3; i++) {
                    if (playerViews[i].getPlayer() != null) {
                        fromPlayerIds[i] = playerViews[i].getPlayer().getId();
                        fromCounter++;
                    }
                    if (playerViews[i + 3].getPlayer() != null) {
                        toPlayerIds[i] = playerViews[i + 3].getPlayer().getId();
                        toCounter++;
                    }
                }

                if (fromCounter < toCounter) {
                    showMessage(
                            R.string.message_trade_team_more_than_18_players,
                            R.string.ok,
                            R.string.cancel,
                            aVoid -> {
                                makeProposal(fromPlayerIds, toPlayerIds);
                            }, null);
                } else {
                    makeProposal(fromPlayerIds, toPlayerIds);
                }
                break;
        }
    }

    private void makeProposal(int[] fromPlayerIds, int[] toPlayerIds) {
        presenter.makeProposal(fromTeamId, toTeamId, fromPlayerIds, toPlayerIds);
    }

    @Override
    public void submitSuccess(TradeResponse response) {
        bus.send(new TradeEvent());
        mActivity.finish();
    }
}
