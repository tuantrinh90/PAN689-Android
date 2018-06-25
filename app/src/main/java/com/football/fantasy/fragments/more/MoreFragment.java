package com.football.fantasy.fragments.more;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.bon.util.DialogUtils;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.activities.AccountActivity;

import butterknife.OnClick;

public class MoreFragment extends BaseMainMvpFragment<IMoreView, IMorePresenter<IMoreView>> implements IMoreView {

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

    @NonNull
    @Override
    public IMorePresenter<IMoreView> createPresenter() {
        return new MoreDataPresenter(getAppComponent());
    }

    @OnClick(R.id.tvLogout)
    public void onViewClicked() {
        DialogUtils.messageBox(mActivity,
                0,
                getString(R.string.app_name),
                getString(R.string.message_logout),
                getString(R.string.ok),
                getString(R.string.cancel),
                (dialog, which) -> presenter.logout(),
                (dialog, which) -> dialog.dismiss());
    }

    @Override
    public void logout() {
        Intent intent = new Intent(getContext(), AccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ActivityCompat.finishAffinity(mActivity);
    }
}
