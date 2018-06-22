package com.football.events;

import com.bon.event_bus.IEvent;
import com.football.models.responses.LeagueResponse;

public class LeagueEvent implements IEvent {

    private LeagueResponse league;

    public LeagueEvent() {
    }

    public LeagueEvent(LeagueResponse league) {
        this.league = league;
    }

    public LeagueResponse getLeague() {
        return league;
    }

}
