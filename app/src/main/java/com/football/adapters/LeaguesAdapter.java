package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.League;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class LeaguesAdapter extends RecyclerView.Adapter<LeaguesAdapter.ViewHolder> {
    Context context;
    List<League> leagues;
    Consumer<League> detailConsumer;
    Consumer<League> approveConsumer;
    Consumer<League> rejectConsumer;
    Consumer<League> joinConsumer;

    public LeaguesAdapter(Context context, List<League> leagues, Consumer<League> detailConsumer,
                          Consumer<League> approveConsumer,
                          Consumer<League> rejectConsumer,
                          Consumer<League> joinConsumer) {
        this.context = context;
        this.leagues = leagues;
        this.detailConsumer = detailConsumer;
        this.approveConsumer = approveConsumer;
        this.rejectConsumer = rejectConsumer;
        this.joinConsumer = joinConsumer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.league_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemCount() <= 0) return;

        League league = leagues.get(position);
        holder.ivAvatar.setImageUri(league.getAvatar());
        holder.tvTitle.setText(league.getTitle());
        holder.tvOwner.setText(league.getOwner());
        holder.tvInvitor.setText(league.getInvitor());
        holder.tvEntrantNumber.setText(String.valueOf(league.getRankNumber()));
        holder.tvEntrantTotal.setText(String.valueOf(league.getRankTotal()));
        holder.tvDescription.setText(league.getDescription());

        // number
        holder.ivUpOrDown.setImageResource(league.getType() == League.TYPE_INCREASE ? R.drawable.bg_green_arrow_up_circle :
                (league.getType() == League.TYPE_DECREASE ? R.drawable.bg_green_arrow_down_red : 0));
        holder.ivNumber.setImageResource(league.getRate() == 1 ? R.drawable.ic_number_one :
                (league.getRate() == 2 ? R.drawable.ic_number_two : R.drawable.ic_number_three));

        // show/hide action
        holder.trInvitor.setVisibility(league.getStatus() == League.PENDING_LEAGUES ? View.VISIBLE : View.GONE);
        holder.llBottomAction.setVisibility(league.getStatus() == League.PENDING_LEAGUES ? View.VISIBLE : View.GONE);
        holder.llNumber.setVisibility(league.getStatus() == League.MY_LEAGUES && league.getRate() >= 1 && league.getRate() <= 3 ? View.VISIBLE : View.GONE);
        holder.tvJoin.setVisibility(league.getStatus() == League.OPEN_LEAGUES ? View.VISIBLE : View.GONE);

        // event
        RxView.clicks(holder.itemView).subscribe(v -> Optional.from(detailConsumer).doIfPresent(c -> c.accept(league)));
        RxView.clicks(holder.tvJoin).subscribe(v -> Optional.from(joinConsumer).doIfPresent(c -> c.accept(league)));
        RxView.clicks(holder.tvCheck).subscribe(v -> Optional.from(approveConsumer).doIfPresent(c -> c.accept(league)));
        RxView.clicks(holder.tvClose).subscribe(v -> Optional.from(rejectConsumer).doIfPresent(c -> c.accept(league)));
    }

    @Override
    public int getItemCount() {
        return leagues == null ? 0 : leagues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivAvatar)
        CircleImageViewApp ivAvatar;
        @BindView(R.id.tvTitle)
        ExtTextView tvTitle;
        @BindView(R.id.tvOwner)
        ExtTextView tvOwner;
        @BindView(R.id.trInvitor)
        TableRow trInvitor;
        @BindView(R.id.tvInvitor)
        ExtTextView tvInvitor;
        @BindView(R.id.tvEntrantNumber)
        ExtTextView tvEntrantNumber;
        @BindView(R.id.tvEntrantTotal)
        ExtTextView tvEntrantTotal;
        @BindView(R.id.tvDescription)
        ExtTextView tvDescription;
        @BindView(R.id.llBottomAction)
        LinearLayout llBottomAction;
        @BindView(R.id.tvCheck)
        ExtTextView tvCheck;
        @BindView(R.id.tvClose)
        ExtTextView tvClose;
        @BindView(R.id.llNumber)
        FrameLayout llNumber;
        @BindView(R.id.ivUpOrDown)
        AppCompatImageView ivUpOrDown;
        @BindView(R.id.ivNumber)
        AppCompatImageView ivNumber;
        @BindView(R.id.tvJoin)
        ExtTextView tvJoin;
        @BindView(R.id.llOpacity)
        LinearLayout llOpacity;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
