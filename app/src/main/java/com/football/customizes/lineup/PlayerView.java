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

    public static final int ORDER_MINIMUM = 1;
    public static final int NONE_ORDER = -1;


    @BindView(R.id.ivRemove)
    public ImageView ivRemove;
    @BindView(R.id.ivPlayer)
    public ImageView ivPlayer;
    @BindView(R.id.tvContent)
    public TextView tvContent;
    @BindView(R.id.tvInjured)
    public TextView tvInjured;

    private OnPlayerViewClickListener mListener;
    private int order;
    private int position = -1; // vị trí: G, M, D, A
    private PlayerResponse player;
    private boolean named = true;
    private boolean removable;
    private boolean editable;
    private boolean addable;

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
            tvInjured.setVisibility(GONE);
        } else {
            ivRemove.setVisibility(removable ? VISIBLE : GONE);
            tvContent.setVisibility(VISIBLE);
            ImageLoaderUtils.displayImage(player.getPhoto(), ivPlayer);
            tvContent.setText(named ? player.getName() : getContext().getString(R.string.money_prefix, player.getTransferValueDisplay()));
            tvInjured.setVisibility(player.getInjured() ? VISIBLE : GONE);
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
            default:
                ivPlayer.setBackgroundResource(R.drawable.bg_player_none);
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

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
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

    public void setTextColor(int color) {
        tvContent.setTextColor(color);
    }

    @OnClick({R.id.ivRemove, R.id.ivPlayer})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.ivRemove:
                if (removable) {
                    Optional.from(mListener).doIfPresent(listener -> listener.onRemove(this, player, position, order));
                }
                break;
            case R.id.ivPlayer:
                if (player == null && addable) {
                    Optional.from(mListener).doIfPresent(listener -> listener.onAddPlayer(this, position, order));
                } else if (player != null && editable) {
                    Optional.from(mListener).doIfPresent(listener -> listener.onEdit(this, player, position, order));
                } else if (player != null) {
                    Optional.from(mListener).doIfPresent(listener -> listener.onClickPlayer(this, player, position, order));
                }
                break;
        }
    }

    public void setOnPlayerViewClickListener(OnPlayerViewClickListener listener) {
        this.mListener = listener;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
        ivRemove.setVisibility(removable ? VISIBLE : GONE);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setAddable(boolean addable) {
        this.addable = addable;
    }

    public interface OnPlayerViewClickListener {
        void onRemove(PlayerView view, PlayerResponse player, int position, int order);

        void onAddPlayer(PlayerView view, int position, int order);

        void onClickPlayer(PlayerView view, PlayerResponse player, int position, int order);

        void onEdit(PlayerView view, PlayerResponse player, int position, int order);
    }
}
