package com.football.adapters;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.UserResponse;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class LeaguesAdapter extends ExtBaseAdapter<LeagueResponse, LeaguesAdapter.ViewHolder> {
    public static final int OPEN_LEAGUES = 1, MY_LEAGUES = 2, PENDING_INVITATIONS = 3;

    private int leagueType;
    private Consumer<LeagueResponse> detailConsumer;
    private Consumer<LeagueResponse> approveConsumer;
    private Consumer<LeagueResponse> rejectConsumer;
    private Consumer<LeagueResponse> joinConsumer;

    public LeaguesAdapter(Context context, int leagueType, List<LeagueResponse> leagueResponses, Consumer<LeagueResponse> detailConsumer,
                          Consumer<LeagueResponse> approveConsumer,
                          Consumer<LeagueResponse> rejectConsumer,
                          Consumer<LeagueResponse> joinConsumer) {
        super(context, leagueResponses);
        this.leagueType = leagueType;
        this.detailConsumer = detailConsumer;
        this.approveConsumer = approveConsumer;
        this.rejectConsumer = rejectConsumer;
        this.joinConsumer = joinConsumer;
    }

    @Override
    protected int getViewId() {
        return R.layout.league_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, LeagueResponse league) {
        holder.ivAvatar.setImageUri(league.getLogo());
        holder.tvTitle.setText(league.getName());
        try {
            holder.tvOwner.setText(league.getOwner() ? holder.itemView.getContext().getString(R.string.me) : league.getUser().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvEntrantNumber.setText(String.valueOf(league.getCurrentNumberOfUser()));
        holder.tvEntrantTotal.setText(String.valueOf(league.getNumberOfUser()));
        holder.tvDescription.setText(league.getDescriptionText(context));

        // hide up/down
        holder.ivUpOrDown.setVisibility(View.GONE);
        if (leagueType == MY_LEAGUES) {
            if (league.getRankStatus() != 0) {
                holder.ivUpOrDown.setVisibility(View.VISIBLE);
                holder.ivUpOrDown.setBackgroundResource(league.getRankStatus() > 0 ? R.drawable.bg_green_arrow_up_circle :
                        (league.getRankStatus() < 0 ? R.drawable.bg_green_arrow_down_red : 0));
                holder.ivUpOrDown.setImageResource(league.getRankStatus() > 0 ? R.drawable.ic_arrow_upward_white_small :
                        (league.getRankStatus() < 0 ? R.drawable.ic_arrow_down_white_small : 0));
            }

            holder.tvNumber.setText(String.valueOf(league.getRank()));
            holder.tvNumber.setVisibility(league.getRank() > 3 ? View.VISIBLE : View.GONE);
            holder.ivNumber.setVisibility(league.getRank() <= 3 ? View.VISIBLE : View.GONE);
            holder.ivNumber.setImageResource(league.getRank() == 1 ? R.drawable.ic_number_one :
                    (league.getRank() == 2 ? R.drawable.ic_number_two : (league.getRank() == 2 ? R.drawable.ic_number_three : 0)));
        }

        if (leagueType == PENDING_INVITATIONS) {
            UserResponse sender = league.getInvitation() != null ? league.getInvitation().getSender() : null;
            holder.tvInvitor.setText(sender != null ? sender.getName() : "");
        }

        // show/hide action
        holder.llNumber.setVisibility(leagueType == MY_LEAGUES ? View.VISIBLE : View.GONE);
        holder.trInvitor.setVisibility(leagueType == PENDING_INVITATIONS ? View.VISIBLE : View.GONE);
        holder.llBottomAction.setVisibility(leagueType == PENDING_INVITATIONS ? View.VISIBLE : View.GONE);
        holder.tvJoin.setVisibility(leagueType == OPEN_LEAGUES && league.getCurrentNumberOfUser() < league.getNumberOfUser() ? View.VISIBLE : View.GONE);

        // event
        RxView.clicks(holder.itemView).subscribe(v -> Optional.from(detailConsumer).doIfPresent(c -> c.accept(league)));
        RxView.clicks(holder.tvJoin).subscribe(v -> Optional.from(joinConsumer).doIfPresent(c -> c.accept(league)));
        RxView.clicks(holder.tvCheck).subscribe(v -> Optional.from(approveConsumer).doIfPresent(c -> c.accept(league)));
        RxView.clicks(holder.tvClose).subscribe(v -> Optional.from(rejectConsumer).doIfPresent(c -> c.accept(league)));
    }

    static class ViewHolder extends ExtPagingListView.ExtViewHolder {
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
        ImageView tvCheck;
        @BindView(R.id.tvClose)
        ImageView tvClose;
        @BindView(R.id.llNumber)
        FrameLayout llNumber;
        @BindView(R.id.ivUpOrDown)
        ImageView ivUpOrDown;
        @BindView(R.id.ivNumber)
        ImageView ivNumber;
        @BindView(R.id.tvNumber)
        ExtTextView tvNumber;
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
