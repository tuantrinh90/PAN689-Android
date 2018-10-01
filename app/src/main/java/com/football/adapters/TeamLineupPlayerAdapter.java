package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.interfaces.Optional;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.AppUtilities;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.Consumer;

public class TeamLineupPlayerAdapter extends DefaultAdapter<PlayerResponse> {
    private Consumer<PlayerResponse> clickCallback;
    private CompositeDisposable mDisposable = new CompositeDisposable();


    public TeamLineupPlayerAdapter(Context context, List<PlayerResponse> dataSet, Consumer<PlayerResponse> clickCallback) {
        super(context, dataSet);
        this.clickCallback = clickCallback;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.team_lineup_player_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new PlayerHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, PlayerResponse data, int position) {
        PlayerHolder holder = (PlayerHolder) defaultHolder;
        ImageLoaderUtils.displayImage(data.getPhoto(), holder.ivAvatar);

        AppUtilities.displayPlayerPosition(holder.ivPositionPrimary, data.getMainPosition());
        AppUtilities.displayPlayerPosition(holder.ivPositionSecond, data.getMinorPosition());

        holder.tvName.setText(data.getName());

        mDisposable.add(RxView.clicks(holder.itemView).subscribe(o ->
                Optional.from(clickCallback).doIfPresent(d ->
                        d.accept(data))));
    }

    static class PlayerHolder extends DefaultHolder {

        @BindView(R.id.ivAvatar)
        CircleImageView ivAvatar;
        @BindView(R.id.ivPositionPrimary)
        ImageView ivPositionPrimary;
        @BindView(R.id.ivPositionSecond)
        ImageView ivPositionSecond;
        @BindView(R.id.tvName)
        ExtTextView tvName;

        public PlayerHolder(View itemView) {
            super(itemView);
        }
    }
}
