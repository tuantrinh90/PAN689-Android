package com.football.fantasy.fragments.leagues.your_team.line_up.draft;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpFragment;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.SocketEventKey;
import com.github.nkzawa.socketio.client.Ack;

public class LineupDraftFragment extends LineUpFragment<ILineupDraftView, ILineupDraftPresenter<ILineupDraftView>> implements ILineupDraftView {

    private static final String TAG = "LineupDraftFragment";

    public static LineupDraftFragment newInstance(LeagueResponse league, Integer teamId) {
        LineupDraftFragment fragment = new LineupDraftFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
        bundle.putInt(KEY_TEAM_ID, teamId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    public ILineupDraftPresenter<ILineupDraftView> createPresenter() {
        return new LineupDraftDataPresenter(getAppComponent());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerSocket();
    }

    @Override
    public void onDestroy() {
        getAppContext().off(SocketEventKey.EVENT_CHANGE_TURN);
        super.onDestroy();
    }

    private void registerSocket() {
        getAppContext().getSocket().on(SocketEventKey.EVENT_CHANGE_TURN, args -> {
            Log.i(SocketEventKey.EVENT_CHANGE_TURN, "registerSocket: ");
        });
    }
}
