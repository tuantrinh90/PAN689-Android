package com.football.fantasy.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;

public class HomeFragment extends BaseMvpFragment<IHomeView, IHomePresenter<IHomeView>> implements IHomeView {
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.home_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public IHomePresenter<IHomeView> createPresenter() {
        return new HomePresenter(getAppComponent());
    }
}
