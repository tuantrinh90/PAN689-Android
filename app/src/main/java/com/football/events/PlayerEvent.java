package com.football.events;

import com.bon.event_bus.IEvent;
import com.football.models.responses.PlayerResponse;

import java8.util.function.BiConsumer;

public class PlayerEvent implements IEvent {

    public static final int ACTION_NONE = 0;
    public static final int ACTION_ADD_CLICK = 1;
    public static final int ACTION_PICK = 2;

    private int action;
    private int position;
    private PlayerResponse data;
    private Integer order;
    private BiConsumer<Boolean, String> callback;

    private PlayerEvent(Builder builder) {
        action = builder.action;
        position = builder.position;
        data = builder.data;
        order = builder.order;
        callback = builder.callback;
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

    public Integer getOrder() {
        return order;
    }

    public BiConsumer<Boolean, String> getCallback() {
        return callback;
    }

    public static final class Builder {
        private int action;
        private int position;
        private PlayerResponse data;
        private Integer order;
        private BiConsumer<Boolean, String> callback;

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

        public Builder order(Integer val) {
            order = val;
            return this;
        }

        public Builder callback(BiConsumer<Boolean, String> val) {
            callback = val;
            return this;
        }

        public PlayerEvent build() {
            return new PlayerEvent(this);
        }
    }
}
