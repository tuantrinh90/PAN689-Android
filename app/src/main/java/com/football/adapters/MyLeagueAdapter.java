package com.football.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.interfaces.Optional;
import com.bon.util.GeneralUtils;
import com.football.fantasy.R;
import com.football.models.League;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import java8.util.function.Consumer;

public class MyLeagueAdapter extends RecyclerView.Adapter<MyLeagueAdapter.ViewHolder> {
    Context context;
    List<League> leagues;
    Consumer<League> leagueConsumer;

    public MyLeagueAdapter(Context context, List<League> leagues, Consumer<League> leagueConsumer) {
        this.context = context;
        this.leagues = leagues;
        this.leagueConsumer = leagueConsumer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.my_league_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemCount() <= 0) return;

        League league = leagues.get(position);
        ImageLoaderUtils.displayImage(league.getAvatar(), holder.ivAvatar, ImageLoaderUtils.getDisplayImageOption(R.drawable.bg_avatar_default));
        holder.tvTitle.setText(league.getTitle());
        holder.tvDescription.setText(league.getDescription());
        holder.tvRankNumber.setText(String.valueOf(league.getRankNumber()));
        holder.tvRankTotal.setText(String.valueOf(league.getRankTotal()));
        holder.ivArrowDown.setOnClickListener(v -> Optional.from(leagueConsumer).doIfPresent(c -> c.accept(league)));

        // update layout
        DisplayMetrics displayMetrics = GeneralUtils.getDisplayMetrics(context);
        int marginLayout = GeneralUtils.convertDpMeasureToPixel(context, R.dimen.padding_layout);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(displayMetrics.widthPixels - marginLayout * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(position == 0 ? marginLayout : marginLayout / 3, 0, (position == getItemCount() - 1) ? marginLayout : marginLayout / 3, 0);
        holder.llContainer.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return leagues == null ? 0 : leagues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llContainer)
        LinearLayout llContainer;
        @BindView(R.id.ivAvatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tvTitle)
        ExtTextView tvTitle;
        @BindView(R.id.tvDescription)
        ExtTextView tvDescription;
        @BindView(R.id.tvRankNumber)
        ExtTextView tvRankNumber;
        @BindView(R.id.tvRankTotal)
        ExtTextView tvRankTotal;
        @BindView(R.id.ivArrowUp)
        AppCompatImageView ivArrowUp;
        @BindView(R.id.ivArrowDown)
        AppCompatImageView ivArrowDown;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
