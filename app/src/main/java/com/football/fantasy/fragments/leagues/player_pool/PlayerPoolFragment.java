package com.football.fantasy.fragments.leagues.player_pool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.PlayerPoolItemAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.events.PlayerQueryEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.display.PlayerPoolDisplayFragment;
import com.football.fantasy.fragments.leagues.player_pool.filter.PlayerPoolFilterFragment;
import com.football.models.responses.PlayerResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

import static com.football.models.responses.PlayerResponse.Options.GOALS;
import static com.football.models.responses.PlayerResponse.Options.POINT;
import static com.football.models.responses.PlayerResponse.Options.TRANSFER_VALUE;

public class PlayerPoolFragment extends BaseMainMvpFragment<IPlayerPoolView, IPlayerPoolPresenter<IPlayerPoolView>> implements IPlayerPoolView {

    private static final int REQUEST_FILTER = 100;
    private static final int REQUEST_DISPLAY = 101;
    private static final int REQUEST_SORT = 102;

    private static final ExtKeyValuePair KEY_VALUE_DEFAULT = new ExtKeyValuePair("1", "Current season");

    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.svSearch)
    SearchView svSearch;
    @BindView(R.id.lvData)
    ExtPagingListView lvData;
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

    List<PlayerResponse> playerResponses;
    PlayerPoolItemAdapter playerPoolItemAdapter;
    private int page = 1;
    private String filterClubs = "";
    private String filterPositions = "";
    private boolean[] sorts = new boolean[]{true, true, true}; // false: asc, true: desc
    private List<ExtKeyValuePair> displayPairs = new ArrayList<>();
    private ExtKeyValuePair seasonPair = KEY_VALUE_DEFAULT;

    public static Bundle newBundle() {
        Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.player_pool_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        initData();
        registerBus();
    }

    private void registerBus() {
        try {
            // action add click on PlayerList
            mCompositeDisposable.add(bus.ofType(PlayerQueryEvent.class)
                    .subscribeWith(new DisposableObserver<PlayerQueryEvent>() {
                        @Override
                        public void onNext(PlayerQueryEvent event) {
                            if (event.getTag() == PlayerQueryEvent.TAG_FILTER) {
                                filterClubs = event.getClub();
                                filterPositions = event.getPosition();

                                // get items
                                lvData.clearItems();
                                lvData.startLoading();
                                getPlayers();
                            } else if (event.getTag() == PlayerQueryEvent.TAG_DISPLAY) {
                                displayPairs = event.getDisplays();

                                displayDisplay();
                                playerPoolItemAdapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayDisplay() {
        playerPoolItemAdapter.setOptions(
                displayPairs.size() > 0 ? displayPairs.get(0).getKey() : "",
                displayPairs.size() > 1 ? displayPairs.get(1).getKey() : "",
                displayPairs.size() > 2 ? displayPairs.get(2).getKey() : "");
        if (displayPairs.size() > 0) {
            tvOption1.setText(displayPairs.get(0).getValue());
        }
        if (displayPairs.size() > 1) {
            tvOption2.setText(displayPairs.get(1).getValue());
        }
        if (displayPairs.size() > 2) {
            tvOption3.setText(displayPairs.get(2).getValue());
        }
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));
    }

    void initData() {

        // display default
        displayPairs.add(PlayerPoolDisplayFragment.OPTION_DISPLAY_DEFAULT_1);
        displayPairs.add(PlayerPoolDisplayFragment.OPTION_DISPLAY_DEFAULT_2);
        displayPairs.add(PlayerPoolDisplayFragment.OPTION_DISPLAY_DEFAULT_3);

        playerResponses = new ArrayList<>();
        playerPoolItemAdapter = new PlayerPoolItemAdapter(
                mActivity,
                playerResponses,
                player -> { // click event
                    AloneFragmentActivity.with(this)
                            .parameters(PlayerDetailFragment.newBundle(
                                    player,
                                    getString(R.string.player_pool)))
                            .start(PlayerDetailFragment.class);
                });
        playerPoolItemAdapter.setOptions(TRANSFER_VALUE, POINT, GOALS);
        lvData.init(mActivity, playerPoolItemAdapter)
                .setOnExtRefreshListener(() -> {
                    page = 1;
                    lvData.clearItems();
                    lvData.startLoading();
                    getPlayers();
                })
                .setOnExtLoadMoreListener(() -> {
                    page++;
                    getPlayers();
                });

        lvData.startLoading();
        getPlayers();
    }

    private void getPlayers() {
        presenter.getPlayers(filterPositions, filterClubs, displayPairs, sorts, page);
    }

    @Override
    public IPlayerPoolPresenter<IPlayerPoolView> createPresenter() {
        return new PlayerPoolPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.league_details;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
    }

    @Override
    public void displayPlayers(List<PlayerResponse> players) {
        Optional.from(lvData).doIfPresent(rv -> {
            rv.addNewItems(players);
        });
    }

    @OnClick({R.id.ivArrow, R.id.filter, R.id.display, R.id.option1, R.id.option2, R.id.option3})
    public void onSortClicked(View view) {
        switch (view.getId()) {
            case R.id.ivArrow:
                displaySelectDialog();
                break;
            case R.id.filter:
                AloneFragmentActivity.with(this)
                        .forResult(REQUEST_FILTER)
                        .parameters(PlayerPoolFilterFragment.newBundle(filterPositions, filterClubs))
                        .start(PlayerPoolFilterFragment.class);
                break;
            case R.id.display:
                StringBuilder displays = new StringBuilder();
                for (ExtKeyValuePair paire : displayPairs) {
                    displays.append(paire.getKey()).append(",");
                }
                AloneFragmentActivity.with(this)
                        .forResult(REQUEST_DISPLAY)
                        .parameters(PlayerPoolDisplayFragment.newBundle(displays.toString()))
                        .start(PlayerPoolDisplayFragment.class);
                break;

            case R.id.option1:
                toggleSort(0);
                ivSort1.setImageResource(getArrowResource(sorts[0]));
                break;

            case R.id.option2:
                toggleSort(1);
                ivSort2.setImageResource(getArrowResource(sorts[1]));
                break;

            case R.id.option3:
                toggleSort(2);
                ivSort3.setImageResource(getArrowResource(sorts[2]));
                break;
        }
    }

    private int getArrowResource(boolean desc) {
        return desc ? R.drawable.ic_arrow_drop_down_black : R.drawable.ic_arrow_downward_white_small;
    }

    private void toggleSort(int index) {
        page = 1;
        lvData.clearItems();
        lvData.startLoading();
        sorts[index] = !sorts[index];
        getPlayers();
    }

    private void displaySelectDialog() {
        List<ExtKeyValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(KEY_VALUE_DEFAULT);
        valuePairs.add(new ExtKeyValuePair("2", "Last Season"));
        valuePairs.add(new ExtKeyValuePair("3", "Avg 3 seasons"));
        valuePairs.add(new ExtKeyValuePair("4", "Avg 5 seasons"));

        ExtKeyValuePairDialogFragment.newInstance()
                .setExtKeyValuePairs(valuePairs)
                .setValue(seasonPair == null ? "" : seasonPair.getKey())
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        seasonPair = extKeyValuePair;
                        updateValue();
                    }
                }).show(getFragmentManager(), null);
    }

    private void updateValue() {
        tvTitle.setText(seasonPair.getValue());
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        Optional.from(lvData).doIfPresent(l -> {
            if (isLoading) {
                l.startLoading(true);
            } else {
                l.stopLoading(true);
            }
        });
    }
}
