package com.football.fantasy.fragments.leagues.player_pool.sort;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayerPoolSortFragment extends BaseMainMvpFragment<IPlayerPoolSortView, IPlayerPoolSortPresenter<IPlayerPoolSortView>> implements IPlayerPoolSortView {
    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.tvSortByColumn)
    EditTextApp tvSortByColumn;
    @BindView(R.id.tvSortByValue)
    EditTextApp tvSortByValue;
    @BindView(R.id.tvSortTheTable)
    ExtTextView tvSortTheTable;

    ExtKeyValuePair keyValuePairKey;
    ExtKeyValuePair keyValuePairValue;

    public static Bundle newBundle() {
        Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.player_pool_sort_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public IPlayerPoolSortPresenter<IPlayerPoolSortView> createPresenter() {
        return new PlayerPoolSortPresenter(getAppComponent());
    }

    @OnClick(R.id.tvSortByColumn)
    void onClickSortByColumn() {
        List<ExtKeyValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new ExtKeyValuePair("value", "Value"));
        valuePairs.add(new ExtKeyValuePair("point", "Point"));
        valuePairs.add(new ExtKeyValuePair("value", "Value"));

        ExtKeyValuePairDialogFragment.newInstance()
                .setExtKeyValuePairs(valuePairs)
                .setValue(keyValuePairKey == null ? "" : keyValuePairKey.getKey())
                .setOnSelectedConsumer(extKeyValuePair -> {
                    keyValuePairKey = extKeyValuePair;
                    updateValue();
                }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.tvSortByValue)
    void onClickSortByValue() {
        List<ExtKeyValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new ExtKeyValuePair("asc", getString(R.string.ascendent)));
        valuePairs.add(new ExtKeyValuePair("desc", getString(R.string.descendent)));
        ExtKeyValuePairDialogFragment.newInstance()
                .setExtKeyValuePairs(valuePairs)
                .setValue(keyValuePairValue == null ? "" : keyValuePairValue.getKey())
                .setOnSelectedConsumer(extKeyValuePair -> {
                    keyValuePairValue = extKeyValuePair;
                    updateValue();
                }).show(getFragmentManager(), null);
    }

    void updateValue() {
        tvSortByColumn.setContent(keyValuePairKey == null ? "" : keyValuePairKey.getValue());
        tvSortByValue.setContent(keyValuePairValue == null ? "" : keyValuePairValue.getValue());
    }

    @OnClick(R.id.tvSortTheTable)
    void onClickSortTheTable() {

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
