package com.football.fantasy.fragments.leagues.player_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.football.common.activities.AloneFragmentActivity;
import com.football.events.PlayerEvent;
import com.football.models.responses.PlayerResponse;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;

public class PlayerDetailForLineupFragment extends PlayerDetailFragment {

    private static final String KEY_MAIN_POSITION = "MAIN_POSITION";
    private static final String KEY_ORDER = "ORDER";

    private int mainPosition = PlayerResponse.POSITION_NONE;
    private int order = NONE_ORDER;

    // only for view lineup
    public static void start(Fragment fragment, PlayerResponse player, int teamId, String title,
                             String gameplayOption, int pickEnable, int mainPosition, int order) {
        Bundle bundle = newBundle(player.getId(), teamId, title, pickEnable, gameplayOption);
        bundle.putInt(KEY_MAIN_POSITION, mainPosition);
        bundle.putInt(KEY_ORDER, order);
        AloneFragmentActivity.with(fragment)
                .parameters(bundle)
                .start(PlayerDetailForLineupFragment.class);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void getDataFromBundle() {
        super.getDataFromBundle();
        mainPosition = getArguments().getInt(KEY_MAIN_POSITION);
        order = getArguments().getInt(KEY_ORDER);
    }

    @Override
    public void onMenuClicked(View view) {
        // bắn sang MH Lineup
        bus.send(new PlayerEvent.Builder()
                .action(PlayerEvent.ACTION_ADD_CLICK)
                .position(mainPosition)
                .order(order)
                .data(player)
                .callback((success, message) -> {
                    showLoading(false);
                    if (success) {
                        mActivity.finish();
                    } else {
                        showMessage(message);
                    }
                })
                .build());
    }
}
