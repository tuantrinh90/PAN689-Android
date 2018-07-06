package com.football.customizes.lineup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.bon.interfaces.Optional;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.function.TriConsumer;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import java8.util.function.BiConsumer;

public class LineupView extends FlexboxLayout implements PlayerView.OnPlayerViewClickListener {

    private static final int LINE = 4;
    public static final int NONE_ORDER = -1;

    private Context mContext;

    private TriConsumer<PlayerResponse, Integer, Integer> editCallback; //  player, position, order
    private TriConsumer<PlayerResponse, Integer, Integer> removeCallback; // player, position, order
    private BiConsumer<Integer, Integer> addCallback; // position, order
    private TriConsumer<PlayerResponse, Integer, Integer> infoCallback; // player, position, order

    private int[] formation = new int[]{4, 6, 6, 2}; // sắp xếp đội hình theo từng hàng, mỗi phần tử tương ứng với số lượng cầu thủ tại hàng đó
    private PlayerResponse[] players = new PlayerResponse[18];
    private boolean editable = false; // có thể edit player
    private boolean removable = false; // có thể remove player
    private boolean addable = false; // có thể add player

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
        setAlignItems(AlignItems.CENTER);
        setFlexWrap(FlexWrap.WRAP);
        setJustifyContent(JustifyContent.CENTER);

        setup();
    }

    private void setup() {
        this.removeAllViews();

        int position = 0;
        for (int line = 0; line < LINE; line++) {
            int playerCount = formation[line]; // số lượng cầu thủ trên 1 hàng
            for (int order = 0; order < playerCount; order++) {
                PlayerView view = createPlayerView(mContext, order, line);
                displayPlayer(view, (players.length == 0 || players.length <= position) ? null : players[position]);
                position++;
                view.setOnPlayerViewClickListener(this);
                this.addView(view);

                // wrap
                LayoutParams lp = new LayoutParams(view.getLayoutParams());
                setFlexItemAttributes(0, lp, order == 0);
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
    private PlayerView createPlayerView(Context context, int order, int line) {
        PlayerView playerView = new PlayerView(context);
        playerView.setOrder(order);
        playerView.setPosition(line);
        playerView.setRemovable(removable);
        playerView.setEditable(editable);
        playerView.setAddable(addable);
        return playerView;
    }

    public void setEditCallback(TriConsumer<PlayerResponse, Integer, Integer> editCallback) {
        this.editCallback = editCallback;
    }

    public void setRemoveCallback(TriConsumer<PlayerResponse, Integer, Integer> removeCallback) {
        this.removeCallback = removeCallback;
    }

    public void setAddCallback(BiConsumer<Integer, Integer> addCallback) {
        this.addCallback = addCallback;
    }

    public void setInfoCallback(TriConsumer<PlayerResponse, Integer, Integer> infoCallback) {
        this.infoCallback = infoCallback;
    }

    public void notifyDataSetChanged() {
        setup();
    }

    public void setupLineup(PlayerResponse[] players, String pitchSelect) {
        String[] arr = pitchSelect.split("-");
        int[] pitchSquad = new int[4];
        for (int i = 0; i < arr.length; i++) {
            pitchSquad[2 - i] = Integer.valueOf(arr[i]);
        }
        pitchSquad[3] = 1;
        setupLineup(players, pitchSquad);
    }

    public void setFormation(String string) {
        String[] arr = string.split("-");
        int[] formation = new int[4];
        for (int i = 0; i < arr.length; i++) {
            formation[2 - i] = Integer.valueOf(arr[i]);
        }
        formation[3] = 1;
        setFormation(formation);
    }

    public void setFormation(int[] formation) {
        this.formation = formation;
    }

    public void setupLineup(PlayerResponse[] players, int[] formation) {
        setFormation(formation);
        setPlayers(players);
        setup();
    }

    public void setPlayers(PlayerResponse[] players) {
        this.players = players;
    }

    public void addPlayer(PlayerResponse player, int line, Integer order) {
        int position = getPosition(null, 3 - line, order);
        if (position != -1) {
            setPlayer(player, position);
        }
    }

    public void removePlayer(PlayerResponse player, int line) {
        int position = getPosition(player, 3 - line, -1);
        if (position != -1) {
            setPlayer(null, position);
        }
    }

    private int getPosition(PlayerResponse player, int line, Integer order) {
        int index = 0;
        for (int i = 0; i < line; i++) {
            index += formation[i];
        }
        for (int i = 0; i < formation[line]; i++) {
            if (order != NONE_ORDER) {
                if (i == order) {
                    return index;
                }
            } else if (((PlayerView) getChildAt(index)).getPlayer() == player) {
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
    public void onRemove(PlayerResponse player, int position, int order) {
        Optional.from(removeCallback).doIfPresent(c -> c.accept(player, 3 - position, order));
    }

    @Override
    public void onAddPlayer(int position, int order) {
        Optional.from(addCallback).doIfPresent(c -> c.accept(3 - position, order));
    }

    @Override
    public void onClickPlayer(PlayerResponse player, int position, int order) {
        Optional.from(infoCallback).doIfPresent(c -> c.accept(player, 3 - position, order));
    }

    @Override
    public void onEdit(PlayerResponse player, int position, int order) {
        Optional.from(editCallback).doIfPresent(c -> c.accept(player, 3 - position, order));
    }

    public void clear() {
        removeAllViews();
    }

    public boolean isSetupComplete() {
        for (PlayerResponse player : players) {
            if (player == null) return false;
        }
        return true;
    }

    public void displayByName(boolean bool) {
        for (int i = 0; i < getChildCount(); i++) {
            ((PlayerView) getChildAt(i)).displayName(bool);
        }
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public void setAddable(boolean addable) {
        this.addable = addable;
    }

    public boolean isFullPosition(int position) {
        int maxPlayers = formation[3 - position];
        int counter = 0;
        for (PlayerResponse player : players) {
            if (player != null && player.getMainPosition().equals(position)) {
                counter++;
                if (maxPlayers == counter) {
                    return true;
                }
            }
        }
        return false;
    }
}
