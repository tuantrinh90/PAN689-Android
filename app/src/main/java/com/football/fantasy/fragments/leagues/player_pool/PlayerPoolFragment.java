package com.football.fantasy.fragments.leagues.player_pool;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.PlayerPoolAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.customizes.searchs.SearchView;
import com.football.events.PlayerQueryEvent;
import com.football.events.TransferEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailForTransferFragment;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.display.DisplayConfigFragment;
import com.football.fantasy.fragments.leagues.player_pool.filter.FilterFragment;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.SeasonResponse;
import com.football.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_NONE;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_NONE_INFO;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_PICK;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_PICKED;
import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_DRAFT;
import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;

public class PlayerPoolFragment extends BaseMvpFragment<IPlayerPoolView, IPlayerPoolPresenter<IPlayerPoolView>> implements IPlayerPoolView {

    private static final String TAG = "PlayerPoolFragment";

    private static final String KEY_ACTION = "ACTION";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_HEADER_TITLE = "HEADER_TITLE";
    private static final String KEY_TRANSFER = "TRANSFER";
    private static final String KEY_PLAYER_TRANSFERS = "PLAYERS_TRANSFER";
    private static final String KEY_TEAM_ID = "TEAM_ID";
    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";
    private static final String KEY_SEASON_ID_TO_TRANSFER = "SEASON_ID_TO_TRANSFER";
    private static final String KEY_GAMEPLAY = "GAMEPLAY";
    private static final String KEY_PLAYER_ACTION = "PLAYER_ACTION";

    private static final int LEAGUE_ID_NONE = 0;
    private static final int SEASON_ID_NONE = 0;

    private static final int REQUEST_FILTER = 100;
    private static final int REQUEST_DISPLAY = 101;

