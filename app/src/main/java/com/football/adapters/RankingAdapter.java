package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.RankingResponse;

import butterknife.BindView;

public class RankingAdapter extends DefaultAdapter<RankingResponse> {


    public RankingAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.league_detail_ranking_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new RankingHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, RankingResponse data, int position) {
        RankingHolder holder = (RankingHolder) defaultHolder;

        holder.textTitle.setText(data.getName());
        holder.textPlayMatch.setText(String.valueOf(data.getResult().getPlayed()));
        holder.textWin.setText(String.valueOf(data.getResult().getWin()));
        holder.textDraw.setText(String.valueOf(data.getResult().getDraw()));
        holder.textLose.setText(String.valueOf(data.getResult().getLose()));
        holder.textPts.setText(String.valueOf(data.getResult().getPoints()));

        if (data.getRank() <= 3 && data.getRank() > 0) {
            holder.textNumber.setVisibility(View.GONE);
            holder.imageNumber.setVisibility(View.VISIBLE);
            if (data.getRank() == 1) {
                holder.imageNumber.setImageResource(R.drawable.ic_number_one);
            } else if (data.getRank() == 2) {
                holder.imageNumber.setImageResource(R.drawable.ic_number_two);
            } else if (data.getRank() == 3) {
                holder.imageNumber.setImageResource(R.drawable.ic_number_three);
            }
        } else {
            holder.textNumber.setText(String.valueOf(data.getRank()));
            holder.textNumber.setVisibility(View.VISIBLE);
            holder.imageNumber.setVisibility(View.GONE);
        }
    }

    static class RankingHolder extends DefaultHolder {

        @BindView(R.id.text_title)
        ExtTextView textTitle;
        @BindView(R.id.text_play_match)
        ExtTextView textPlayMatch;
        @BindView(R.id.text_win)
        ExtTextView textWin;
        @BindView(R.id.text_draw)
        ExtTextView textDraw;
        @BindView(R.id.text_lose)
        ExtTextView textLose;
        @BindView(R.id.text_pts)
        ExtTextView textPts;
        @BindView(R.id.image_number)
        ImageView imageNumber;
        @BindView(R.id.text_number)
        ExtTextView textNumber;

        public RankingHolder(View itemView) {
            super(itemView);
        }
    }
}
