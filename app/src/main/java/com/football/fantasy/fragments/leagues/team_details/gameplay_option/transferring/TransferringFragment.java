package com.football.fantasy.fragments.leagues.team_details.gameplay_option.transferring;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.textview.ExtTextView;
import com.bon.util.DialogUtils;
import com.football.adapters.InjuredPlayerAdapter;
import com.football.adapters.PlayerPoolAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.customizes.textview.ExtTextViewCountdown;
import com.football.events.PlayerQueryEvent;
import com.football.events.TransferEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.PlayerPoolFragment;
import com.football.fantasy.fragments.leagues.player_pool.display.DisplayConfigFragment;
import com.football.fantasy.fragments.leagues.player_pool.filter.FilterFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;
import static com.football.models.responses.PlayerResponse.Options.ASSISTS;
import static com.football.models.responses.PlayerResponse.Options.GOALS;
import static com.football.models.responses.PlayerResponse.Options.POINT;
import static com.football.models.responses.PlayerResponse.Options.VALUE;

public class TransferringFragment extends BaseMvpFragment<ITransferringView, ITransferringPresenter<ITransferringView>> implements ITransferringView {

    private static final String TAG = "TransferringFragment";

    public static final String ACTION_ONLY_REMOVE = "remove";

    private static final String KEY_ACTION = "ACTION";
    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_LEAGUE = "LEAGUE_ID";
    private static final String KEY_NUMBER_PLAYER_TO_REMOVE = "NUMBER_PLAYER_TO_REMOVE";

    private static final int REQUEST_FILTER = 100;
    private static final int REQUEST_DISPLAY = 101;

    @BindView(R.id.rvPlayer)
    ExtRecyclerView<PlayerResponse> rvPlayer;
    @BindView(R.id.rv_injured)
    ExtRecyclerView<PlayerResponse> rvInjured;
    @BindView(R.id.tvOption1)
    ExtTextView tvOption1;
    @BindView(R.id.tvOption2)
    ExtTextView tvOption2;
    @BindView(R.id.tvOption3)
    ExtTextView tvOption3;
    @BindView(R.id.ivSort1)
    ImageView ivSort1;
    @BindView(R.id.ivSort2)
    ImageView ivSort2;
    @BindView(R.id.ivSort3)
    ImageView ivSort3;
    @BindView(R.id.option1)
    LinearLayout option1;
    @BindView(R.id.option2)
    LinearLayout option2;
    @BindView(R.id.option3)
    LinearLayout option3;

    @BindView(R.id.relativeLayout2)
    View budgetView;

    @BindView(R.id.tvTransferringPlayerLeft)
    ExtTextView tvTransferringPlayerLeft;
    @BindView(R.id.tvTransferringTimeLeft)
    ExtTextView tvTransferringTimeLeft;

    @BindView(R.id.tvTransferringPlayerLeftValue)
    ExtTextView tvTransferringPlayerLeftValue;
    @BindView(R.id.tvTransferringTimeLeftValue)
    ExtTextViewCountdown tvTransferringTimeLeftValue;
    @BindView(R.id.tvBudgetValue)
    ExtTextView tvBudgetValue;

    private String action;
    private TeamResponse team;
    private LeagueResponse league;
    private int numberPlayerToRemove;

    private int transferLeft;

    private String filterClubs = "";
    private String filterPositions = "";
    private int[] sorts = new int[]{Constant.SORT_NONE, Constant.SORT_NONE, Constant.SORT_NONE}; // -1: NONE, 0: desc, 1: asc
    private List<ExtKeyValuePair> displays = new ArrayList<>();

    public static TransferringFragment newInstance(TeamResponse team, LeagueResponse league, String action, int numberPlayerToRemove) {
        TransferringFragment fragment = new TransferringFragment();
        Bundle bundle = newBundle(action, team, league, numberPlayerToRemove);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static Bundle newBundle(String action, TeamResponse team, LeagueResponse league, int numberPlayerToRemove) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ACTION, action);
        bundle.putSerializable(KEY_TEAM, team);
        bundle.putSerializable(KEY_LEAGUE, league);
        bundle.putInt(KEY_NUMBER_PLAYER_TO_REMOVE, numberPlayerToRemove);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_transferring_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        registerBus();
        initView();