    private static final int ACTION_NOTIFICATION_TRANSFER = 1001;
    private static final int ACTION_TRANSFERRING_MULTI_PLAYER = 1002;
    private static final int ACTION_TRANSFERRING_SINGLE_PLAYER = 1003;
    private static final int ACTION_ONLY_VIEW = 1004;

    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.tvSeason)
    ExtTextView tvSeason;
    @BindView(R.id.svSearch)
    SearchView svSearch;
    @BindView(R.id.rvPlayer)
    ExtRecyclerView<PlayerResponse> rvPlayer;
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

    private String title;
    private int teamId;
    private int leagueId;
    private String gameplay;
    private String playerAction;

    private int action;
    private String headerTitle;
    private ArrayList<Integer> playerIds;
    private PlayerResponse playerTransfer;
    private int seasonIdToTransfer;

    private int page = Constant.PAGE_START_INDEX;
    private String filterClubs = "";
    private String filterPositions = "";
    private int[] sorts = new int[]{Constant.SORT_NONE, Constant.SORT_NONE, Constant.SORT_NONE}; // -1: NONE, 0: desc, 1: asc
    private List<ExtKeyValuePair> displayPairs = new ArrayList<>();
    private ExtKeyValuePair selectedSeason;
    private List<SeasonResponse> seasons;
    private String query = "";

    // dành riêng cho transfer player
    public static void start(Fragment fragment, String title, String headerTitle,
                             ArrayList<Integer> playerIds, int seasonId, int teamId,
                             int leagueId, String gameplay) {
        AloneFragmentActivity.with(fragment)
                .parameters(PlayerPoolFragment.newBundle(ACTION_TRANSFERRING_MULTI_PLAYER,
                        title, headerTitle, playerIds, teamId, null, leagueId, seasonId, gameplay))
                .start(PlayerPoolFragment.class);
    }

    public static void start(Fragment fragment, String title, String headerTitle,
                             PlayerResponse transfer, int leagueId, String gameplay) {
        AloneFragmentActivity.with(fragment)
                .parameters(PlayerPoolFragment.newBundle(ACTION_TRANSFERRING_SINGLE_PLAYER,
                        title, headerTitle, null, -1, transfer, leagueId, -1, gameplay))
                .start(PlayerPoolFragment.class);
    }

    public static void start(Context context, String title) {
        AloneFragmentActivity.with(context)
                .parameters(PlayerPoolFragment.newBundle(ACTION_ONLY_VIEW,
                        title, "", null, -1, null, LEAGUE_ID_NONE, SEASON_ID_NONE, ""))
                .start(PlayerPoolFragment.class);
    }

    public static void startForNotification(Context context, String title, int leagueId,
                                            int teamId, String gameplay, String playerAction) {
        Bundle bundle = PlayerPoolFragment.newBundle(ACTION_NOTIFICATION_TRANSFER,
                title, context.getString(R.string.player_pool),
                null, teamId, null, leagueId,
                SEASON_ID_NONE, gameplay);
        bundle.putString(KEY_PLAYER_ACTION, playerAction);
        AloneFragmentActivity.with(context)
                .parameters(bundle)
                .start(PlayerPoolFragment.class);
    }

    private static Bundle newBundle(int action, String title, String headerTitle,
                                    ArrayList<Integer> playerTransfers, int teamId,
                                    PlayerResponse transfer, int leagueId, int seasonId,
                                    String gameplay) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ACTION, action);
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_HEADER_TITLE, headerTitle);
        bundle.putIntegerArrayList(KEY_PLAYER_TRANSFERS, playerTransfers);
        bundle.putInt(KEY_TEAM_ID, teamId);
        bundle.putSerializable(KEY_TRANSFER, transfer);
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        bundle.putInt(KEY_SEASON_ID_TO_TRANSFER, seasonId);
        bundle.putString(KEY_GAMEPLAY, gameplay);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.player_pool_fragment;
    }

    @Override
    public String getTitleString() {
        return title;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        initAdapter();
        displayDisplay();
        presenter.getSeasons();
        registerBus();
    }

    private void getDataFromBundle() {
        action = getArguments().getInt(KEY_ACTION);
        title = getArguments().getString(KEY_TITLE);
        headerTitle = getArguments().getString(KEY_HEADER_TITLE);
        playerIds = getArguments().getIntegerArrayList(KEY_PLAYER_TRANSFERS);
        teamId = getArguments().getInt(KEY_TEAM_ID);
        playerTransfer = (PlayerResponse) getArguments().getSerializable(KEY_TRANSFER);
        leagueId = getArguments().getInt(KEY_LEAGUE_ID);
        seasonIdToTransfer = getArguments().getInt(KEY_SEASON_ID_TO_TRANSFER);
        gameplay = getArguments().getString(KEY_GAMEPLAY);
        playerAction = getArguments().getString(KEY_PLAYER_ACTION);
    }

    private void registerBus() {
        // action add click onEvent PlayerList
        onEvent(PlayerQueryEvent.class, event -> {
            if (event.getFrom().equals(TAG)) {
                if (event.getTag() == PlayerQueryEvent.TAG_FILTER) {
                    filterClubs = event.getClub();
                    filterPositions = event.getPosition();

                    // get items
                    refresh();
                } else if (event.getTag() == PlayerQueryEvent.TAG_DISPLAY) {
                    displayPairs = event.getDisplays();

                    displayDisplay();
                }
            }
        });

        // action add click onEvent PlayerList
        onEvent(TransferEvent.class, event -> {
            if (event.getAction() == TransferEvent.RECEIVER) {
                showLoading(false);
                if (!TextUtils.isEmpty(event.getMessage())) {
                    showMessage(event.getMessage());
                } else {
                    mActivity.finish();
                }
            }
        });
    }

    private void displayDisplay() {
        ((PlayerPoolAdapter) rvPlayer.getAdapter()).setOptions(
                displayPairs.size() > 0 ? displayPairs.get(0).getKey() : "",
                displayPairs.size() > 1 ? displayPairs.get(1).getKey() : "",
                displayPairs.size() > 2 ? displayPairs.get(2).getKey() : "");
        if (displayPairs.size() > 0) {
            tvOption1.setText(displayPairs.get(0).getValue());
        }
        option1.setVisibility(displayPairs.size() > 0 ? View.VISIBLE : View.INVISIBLE);

        if (displayPairs.size() > 1) {
            tvOption2.setText(displayPairs.get(1).getValue());
        }
        option2.setVisibility(displayPairs.size() > 1 ? View.VISIBLE : View.INVISIBLE);

        if (displayPairs.size() > 2) {
            tvOption3.setText(displayPairs.get(2).getValue());
        }
        option3.setVisibility(displayPairs.size() > 2 ? View.VISIBLE : View.INVISIBLE);
        rvPlayer.notifyDataSetChanged();
    }

    void initView() {
        if (!TextUtils.isEmpty(headerTitle)) {
            tvHeader.setText(headerTitle);
        }

        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));

        svSearch.setSearchConsumer(query -> {
            this.query = query.trim();
            refresh();
        });

        // display default
        boolean isTransfer = !gameplay.equals(GAMEPLAY_OPTION_DRAFT);
        if (isTransfer)
            displayPairs.add(DisplayConfigFragment.getOptionDisplayDefault1(getAppContext()));
        displayPairs.add(DisplayConfigFragment.getOptionDisplayDefault2(getAppContext()));
        displayPairs.add(DisplayConfigFragment.getOptionDisplayDefault3(getAppContext()));
        if (!isTransfer)
            displayPairs.add(DisplayConfigFragment.getOptionDisplayDefault4(getAppContext()));

    }

    void initAdapter() {
        PlayerPoolAdapter adapter;
        adapter = new PlayerPoolAdapter(
                getContext(),
                player -> { // click event
                    if (action == ACTION_TRANSFERRING_SINGLE_PLAYER) {
                        PlayerDetailForTransferFragment.start(
                                this,
                                player,
                                playerTransfer,
                                -1,
                                getString(R.string.player_list),
                                player.getSelected() ? PICK_PICKED : PICK_PICK,
                                gameplay);
                    } else {
                        PlayerDetailFragment.start(
                                getContext(),
                                player.getId(),
                                -1,
                                getString(R.string.player_list),
                                gameplay.equals(GAMEPLAY_OPTION_TRANSFER) ? PICK_NONE_INFO : PICK_NONE,
                                gameplay);
                    }
                });

        // handle action
        if (action == ACTION_NOTIFICATION_TRANSFER) {
            adapter.setOptionAddCallback(player -> {
                presenter.transferPlayer(teamId, gameplay, 0, player.getId());
            });

        } else if (action == ACTION_TRANSFERRING_SINGLE_PLAYER) {
            // update positions
            filterPositions = String.valueOf(playerTransfer.getMainPosition());

            // bắn về cho MH Transferring Player
            adapter.setOptionAddCallback(player -> {
                showLoading(true);
                bus.send(new TransferEvent(playerTransfer, player));
            });

        } else if (action == ACTION_TRANSFERRING_MULTI_PLAYER) {
            adapter.setOptionAddCallback(player -> {
                showLoading(true);
                if (playerIds.size() > 0) {
                    // playerIds.get(0) vì playerIds được remove first item liên tục sau khi transfer thành công
                    presenter.transferPlayer(teamId, gameplay, 0, player.getId());
                }
            });
        }

        rvPlayer.adapter(adapter)
                .loadingLayout(0)
                .refreshListener(this::refresh)
                .loadMoreListener(() -> {
                    page++;
                    getPlayers();
                })
                .build();
    }

    private void refresh() {
        page = Constant.PAGE_START_INDEX;
        rvPlayer.clear();
        rvPlayer.startLoading();
        getPlayers();
    }

    private void getPlayers() {
        if (selectedSeason == null) {
            showMessage(getString(R.string.message_season_not_found));
            rvPlayer.startLoading();
        } else {
            presenter.getPlayers(
                    selectedSeason.getKey(),
                    leagueId,
                    seasonIdToTransfer,
                    playerTransfer,
                    filterPositions,
                    filterClubs,
                    displayPairs,
                    sorts,
                    page,
                    query,
                    playerAction);
        }
    }

    @NonNull
    @Override
    public IPlayerPoolPresenter<IPlayerPoolView> createPresenter() {
        return new PlayerPoolPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
            rvPlayer.stopLoading();
        }
    }

    @Override
    public void displayPlayers(List<PlayerResponse> players) {
        rvPlayer.addItems(players);
    }

    @Override
    public void displaySeasons(List<SeasonResponse> seasons) {
        this.seasons = seasons;
        selectedSeason = new ExtKeyValuePair(String.valueOf(seasons.get(0).getId()), seasons.get(0).getName());
        updateValue();

        rvPlayer.startLoading();
        getPlayers();
    }

    @Override
    public void handleTransferSuccess(int deletedPlayerId) {
        refresh();
        if (playerIds != null) {
            playerIds.remove(0);
            if (playerIds.size() == 0) {
                showMessage(getString(R.string.transfer_success), R.string.ok, aVoid -> {
                    mActivity.finish();
                });
            }
        } else {
            showMessage(getString(R.string.transfer_success), R.string.ok, aVoid -> {
                mActivity.finish();
            });
        }
    }

    @OnClick({R.id.season, R.id.filter, R.id.display, R.id.option1, R.id.option2, R.id.option3})
    public void onSortClicked(View view) {
        switch (view.getId()) {
            case R.id.season:
                displaySelectDialog();
                break;
            case R.id.filter:
                AloneFragmentActivity.with(this)
                        .forResult(REQUEST_FILTER)
                        .parameters(FilterFragment.newBundle(TAG, filterPositions, filterClubs, leagueId != LEAGUE_ID_NONE))
                        .start(FilterFragment.class);
                break;
            case R.id.display:
                StringBuilder displays = new StringBuilder();
                for (ExtKeyValuePair pair : displayPairs) {
                    displays.append(pair.getKey()).append(",");
                }
                AloneFragmentActivity.with(this)
                        .forResult(REQUEST_DISPLAY)
                        .parameters(DisplayConfigFragment.newBundle(!gameplay.equals(GAMEPLAY_OPTION_DRAFT), TAG, displays.toString()))
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
        page = 1;
        rvPlayer.startLoading();
        rvPlayer.clear();
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
        getPlayers();
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

    private void displaySelectDialog() {
        if (seasons == null || seasons.isEmpty()) {
            showMessage(getString(R.string.message_season_not_found));
        } else {
            List<ExtKeyValuePair> valuePairs = new ArrayList<>();
            for (SeasonResponse season : seasons) {
                valuePairs.add(new ExtKeyValuePair(String.valueOf(season.getId()), season.getName()));
            }

            ExtKeyValuePairDialogFragment.newInstance()
                    .title(getString(R.string.select_season))
                    .setExtKeyValuePairs(valuePairs)
                    .setValue(selectedSeason == null ? valuePairs.get(0).getKey() : selectedSeason.getKey())
                    .setOnSelectedConsumer(extKeyValuePair -> {
                        if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                            selectedSeason = extKeyValuePair;
                            updateValue();

                            // update add button: nếu là current season thì mới cho hiện addButton
                            boolean currentSeason = selectedSeason.getKey().equals(String.valueOf(seasons.get(0).getId()));
                            ((PlayerPoolAdapter) rvPlayer.getAdapter()).setVisibleAddButton(currentSeason);

                            // refresh thì ko cần notifyDataSetChanged
                            refresh();

                        }
                    }).show(getChildFragmentManager(), null);
        }
    }

    private void updateValue() {
        tvSeason.setText(selectedSeason.getValue());
    }

}
