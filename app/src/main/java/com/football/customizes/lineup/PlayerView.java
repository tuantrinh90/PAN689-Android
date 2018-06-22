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
    private int index;
    private int position; // vị trí: G, M, D, A
    private PlayerResponse player;
    private boolean named = true;
    private boolean editable;

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
            ivRemove.setVisibility(INVISIBLE);
            tvContent.setVisibility(INVISIBLE);
            ivPlayer.setImageResource(0);
        } else {
            ivRemove.setVisibility(editable ? VISIBLE : GONE);
            tvContent.setVisibility(VISIBLE);
            ImageLoaderUtils.displayImage(player.getPhoto(), ivPlayer);
            tvContent.setText(named ? player.getName() : getContext().getString(R.string.money_prefix, player.getTransferValueDisplay()));
        }
        switch (3 - position) {
            case PlayerResponse.POSITION_GOALKEEPER:
                ivPlayer.setBackgroundResource(R.drawable.bg_player_g);
                break;
            case PlayerResponse.POSITION_DEFENDER:
                ivPlayer.setBackgroundResource(R.drawable.bg_player_d);
                break;
            case PlayerResponse.POSITION_MIDFIELDER:
                ivPlayer.setBackgroundResource(R.drawable.bg_player_m);
                break;
            case PlayerResponse.POSITION_ATTACKER:
                ivPlayer.setBackgroundResource(R.drawable.bg_player_a);
                break;
        }
    }

    public void displayName(boolean bool) {
        named = bool;
        if (player != null) {
            tvContent.setText(bool ? player.getName() : getContext().getString(R.string.money_prefix, player.getTransferValueDisplay()));
        }
    }

    public PlayerResponse getPlayer() {
        return player;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setPlayer(PlayerResponse player) {
        this.player = player;
        displayPlayer();
    }

    @OnClick({R.id.ivRemove, R.id.ivPlayer})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.ivRemove:
                if (editable) {
                    Optional.from(mListener).doIfPresent(listener -> listener.onRemove(player, index));
                }
                break;
            case R.id.ivPlayer:
                if (player == null && editable) {
                    Optional.from(mListener).doIfPresent(listener -> listener.onPlayerClick(position, index));
                }
                break;
        }
    }

    public void setOnPlayerViewClickListener(OnPlayerViewClickListener listener) {
        this.mListener = listener;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        ivRemove.setVisibility(editable ? VISIBLE : GONE);
    }

    public interface OnPlayerViewClickListener {
        void onRemove(PlayerResponse player, int position);

        void onPlayerClick(int position, int index);
    }
}
