package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.interfaces.Optional;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.UserResponse;
import com.football.utilities.AppUtilities;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import java8.util.function.Consumer;

public class LeaguesAdapter extends DefaultAdapter<LeagueResponse> {
    public static final int OPEN_LEAGUES = 1, MY_LEAGUES = 2, PENDING_INVITATIONS = 3;

    private int leagueType;
    private Consumer<LeagueResponse> clickCallback;
    private Consumer<LeagueResponse> approveCallback;
    private Consumer<LeagueResponse> rejectCallback;
    private Consumer<LeagueResponse> joinCallback;

    public LeaguesAdapter(Context context, int leagueType, Consumer<LeagueResponse> clickCallback,
                          Consumer<LeagueResponse> approveCallback,
                          Consumer<LeagueResponse> rejectCallback,
                          Consumer<LeagueResponse> joinCallback) {
        super(context);
        this.leagueType = leagueType;
        this.clickCallback = clickCallback;
        this.approveCallback = approveCallback;
        this.rejectCallback = rejectCallback;
        this.joinCallback = joinCallback;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.league_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, LeagueResponse league, int position) {
        ViewHolder holder = (ViewHolder) defaultHolder;

        ImageLoaderUtils.displayImage(league.getLogo(), holder.ivAvatar);
        holder.tvTitle.setText(league.getName());
        holder.tvOwner.setText(AppUtilities.getNameOrMe(mContext, league));
        holder.tvEntrantNumber.setText(String.valueOf(league.getCurrentNumberOfUser()));
        holder.tvEntrantTotal.setText(String.valueOf(league.getNumberOfUser()));
        holder.tvDescription.setText(league.getDescriptionText(mContext));

        // hide up/down
        holder.ivUpOrDown.setVisibility(View.GONE);
        if (leagueType == MY_LEAGUES) {
            int rankStatus = league.getRankStatus();
            if (rankStatus != 0) {
                holder.ivUpOrDown.setVisibility(View.VISIBLE);
                holder.ivUpOrDown.setBackgroundResource(rankStatus > 0 ? R.drawable.bg_green_arrow_up_circle :
                        R.drawable.bg_green_arrow_down_red);
                holder.ivUpOrDown.setImageResource(rankStatus > 0 ? R.drawable.ic_arrow_upward_white_small :
                        R.drawable.ic_arrow_down_white_small);
            }

            if (league.getRank() <= 3 && league.getRank() > 0) {
                holder.tvNumber.setVisibility(View.GONE);
                holder.ivNumber.setVisibility(View.VISIBLE);
                if (league.getRank() == 1) {
                    holder.ivNumber.setImageResource(R.drawable.ic_number_one);
                } else if (league.getRank() == 2) {
                    holder.ivNumber.setImageResource(R.drawable.ic_number_two);
                } else if (league.getRank() == 3) {
                    holder.ivNumber.setImageResource(R.drawable.ic_number_three);
                }
            } else {
                holder.tvNumber.setText(String.valueOf(league.getRank()));
                holder.tvNumber.setVisibility(View.VISIBLE);
                holder.ivNumber.setVisibility(View.GONE);
            }
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
        if (league.getStatus().equals(LeagueResponse.ON_GOING)) {
            holder.llBottomStatus.setBackgroundResource(R.drawable.bg_green_gradient_radius_bottom);
        } else if (league.getStatus().equals(LeagueResponse.FINISHED)) {
            holder.llBottomStatus.setBackgroundResource(R.drawable.bg_yellow_gradient_radius_bottom);
        } else {
            holder.llBottomStatus.setBackgroundResource(R.drawable.bg_blue_gradient_radius_bottom);
        }

        // event
        RxView.clicks(holder.itemView).subscribe(v -> Optional.from(clickCallback).doIfPresent(c -> c.accept(league)));
        RxView.clicks(holder.tvJoin).subscribe(v -> Optional.from(joinCallback).doIfPresent(c -> c.accept(league)));
        RxView.clicks(holder.tvCheck).subscribe(v -> Optional.from(approveCallback).doIfPresent(c -> c.accept(league)));
        RxView.clicks(holder.tvClose).subscribe(v -> Optional.from(rejectCallback).doIfPresent(c -> c.accept(league)));
    }

    static class ViewHolder extends DefaultHolder {
        @BindView(R.id.ivAvatar)
        ImageView ivAvatar;
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
        @BindView(R.id.llBottomStatus)
        LinearLayout llBottomStatus;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
