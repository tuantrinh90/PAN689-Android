package com.football.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class TeamAdapter extends ExtBaseAdapter<TeamResponse, TeamAdapter.ViewHolder> {
    LeagueResponse leagueResponse;
    Consumer<TeamResponse> detailConsumer;
    Consumer<TeamResponse> teamConsumer;

    public TeamAdapter(Context context, List<TeamResponse> teamResponses,
                       LeagueResponse leagueResponse,
                       Consumer<TeamResponse> detailConsumer,
                       Consumer<TeamResponse> teamConsumer) {
        super(context, teamResponses);
        this.leagueResponse = leagueResponse;
        this.detailConsumer = detailConsumer;
        this.teamConsumer = teamConsumer;
    }

    @Override
    protected int getViewId() {
        return R.layout.team_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, TeamResponse teamResponse) {
        holder.ivAvatar.setImageUri(teamResponse.getLogo());
        holder.tvTeam.setText(teamResponse.getName());
        holder.tvDescription.setText(teamResponse.getDescription());

        holder.tvRemove.setVisibility(View.GONE);
        holder.ivLock.setVisibility(View.GONE);
        if (leagueResponse.getOwner()) {
            holder.tvRemove.setVisibility(teamResponse.getOwner() ? View.GONE : View.VISIBLE);
            holder.ivLock.setVisibility(teamResponse.getOwner() ? View.VISIBLE : View.GONE);
        }

        // click
        RxView.clicks(holder.itemView).subscribe(o -> Optional.from(detailConsumer).doIfPresent(t -> t.accept(teamResponse)));
        RxView.clicks(holder.tvRemove).subscribe(o -> Optional.from(teamConsumer).doIfPresent(t -> t.accept(teamResponse)));
    }

    class ViewHolder extends ExtPagingListView.ExtViewHolder {
        @BindView(R.id.ivAvatar)
        CircleImageViewApp ivAvatar;
        @BindView(R.id.tvTeam)
        ExtTextView tvTeam;
        @BindView(R.id.tvDescription)
        ExtTextView tvDescription;
        @BindView(R.id.tvRemove)
        ExtTextView tvRemove;
        @BindView(R.id.ivLock)
        ImageView ivLock;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