        showLoading(true);
        refreshData();
    }

    @Override
    public void onDestroyView() {
        tvTransferringTimeLeftValue.onDestroyView();
        super.onDestroyView();
    }

    private void getDataFromBundle() {
        action = getArguments().getString(KEY_ACTION);
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        numberPlayerToRemove = getArguments().getInt(KEY_NUMBER_PLAYER_TO_REMOVE);
    }

    @NonNull
    @Override
    public ITransferringPresenter<ITransferringView> createPresenter() {
        return new TransferringPresenter(getAppComponent());
    }

    private void registerBus() {
        // action add click onEvent PlayerList
        onEvent(PlayerQueryEvent.class, event -> {
            if (event.getFrom().equals(TAG)) {
                if (event.getTag() == PlayerQueryEvent.TAG_FILTER) {
                    filterClubs = event.getClub();
                    filterPositions = event.getPosition();

                    // get items
                    refreshData();
                } else if (event.getTag() == PlayerQueryEvent.TAG_DISPLAY) {
                    displays = event.getDisplays();

                    displayDisplay();
                }
            }
        });

        // action add click onEvent PlayerPool
        onEvent(TransferEvent.class, event -> {
            if (event.getAction() == TransferEvent.SENDER) {
                presenter.transferPlayer(team.getId(),
                        league.getGameplayOption(),
                        event.getFromPlayer().getId(),
                        event.getToPlayer().getId());
            }
        });
    }

    private void initView() {
        boolean isTransfer = league.equalsGameplay(GAMEPLAY_OPTION_TRANSFER);
        if (isTransfer) {
            budgetView.setVisibility(View.VISIBLE);
            tvTransferringPlayerLeft.setText(R.string.transferring_player_left);
            tvTransferringTimeLeft.setText(R.string.transferring_time_left);
        } else {
            tvTransferringPlayerLeft.setMaxEms(999);
            tvTransferringTimeLeft.setMaxEms(999);
            tvTransferringPlayerLeft.setLines(1);
            tvTransferringTimeLeft.setLines(1);
            budgetView.setVisibility(View.GONE);
            tvTransferringPlayerLeft.setText(R.string.waiving_player_left);
            tvTransferringTimeLeft.setText(R.string.waiving_time_left);
        }

        // display default
        if (isTransfer) displays.add(DisplayConfigFragment.OPTION_DISPLAY_DEFAULT_1);
        displays.add(DisplayConfigFragment.OPTION_DISPLAY_DEFAULT_2);
        displays.add(DisplayConfigFragment.OPTION_DISPLAY_DEFAULT_3);
        if (!isTransfer) displays.add(DisplayConfigFragment.OPTION_DISPLAY_DEFAULT_4);

        PlayerPoolAdapter adapter;
        adapter = new PlayerPoolAdapter(
                getContext(),
                player -> { // click event
                    PlayerDetailFragment.start(getContext(),
                            player.getId(),
                            -1,
                            getString(R.string.transferring_player),
                            PlayerDetailFragment.PICK_NONE_INFO,
                            league.getGameplayOption());
                });
        // remove click
        adapter.setOptionDeleteCallback(this::transferPlayer);
        if (isTransfer) {
            adapter.setOptions(VALUE, POINT, GOALS);
        } else {
            adapter.setOptions(POINT, GOALS, ASSISTS);
        }

        rvPlayer.adapter(adapter)
                .refreshListener(this::refreshData)
                .build();

        // injured
        // remove click
        InjuredPlayerAdapter injuredPlayerAdapter = new InjuredPlayerAdapter(
                getContext(),
                this::transferPlayer);
        rvInjured
                .adapter(injuredPlayerAdapter)
                .layoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false))
                .noDataMessage("")
                .build();

        displayDisplay();
    }

    private void displayDisplay() {
        ((PlayerPoolAdapter) rvPlayer.getAdapter()).setOptions(
                displays.size() > 0 ? displays.get(0).getKey() : "",
                displays.size() > 1 ? displays.get(1).getKey() : "",
                displays.size() > 2 ? displays.get(2).getKey() : "");
        if (displays.size() > 0) {
            tvOption1.setText(displays.get(0).getValue());
        }
        option1.setVisibility(displays.size() > 0 ? View.VISIBLE : View.INVISIBLE);

        if (displays.size() > 1) {
            tvOption2.setText(displays.get(1).getValue());
        }
        option2.setVisibility(displays.size() > 1 ? View.VISIBLE : View.INVISIBLE);

        if (displays.size() > 2) {
            tvOption3.setText(displays.get(2).getValue());
        }
        option3.setVisibility(displays.size() > 2 ? View.VISIBLE : View.INVISIBLE);
        rvPlayer.notifyDataSetChanged();
    }

    private void refreshData() {
        rvPlayer.clear();
        rvPlayer.startLoading();
        rvInjured.clear();
        getTeamPlayers();
    }

    private void getTeamPlayers() {
        presenter.getTeamTransferring(team.getId(), league.getGameplayOption(), filterPositions, filterClubs, displays, sorts);
    }

    private void transferPlayer(PlayerResponse player) {
        if (action.equals(ACTION_ONLY_REMOVE)) {
            presenter.transferPlayer(team.getId(), league.getGameplayOption(), player.getId(), 0);
        }
        // append PlayerPool
        else if (tvTransferringTimeLeftValue.isRunning()) {
            if (transferLeft > 0 || player.getInjured()) {
                PlayerPoolFragment.start(
                        this,
                        getString(R.string.transferring_player),
                        getString(R.string.player_pool),
                        player,
                        league.getId(),
                        league.getGameplayOption());
            } else {
                showMessage(getString(R.string.transferring_player_left_less_than_1));
            }
        } else {
            DialogUtils.messageBox(mActivity,
                    0,
                    getString(R.string.app_name),
                    getString(R.string.message_transferring_player_left_is_0),
                    getString(R.string.ok),
                    (dialog, which) -> {
                    });
        }
    }

    @OnClick({R.id.filter, R.id.display, R.id.option1, R.id.option2, R.id.option3})
    public void onSortClicked(View view) {
        switch (view.getId()) {
            case R.id.filter:
                AloneFragmentActivity.with(this)
                        .forResult(REQUEST_FILTER)
                        .parameters(FilterFragment.newBundle(TAG, filterPositions, filterClubs, false))
                        .start(FilterFragment.class);
                break;
            case R.id.display:
                StringBuilder displays = new StringBuilder();
                for (ExtKeyValuePair pair : this.displays) {
                    displays.append(pair.getKey()).append(",");
                }
                AloneFragmentActivity.with(this)
                        .forResult(REQUEST_DISPLAY)
                        .parameters(DisplayConfigFragment.newBundle(league.equalsGameplay(GAMEPLAY_OPTION_TRANSFER), TAG, displays.toString()))
                        .start(DisplayConfigFragment.class);
                break;

            case R.id.option1:
                toggleSort(0);
                ivSort1.setImageResource(getArrowResource(sorts[0]));
                clearState(ivSort2, 1);
                clearState(ivSort3, 2);
                break;

            case R.id.option2:
                toggleSort(1);
                ivSort2.setImageResource(getArrowResource(sorts[1]));
                clearState(ivSort1, 0);
                clearState(ivSort3, 2);
                break;

            case R.id.option3:
                toggleSort(2);
                ivSort3.setImageResource(getArrowResource(sorts[2]));
                clearState(ivSort1, 0);
                clearState(ivSort2, 1);
                break;
        }
    }

    private void toggleSort(int index) {
        rvPlayer.startLoading();
        rvPlayer.clear();
        rvInjured.clear();
        switch (sorts[index]) {
            case Constant.SORT_NONE:
                sorts[index] = Constant.SORT_DESC;
                break;
            case Constant.SORT_DESC:
                sorts[index] = Constant.SORT_ASC;
                break;
            case Constant.SORT_ASC:
                sorts[index] = Constant.SORT_DESC;
                break;
        }
        getTeamPlayers();
    }

    private int getArrowResource(int state) {
        switch (state) {
            case Constant.SORT_NONE:
                return R.drawable.ic_sort_none;

            case Constant.SORT_DESC:
                return R.drawable.ic_sort_desc;

            case Constant.SORT_ASC:
                return R.drawable.ic_sort_asc;
        }
        return R.drawable.ic_sort_none;
    }

    private void clearState(ImageView imageView, int index) {
        imageView.setImageResource(R.drawable.ic_sort_none);
        sorts[index] = Constant.SORT_NONE;
    }

    @OnClick({R.id.ivMoreTransferringPlayerLeft, R.id.ivMoreTransferringTimeLeft, R.id.ivMoreBudget})
    public void oInfoClicked(View view) {
        boolean isTransfer = league.equalsGameplay(GAMEPLAY_OPTION_TRANSFER);

        switch (view.getId()) {
            case R.id.ivMoreTransferringPlayerLeft:
                showMessage(getString(isTransfer ? R.string.message_info_transferring_player_left : R.string.message_info_waving_player_left));
                break;
            case R.id.ivMoreTransferringTimeLeft:
                showMessage(getString(isTransfer ? R.string.message_info_transferring_time_left : R.string.message_info_waving_time_left));
                break;
            case R.id.ivMoreBudget:
                showMessage(getString(R.string.message_info_budget));
                break;
        }
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
            rvInjured.stopLoading();
            rvPlayer.stopLoading();
        }
    }

    @Override
    public void displayPlayers(List<PlayerResponse> players) {
        rvPlayer.addItems(players);
    }

    @Override
    public void displayInjuredPlayers(List<PlayerResponse> injuredPlayers) {
        rvInjured.addItems(injuredPlayers);
    }

    @Override
    public void displayHeader(boolean canTransfer, int current, int max, long transferTimeLeft, long budget) {
        transferLeft = max - current;
        tvTransferringPlayerLeftValue.setText(transferLeft + "/" + max);
        tvTransferringTimeLeftValue.setTime(transferTimeLeft);
        tvTransferringTimeLeftValue.start();
        tvBudgetValue.setText(getString(R.string.money_prefix, AppUtilities.getMoney(budget)));
    }

    @Override
    public void transferSuccess() {
        if (action.equals(ACTION_ONLY_REMOVE)) {
            numberPlayerToRemove--;
            if (numberPlayerToRemove <= 0) {
                showMessage(
                        getString(R.string.message_remove_player_success),
                        R.string.ok,
                        aVoid -> {
                            mActivity.finish();
                        });
            }
        }
        bus.send(new TransferEvent(TransferEvent.RECEIVER, ""));
        refreshData();
    }

    @Override
    public void transferError(String error) {
        bus.send(new TransferEvent(TransferEvent.RECEIVER, error));
    }
}
