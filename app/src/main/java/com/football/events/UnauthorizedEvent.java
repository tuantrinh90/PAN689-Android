package com.football.events;

import com.bon.event_bus.IEvent;

public class UnauthorizedEvent implements IEvent {
    String message;

    public UnauthorizedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UnauthorizedEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
