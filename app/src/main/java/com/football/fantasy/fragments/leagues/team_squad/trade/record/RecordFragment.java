package com.football.fantasy.fragments.leagues.team_squad.trade.record;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class RecordFragment extends BaseMainMvpFragment<IRecordView, IRecordPresenter<IRecordView>> implements IRecordView {
    public static RecordFragment newInstance() {
        return new RecordFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_record_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @NonNull
    @Override
    public IRecordPresenter<IRecordView> createPresenter() {
        return new RecordDataPresenter(getAppComponent());
    }
}
