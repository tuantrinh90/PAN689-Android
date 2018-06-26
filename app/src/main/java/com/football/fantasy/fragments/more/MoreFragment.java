package com.football.fantasy.fragments.more;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.activities.AccountActivity;
import com.football.fantasy.fragments.more.profile.ProfileFragment;

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

    @OnClick({R.id.profile, R.id.settings, R.id.gamerules, R.id.contact, R.id.logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile:
                AloneFragmentActivity
                        .with(this)
                        .start(ProfileFragment.class);
                break;
            case R.id.settings:
                break;
            case R.id.gamerules:
                break;
            case R.id.contact:
                break;
            case R.id.logout:
                DialogUtils.messageBox(mActivity,
                        0,
                        getString(R.string.app_name),
                        getString(R.string.message_logout),
                        getString(R.string.ok),
                        getString(R.string.cancel),
                        (dialog, which) -> presenter.logout(),
                        (dialog, which) -> dialog.dismiss());
                break;
        }
    }

    @Override
    public void logout() {
        Intent intent = new Intent(getContext(), AccountActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ActivityCompat.finishAffinity(mActivity);
    }
}
