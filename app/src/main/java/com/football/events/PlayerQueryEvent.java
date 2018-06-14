package com.football.events;

import com.bon.event_bus.IEvent;

public class PlayerQueryEvent implements IEvent {

    public static final int TAG_FILTER = 0;
    public static final int TAG_DISPLAY = 1;

    private int tag;
    private String position;
    private String club;

    private PlayerQueryEvent(Builder builder) {
        tag = builder.tag;
        position = builder.position;
        club = builder.club;
    }

    public int getTag() {
        return tag;
    }

    public String getPosition() {
        return position;
    }

    public String getClub() {
        return club;
    }

    public static final class Builder {
        private int tag;
        private String position;
        private String club;

        public Builder() {
        }

        public Builder tag(int val) {
            tag = val;
            return this;
        }

        public Builder position(String val) {
            position = val;
            return this;
        }

        public Builder club(String val) {
            club = val;
            return this;
        }

        public PlayerQueryEvent build() {
            return new PlayerQueryEvent(this);
        }
    }
}
