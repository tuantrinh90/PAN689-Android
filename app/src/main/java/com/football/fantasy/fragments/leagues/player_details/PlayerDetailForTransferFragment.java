package com.football.fantasy.fragments.leagues.player_details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.football.common.activities.AloneFragmentActivity;
import com.football.events.TransferEvent;
import com.football.models.responses.PlayerResponse;

public class PlayerDetailForTransferFragment extends PlayerDetailFragment {

    private static final String KEY_PLAYER_TO = "PLAYER_TO";


    private PlayerResponse playerTransfer;

    // only for transfer
    public static void start(Fragment fragment, PlayerResponse playerFrom, PlayerResponse playerTo, int teamId, String title,
                             int pickEnable, String gameplayOption) {
        Bundle bundle = newBundle(playerFrom, teamId, title, pickEnable, gameplayOption);
        bundle.putSerializable(KEY_PLAYER_TO, playerTo);
        AloneFragmentActivity.with(fragment)
                .parameters(bundle)
                .start(PlayerDetailForTransferFragment.class);
    }

    @Override
    protected void getDataFromBundle() {
        super.getDataFromBundle();
        playerTransfer = (PlayerResponse) getArguments().getSerializable(KEY_PLAYER_TO);
    }

    @Override
    public void onMenuClicked(View view) {
        bus.send(new TransferEvent(playerTransfer, player));
        mActivity.finish();
    }
}
