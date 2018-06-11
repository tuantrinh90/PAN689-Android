package com.football.customizes.lineup;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bon.image.ImageLoaderUtils;
import com.bon.interfaces.Optional;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerView extends LinearLayout {

    @BindView(R.id.ivRemove)
    ImageView ivRemove;
    @BindView(R.id.ivPlayer)
    ImageView ivPlayer;
    @BindView(R.id.tvContent)
    TextView tvContent;

    private OnPlayerViewClickListener mListener;
    private int position;
    private PlayerResponse player;

    public PlayerView(Context context) {
        this(context, null);
    }

    public PlayerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.player_view, this);
        ButterKnife.bind(this, view);
    }

    private void displayPlayer() {
        if (player == null) {
            ivRemove.setVisibility(GONE);
        } else {
            ivRemove.setVisibility(VISIBLE);
            ImageLoaderUtils.displayImage(player.getPhoto(), ivPlayer);
            tvContent.setText(player.getName());
        }
    }

    public PlayerResponse getPlayer() {
        return player;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPlayer(PlayerResponse player) {
        this.player = player;
        displayPlayer();
    }

    public void revmoePlayer() {
        player = null;
        displayPlayer();
    }

    @OnClick({R.id.ivRemove, R.id.ivPlayer})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.ivRemove:
                Optional.from(mListener).doIfPresent(listener -> listener.onRemove(position));
                break;
            case R.id.ivPlayer:
                Optional.from(mListener).doIfPresent(listener -> listener.onPlayerClick(player, position));
                break;
        }
    }

    public void setOnPlayerViewClickListener(OnPlayerViewClickListener listener) {
        this.mListener = listener;
    }

    public interface OnPlayerViewClickListener {
        void onRemove(int position);

        void onPlayerClick(PlayerResponse player, int position);
    }
}
