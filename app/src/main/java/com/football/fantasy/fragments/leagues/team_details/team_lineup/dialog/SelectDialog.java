package com.football.fantasy.fragments.leagues.team_details.team_lineup.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.fragment.ExtBaseBottomDialogFragment;
import com.football.adapters.TeamLineupPlayerAdapter;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import java8.util.function.Consumer;

public class SelectDialog extends ExtBaseBottomDialogFragment {
    @BindView(R.id.rvPlayer)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    Unbinder unbinder;

    private List<PlayerResponse> players;
    private Consumer<PlayerResponse> clickCallback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_lineup_select_player_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TeamLineupPlayerAdapter adapter = new TeamLineupPlayerAdapter(
                getContext(),
                players,
                player -> {
                    clickCallback.accept(player);
                    dismiss();
                });

        // setup adapter
        rvPlayer
                .adapter(adapter)
                .layoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false))
                .build();
    }

    @OnClick({R.id.tvCancel, R.id.rvPlayer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.rvPlayer:
                break;
        }
    }

    public SelectDialog setPlayers(List<PlayerResponse> players) {
        this.players = players;
        return this;
    }

    public SelectDialog setClickCallback(Consumer<PlayerResponse> clickCallback) {
        this.clickCallback = clickCallback;
        return this;
    }

    public static final class Builder {
        private List<PlayerResponse> players;

        public Builder() {
        }

        public Builder players(List<PlayerResponse> val) {
            players = val;
            return this;
        }

        public SelectDialog build() {
            SelectDialog dialog = new SelectDialog();
            dialog.players = this.players;
            return dialog;
        }
    }
}
