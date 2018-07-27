package com.football.fantasy.fragments.leagues.player_pool.filter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.FilterAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.events.PlayerQueryEvent;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.RealClubResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayerPoolFilterFragment extends BaseMvpFragment<IPlayerPoolFilterView, IPlayerPoolFilterPresenter<IPlayerPoolFilterView>> implements IPlayerPoolFilterView {

    private static final String KEY_FROM = "FROM";
    public static final String KEY_POSITION = "POSITION";
    public static final String KEY_CLUB = "CLUB";
    private static final String KEY_ONLY_CLUB = "ONLY_CLUB";

    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.rvFilterByPosition)
    RecyclerView rvFilterByPosition;
    @BindView(R.id.rvFilterByClub)
    RecyclerView rvFilterByClub;
    @BindView(R.id.tvApplyFilter)
    ExtTextView tvApplyFilter;
    @BindView(R.id.filter_position)
    View filterPosition;

    private List<ExtKeyValuePair> keyValuePairPositions;
    private List<ExtKeyValuePair> keyValuePairClubs;

    private String from;
    private String filterPositions;
    private String filterClubs;
    private boolean onlyClubs;

    private FilterAdapter filterPositionAdapter;
    private FilterAdapter filterClubAdapter;

    public static Bundle newBundle(String from, String filterPositions, String filterClubs, boolean onlyClub) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FROM, from);
        bundle.putString(KEY_POSITION, filterPositions);
        bundle.putString(KEY_CLUB, filterClubs);
        bundle.putBoolean(KEY_ONLY_CLUB, onlyClub);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.player_pool_filter_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();

        if (!onlyClubs) {
            initFilter();
        } else {
            filterPosition.setVisibility(View.GONE);
        }
        presenter.getRealClubs();
    }

    private void getDataFromBundle() {
        from = getArguments().getString(KEY_FROM);
        filterPositions = getArguments().getString(KEY_POSITION);
        filterClubs = getArguments().getString(KEY_CLUB);
        onlyClubs = getArguments().getBoolean(KEY_ONLY_CLUB);
    }

    void initFilter() {
        // position
        keyValuePairPositions = new ArrayList<>();
        String midfielder = String.valueOf(PlayerResponse.POSITION_MIDFIELDER);
        String goalkeeper = String.valueOf(PlayerResponse.POSITION_GOALKEEPER);
        String attacker = String.valueOf(PlayerResponse.POSITION_ATTACKER);
        String defender = String.valueOf(PlayerResponse.POSITION_DEFENDER);

        boolean allCheck = TextUtils.isEmpty(filterPositions);

        keyValuePairPositions.add(new ExtKeyValuePair(midfielder, getString(R.string.midfielder), allCheck || filterPositions.contains(midfielder)));
        keyValuePairPositions.add(new ExtKeyValuePair(goalkeeper, getString(R.string.goalkeeper), allCheck || filterPositions.contains(goalkeeper)));
        keyValuePairPositions.add(new ExtKeyValuePair(attacker, getString(R.string.attacker), allCheck || filterPositions.contains(attacker)));
        keyValuePairPositions.add(new ExtKeyValuePair(defender, getString(R.string.defender), allCheck || filterPositions.contains(defender)));

        filterPositionAdapter = new FilterAdapter(mActivity, keyValuePairPositions, extKeyValuePair -> {
            if (!onlyClubs) {
                extKeyValuePair.setSelected(!extKeyValuePair.isSelected());
                filterPositionAdapter.notifyDataSetChanged(keyValuePairPositions);
            }
        });

        rvFilterByPosition.setLayoutManager(new GridLayoutManager(mActivity, 2));
        rvFilterByPosition.setAdapter(filterPositionAdapter);

    }

    @Override
    public IPlayerPoolFilterPresenter<IPlayerPoolFilterView> createPresenter() {
        return new PlayerPoolFilterPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.player_list;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    @OnClick({R.id.tvApplyFilter})
    public void onApplyFilter() {
        String positions = "";
        if (!onlyClubs) {
            StringBuilder positionsBuilder = new StringBuilder();
            for (ExtKeyValuePair key : keyValuePairPositions) {
                if (key.isSelected()) {
                    positionsBuilder.append(key.getKey()).append(",");
                }
            }
            positions = TextUtils.isEmpty(positionsBuilder) || positionsBuilder.toString().contains(String.valueOf(PlayerResponse.POSITION_NONE)) ?
                    "" : positionsBuilder.substring(0, positionsBuilder.lastIndexOf(","));
        }

        StringBuilder clubsBuilder = new StringBuilder();
        for (ExtKeyValuePair key : keyValuePairClubs) {
            if (key.isSelected()) {
                clubsBuilder.append(key.getKey()).append(",");
            }
        }
        String clubs = TextUtils.isEmpty(clubsBuilder) ? "" : clubsBuilder.substring(0, clubsBuilder.lastIndexOf(","));

        // bắn sang màn hình playerPoolFragment
        bus.send(new PlayerQueryEvent.Builder()
                .from(from)
                .tag(PlayerQueryEvent.TAG_FILTER)
                .position(positions)
                .club(clubs)
                .build());

        getActivity().finish();
    }

    @Override
    public void displayClubs(List<RealClubResponse> clubs) {
        List<String> filter;
        if (!TextUtils.isEmpty(filterClubs)) {
            filter = Arrays.asList(filterClubs.split(","));
        } else {
            filter = new ArrayList<>();
        }

        // clubs
        keyValuePairClubs = new ArrayList<>();
        for (RealClubResponse club : clubs) {
            keyValuePairClubs.add(new ExtKeyValuePair(String.valueOf(club.getId()), club.getName(), filter.contains(club.getId().toString())));
        }

        filterClubAdapter = new FilterAdapter(mActivity, keyValuePairClubs, extKeyValuePair -> {
            extKeyValuePair.setSelected(!extKeyValuePair.isSelected());
            filterClubAdapter.notifyDataSetChanged(keyValuePairClubs);
        });

        rvFilterByClub.setLayoutManager(new GridLayoutManager(mActivity, 2));
        rvFilterByClub.setAdapter(filterClubAdapter);
    }
}
