package com.football.fantasy.fragments.match_up;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;

public class MatchUpFragment extends BaseMvpFragment<IMatchUpView, IMatchUpPresenter<IMatchUpView>> implements IMatchUpView {
    public static MatchUpFragment newInstance() {
        return new MatchUpFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.match_up_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @NonNull
    @Override
    public IMatchUpPresenter<IMatchUpView> createPresenter() {
        return new MatchUpDataPresenter(getAppComponent());
    }
}
