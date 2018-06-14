package com.football.customizes.lineup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.bon.interfaces.Optional;
import com.football.models.responses.PlayerResponse;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java8.util.function.BiConsumer;

public class LineupView extends FlexboxLayout implements PlayerView.OnPlayerViewClickListener {

    private static final int LINE = 4;

    private Context mContext;

    private BiConsumer<PlayerResponse, Integer> removeConsumer;
    private BiConsumer<Integer, Integer> clickConsumer;

    private int[] squad = new int[]{4, 6, 6, 2}; // sắp xếp đội hình theo từng hàng, mỗi phần tử tương ứng với số lượng cầu thủ tại hàng đó
    private PlayerResponse[] players = new PlayerResponse[18];

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
        setAlignItems(AlignItems.CENTER);
        setFlexWrap(FlexWrap.WRAP);
        setJustifyContent(JustifyContent.CENTER);

        setup();
    }

    private void setup() {
        this.removeAllViews();

        int position = 0;
        for (int line = 0; line < LINE; line++) {
            int playerCount = squad[line]; // số lượng cầu thủ trên 1 hàng
            for (int i = 0; i < playerCount; i++) {
                PlayerView view = createPlayerView(mContext, players.length - position - 1, line);
                displayPlayer(view, null);
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

    private void displayPlayer(PlayerView view, PlayerResponse player) {
        view.setPlayer(player);
    }

    private static void setFlexItemAttributes(int margin, FlexboxLayout.LayoutParams lp, boolean isWrapBefore) {
        lp.setMargins(margin, 0, margin, 0);

        lp.setFlexGrow(FlexboxLayout.LayoutParams.FLEX_GROW_DEFAULT);
        lp.setFlexShrink(FlexboxLayout.LayoutParams.FLEX_SHRINK_DEFAULT);
        lp.setWrapBefore(isWrapBefore);
    }

    @NonNull
    private PlayerView createPlayerView(Context context, int i, int line) {
        PlayerView playerView = new PlayerView(context);
        playerView.setIndex(i);
        playerView.setPosition(line);
        return playerView;
    }

    public void setRemoveConsumer(BiConsumer<PlayerResponse, Integer> removeConsumer) {
        this.removeConsumer = removeConsumer;
    }

    public void setClickConsumer(BiConsumer<Integer, Integer> clickConsumer) {
        this.clickConsumer = clickConsumer;
    }

    public void notifyDataSetChanged() {
        setup();
    }

    public void setupLineup(PlayerResponse[] players, int[] squad) {
        setSquad(squad);
        setPlayers(players);
        setup();
    }

    public void setSquad(int[] squad) {
        this.squad = squad;
    }

    public void setPlayers(PlayerResponse[] players) {
        this.players = players;
    }

    public void addPlayer(PlayerResponse player, int line) {
        int position = getPosition(null, line);
        if (position != -1) {
            setPlayer(player, position);
        }
    }

    public void removePlayer(PlayerResponse player, int line) {
        int position = getPosition(player, line);
        if (position != -1) {
            setPlayer(null, position);
        }
    }

    private int getPosition(PlayerResponse player, int line) {
        int index = 0;
        for (int i = 0; i < line; i++) {
            index += squad[i];
        }
        for (int i = 0; i < squad[line]; i++) {
            if (((PlayerView) getChildAt(index)).getPlayer() == player) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public void setPlayer(PlayerResponse player, int position) {
        this.players[position] = player;
        PlayerView view = (PlayerView) getChildAt(position);
        displayPlayer(view, player);
    }

    @Override
    public void onRemove(PlayerResponse player, int position) {
        Optional.from(removeConsumer).doIfPresent(c -> c.accept(player, position));
    }

    @Override
    public void onPlayerClick(int position, int index) {
        Optional.from(clickConsumer).doIfPresent(c -> c.accept(position, index));
    }

    public void clear() {
        removeAllViews();
    }

    public void displayByName(boolean bool) {
        for (int i = 0; i < getChildCount(); i++) {
            ((PlayerView) getChildAt(i)).displayName(bool);
        }
    }
}
