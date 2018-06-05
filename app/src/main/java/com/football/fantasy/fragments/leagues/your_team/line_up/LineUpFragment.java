package com.football.fantasy.fragments.leagues.your_team.line_up;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bon.util.DialogUtils;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.fantasy.R;

import butterknife.BindView;

public class LineUpFragment extends BaseMainMvpFragment<ILineUpView, ILineUpPresenter<ILineUpView>> implements ILineUpView {
    public static LineUpFragment newInstance() {
        return new LineUpFragment();
    }

    @BindView(R.id.lineupView)
    LineupView lineupView;

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
        lineupView.setPlayerBiConsumer((player, position) -> {
            Log.d("dd", "setPlayerBiConsumer: " + position);
        });
        lineupView.setRemoveConsumer(position -> {
            DialogUtils.messageBox(mActivity,
                    0,
                    getString(R.string.app_name),
                    getString(R.string.message_confirm_remove_lineup_player),
                    getString(R.string.ok),
                    getString(R.string.cancel),
                    (dialog, which) -> lineupView.removePlayerView(position),
                    (dialog, which) -> lineupView.removePlayerView(position));
        });
    }

    @Override
    public ILineUpPresenter<ILineUpView> createPresenter() {
        return new LineUpPresenter(getAppComponent());
    }
}
