package com.football.fantasy.fragments.leagues.your_team.players_popup;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.football.adapters.PlayerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.customizes.searchs.SearchView;
import com.football.events.PickEvent;
import com.football.events.PlayerEvent;
import com.football.events.PlayerQueryEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailForLineupFragment;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.filter.PlayerPoolFilterFragment;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

public class PlayerPopupFragment extends BaseMvpFragment<IPlayerPopupView, IPlayerPopupPresenter<IPlayerPopupView>> implements IPlayerPopupView {

    private static final String TAG = "PlayerPopupFragment";

    private static final String KEY_POSITION = "POSITION";
    private static final String KEY_ORDER = "ORDER";
    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";

    @BindView(R.id.svSearch)
    SearchView svSearchView;
    @BindView(R.id.ivSortValue)
    ImageView ivSortValue;
    @BindView(R.id.rvPlayer)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    PlayerAdapter playerAdapter;

    private int leagueId = 0;
    private int page = 1;
    private String query = "";
    private Integer mainPosition = null;
    private int order;

    private int valueDirection = Constant.SORT_NONE;
    private String filterClubs = "";

    public static PlayerPopupFragment newInstance() {
        return new PlayerPopupFragment();
    }

    public static Bundle newBundle(int position, Integer order, int leagueId) {
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_ORDER, order);
        args.putInt(KEY_LEAGUE_ID, leagueId);
        return args;
    }

    @Override
    public int getResourceId() {
        return R.layout.player_popup_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        initView();
        getPlayers();
        registerBus();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            order = bundle.getInt(KEY_ORDER);
            leagueId = bundle.getInt(KEY_LEAGUE_ID);
            mainPosition = bundle.getInt(KEY_POSITION);
        }
    }

    private void registerBus() {
        // action add click on PlayerList
        mCompositeDisposable.add(bus.ofType(PlayerQueryEvent.class)
                .subscribeWith(new DisposableObserver<PlayerQueryEvent>() {
                    @Override
                    public void onNext(PlayerQueryEvent event) {
                        if (event.getFrom().equals(TAG)) {
                            if (event.getTag() == PlayerQueryEvent.TAG_FILTER) {
                                filterClubs = event.getClub();

                                refresh();
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

        // pick Player
        mCompositeDisposable.add(bus.ofType(PickEvent.class)
                .subscribeWith(new DisposableObserver<PickEvent>() {
                    @Override
                    public void onNext(PickEvent event) {
                        mActivity.finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    // bắn sang màn hình LineUp
    private void sendToLineup(PlayerResponse player) {
        showLoading(true);
        bus.send(new PlayerEvent.Builder()
                .action(PlayerEvent.ACTION_ADD_CLICK)
                .position(mainPosition)
                .order(order)
                .data(player)
                .callback((success, message) -> {
                    showLoading(false);
                    if (success) {
                        // bắn về cho PlayerList để cập nhật trạng thái đã pick
                        mCompositeDisposable.dispose();
                        bus.send(new PickEvent(PickEvent.ACTION_PICK, player.getId()));

                        mActivity.finish();
                    } else {
                        showMessage(message);
                    }
                })
                .build());
    }

    @NonNull
    @Override
    public IPlayerPopupPresenter<IPlayerPopupView> createPresenter() {
        return new PlayerPopupDataPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.lineup;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    void initView() {
        try {
            // search view
            svSearchView.getFilter().setImageResource(R.drawable.ic_filter_list_black_24_px);
            svSearchView.setFilerConsumer(aVoid -> {
                AloneFragmentActivity.with(this)
                        .parameters(PlayerPoolFilterFragment.newBundle(TAG, String.valueOf(mainPosition), filterClubs, true))
                        .start(PlayerPoolFilterFragment.class);
            });

            // update hint
            svSearchView.getSearchView().setHint(R.string.search_public_players);

            // search view
            svSearchView.setSearchConsumer(query -> onPerformSearch(query.trim()));

            // playerResponses
            playerAdapter = new PlayerAdapter(
                    getContext(),
                    player -> { // item click
                        PlayerDetailForLineupFragment.start(this,
                                player,
                                -1,
                                getString(R.string.player_list),
                                player.getSelected() ? PlayerDetailFragment.PICK_PICKED : PlayerDetailFragment.PICK_PICK,
                                mainPosition,
                                order);
                    },
                    (player, position) -> { // add click
                        sendToLineup(player);
                    });
            rvPlayer.adapter(playerAdapter)
                    .loadMoreListener(() -> {
                        page++;
                        getPlayers();
                    })
                    .refreshListener(() -> {
                        page = 1;
                        rvPlayer.clear();
                        rvPlayer.startLoading();
                        getPlayers();
                    })
                    .build();

            // load data
            rvPlayer.startLoading();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    void onPerformSearch(String q) {
        query = q;
        rvPlayer.clear();
        rvPlayer.startLoading();
        page = 1;
        getPlayers();
    }

    private void getPlayers() {
        presenter.getPlayers(leagueId, valueDirection, page, query, mainPosition, filterClubs);
    }

    @OnClick(R.id.sortValue)
    public void onSortClick() {
        toggleSort();
        ivSortValue.setImageResource(getArrowResource(valueDirection));
    }

    private void toggleSort() {
        page = 1;
        rvPlayer.clear();
        rvPlayer.startLoading();
        switch (valueDirection) {
            case Constant.SORT_NONE:
                valueDirection = Constant.SORT_DESC;
                break;
            case Constant.SORT_DESC:
                valueDirection = Constant.SORT_ASC;
                break;
            case Constant.SORT_ASC:
                valueDirection = Constant.SORT_DESC;
                break;
        }

        refresh();
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

    private void refresh() {
        page = 1;
        rvPlayer.clear();
        rvPlayer.startLoading();
        getPlayers();
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
            rvPlayer.stopLoading();
        }
    }

    @Override
    public void displayPlayers(List<PlayerResponse> data) {
        rvPlayer.addItems(data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
