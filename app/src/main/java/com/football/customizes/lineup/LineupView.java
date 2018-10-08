package com.football.customizes.lineup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.football.models.responses.PlayerResponse;
import com.football.utilities.function.TriConsumer;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;

public class LineupView extends FlexboxLayout implements PlayerView.OnPlayerViewClickListener {

    private static final int LINE = 4;

    private Context mContext;

    private TriConsumer<PlayerResponse, Integer, Integer> editCallback; //  player, position, order
    private TriConsumer<PlayerResponse, Integer, Integer> removeCallback; // player, position, order
    private TriConsumer<PlayerView, Integer, Integer> addCallback; // position, order
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
        // clear data
        for (int i = 0, playersLength = players.length; i < playersLength; i++) {
            players[i] = null;
        }

        int position = 0;
        for (int line = 0; line < LINE; line++) {
            int playerCount = formation[line]; // số lượng cầu thủ trên 1 hàng
            for (int order = PlayerView.ORDER_MINIMUM; order < playerCount + PlayerView.ORDER_MINIMUM; order++) {
                PlayerView view = createPlayerView(mContext, order, line);
                displayPlayer(view, (players.length == 0 || players.length <= position) ? null : players[position]);
                position++;
                view.setOnPlayerViewClickListener(this);
                this.addView(view);

                // wrap
                LayoutParams lp = new LayoutParams(view.getLayoutParams());
                setFlexItemAttributes(0, lp, order == PlayerView.ORDER_MINIMUM);
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

    public void setAddCallback(TriConsumer<PlayerView, Integer, Integer> addCallback) {
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

    private static final String TAG = "LineupView";

    public PlayerView getPlayerView(int line, int order) {
        int position = getPosition(null, 3 - line, order);
        return (PlayerView) getChildAt(position);
    }

    public PlayerView addPlayer(PlayerResponse player, int line, Integer order) {
        int position = getPosition(null, 3 - line, order);
        if (position != -1) {
            Log.i(TAG, "addPlayer: " + String.format("line: %d, order: %d, position: %d", line, order, position));
            return setPlayer(player, position);
        }
        return null;
    }

    public void removePlayer(PlayerResponse player, int line) {
        int position = getPosition(player, 3 - line, PlayerView.NONE_ORDER);
        if (position >= 0) {
            setPlayer(null, position);
        }
    }

    private int getPosition(PlayerResponse player, int line, Integer order) {
        int index = 0;
        for (int i = 0; i < line; i++) {
            index += formation[i];
        }
        for (int i = 1; i <= formation[line]; i++) {
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

    public int getOrder(int line) {
        line = 3 - line;
        int index = 0;
        for (int i = 0; i < line; i++) {
            index += formation[i];
        }
        for (int i = 1; i <= formation[line]; i++) {
            if (players[index] == null) {
                return i;
            }
            index++;
        }
        return -1;
    }

    public PlayerView setPlayer(PlayerResponse player, int position) {
        this.players[position] = player;
        PlayerView view = (PlayerView) getChildAt(position);
        displayPlayer(view, player);
        return view;
    }

    @Override
    public void onRemove(PlayerView view, PlayerResponse player, int position, int order) {
        if (removeCallback != null) removeCallback.accept(player, 3 - position, order);
    }

    @Override
    public void onAddPlayer(PlayerView view, int position, int order) {
        if (addCallback != null) addCallback.accept(view, 3 - position, order);
    }

    @Override
    public void onClickPlayer(PlayerView view, PlayerResponse player, int position, int order) {
        if (infoCallback != null) infoCallback.accept(player, 3 - position, order);
    }

    @Override
    public void onEdit(PlayerView view, PlayerResponse player, int position, int order) {
        if (editable && editCallback != null) addCallback.accept(view, 3 - position, order);
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
        for (int i = 0; i < getChildCount(); i++) {
            ((PlayerView) getChildAt(i)).setEditable(editable);
        }
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
        for (int i = 0; i < getChildCount(); i++) {
            ((PlayerView) getChildAt(i)).setRemovable(removable);
        }
    }

    public void setAddable(boolean addable) {
        this.addable = addable;
        for (int i = 0; i < getChildCount(); i++) {
            ((PlayerView) getChildAt(i)).setAddable(addable);
        }
    }

    public boolean isFullPosition(int position) {
        int maxPlayers = formation[3 - position];
        int counter = 0;
        for (PlayerResponse player : players) {
            if (player != null && player.getMainPosition() == position) {
                counter++;
                if (maxPlayers == counter) {
                    return true;
                }
            }
        }
        return false;
    }
}
