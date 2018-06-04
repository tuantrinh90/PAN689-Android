package com.football.fantasy.fragments.leagues.your_team.line_up;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;

public class LineUpFragment extends BaseMainMvpFragment<ILineUpView, ILineUpPresenter<ILineUpView>> implements ILineUpView {
    public static LineUpFragment newInstance() {
        return new LineUpFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.lineup_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {

    }

    @Override
    public ILineUpPresenter<ILineUpView> createPresenter() {
        return new LineUpPresenter(getAppComponent());
    }
}
