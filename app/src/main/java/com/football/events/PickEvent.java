package com.football.events;

import com.bon.event_bus.IEvent;

public class PickEvent implements IEvent {

    public static final int DONE = -1;
    public static int ACTION_PICK = 0;
    public static int ACTION_REMOVE = 1;

    private int action;
    private int playerId;

    public PickEvent(int action, int playerId) {
        this.action = action;
        this.playerId = playerId;
    }

    public int getAction() {
        return action;
    }

    public int getPlayerId() {
        return playerId;
    }
}
