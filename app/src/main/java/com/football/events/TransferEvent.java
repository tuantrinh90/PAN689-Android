package com.football.events;

import com.bon.event_bus.IEvent;
import com.football.models.responses.PlayerResponse;

public class TransferEvent implements IEvent {

    public static final int SENDER = 0;
    public static final int RECEIVER = 1;


    private int action; // sender or receiver
    private String message; // only action receiver
    private PlayerResponse fromPlayer;
    private PlayerResponse toPlayer;

    public TransferEvent(PlayerResponse fromPlayer, PlayerResponse toPlayer) {
        this.action = SENDER;
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
    }

    public TransferEvent(int action, String message) {
        this.action = action;
        this.message = message;
    }

    public int getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }

    public PlayerResponse getFromPlayer() {
        return fromPlayer;
    }

    public PlayerResponse getToPlayer() {
        return toPlayer;
    }

}
