package com.football.events;

import com.bon.event_bus.IEvent;
import com.football.models.responses.LeagueResponse;

public class StartLeagueEvent implements IEvent {

    private LeagueResponse league;

    public StartLeagueEvent(LeagueResponse league) {
        this.league = league;
    }

    public LeagueResponse getLeague() {
        return league;
    }
}
