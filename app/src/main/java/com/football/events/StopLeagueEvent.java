package com.football.events;

import com.bon.event_bus.IEvent;

public class StopLeagueEvent implements IEvent {
    private int leagueId;

    public StopLeagueEvent(int leagueId) {
        this.leagueId = leagueId;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    @Override
    public String toString() {
        return "StopLeagueEvent{" +
                "leagueId=" + leagueId +
                '}';
    }
}
