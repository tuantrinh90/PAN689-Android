package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.AppUtilities;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.Consumer;

public class PlayerPoolAdapter extends DefaultAdapter<PlayerResponse> {
    private CompositeDisposable mDisposable;
    private final Consumer<PlayerResponse> clickConsumer;

    // options
    private Consumer<PlayerResponse> deleteConsumer;
    private Consumer<PlayerResponse> addConsumer;

    private String option1;
    private String option2;
    private String option3;
    private boolean visibleAddButton = true;

    public PlayerPoolAdapter(Context context, Consumer<PlayerResponse> clickConsumer) {
        super(context);
        this.clickConsumer = clickConsumer;
        mDisposable = new CompositeDisposable();
    }

    public void setOptionDeleteCallback(Consumer<PlayerResponse> deleteConsumer) {
        this.deleteConsumer = deleteConsumer;
    }

    public void setOptionAddCallback(Consumer<PlayerResponse> addConsumer) {
        this.addConsumer = addConsumer;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new PlayerHolder(v);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.player_pool_item;
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, PlayerResponse data, int position) {
        PlayerHolder holder = (PlayerHolder) defaultHolder;

        holder.tvName.setText(data.getName());
        holder.tvClub.setText(data.getRealClub().getName());
        holder.tvOption1.setText(getOptionValue(holder.itemView.getContext(), data, option1, holder.ivOption1));
        holder.tvOption2.setText(getOptionValue(holder.itemView.getContext(), data, option2, holder.ivOption2));
        holder.tvOption3.setText(getOptionValue(holder.itemView.getContext(), data, option3, holder.ivOption3));

        holder.ivDelete.setVisibility(deleteConsumer != null ? View.VISIBLE : View.GONE);
        holder.ivDelete.setOnClickListener(v -> {
            if (deleteConsumer != null) {
                deleteConsumer.accept(getItem(defaultHolder.getAdapterPosition()));
            }
        });

        if (visibleAddButton && addConsumer != null) {
            holder.ivAdd.setVisibility(View.VISIBLE);
            boolean checked = data.getSelected();
            holder.ivAdd.setImageResource(checked ? R.drawable.ic_check_green_24_px_2 : R.drawable.ic_add_circle_white);
            holder.ivAdd.setBackgroundResource(checked ? R.drawable.bg_circle_white_border : R.drawable.bg_circle_yellow);
        } else {
            holder.ivAdd.setVisibility(View.GONE);
        }

        holder.ivAdd.setOnClickListener(v -> {
            if (addConsumer != null) {
                addConsumer.accept(getItem(defaultHolder.getAdapterPosition()));
            }
        });

        AppUtilities.displayPlayerPosition(holder.ivPositionPrimary, data.getMainPosition());
        AppUtilities.displayPlayerPosition(holder.ivPositionSecond, data.getMinorPosition());

        mDisposable.add(RxView.clicks(holder.itemView).subscribe(o ->
                Optional.from(clickConsumer).doIfPresent(d -> d.accept(data))));
    }

    public void setOptions(String option1, String option2, String option3) {
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
    }

    /**
     * GOALS, ASSISTS, CLEAN_SHEET, DUELS_THEY_WIN, PASSES, SHOTS,
     * SAVES, YELLOW_CARDS, DRIBBLES, TURNOVERS, BALLS_RECOVERED, FOULS_COMMITTED
     */
    private String getOptionValue(Context context, PlayerResponse data, String option, ImageView ivOption) {
        ivOption.setVisibility(View.GONE);
        String value = "";
        switch (option) {
            case PlayerResponse.Options.VALUE:
                value = context.getString(R.string.money_prefix, data.getTransferValueDisplay());
                break;
            case PlayerResponse.Options.POINT:
                value = String.valueOf(data.getPoint());
                // set ivOption up or down here
                if (data.getRankStatus() == PlayerResponse.RANK_STATUS_NONE) {
                    ivOption.setVisibility(View.GONE);
                } else if (data.getRankStatus() == PlayerResponse.RANK_STATUS_DOWN) {
                    ivOption.setVisibility(View.VISIBLE);
                    ivOption.setImageResource(R.drawable.ic_status_down);
                } else if (data.getRankStatus() == PlayerResponse.RANK_STATUS_UP) {
                    ivOption.setVisibility(View.VISIBLE);
                    ivOption.setImageResource(R.drawable.ic_status_up);
                }

                break;
            case PlayerResponse.Options.GOALS:
                value = String.valueOf(data.getGoals());
                break;
            case PlayerResponse.Options.ASSISTS:
                value = String.valueOf(data.getAssists());
                break;
            case PlayerResponse.Options.CLEAN_SHEET:
                value = String.valueOf(data.getCleanSheet());
                break;
            case PlayerResponse.Options.DUELS_THEY_WIN:
                value = String.valueOf(data.getDuelsTheyWin());
                break;
            case PlayerResponse.Options.PASSES:
                value = String.valueOf(data.getPasses());
                break;
            case PlayerResponse.Options.SHOTS:
                value = String.valueOf(data.getShots());
                break;
            case PlayerResponse.Options.SAVES:
                value = String.valueOf(data.getSaves());
                break;
            case PlayerResponse.Options.YELLOW_CARDS:
                value = String.valueOf(data.getYellowCards());
                break;
            case PlayerResponse.Options.DRIBBLES:
                value = String.valueOf(data.getDribbles());
                break;
            case PlayerResponse.Options.TURNOVERS:
                value = String.valueOf(data.getTurnovers());
                break;
            case PlayerResponse.Options.BALLS_RECOVERED:
                value = String.valueOf(data.getBallsRecovered());
                break;
            case PlayerResponse.Options.FOULS_COMMITTED:
                value = String.valueOf(data.getFoulsCommitted());
                break;
        }
        return value;
    }

    public void setVisibleAddButton(boolean visibleAddButton) {
        this.visibleAddButton = visibleAddButton;
    }

    public static class PlayerHolder extends DefaultHolder {
        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.tvClub)
        ExtTextView tvClub;
        @BindView(R.id.ivPositionPrimary)
        ImageView ivPositionPrimary;
        @BindView(R.id.ivPositionSecond)
        ImageView ivPositionSecond;
        @BindView(R.id.tvOption1)
        ExtTextView tvOption1;
        @BindView(R.id.tvOption2)
        ExtTextView tvOption2;
        @BindView(R.id.tvOption3)
        ExtTextView tvOption3;
        @BindView(R.id.ivOption1)
        ImageView ivOption1;
        @BindView(R.id.ivOption2)
        ImageView ivOption2;
        @BindView(R.id.ivOption3)
        ImageView ivOption3;
        @BindView(R.id.ivDelete)
        ImageView ivDelete;
        @BindView(R.id.ivAdd)
        ImageView ivAdd;

        public PlayerHolder(View itemView) {
            super(itemView);
        }
    }
}
