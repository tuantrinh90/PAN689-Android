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
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class LeaguesAdapter extends ExtBaseAdapter<LeagueResponse, LeaguesAdapter.ViewHolder> {
    Consumer<LeagueResponse> detailConsumer;
    Consumer<LeagueResponse> approveConsumer;
    Consumer<LeagueResponse> rejectConsumer;
    Consumer<LeagueResponse> joinConsumer;

    public LeaguesAdapter(Context context, List<LeagueResponse> leagueResponses, Consumer<LeagueResponse> detailConsumer,
                          Consumer<LeagueResponse> approveConsumer,
                          Consumer<LeagueResponse> rejectConsumer,
                          Consumer<LeagueResponse> joinConsumer) {
        super(context, leagueResponses);
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
    protected void onBindViewHolder(ViewHolder holder, LeagueResponse leagueResponse) {
        holder.ivAvatar.setImageUri(leagueResponse.getLogo());
        holder.tvTitle.setText(leagueResponse.getName());
        holder.tvOwner.setText(leagueResponse.getUser().getName());
        holder.tvInvitor.setText("Chua Ro lOgic");
        holder.tvEntrantNumber.setText(String.valueOf(leagueResponse.getCurrentNumberOfUser()));
        holder.tvEntrantTotal.setText(String.valueOf(leagueResponse.getNumberOfUser()));
        holder.tvDescription.setText(leagueResponse.getDescription());

        // number
//        holder.ivUpOrDown.setBackgroundResource(leagueResponse.getType() == LeagueResponse.TYPE_INCREASE ? R.drawable.bg_green_arrow_up_circle :
//                (leagueResponse.getType() == LeagueResponse.TYPE_DECREASE ? R.drawable.bg_green_arrow_down_red : 0));
//        holder.ivUpOrDown.setImageResource(leagueResponse.getType() == LeagueResponse.TYPE_INCREASE ? R.drawable.ic_arrow_upward_white_small :
//                (leagueResponse.getType() == LeagueResponse.TYPE_DECREASE ? R.drawable.ic_arrow_down_white_small : 0));
//
//        holder.ivNumber.setImageResource(leagueResponse.getRate() == 1 ? R.drawable.ic_number_one :
//                (leagueResponse.getRate() == 2 ? R.drawable.ic_number_two : R.drawable.ic_number_three));

        // show/hide action
//        holder.trInvitor.setVisibility(leagueResponse.getStatus() == LeagueResponse.PENDING_LEAGUES ? View.VISIBLE : View.GONE);
//        holder.llBottomAction.setVisibility(leagueResponse.getStatus() == LeagueResponse.PENDING_LEAGUES ? View.VISIBLE : View.GONE);
//        holder.llNumber.setVisibility(leagueResponse.getStatus() == LeagueResponse.MY_LEAGUES && leagueResponse.getRate() >= 1 && leagueResponse.getRate() <= 3 ? View.VISIBLE : View.GONE);
//        holder.tvJoin.setVisibility(leagueResponse.getStatus() == LeagueResponse.OPEN_LEAGUES ? View.VISIBLE : View.GONE);

        // event
        RxView.clicks(holder.itemView).subscribe(v -> Optional.from(detailConsumer).doIfPresent(c -> c.accept(leagueResponse)));
        RxView.clicks(holder.tvJoin).subscribe(v -> Optional.from(joinConsumer).doIfPresent(c -> c.accept(leagueResponse)));
        RxView.clicks(holder.tvCheck).subscribe(v -> Optional.from(approveConsumer).doIfPresent(c -> c.accept(leagueResponse)));
        RxView.clicks(holder.tvClose).subscribe(v -> Optional.from(rejectConsumer).doIfPresent(c -> c.accept(leagueResponse)));
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
