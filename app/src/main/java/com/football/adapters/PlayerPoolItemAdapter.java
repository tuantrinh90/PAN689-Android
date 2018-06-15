package com.football.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.AppUtilities;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.Consumer;

public class PlayerPoolItemAdapter extends ExtBaseAdapter<PlayerResponse, PlayerPoolItemAdapter.ViewHolder> {

    private final Consumer<PlayerResponse> clickConsumer;
    private CompositeDisposable mDisposable;
    private String option1;
    private String option2;
    private String option3;

    /**
     * @param ctx
     * @param its
     */
    public PlayerPoolItemAdapter(Context ctx, List<PlayerResponse> its, Consumer<PlayerResponse> clickConsumer) {
        super(ctx, its);
        this.clickConsumer = clickConsumer;

        mDisposable = new CompositeDisposable();
    }

    @Override
    protected int getViewId() {
        return R.layout.player_pool_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, PlayerResponse data) {
//        ImageLoaderUtils.displayImage(data.getPhoto(), holder.ivAvatar);
        holder.tvName.setText(data.getName());
        holder.tvClub.setText(data.getRealClub().getName());
        holder.tvOption1.setText(getOptionValue(holder.itemView.getContext(), data, option1));
        holder.tvOption2.setText(getOptionValue(holder.itemView.getContext(), data, option2));
        holder.tvOption3.setText(getOptionValue(holder.itemView.getContext(), data, option3));
        AppUtilities.displayPlayerPosition(holder.tvPositionPrimary, data.getMainPosition(), data.getMainPositionText());
        AppUtilities.displayPlayerPosition(holder.tvPositionSecond, data.getMinorPosition(), data.getMinorPositionText());
        boolean checked = data.getSelected();
        holder.ivAdd.setImageResource(checked ? R.drawable.ic_tick : R.drawable.ic_add_white_small);
        holder.ivAdd.setBackgroundResource(checked ? R.drawable.bg_circle_white_border : R.drawable.bg_circle_yellow);

        mDisposable.add(RxView.clicks(holder.itemView).subscribe(o ->
                Optional.from(clickConsumer).doIfPresent(d ->
                        d.accept(data))));
    }

    public void setOptions(String option1, String option2, String option3) {
        if (!TextUtils.isEmpty(option1)) {
            this.option1 = option1;
        }
        if (!TextUtils.isEmpty(option2)) {
            this.option2 = option2;
        }
        if (!TextUtils.isEmpty(option3)) {
            this.option3 = option3;
        }
    }

    /**
     * GOALS, ASSISTS, CLEAN_SHEET, DUELS_THEY_WIN, PASSES, SHOTS,
     * SAVES, YELLOW_CARDS, DRIBBLES, TURNOVERS, BALLS_RECOVERED, FOULS_COMMITTED
     */
    private String getOptionValue(Context context, PlayerResponse data, String option) {
        String value = "";
        switch (option) {
            case PlayerResponse.Options.TRANSFER_VALUE:
                value = context.getString(R.string.money_prefix, data.getTransferValueDisplay());
                break;
            case PlayerResponse.Options.POINT:
                value = String.valueOf(data.getPoint());
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

    class ViewHolder extends ExtPagingListView.ExtViewHolder {
        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.tvClub)
        ExtTextView tvClub;
        @BindView(R.id.tvPositionPrimary)
        ExtTextView tvPositionPrimary;
        @BindView(R.id.tvPositionSecond)
        ExtTextView tvPositionSecond;
        @BindView(R.id.tvOption1)
        ExtTextView tvOption1;
        @BindView(R.id.tvOption2)
        ExtTextView tvOption2;
        @BindView(R.id.ivChange)
        ImageView ivChange;
        @BindView(R.id.tvOption3)
        ExtTextView tvOption3;
        @BindView(R.id.ivAdd)
        ImageView ivAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
