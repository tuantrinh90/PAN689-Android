package com.football.events;

import com.bon.event_bus.IEvent;

public class GeneralEvent<T> implements IEvent {

    public enum SOURCE {
        LINEUP_DRAFT,
        LINEUP_REMOVE_PLAYER
    }

    private SOURCE source;
    private T data;

    public GeneralEvent(SOURCE source) {
        this.source = source;
    }

    public GeneralEvent(SOURCE source, T data) {
        this.source = source;
        this.data = data;
    }

    public SOURCE getSource() {
        return source;
    }

    public T getData() {
        return data;
    }
}
