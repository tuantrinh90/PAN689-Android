package com.football.fantasy.fragments.leagues.team_squad.trade.transferring;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.PlayerPoolAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.display.PlayerPoolDisplayFragment;
import com.football.fantasy.fragments.leagues.player_pool.filter.PlayerPoolFilterFragment;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.models.responses.PlayerResponse.Options.GOALS;
import static com.football.models.responses.PlayerResponse.Options.POINT;
import static com.football.models.responses.PlayerResponse.Options.VALUE;

public class TransferringFragment extends BaseMainMvpFragment<ITransferringView, ITransferringPresenter<ITransferringView>> implements ITransferringView {
    public static TransferringFragment newInstance() {
        return new TransferringFragment();
    }

    private static final String TAG = "TransferringFragment";
    private static final int REQUEST_FILTER = 100;
    private static final int REQUEST_DISPLAY = 101;

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

    private int page = 1;
    private String filterClubs = "";
    private String filterPositions = "";
    private int[] sorts = new int[]{Constant.SORT_NONE, Constant.SORT_NONE, Constant.SORT_NONE}; // -1: NONE, 0: desc, 1: asc
    private List<ExtKeyValuePair> displayPairs = new ArrayList<>();

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_transferring_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
    }

    @NonNull
    @Override
    public ITransferringPresenter<ITransferringView> createPresenter() {
        return new TransferringDataPresenter(getAppComponent());
    }

    private void initView() {

        PlayerPoolAdapter adapter;
        adapter = new PlayerPoolAdapter(
                new ArrayList<>(),
                player -> { // click event
                    AloneFragmentActivity.with(this)
                            .parameters(PlayerDetailFragment.newBundle(
                                    player,
                                    getString(R.string.player_pool), false))
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

    }

    private void refreshData() {
        page = 1;
        rvPlayer.clear();
        rvPlayer.startLoading();
        getPlayers();
    }

    private void getPlayers() {
        presenter.getPlayers(filterPositions, filterClubs, displayPairs, sorts, page);
    }


    @OnClick({R.id.filter, R.id.display, R.id.option1, R.id.option2, R.id.option3})
    public void onSortClicked(View view) {
        switch (view.getId()) {
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
    }

}
