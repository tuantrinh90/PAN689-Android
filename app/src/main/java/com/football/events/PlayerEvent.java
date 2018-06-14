package com.football.events;

import com.bon.event_bus.IEvent;
import com.football.models.responses.PlayerResponse;

public class PlayerEvent implements IEvent {

    public static final int ACTION_ADD_CLICK = 1;

    private int action;
    private int position;
    private PlayerResponse data;
    private int index;

    private PlayerEvent(Builder builder) {
        action = builder.action;
        position = builder.position;
        data = builder.data;
        index = builder.index;
    }

    public int getAction() {
        return action;
    }

    public int getPosition() {
        return position;
    }

    public PlayerResponse getData() {
        return data;
    }

    public int getIndex() {
        return index;
    }

    public static final class Builder {
        private int action;
        private int position;
        private PlayerResponse data;
        private int index;

        public Builder() {
        }

        public Builder action(int val) {
            action = val;
            return this;
        }

        public Builder position(int val) {
            position = val;
            return this;
        }

        public Builder data(PlayerResponse val) {
            data = val;
            return this;
        }
        public Builder index(int val) {
            index = val;
            return this;
        }

        public PlayerEvent build() {
            return new PlayerEvent(this);
        }
    }
}
