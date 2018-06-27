package com.football.events;

import com.bon.event_bus.IEvent;
import com.football.models.responses.LeagueResponse;

public class LeagueEvent implements IEvent {

    public static final int ACTION_UPDATE = 0;
    public static final int ACTION_LEAVE = 1;

    private int action;

    private LeagueResponse league;

    public LeagueEvent() {
    }

    public LeagueEvent(int action, LeagueResponse league) {
        this.action = action;
        this.league = league;
    }

    public LeagueResponse getLeague() {
        return league;
    }

    public int getAction() {
        return action;
    }
}
