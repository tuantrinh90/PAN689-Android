package com.football.customizes.lineup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.bon.interfaces.Optional;
import com.football.fantasy.R;
import com.football.models.Player;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java8.util.function.BiConsumer;
import java8.util.function.Consumer;

public class LineupView extends FlexboxLayout implements PlayerView.OnPlayerViewClickListener {

    private static final int LINE = 4;

    private Context mContext;

    private Consumer<Integer> removeConsumer;
    private BiConsumer<Player, Integer> playerBiConsumer;

    private int[] squad = new int[]{1, 3, 5, 2}; // sắp xếp đội hình theo từng hàng, mỗi phần tử tương ứng với số lượng cầu thủ tại hàng đó
    private Player[] players;

    public LineupView(Context context) {
        this(context, null);
    }

    public LineupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
//        setAlignContent(AlignContent.SPACE_AROUND);
//        setAlignItems(AlignItems.CENTER);
        setFlexWrap(FlexWrap.WRAP);
        setJustifyContent(JustifyContent.SPACE_EVENLY);

        setup();
    }

    private void setup() {
        this.removeAllViews();

        int position = 0;
        for (int line = LINE - 1; line >= 0; line--) {
            int playerCount = squad[line]; // số lượng cầu thủ trên 1 hàng
            for (int i = 0; i < playerCount; i++) {
                PlayerView view = createPlayerView(mContext);
                Player player = null;
                if (players != null && players.length > position) {
                    player = players[position];
                }
                displayPlayer(view, player, position);
                position++;
                view.setOnPlayerViewClickListener(this);
                this.addView(view);

                // wrap
                LayoutParams lp = new LayoutParams(view.getLayoutParams());
                setFlexItemAttributes(0, lp, i == 0);
                view.setLayoutParams(lp);
            }
        }
    }

    private int getMargin(int playerCount) {
        int margin;
        switch (playerCount) {
            case 2:
            case 3:
                margin = mContext.getResources().getDimensionPixelSize(R.dimen.player_margin_large);
                break;
            case 4:
                margin = mContext.getResources().getDimensionPixelSize(R.dimen.player_margin_normal);
                break;
            case 5:
                margin = mContext.getResources().getDimensionPixelSize(R.dimen.player_margin_small);
                break;
            case 6:
            default:
                margin = mContext.getResources().getDimensionPixelSize(R.dimen.player_margin_none);
                break;

        }

        return margin;
    }

    private void displayPlayer(PlayerView view, Player player, int position) {
        view.setPlayer(player, position);
    }

    private static void setFlexItemAttributes(int margin, FlexboxLayout.LayoutParams lp, boolean isWrapBefore) {
        lp.setMargins(margin, 0, margin, 0);

        lp.setFlexGrow(FlexboxLayout.LayoutParams.FLEX_GROW_DEFAULT);
        lp.setFlexShrink(FlexboxLayout.LayoutParams.FLEX_SHRINK_DEFAULT);
        lp.setWrapBefore(isWrapBefore);
    }


    @NonNull
    private PlayerView createPlayerView(Context context) {
        return new PlayerView(context);
    }

    public void setRemoveConsumer(Consumer<Integer> removeConsumer) {
        this.removeConsumer = removeConsumer;
    }

    public void setPlayerBiConsumer(BiConsumer<Player, Integer> playerBiConsumer) {
        this.playerBiConsumer = playerBiConsumer;
    }

    public void notifyDataSetChanged() {
        setup();
    }

    public void setLineup(Player[] players, int[] squad) {
        setSquad(squad);
        setPlayers(players);
    }

    public void setSquad(int[] squad) {
        this.squad = squad;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public void addPlayer(Player player, int line) {
        for (int i = 0; i < squad[line]; i++) {
            int position = getPosition(line, i);
            if (players != null && players.length > position && players[position] != null) {
                setPlayer(player, position);
            }
        }
    }

    public void setPlayer(Player player, int position) {
        this.players[position] = player;
        PlayerView view = (PlayerView) getChildAt(position);
        displayPlayer(view, player, position);
    }

    public void setPlayer(Player player, int line, int positionInLine) {
        int index = getPosition(line, positionInLine);
        setPlayer(player, index);
    }

    private int getPosition(int line, int positionInLine) {
        int index = 0;
        for (int i = 0; i < line; i++) {
            index += squad[i];
        }
        index += positionInLine;
        return index;
    }

    public void removePlayerView(int position) {

    }

    @Override
    public void onRemove(int position) {
        Optional.from(removeConsumer).doIfPresent(c -> c.accept(position));
    }

    @Override
    public void onPlayerClick(Player player, int position) {
        Optional.from(playerBiConsumer).doIfPresent(c -> c.accept(player, position));
    }
}
