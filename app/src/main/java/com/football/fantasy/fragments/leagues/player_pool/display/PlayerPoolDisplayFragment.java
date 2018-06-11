package com.football.fantasy.fragments.leagues.player_pool.display;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.FilterAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayerPoolDisplayFragment extends BaseMainMvpFragment<IPlayerPoolDisplayView, IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView>> implements IPlayerPoolDisplayView {
    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.rvRecyclerView)
    RecyclerView rvRecyclerView;
    @BindView(R.id.tvApplyToTable)
    ExtTextView tvApplyToTable;

    List<ExtKeyValuePair> keyValuePairs;
    FilterAdapter filterAdapter;

    @Override
    public int getResourceId() {
        return R.layout.player_pool_display_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

     void initView() {
         // position
         keyValuePairs = new ArrayList<>();
         keyValuePairs.add(new ExtKeyValuePair(getString(R.string.all), getString(R.string.all)));
         keyValuePairs.add(new ExtKeyValuePair(getString(R.string.midfielder), getString(R.string.midfielder)));
         keyValuePairs.add(new ExtKeyValuePair(getString(R.string.goalkeeper), getString(R.string.goalkeeper)));
         keyValuePairs.add(new ExtKeyValuePair(getString(R.string.attacker), getString(R.string.attacker)));
         keyValuePairs.add(new ExtKeyValuePair(getString(R.string.defender), getString(R.string.defender)));

         filterAdapter = new FilterAdapter(mActivity, keyValuePairs, extKeyValuePair -> {
             extKeyValuePair.setSelected(!extKeyValuePair.isSelected());
             filterAdapter.notifyDataSetChanged(keyValuePairs);
         });

         rvRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
         rvRecyclerView.setAdapter(filterAdapter);
    }

    @Override
    public IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView> createPresenter() {
        return new PlayerPoolDisplayPresenter(getAppComponent());
    }

    @OnClick(R.id.tvApplyToTable)
    void onClickApplyToTable(){

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
}
