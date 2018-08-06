package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.lineup.PlayerView;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_team_squad.ProposalTeamSquadFragment;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TradeResponse;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class ProposalFragment extends BaseMvpFragment<IProposalView, IProposalPresenter<IProposalView>> implements IProposalView {

    private static final String KEY_FROM_TEAM_ID = "TEAM";
    private static final String KEY_TO_TEAM_ID = "TO_TEAM";

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
    @BindViews({R.id.player11, R.id.player12, R.id.player13, R.id.player21, R.id.player22, R.id.player23})
    PlayerView[] playerViews;
    @BindView(R.id.buttonMakeProposal)
    ExtTextView buttonMakeProposal;

    private int fromTeamId;
    private int toTeamId;

    public static void start(Context context, int fromTeam, int toTeam) {
        AloneFragmentActivity.with(context)
                .parameters(ProposalFragment.newBundle(fromTeam, toTeam))
                .start(ProposalFragment.class);
    }

    private static Bundle newBundle(int fromTeam, int toTeam) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_FROM_TEAM_ID, fromTeam);
        bundle.putInt(KEY_TO_TEAM_ID, toTeam);
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

    private void initData() {
        // action add click on PlayerList
        mCompositeDisposable.add(bus.ofType(PlayerEvent.class)
                .subscribeWith(new DisposableObserver<PlayerEvent>() {
                    @Override
                    public void onNext(PlayerEvent event) {
                        if (event.getAction() == PlayerEvent.ACTION_ADD_CLICK) {
                            PlayerResponse player = event.getData();
                            playerViews[event.getPosition()].setPlayer(player);

                            setEnableMakeProposalButton(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    private void initView() {
        setEnableMakeProposalButton(false);

        for (PlayerView player : playerViews) {
            player.setPlayer(null);
            player.setAddable(true);
            player.setRemovable(true);
            player.setOnPlayerViewClickListener(new PlayerView.OnPlayerViewClickListener() {
                @Override
                public void onRemove(PlayerView view, PlayerResponse player, int position, int order) {
                    ProposalFragment.this.onRemove(view);
                }

                @Override
                public void onAddPlayer(PlayerView view, int position, int order) {
                    ProposalFragment.this.onAddPlayer(view);
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

    private void setEnableMakeProposalButton(boolean enable) {
        buttonMakeProposal.setEnabled(enable);
        buttonMakeProposal.setBackgroundResource(enable ? R.drawable.bg_button_yellow : R.drawable.bg_button_white);
    }

    private void onAddPlayer(PlayerView view) {
        switch (view.getId()) {
            case R.id.player11:
                ProposalTeamSquadFragment.start(getContext(), fromTeamId, MY_TEAM_INDEX_1);
                break;
            case R.id.player12:
                ProposalTeamSquadFragment.start(getContext(), fromTeamId, MY_TEAM_INDEX_2);
                break;
            case R.id.player13:
                ProposalTeamSquadFragment.start(getContext(), fromTeamId, MY_TEAM_INDEX_3);
                break;
            case R.id.player21:
                ProposalTeamSquadFragment.start(getContext(), toTeamId, OTHER_TEAM_INDEX_1);
                break;
            case R.id.player22:
                ProposalTeamSquadFragment.start(getContext(), toTeamId, OTHER_TEAM_INDEX_2);
                break;
            case R.id.player23:
                ProposalTeamSquadFragment.start(getContext(), toTeamId, OTHER_TEAM_INDEX_3);
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
                    player.setPlayer(null);

                    boolean hasView = false;
                    for (PlayerView playerView : playerViews) {
                        if (playerView.getPlayer() != null) {
                            hasView = true;
                            break;
                        }
                    }
                    setEnableMakeProposalButton(hasView);
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

                for (int i = 0; i < 3; i++) {
                    if (playerViews[i].getPlayer() != null) {
                        fromPlayerIds[i] = playerViews[i].getPlayer().getId();
                    }
                    if (playerViews[i + 3].getPlayer() != null) {
                        toPlayerIds[i] = playerViews[i + 3].getPlayer().getId();
                    }
                }

                presenter.makeProposal(fromTeamId, toTeamId, fromPlayerIds, toPlayerIds);
                break;
        }
    }

    @Override
    public void submitSuccess(TradeResponse response) {

    }
}
