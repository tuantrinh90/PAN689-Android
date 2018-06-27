package com.football.fantasy.fragments.leagues.player_pool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.customizes.searchs.SearchView;
import com.football.events.PlayerQueryEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.display.PlayerPoolDisplayFragment;
import com.football.fantasy.fragments.leagues.player_pool.filter.PlayerPoolFilterFragment;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.SeasonResponse;
import com.football.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

import static com.football.models.responses.PlayerResponse.Options.GOALS;
import static com.football.models.responses.PlayerResponse.Options.POINT;
import static com.football.models.responses.PlayerResponse.Options.VALUE;

public class PlayerPoolFragment extends BaseMainMvpFragment<IPlayerPoolView, IPlayerPoolPresenter<IPlayerPoolView>> implements IPlayerPoolView {

    private static final String TAG = "PlayerPoolFragment";

    private static final String KEY_TITLE = "TITLE";

    private static final int REQUEST_FILTER = 100;
    private static final int REQUEST_DISPLAY = 101;

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

    private int page = 1;
    private String filterClubs = "";
    private String filterPositions = "";
    private int[] sorts = new int[]{Constant.SORT_NONE, Constant.SORT_NONE, Constant.SORT_NONE}; // -1: NONE, 0: desc, 1: asc
    private List<ExtKeyValuePair> displayPairs = new ArrayList<>();
    private ExtKeyValuePair currentSeason;
    private List<SeasonResponse> seasons;

    public static Bundle newBundle(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
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
        initData();
        registerBus();
    }

    private void getDataFromBundle() {
        title = getArguments().getString(KEY_TITLE);
    }

    private void registerBus() {
        try {
            // action add click on PlayerList
            mCompositeDisposable.add(bus.ofType(PlayerQueryEvent.class)
                    .subscribeWith(new DisposableObserver<PlayerQueryEvent>() {
                        @Override
                        public void onNext(PlayerQueryEvent event) {
                            if (event.getFrom().equals(TAG)) {
                                if (event.getTag() == PlayerQueryEvent.TAG_FILTER) {
                                    filterClubs = event.getClub();
                                    filterPositions = event.getPosition();

                                    // get items
                                    rvPlayer.clear();
                                    rvPlayer.startLoading();
                                    getPlayers();
                                } else if (event.getTag() == PlayerQueryEvent.TAG_DISPLAY) {
                                    displayPairs = event.getDisplays();

                                    displayDisplay();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));
    }

    void initData() {

        // display default
        displayPairs.add(PlayerPoolDisplayFragment.OPTION_DISPLAY_DEFAULT_1);
        displayPairs.add(PlayerPoolDisplayFragment.OPTION_DISPLAY_DEFAULT_2);
        displayPairs.add(PlayerPoolDisplayFragment.OPTION_DISPLAY_DEFAULT_3);

        PlayerPoolAdapter adapter;
        adapter = new PlayerPoolAdapter(
                new ArrayList<>(),
                player -> { // click event
                    AloneFragmentActivity.with(this)
                            .parameters(PlayerDetailFragment.newBundle(
                                    player,
                                    getString(R.string.player_pool)))
                            .start(PlayerDetailFragment.class);
                });
        adapter.setOptions(VALUE, POINT, GOALS);

        rvPlayer.adapter(adapter)
                .loadingLayout(0)
                .refreshListener(() -> {
                    refreshData();
                })
                .loadMoreListener(() -> {
                    page++;
                    getPlayers();
                })
                .build();

        presenter.getSeasons();
    }

    private void refreshData() {
        page = 1;
        rvPlayer.clear();
        rvPlayer.startLoading();
        getPlayers();
    }

    private void getPlayers() {
        if (currentSeason == null) {
            showMessage(getString(R.string.message_season_not_found));
            rvPlayer.startLoading();
        } else {
//            lvData.setMessage(getString(R.string.loading));
            presenter.getPlayers(currentSeason.getKey(), filterPositions, filterClubs, displayPairs, sorts, page);
        }
    }

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
    public void displayPlayers(List<PlayerResponse> players) {
        rvPlayer.addItems(players);
    }

    @Override
    public void displaySeasons(List<SeasonResponse> seasons) {
        this.seasons = seasons;
        currentSeason = new ExtKeyValuePair(String.valueOf(seasons.get(0).getId()), seasons.get(0).getName());
        updateValue();

        rvPlayer.startLoading();
        getPlayers();
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
                        .parameters(PlayerPoolFilterFragment.newBundle(TAG, filterPositions, filterClubs, false))
                        .start(PlayerPoolFilterFragment.class);
                break;
            case R.id.display:
                StringBuilder displays = new StringBuilder();
                for (ExtKeyValuePair pair : displayPairs) {
                    displays.append(pair.getKey()).append(",");
                }
                AloneFragmentActivity.with(this)
                        .forResult(REQUEST_DISPLAY)
                        .parameters(PlayerPoolDisplayFragment.newBundle(TAG, displays.toString()))
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

    private void displaySelectDialog() {
        if (seasons == null || seasons.isEmpty()) {
            showMessage(getString(R.string.message_season_not_found));
        } else {
            List<ExtKeyValuePair> valuePairs = new ArrayList<>();
            for (SeasonResponse season : seasons) {
                valuePairs.add(new ExtKeyValuePair(String.valueOf(season.getId()), season.getName()));
            }

            ExtKeyValuePairDialogFragment.newInstance()
                    .setExtKeyValuePairs(valuePairs)
                    .setValue(currentSeason == null ? valuePairs.get(0).getKey() : currentSeason.getKey())
                    .setOnSelectedConsumer(extKeyValuePair -> {
                        if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                            currentSeason = extKeyValuePair;
                            updateValue();
                            refreshData();
                        }
                    }).show(getFragmentManager(), null);
        }
    }

    private void updateValue() {
        tvSeason.setText(currentSeason.getValue());
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
//        Optional.from(lvData).doIfPresent(l -> {
//            if (isLoading) {
//                l.startLoading(true);
//            } else {
//                l.stopLoading(true);
//            }
//        });
    }
}
