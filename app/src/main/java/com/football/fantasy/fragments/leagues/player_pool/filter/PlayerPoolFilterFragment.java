package com.football.fantasy.fragments.leagues.player_pool.filter;

import android.app.Activity;
import android.content.Intent;
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
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_pool.display.IPlayerPoolDisplayPresenter;
import com.football.fantasy.fragments.leagues.player_pool.display.IPlayerPoolDisplayView;
import com.football.fantasy.fragments.leagues.player_pool.display.PlayerPoolDisplayPresenter;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.RealClubResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayerPoolFilterFragment extends BaseMainMvpFragment<IPlayerPoolDisplayView, IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView>> implements IPlayerPoolDisplayView {

    public static final String KEY_POSITION = "POSITION";
    public static final String KEY_CLUB = "CLUB";

    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.rvFilterByPosition)
    RecyclerView rvFilterByPosition;
    @BindView(R.id.rvFilterByClub)
    RecyclerView rvFilterByClub;
    @BindView(R.id.tvApplyFilter)
    ExtTextView tvApplyFilter;

    List<ExtKeyValuePair> keyValuePairPositions;
    List<ExtKeyValuePair> keyValuePairClubs;

    FilterAdapter filterPositionAdapter;
    FilterAdapter filterClubAdapter;

    public static Bundle newBundle() {
        Bundle bundle = new Bundle();
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
        initView();

        presenter.getRealClubs();
    }

    void initView() {
        // position
        keyValuePairPositions = new ArrayList<>();
        keyValuePairPositions.add(new ExtKeyValuePair(String.valueOf(PlayerResponse.POSITION_NONE), getString(R.string.all)));
        keyValuePairPositions.add(new ExtKeyValuePair(String.valueOf(PlayerResponse.POSITION_MIDFIELDER), getString(R.string.midfielder)));
        keyValuePairPositions.add(new ExtKeyValuePair(String.valueOf(PlayerResponse.POSITION_GOALKEEPER), getString(R.string.goalkeeper)));
        keyValuePairPositions.add(new ExtKeyValuePair(String.valueOf(PlayerResponse.POSITION_ATTACKER), getString(R.string.attacker)));
        keyValuePairPositions.add(new ExtKeyValuePair(String.valueOf(PlayerResponse.POSITION_DEFENDER), getString(R.string.defender)));

        filterPositionAdapter = new FilterAdapter(mActivity, keyValuePairPositions, extKeyValuePair -> {
            extKeyValuePair.setSelected(!extKeyValuePair.isSelected());
            filterPositionAdapter.notifyDataSetChanged(keyValuePairPositions);
        });

        rvFilterByPosition.setLayoutManager(new GridLayoutManager(mActivity, 2));
        rvFilterByPosition.setAdapter(filterPositionAdapter);

    }

    @Override
    public IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView> createPresenter() {
        return new PlayerPoolDisplayPresenter(getAppComponent());
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
        StringBuilder positionsBuilder = new StringBuilder();
        for (ExtKeyValuePair key : keyValuePairPositions) {
            if (key.isSelected()) {
                positionsBuilder.append(key.getKey()).append(",");
            }
        }
        String positions = TextUtils.isEmpty(positionsBuilder) ? "" : positionsBuilder.substring(0, positionsBuilder.lastIndexOf(","));

        StringBuilder clubsBuilder = new StringBuilder();
        for (ExtKeyValuePair key : keyValuePairClubs) {
            if (key.isSelected()) {
                clubsBuilder.append(key.getKey()).append(",");
            }
        }
        String clubs = TextUtils.isEmpty(clubsBuilder) ? "" : clubsBuilder.substring(0, clubsBuilder.lastIndexOf(","));

        Intent intent = new Intent();
        intent.putExtra(KEY_POSITION, positions);
        intent.putExtra(KEY_CLUB, clubs);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish(); // TODO: 6/13/2018 chưa bắn đc về
    }

    @Override
    public void displayClubs(List<RealClubResponse> clubs) {

        // clubs
        keyValuePairClubs = new ArrayList<>();
        for (RealClubResponse club : clubs) {
            keyValuePairClubs.add(new ExtKeyValuePair(String.valueOf(club.getId()), club.getName()));
        }

        filterClubAdapter = new FilterAdapter(mActivity, keyValuePairClubs, extKeyValuePair -> {
            extKeyValuePair.setSelected(!extKeyValuePair.isSelected());
            filterClubAdapter.notifyDataSetChanged(keyValuePairClubs);
        });

        rvFilterByClub.setLayoutManager(new GridLayoutManager(mActivity, 2));
        rvFilterByClub.setAdapter(filterClubAdapter);
    }
}
