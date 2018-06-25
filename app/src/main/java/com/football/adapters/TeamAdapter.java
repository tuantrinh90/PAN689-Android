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
import com.football.utilities.AppUtilities;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.Consumer;

public class TeamAdapter extends ExtBaseAdapter<TeamResponse, TeamAdapter.ViewHolder> {

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private LeagueResponse leagueResponse;
    private Consumer<TeamResponse> detailConsumer;
    private Consumer<TeamResponse> teamConsumer;

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
    protected void onBindViewHolder(ViewHolder holder, TeamResponse data) {
        holder.ivAvatar.setImageUri(data.getLogo());
        holder.tvTeam.setText(data.getName());
        holder.tvOwner.setText(data.getUser() == null ? "" : AppUtilities.getNameOrMe(holder.itemView.getContext(), data));

        holder.tvRemove.setVisibility(View.GONE);
        holder.ivLock.setVisibility(View.GONE);

        if (leagueResponse.getOwner()) {
            holder.tvRemove.setVisibility(data.getOwner() ? View.GONE : View.VISIBLE);
        }

        holder.ivLock.setVisibility(data.getOwner() ? View.VISIBLE : View.GONE);

        // click
        mDisposable.add(RxView.clicks(holder.itemView).subscribe(o -> Optional.from(detailConsumer).doIfPresent(t -> t.accept(data))));
        mDisposable.add(RxView.clicks(holder.tvRemove).subscribe(o -> Optional.from(teamConsumer).doIfPresent(t -> t.accept(data))));
    }

    class ViewHolder extends ExtPagingListView.ExtViewHolder {
        @BindView(R.id.ivAvatar)
        CircleImageViewApp ivAvatar;
        @BindView(R.id.tvTeam)
        ExtTextView tvTeam;
        @BindView(R.id.tvOwner)
        ExtTextView tvOwner;
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
