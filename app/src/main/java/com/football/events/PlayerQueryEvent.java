package com.football.events;

import com.bon.event_bus.IEvent;

public class PlayerQueryEvent implements IEvent {

    public static final int TAG_FILTER = 0;
    public static final int TAG_DISPLAY = 1;

    private int tag;
    private String position;
    private String club;
    private String display;

    private PlayerQueryEvent(Builder builder) {
        tag = builder.tag;
        position = builder.position;
        club = builder.club;
        display = builder.display;
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

    public String getDisplay() {
        return display;
    }

    public static final class Builder {
        private int tag;
        private String position;
        private String club;
        private String display;

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

        public Builder display(String val) {
            display = val;
            return this;
        }

        public PlayerQueryEvent build() {
            return new PlayerQueryEvent(this);
        }
    }
}
