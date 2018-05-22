package com.football.fantasy.fragments.more;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;

public class MoreFragment extends BaseMvpFragment<IMoreView, IMorePresenter<IMoreView>> implements IMoreView {
    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.more_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public IMorePresenter<IMoreView> createPresenter() {
        return new MoreDataPresenter(getAppComponent());
    }
}
